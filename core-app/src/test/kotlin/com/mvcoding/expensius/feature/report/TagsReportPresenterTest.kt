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
import com.mvcoding.expensius.feature.transaction.TransactionsProvider
import com.mvcoding.expensius.model.Currency
import com.mvcoding.expensius.model.ReportGroup.DAY
import com.mvcoding.expensius.rxSchedulers
import com.nhaarman.mockito_kotlin.mock
import rx.lang.kotlin.BehaviorSubject

class TagsReportPresenterTest {
    val reportStepsSubject = BehaviorSubject(DAY)
    val mainCurrency = Currency("GBP")
    //    val noTag = aNewTag()
    //    val tag1 = aTag().withTitle("tag1").withOrder(1)
    //    val tag2 = aTag().withTitle("tag2").withOrder(0)
    //    val tag3 = aTag().withTitle("tag3").withOrder(2)
    val transactionsProvider = mock<TransactionsProvider>()
    val settings = mock<Settings>()
    val view = mock<TagsReportPresenter.View>()
    val filter = Filter()
    val presenter = TagsReportPresenter(
            filter,
            transactionsProvider,
            settings,
            rxSchedulers())

    //    @Before
    //    fun setUp() {
    //        whenever(settings.mainCurrency).thenReturn(mainCurrency)
    //        whenever(settings.reportSteps()).thenReturn(reportStepsSubject)
    //    }
    //
    //    @Test
    //    fun showsIntervalIsRequiredWhenIntervalInFilterIsEmpty() {
    //        filter.clearInterval()
    //
    //        presenter.attach(view)
    //
    //        verify(view).showIntervalIsRequired()
    //        verify(view, never()).showTagsReportItems(any())
    //    }
    //
    //    @Test
    //    fun showsTagsReportForGivenFilterAndWhenReportStepIsDay() {
    //        val interval = Interval(DateTime.now().withTimeAtStartOfDay(), Period.days(10))
    //        filter.setInterval(interval)
    //        filter.setTransactionType(EXPENSE)
    //        setReportStep(DAY)
    //        prepareTransactions(interval)
    //
    //        presenter.attach(view)
    //
    //        verify(transactionsProvider, times(1)).transactions(any())
    //        verify(view).hideIntervalIsRequired()
    //        verify(view).showTagsReportItems(expectedTagsReportItems(interval, DAY))
    //    }
    //
    //    @Test
    //    fun showsTagsReportForGivenFilterAndWhenReportStepIsWeek() {
    //        val interval = Interval(DateTime.now().withDayOfWeek(1).withTimeAtStartOfDay(), Period.weeks(4))
    //        filter.setInterval(interval)
    //        filter.setTransactionType(EXPENSE)
    //        setReportStep(WEEK)
    //        prepareTransactions(interval)
    //
    //        presenter.attach(view)
    //
    //        verify(transactionsProvider, times(1)).transactions(any())
    //        verify(view).hideIntervalIsRequired()
    //        verify(view).showTagsReportItems(expectedTagsReportItems(interval, WEEK))
    //    }
    //
    //    @Test
    //    fun showsTagsReportForGivenFilterAndWhenReportStepIsMonth() {
    //        val interval = Interval(DateTime.now().withDayOfMonth(1).withTimeAtStartOfDay(), Period.months(4))
    //        filter.setInterval(interval)
    //        filter.setTransactionType(EXPENSE)
    //        setReportStep(MONTH)
    //        prepareTransactions(interval)
    //
    //        presenter.attach(view)
    //
    //        verify(transactionsProvider, times(1)).transactions(any())
    //        verify(view).hideIntervalIsRequired()
    //        verify(view).showTagsReportItems(expectedTagsReportItems(interval, MONTH))
    //    }
    //
    //    private fun prepareTransactions(interval: Interval) {
    //        val timestampInTheMiddle = (interval.startMillis + interval.endMillis) / 2
    //        val transactions = listOf(
    //                aTransaction("1.2", interval.startMillis, tag1).withCurrency("USD"),
    //                aTransaction("3.4", interval.startMillis + 1, tag1, tag2),
    //                aTransaction("5.6", interval.startMillis + 2, tag1, tag2, tag3),
    //                aTransaction("7.8", timestampInTheMiddle, tag1),
    //                aTransaction("9", timestampInTheMiddle + 1),
    //                aTransaction("10", interval.endMillis - 1, tag2, tag3))
    //        whenever(transactionsProvider.transactions(TransactionsFilter(NONE, interval, EXPENSE, CONFIRMED)))
    //                .thenReturn(just(transactions))
    //    }
    //
    //    private fun aTransaction(amount: String, timestamp: Long, vararg tags: Tag): Transaction {
    //        return aTransaction()
    //                .withAmount(BigDecimal(amount))
    //                .withCurrency(mainCurrency)
    //                .withExchangeRate(TEN)
    //                .withTags(*tags)
    //                .withTimestamp(timestamp)
    //                .withTransactionType(EXPENSE)
    //    }
    //
    //    private fun expectedTagsReportItems(interval: Interval, reportGroup: ReportGroup): List<TagsReportItem> {
    //        val stepPeriod = reportGroup.toPeriod()
    //        val numberOfSteps = reportGroup.toNumberOfGroups(interval)
    //
    //        val intervalAtTheBeginning = reportGroup.toInterval(interval.startMillis)
    //        val tagsReportItemAtTheBeginning = TagsReportItem(intervalAtTheBeginning, listOf(
    //                TagWithAmount(tag2, BigDecimal("9.0")),
    //                TagWithAmount(tag1, BigDecimal("21.0")),
    //                TagWithAmount(tag3, BigDecimal("5.6"))
    //        ))
    //
    //
    //        val timestampInTheMiddle = (interval.startMillis + interval.endMillis) / 2
    //        val intervalInTheMiddle = reportGroup.toInterval(timestampInTheMiddle)
    //        val tagsReportItemInTheMiddle = TagsReportItem(intervalInTheMiddle, listOf(
    //                TagWithAmount(noTag, BigDecimal("9")),
    //                TagWithAmount(tag1, BigDecimal("7.8"))
    //        ))
    //
    //        val intervalAtTheEnd = reportGroup.toInterval(interval.endMillis - 1)
    //        val tagsReportItemAtTheEnd = TagsReportItem(intervalAtTheEnd, listOf(
    //                TagWithAmount(tag2, BigDecimal("10")),
    //                TagWithAmount(tag3, BigDecimal("10"))
    //        ))
    //
    //        val lastIntervalIndex = numberOfSteps - 1
    //        val expectedTagsReportItems = (0..lastIntervalIndex).map {
    //            when {
    //                it == 0 -> tagsReportItemAtTheBeginning
    //                interval.start.plus(stepPeriod.multipliedBy(it)).millis == intervalInTheMiddle.startMillis -> tagsReportItemInTheMiddle
    //                it == lastIntervalIndex -> tagsReportItemAtTheEnd
    //                else -> TagsReportItem(interval.withStart(interval.start.plus(stepPeriod.multipliedBy(it))).withPeriodAfterStart(stepPeriod), emptyList())
    //            }
    //        }
    //        return expectedTagsReportItems
    //    }
    //
    //    private fun setReportStep(reportGroup: ReportGroup) = reportStepsSubject.onNext(reportGroup)
}