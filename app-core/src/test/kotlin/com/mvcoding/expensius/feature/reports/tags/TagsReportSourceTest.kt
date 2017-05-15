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

package com.mvcoding.expensius.feature.reports.tags

import com.mvcoding.expensius.data.DataSource
import com.mvcoding.expensius.data.ParameterDataSource
import com.mvcoding.expensius.model.*
import com.mvcoding.expensius.model.extensions.*
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Test
import rx.Observable.just
import rx.observers.TestSubscriber
import java.math.BigDecimal

class TagsReportSourceTest {

    val transactionsSource = mock<DataSource<List<Transaction>>>()
    val otherTransactionsSource = mock<DataSource<List<Transaction>>>()
    val localFilterSource = mock<DataSource<LocalFilter>>()
    val reportSettingsSource = mock<DataSource<ReportSettings>>()
    val moneyConversionSource = mock<ParameterDataSource<MoneyConversion, Money>>()
    val tagsReportSource = TagsReportSource(transactionsSource, otherTransactionsSource, localFilterSource, reportSettingsSource, moneyConversionSource)
    val subscriber = TestSubscriber<TagsReport>()

    @Before
    fun setUp() {
        whenever(transactionsSource.data()).thenReturn(just(someTransactions()))
        whenever(otherTransactionsSource.data()).thenReturn(just(someTransactions()))
        whenever(localFilterSource.data()).thenReturn(just(aLocalFilter()))
        whenever(reportSettingsSource.data()).thenReturn(just(aReportSettings()))
        whenever(moneyConversionSource.data(any())).thenAnswer { it.getArgument<MoneyConversion>(0).let { just(it.money) } }
    }

    @Test
    fun `groups money based on tags and orders groups by amount and tag order`() {
        val tagA = aTag().withOrder(1)
        val tagB = aTag().withOrder(2)
        val tagC = aTag().withOrder(3)
        val reportSettings = aReportSettings()
        val currency = reportSettings.currency
        val transactionWithAB = aTransaction().withTags(setOf(tagA, tagB)).withCurrency(currency).withAmount(1)
        val transactionWithB = aTransaction().withTags(setOf(tagB)).withCurrency(currency).withAmount(2)
        val transactionWithC = aTransaction().withTags(setOf(tagC)).withCurrency(currency).withAmount(3)
        val transactions = listOf(transactionWithAB, transactionWithB, transactionWithC)
        val totalMoney = transactions.map { it.money.amount }.fold(BigDecimal.ZERO) { sum, amount -> sum + amount }.let { Money(it, currency) }
        val groupedMoneys = listOf(
                GroupedMoney(tagB, Money(BigDecimal.valueOf(3.0), currency)),
                GroupedMoney(tagC, Money(BigDecimal.valueOf(3.0), currency)),
                GroupedMoney(tagA, Money(BigDecimal.valueOf(1.0), currency)))
        val expectedTagsReport = TagsReport(groupedMoneys, totalMoney, groupedMoneys, totalMoney)
        whenever(transactionsSource.data()).thenReturn(just(transactions))
        whenever(otherTransactionsSource.data()).thenReturn(just(transactions))
        whenever(localFilterSource.data()).thenReturn(just(aLocalFilter().withNoFilters()))
        whenever(reportSettingsSource.data()).thenReturn(just(reportSettings))

        tagsReportSource.data().subscribe(subscriber)

        subscriber.assertValues(expectedTagsReport)
    }

    @Test
    fun `applies exchange rate to different currencies`() {
        val tag = aTag()
        val reportSettings = aReportSettings()
        val currency = reportSettings.currency
        val anotherCurrency = aCurrency(currency.code)
        val transactionWithDifferentCurrency = aTransaction().withTags(setOf(tag)).withCurrency(anotherCurrency)
        val exchangeRate = anAmount()

        val transactions = listOf(transactionWithDifferentCurrency)
        val totalMoney = transactions.map { it.money.amount }.fold(BigDecimal.ZERO) { sum, amount -> sum + amount }.let { Money(it * exchangeRate, currency) }
        val groupedMoneys = listOf(GroupedMoney(tag, Money(transactionWithDifferentCurrency.money.amount * exchangeRate, currency)))
        val expectedTagsReport = TagsReport(groupedMoneys, totalMoney, groupedMoneys, totalMoney)
        whenever(transactionsSource.data()).thenReturn(just(transactions))
        whenever(otherTransactionsSource.data()).thenReturn(just(transactions))
        whenever(localFilterSource.data()).thenReturn(just(aLocalFilter().withNoFilters()))
        whenever(reportSettingsSource.data()).thenReturn(just(reportSettings))
        whenever(moneyConversionSource.data(any())).thenAnswer {
            it.getArgument<MoneyConversion>(0).let {
                just(if (it.money.currency == it.toCurrency) it.money else Money(it.money.amount * exchangeRate, it.toCurrency))
            }
        }

        tagsReportSource.data().subscribe(subscriber)

        subscriber.assertValues(expectedTagsReport)
    }

    @Test
    fun `applies local filter for transactions`() {
        val transactionFilteredOut = aTransaction().withTransactionType(TransactionType.INCOME)
        val reportSettings = aReportSettings()
        val currency = reportSettings.currency
        val localFilter = aLocalFilter().withNoFilters().withTransactionType(TransactionType.EXPENSE)
        val transactions = listOf(transactionFilteredOut)
        val totalMoney = Money(BigDecimal.ZERO, currency)
        val expectedTagsReport = TagsReport(emptyList(), totalMoney, emptyList(), totalMoney)
        whenever(transactionsSource.data()).thenReturn(just(transactions))
        whenever(otherTransactionsSource.data()).thenReturn(just(transactions))
        whenever(localFilterSource.data()).thenReturn(just(localFilter))
        whenever(reportSettingsSource.data()).thenReturn(just(reportSettings))

        tagsReportSource.data().subscribe(subscriber)

        subscriber.assertValues(expectedTagsReport)
    }
}