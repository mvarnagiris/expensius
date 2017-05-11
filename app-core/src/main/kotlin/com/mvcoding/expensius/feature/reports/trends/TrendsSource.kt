/*
 * Copyright (C) 2017 Mantas Varnagiris.
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
import org.joda.time.Interval
import rx.Observable
import rx.Observable.combineLatest
import rx.Observable.from
import java.math.BigDecimal

class TrendsSource(
        private val transactionsSource: DataSource<List<Transaction>>,
        private val otherTransactionsSource: DataSource<List<Transaction>>,
        private val localFilterSource: DataSource<LocalFilter>,
        private val reportSettingsSource: DataSource<ReportSettings>,
        private val moneyConversionSource: ParameterDataSource<MoneyConversion, Money>) : DataSource<Trends> {

    override fun data(): Observable<Trends> = combineLatest(
            dataForTotalMoneyWithGroupedMoneys(transactionsSource),
            dataForTotalMoneyWithGroupedMoneys(otherTransactionsSource),
            { (totalMoney, groupedMoney), (otherTotalMoney, otherGroupedMoney) -> Trends(groupedMoney, totalMoney, otherGroupedMoney, otherTotalMoney) })

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

                    from(filteredTransactions)
                            .flatMap { transaction -> moneyConversionSource.data(MoneyConversion(transaction.money, currency)).map { transaction.withMoney(it) } }
                            .toList()
                            .map {
                                if (it.isEmpty()) NullModels.noMoney to emptyList<GroupedMoney<Interval>>()
                                else {
                                    val groupedMoneys = reportSettings.reportGroup.group(it)
                                    val totalMoney = groupedMoneys.values
                                            .map { it.amount }
                                            .fold(BigDecimal.ZERO) { sum, amount -> sum + amount }
                                            .let { Money(it, currency) }

                                    val firstTransaction = it.first()
                                    val totalInterval = reportSettings.reportPeriod.interval(firstTransaction.timestamp)
                                    val splitIntervals = reportSettings.reportGroup.splitIntoGroupIntervals(totalInterval)
                                    val defaultMoney = Money(BigDecimal.ZERO, currency)
                                    totalMoney to splitIntervals.map { GroupedMoney(it, groupedMoneys.getOrDefault(it, defaultMoney)) }
                                }
                            }
                }
    }

    private data class TrendsData(
            val transactions: List<Transaction>,
            val localFilter: LocalFilter,
            val reportSettings: ReportSettings)
}