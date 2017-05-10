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
import rx.Observable
import rx.Observable.*

class TrendsSource(
        private val transactionsSource: DataSource<List<Transaction>>,
        private val otherTransactionsSource: DataSource<List<Transaction>>,
        private val reportSettingsSource: DataSource<ReportSettings>,
        private val moneyConversionSource: ParameterDataSource<MoneyConversion, Money>) : DataSource<Trends> {

    override fun data(): Observable<Trends> = never()//transactionsSource.data().map { it.allItems }

    private fun transactionsAndReportSettings(transactionsSource: DataSource<List<Transaction>>) = combineLatest(
            transactionsSource.data(),
            reportSettingsSource.data(),
            ::TransactionsAndReportSettings)

    private fun Observable<TransactionsAndReportSettings>.switchMapToListOfMoneyForGivenCurrency() = switchMap {
        it.transactions.withMoneyForGivenCurrency(it.reportSettings.currency)
    }

    private fun List<Transaction>.withMoneyForGivenCurrency(currency: Currency) = from(this).flatMap { it.withMoneyForGivenCurrency(currency) }.toList()
    private fun Transaction.withMoneyForGivenCurrency(currency: Currency) = moneyConversionSource.data(MoneyConversion(money, currency)).map { withMoney(it) }

    private data class TransactionsAndReportSettings(val transactions: List<Transaction>, val reportSettings: ReportSettings)
}