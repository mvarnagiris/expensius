/*
 * Copyright (C) 2016 Mantas Varnagiris.
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

package com.mvcoding.expensius.feature.report

import com.mvcoding.expensius.Settings
import com.mvcoding.expensius.feature.report.TagsReportPresenter.TagWithAmount
import com.mvcoding.expensius.feature.report.TagsReportPresenter.TagsReportItem
import com.mvcoding.expensius.feature.tag.aNewTag
import com.mvcoding.expensius.feature.tag.aTag
import com.mvcoding.expensius.feature.tag.withTitle
import com.mvcoding.expensius.feature.transaction.*
import com.mvcoding.expensius.feature.transaction.TransactionState.CONFIRMED
import com.mvcoding.expensius.feature.transaction.TransactionType.EXPENSE
import com.mvcoding.expensius.model.Currency
import com.mvcoding.expensius.model.ModelState.NONE
import com.mvcoding.expensius.model.Tag
import com.mvcoding.expensius.model.Transaction
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.joda.time.DateTime
import org.joda.time.Interval
import org.joda.time.Period
import org.junit.Before
import org.junit.Test
import rx.Observable.just
import java.math.BigDecimal
import java.math.BigDecimal.TEN

class TagsReportPresenterTest {
    val mainCurrency = Currency("GBP")
    val noTag = aNewTag()
    val tag1 = aTag().withTitle("tag1").withOrder(1)
    val tag2 = aTag().withTitle("tag2").withOrder(0)
    val tag3 = aTag().withTitle("tag3").withOrder(2)
    val transactionsProvider = mock<TransactionsProvider>()
    val settings = mock<Settings>()
    val view = mock<TagsReportPresenter.View>()

    @Before
    fun setUp() {
        whenever(settings.mainCurrency).thenReturn(mainCurrency)
    }

    @Test
    fun showsTagsReportForPast30Days() {
        val startOfTomorrow = DateTime.now().plusDays(1).withTimeAtStartOfDay()
        val last30Days = Interval(startOfTomorrow.minusDays(30), startOfTomorrow)
        prepareTransactions(last30Days)
        val expectedTagsReportItems = expectedTagsReportItems(last30Days)
        val presenter = TagsReportPresenter(EXPENSE, last30Days, transactionsProvider, settings)

        presenter.onViewAttached(view)

        verify(view).showTagsReportItems(expectedTagsReportItems)
    }

    private fun prepareTransactions(last30Days: Interval) {
        val transactions = listOf(
                aTransaction("1.2", last30Days.startMillis, tag1).withCurrency("USD"),
                aTransaction("3.4", last30Days.startMillis + 1, tag1, tag2),
                aTransaction("5.6", last30Days.startMillis + 2, tag1, tag2, tag3),
                aTransaction("7.8", last30Days.withEnd(last30Days.end.minusDays(15)).endMillis, tag1),
                aTransaction("9", last30Days.withEnd(last30Days.end.minusDays(15)).endMillis + 1),
                aTransaction("10", last30Days.endMillis - 1, tag2, tag3))
        whenever(transactionsProvider.transactions(TransactionsFilter(NONE, last30Days, EXPENSE, CONFIRMED))).thenReturn(just(transactions))
    }

    private fun aTransaction(amount: String, timestamp: Long, vararg tags: Tag): Transaction {
        return aTransaction()
                .withAmount(BigDecimal(amount))
                .withCurrency(mainCurrency)
                .withExchangeRate(TEN)
                .withTags(*tags)
                .withTimestamp(timestamp)
    }

    private fun expectedTagsReportItems(last30Days: Interval): List<TagsReportItem> {
        val interval30DaysAgo = last30Days.withPeriodAfterStart(Period.days(1))
        val tagsReportItem30DaysAgo = TagsReportItem(interval30DaysAgo, listOf(
                TagWithAmount(tag2, BigDecimal("9.0")),
                TagWithAmount(tag1, BigDecimal("21.0")),
                TagWithAmount(tag3, BigDecimal("5.6"))
        ))

        val interval15DaysAgo = last30Days.withEnd(last30Days.end.minusDays(14)).withPeriodBeforeEnd(Period.days(1))
        val tagsReportItem15DaysAgo = TagsReportItem(interval15DaysAgo, listOf(
                TagWithAmount(noTag, BigDecimal("9")),
                TagWithAmount(tag1, BigDecimal("7.8"))
        ))

        val intervalToday = last30Days.withPeriodBeforeEnd(Period.days(1))
        val tagsReportItemToday = TagsReportItem(intervalToday, listOf(
                TagWithAmount(tag2, BigDecimal("10")),
                TagWithAmount(tag3, BigDecimal("10"))
        ))

        val expectedTagsReportItems = (0..29).map {
            when (it) {
                0 -> tagsReportItem30DaysAgo
                15 -> tagsReportItem15DaysAgo
                29 -> tagsReportItemToday
                else -> TagsReportItem(
                        last30Days.withEnd(last30Days.end.minusDays(29 - it)).withPeriodBeforeEnd(Period.days(1)), emptyList())
            }
        }
        return expectedTagsReportItems
    }
}