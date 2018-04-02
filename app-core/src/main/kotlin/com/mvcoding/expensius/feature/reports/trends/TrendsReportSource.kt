/*
 * Copyright (C) 2018 Mantas Varnagiris.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

package com.mvcoding.expensius.feature.reports.trends

import com.mvcoding.expensius.data.DataSource
import com.mvcoding.expensius.data.ParameterDataSource
import com.mvcoding.expensius.model.*
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables.combineLatest
import org.joda.time.Interval
import java.math.BigDecimal

class TrendsReportSource(
        private val transactionsSource: DataSource<List<Transaction>>,
        private val otherTransactionsSource: DataSource<List<Transaction>>,
        private val localFilterSource: DataSource<LocalFilter>,
        private val reportSettingsSource: DataSource<ReportSettings>,
        private val moneyConversionSource: ParameterDataSource<MoneyConversion, Money>) : DataSource<TrendsReport> {

    override fun data(): Observable<TrendsReport> = combineLatest(
            dataForTotalMoneyWithGroupedMoneys(transactionsSource),
            dataForTotalMoneyWithGroupedMoneys(otherTransactionsSource),
            { (totalMoney, groupedMoney), (otherTotalMoney, otherGroupedMoney) -> TrendsReport(groupedMoney, totalMoney, otherGroupedMoney, otherTotalMoney) })

    private fun dataForTotalMoneyWithGroupedMoneys(transactionsSource: DataSource<List<Transaction>>): Observable<Pair<Money, List<GroupedMoney<Interval>>>> {
        return combineLatest(
                transactionsSource.data(),
                localFilterSource.data(),
                reportSettingsSource.data(),
                ::TrendsData)
                .switchMap {
                    val reportSettings = it.reportSettings
                    val filteredTransactions = it.localFilter.filter(it.transactions)
                    val currency = reportSettings.currency

                    Observable.fromIterable(filteredTransactions)
                            .flatMap { transaction -> moneyConversionSource.data(MoneyConversion(transaction.money, currency)).map { transaction.withMoney(it) } }
                            .toList()
                            .toObservable()
                            .map {
                                val groupedMoneys = reportSettings.reportPeriod.groupToFillWholePeriod(it, reportSettings.reportGroup, currency)
                                val totalMoney = groupedMoneys
                                        .map { it.money.amount }
                                        .filter { it != BigDecimal.ZERO }
                                        .fold(BigDecimal.ZERO) { sum, amount -> sum + amount }
                                        .let { Money(it, currency) }
                                totalMoney to groupedMoneys
                            }
                }
    }

    private data class TrendsData(
            val transactions: List<Transaction>,
            val localFilter: LocalFilter,
            val reportSettings: ReportSettings)
}