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
import com.mvcoding.expensius.feature.Filter
import com.mvcoding.expensius.feature.ReportStep
import com.mvcoding.expensius.feature.ReportStep.Step.DAY
import com.mvcoding.expensius.feature.ReportStep.Step.MONTH
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
import com.mvcoding.expensius.rxSchedulers
import com.nhaarman.mockito_kotlin.*
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
    val filter = Filter()
    val reportStep = ReportStep()
    val presenter = TagsReportPresenter(
            filter,
            reportStep,
            transactionsProvider,
            settings,
            rxSchedulers())

    @Before
    fun setUp() {
        whenever(settings.mainCurrency).thenReturn(mainCurrency)
    }

    @Test
    fun showsIntervalIsRequiredWhenIntervalInFilterIsEmpty() {
        filter.clearInterval()

        presenter.onViewAttached(view)

        verify(view).showIntervalIsRequired()
        verify(view, never()).showTagsReportItems(any())
    }

    @Test
    fun showsTagsReportForGivenFilterAndReportStep() {
        val interval = Interval(DateTime.now().withTimeAtStartOfDay(), Period.days(10))
        filter.setInterval(interval)
        filter.setTransactionType(EXPENSE)
        reportStep.setStep(DAY)
        prepareTransactions(interval)
        val expectedTagsReportItems = expectedTagsReportItems(interval, DAY)

        presenter.onViewAttached(view)

        verify(view).hideIntervalIsRequired()
        verify(view).showTagsReportItems(expectedTagsReportItems)
    }

    @Test
    fun updatesResultsWhenStepIsUpdated() {
        val interval = Interval(DateTime.now().withDayOfMonth(1).withTimeAtStartOfDay(), Period.months(4))
        filter.setInterval(interval)
        filter.setTransactionType(EXPENSE)
        reportStep.setStep(MONTH)
        prepareTransactions(interval)
        val expectedTagsReportItems = expectedTagsReportItems(interval, MONTH)
        presenter.onViewAttached(view)

//        reportStep.setStep(WEEK)

        verify(view).hideIntervalIsRequired()
        verify(view).showTagsReportItems(expectedTagsReportItems)
    }

    private fun prepareTransactions(interval: Interval) {
        val timestampInTheMiddle = interval.withEnd(interval.end.minusMillis(interval.toDurationMillis().toInt() / 2)).endMillis
        val transactions = listOf(
                aTransaction("1.2", interval.startMillis, tag1).withCurrency("USD"),
                aTransaction("3.4", interval.startMillis + 1, tag1, tag2),
                aTransaction("5.6", interval.startMillis + 2, tag1, tag2, tag3),
                aTransaction("7.8", timestampInTheMiddle, tag1),
                aTransaction("9", timestampInTheMiddle + 1),
                aTransaction("10", interval.endMillis - 1, tag2, tag3))
        whenever(transactionsProvider.transactions(TransactionsFilter(NONE, interval, EXPENSE, CONFIRMED)))
                .thenReturn(just(transactions))
    }

    private fun aTransaction(amount: String, timestamp: Long, vararg tags: Tag): Transaction {
        return aTransaction()
                .withAmount(BigDecimal(amount))
                .withCurrency(mainCurrency)
                .withExchangeRate(TEN)
                .withTags(*tags)
                .withTimestamp(timestamp)
                .withTransactionType(EXPENSE)
    }

    private fun expectedTagsReportItems(interval: Interval, step: ReportStep.Step): List<TagsReportItem> {
        val stepPeriod = step.toPeriod()
        val numberOfSteps = step.toNumberOfSteps(interval)

        val intervalAtTheBeginning = step.toInterval(interval.startMillis)
        val tagsReportItemAtTheBeginning = TagsReportItem(intervalAtTheBeginning, listOf(
                TagWithAmount(tag2, BigDecimal("9.0")),
                TagWithAmount(tag1, BigDecimal("21.0")),
                TagWithAmount(tag3, BigDecimal("5.6"))
        ))


        val midIntervalMillis = interval.end.minus(stepPeriod.multipliedBy(numberOfSteps / 2)).millis
        val intervalInTheMiddle = step.toInterval(midIntervalMillis)
        val tagsReportItemInTheMiddle = TagsReportItem(intervalInTheMiddle, listOf(
                TagWithAmount(noTag, BigDecimal("9")),
                TagWithAmount(tag1, BigDecimal("7.8"))
        ))

        val intervalAtTheEnd = step.toInterval(interval.endMillis - 1)
        val tagsReportItemAtTheEnd = TagsReportItem(intervalAtTheEnd, listOf(
                TagWithAmount(tag2, BigDecimal("10")),
                TagWithAmount(tag3, BigDecimal("10"))
        ))

        val lastIntervalIndex = numberOfSteps - 1
        val expectedTagsReportItems = (0..lastIntervalIndex).map {
            when (it) {
                0 -> tagsReportItemAtTheBeginning
                numberOfSteps / 2 -> tagsReportItemInTheMiddle
                lastIntervalIndex -> tagsReportItemAtTheEnd
                else -> TagsReportItem(
                        interval.withEnd(interval.end.minusDays(lastIntervalIndex - it)).withPeriodBeforeEnd(stepPeriod), emptyList())
            }
        }
        return expectedTagsReportItems
    }
}