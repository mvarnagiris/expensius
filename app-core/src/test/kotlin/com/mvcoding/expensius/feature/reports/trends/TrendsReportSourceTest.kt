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
import com.mvcoding.expensius.model.TransactionType.EXPENSE
import com.mvcoding.expensius.model.TransactionType.INCOME
import com.mvcoding.expensius.model.extensions.*
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Test
import rx.Observable.just
import rx.observers.TestSubscriber

class TrendsReportSourceTest {

    val transactionsSource = mock<DataSource<List<Transaction>>>()
    val otherTransactionsSource = mock<DataSource<List<Transaction>>>()
    val localFilterSource = mock<DataSource<LocalFilter>>()
    val reportSettingsSource = mock<DataSource<ReportSettings>>()
    val moneyConversionSource = mock<ParameterDataSource<MoneyConversion, Money>>()
    val trendsSource = TrendsReportSource(transactionsSource, otherTransactionsSource, localFilterSource, reportSettingsSource, moneyConversionSource)
    val subscriber = TestSubscriber<TrendsReport>()

    @Before
    fun setUp() {
        whenever(transactionsSource.data()).thenReturn(just(someTransactions()))
        whenever(otherTransactionsSource.data()).thenReturn(just(someTransactions()))
        whenever(localFilterSource.data()).thenReturn(just(aLocalFilter()))
        whenever(reportSettingsSource.data()).thenReturn(just(aReportSettings()))
        whenever(moneyConversionSource.data(any())).thenAnswer { it.getArgument<MoneyConversion>(0).let { just(it.money) } }
    }

    @Test
    fun `creates Trends from two transactions sources filtering them and then grouping and converting money to required currency`() {
        val reportSettings = aReportSettings()
        val reportPeriod = reportSettings.reportPeriod
        val reportGroup = reportSettings.reportGroup
        val currency = reportSettings.currency
        val otherCurrency = aCurrency(excludeCode = currency.code)
        val exchangeRate = anAmount()
        val localFilter = aLocalFilter().withNoFilters().withTransactionType(EXPENSE)

        val transactionFilteredOut = aTransaction().withTransactionType(INCOME)
        val transactionWithRequiredCurrency = aTransaction().withTransactionType(EXPENSE).withMoney(aMoney().withCurrency(currency))
        val transactionWithOtherCurrency = aTransaction()
                .withTransactionType(EXPENSE)
                .withMoney(aMoney().withCurrency(otherCurrency))
                .withTimestamp(transactionWithRequiredCurrency.timestamp)
        val totalAmount = transactionWithRequiredCurrency.money.amount + (transactionWithOtherCurrency.money.amount * exchangeRate)
        val allTransactions = listOf(transactionFilteredOut, transactionWithRequiredCurrency, transactionWithOtherCurrency)
        val filteredTransactionsWithCorrectCurrency = listOf(
                transactionWithRequiredCurrency,
                transactionWithOtherCurrency.withAmount(transactionWithOtherCurrency.money.amount * exchangeRate))

        val otherTransactionFilteredOut = aTransaction().withTransactionType(INCOME)
        val otherTransactionWithRequiredCurrency = aTransaction().withTransactionType(EXPENSE).withMoney(aMoney().withCurrency(currency))
        val otherTransactionWithOtherCurrency = aTransaction()
                .withTransactionType(EXPENSE)
                .withMoney(aMoney().withCurrency(otherCurrency))
                .withTimestamp(otherTransactionWithRequiredCurrency.timestamp)
        val otherTotalAmount = otherTransactionWithRequiredCurrency.money.amount + (otherTransactionWithOtherCurrency.money.amount * exchangeRate)
        val otherAllTransactions = listOf(otherTransactionFilteredOut, otherTransactionWithRequiredCurrency, otherTransactionWithOtherCurrency)
        val otherFilteredTransactionsWithCorrectCurrency = listOf(
                otherTransactionWithRequiredCurrency,
                otherTransactionWithOtherCurrency.withAmount(otherTransactionWithOtherCurrency.money.amount * exchangeRate))

        val expectedTrends = TrendsReport(
                reportPeriod.groupToFillWholePeriod(filteredTransactionsWithCorrectCurrency, reportGroup, currency),
                Money(totalAmount, currency),
                reportPeriod.groupToFillWholePeriod(otherFilteredTransactionsWithCorrectCurrency, reportGroup, currency),
                Money(otherTotalAmount, currency))

        whenever(transactionsSource.data()).thenReturn(just(allTransactions))
        whenever(otherTransactionsSource.data()).thenReturn(just(otherAllTransactions))
        whenever(localFilterSource.data()).thenReturn(just(localFilter))
        whenever(reportSettingsSource.data()).thenReturn(just(reportSettings))
        whenever(moneyConversionSource.data(any()))
                .thenAnswer {
                    it.getArgument<MoneyConversion>(0).let {
                        just(if (it.money.currency == it.toCurrency) it.money else Money(it.money.amount * exchangeRate, it.toCurrency))
                    }
                }

        trendsSource.data().subscribe(subscriber)

        subscriber.assertValues(expectedTrends)
    }
}