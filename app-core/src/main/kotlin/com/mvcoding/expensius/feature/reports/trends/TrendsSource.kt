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
import rx.Observable.*

class TrendsSource(
        private val transactionsSource: DataSource<List<Transaction>>,
        private val otherTransactionsSource: DataSource<List<Transaction>>,
        private val localFilterSource: DataSource<LocalFilter>,
        private val reportSettingsSource: DataSource<ReportSettings>,
        private val moneyConversionSource: ParameterDataSource<MoneyConversion, Money>) : DataSource<Trends> {

    override fun data(): Observable<Trends> = never()//transactionsSource.data().map { it.allItems }

    private fun asd(transactionsSource: DataSource<List<Transaction>>): Observable<Pair<Money, List<GroupedMoney<Interval>>>> {
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
                                if (it.isEmpty()) NullModels.noTrends
                                else {

//                                    reportSettings.reportPeriod.interval(it.fir)
//                                    reportSettings.reportGroup.group(it)

                                    NullModels.noTrends
                                }
                            }

                    never<Pair<Money, List<GroupedMoney<Interval>>>>()
                }
    }


//    private fun DataSource<List<Transaction>>.dataForGroupedMoneys() = transactionsAndReportSettings(this)
//            .switchMapToListOfTransactionsWithMoneyForGivenCurrency()
//
//    private fun transactionsAndReportSettings(transactionsSource: DataSource<List<Transaction>>) = combineLatest(
//            transactionsSource.data(),
//            reportSettingsSource.data(),
//            ::TrendsData)
//
//    private fun Observable<TrendsData>.switchMapToListOfTransactionsWithMoneyForGivenCurrency() = switchMap {
//        it.transactions.withMoneyForGivenCurrency(it.reportSettings.currency)
//    }
//
//    private fun List<Transaction>.withMoneyForGivenCurrency(currency: Currency) = from(this).flatMap { it.withMoneyForGivenCurrency(currency) }.toList()
//    private fun Transaction.withMoneyForGivenCurrency(currency: Currency) = moneyConversionSource.data(MoneyConversion(money, currency)).map { withMoney(it) }

    private data class TrendsData(
            val transactions: List<Transaction>,
            val localFilter: LocalFilter,
            val reportSettings: ReportSettings)
}