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

import com.mvcoding.expensius.feature.report.TagsReportPresenter.TagsReportItem
import com.mvcoding.expensius.feature.tag.aNewTag
import com.mvcoding.expensius.feature.tag.aTag
import com.mvcoding.expensius.feature.tag.withTitle
import com.mvcoding.expensius.feature.transaction.*
import com.mvcoding.expensius.model.ModelState.NONE
import com.mvcoding.expensius.model.Tag
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.joda.time.DateTime
import org.joda.time.Interval
import org.joda.time.Period
import org.junit.Test
import rx.Observable.just
import java.math.BigDecimal
import java.math.BigDecimal.ZERO

class TagsReportPresenterTest {
    val transactionsProvider = mock<TransactionsProvider>()
    val view = mock<TagsReportPresenter.View>()

    @Test
    fun showsTagsReportForPast30Days() {
        val startOfTomorrow = DateTime.now().plusDays(1).withTimeAtStartOfDay()
        val last30Days = Interval(startOfTomorrow.minusDays(30), startOfTomorrow)
        val noTag = aNewTag()
        val tag1 = aTag().withTitle("tag1")
        val tag2 = aTag().withTitle("tag2")
        val tag3 = aTag().withTitle("tag3")
        prepareTransactions(last30Days, startOfTomorrow, tag1, tag2, tag3)
        val expectedTagsReportItems = expectedTagsReportItems(last30Days, noTag, startOfTomorrow, tag1, tag2, tag3)
        val presenter = TagsReportPresenter(last30Days, transactionsProvider)

        presenter.onViewAttached(view)

        verify(view).showTagsReportItems(expectedTagsReportItems)
    }

    private fun expectedTagsReportItems(
            last30Days: Interval,
            noTag: Tag,
            startOfTomorrow: DateTime,
            tag1: Tag,
            tag2: Tag,
            tag3: Tag): List<TagsReportItem> {
        val emptyTagsReportItemTagsWithAmount = mapOf(noTag to ZERO, tag1 to ZERO, tag2 to ZERO, tag3 to ZERO)

        val tagsReportItem30DaysAgo = TagsReportItem(
                last30Days.withPeriodAfterStart(Period.days(1)),
                mapOf(noTag to ZERO,
                        tag1 to BigDecimal("10.2"),
                        tag2 to BigDecimal("9.0"),
                        tag3 to BigDecimal("5.6")))

        val tagsReportItem15DaysAgo = TagsReportItem(
                last30Days.withEnd(startOfTomorrow.minusDays(14)).withPeriodBeforeEnd(Period.days(1)),
                mapOf(noTag to BigDecimal("9"),
                        tag1 to BigDecimal("7.8"),
                        tag2 to ZERO,
                        tag3 to ZERO))

        val tagsReportItem0DaysAgo = TagsReportItem(
                last30Days.withPeriodBeforeEnd(Period.days(1)),
                mapOf(noTag to ZERO,
                        tag1 to ZERO,
                        tag2 to BigDecimal("10"),
                        tag3 to BigDecimal("10")))


        val expectedTagsReportItems = (0..29).map {
            when (it) {
                0 -> tagsReportItem30DaysAgo
                15 -> tagsReportItem15DaysAgo
                29 -> tagsReportItem0DaysAgo
                else -> TagsReportItem(
                        last30Days.withEnd(startOfTomorrow.minusDays(29 - it)).withPeriodBeforeEnd(Period.days(1)),
                        emptyTagsReportItemTagsWithAmount
                )
            }
        }
        return expectedTagsReportItems
    }

    private fun prepareTransactions(last30Days: Interval,
            startOfTomorrow: DateTime,
            tag1: Tag,
            tag2: Tag,
            tag3: Tag) {
        val transaction30DaysAgo1 = aTransaction()
                .withAmount(BigDecimal("1.2"))
                .withTags(tag1)
                .withTimestamp(last30Days.startMillis)
        val transaction30DaysAgo2 = aTransaction()
                .withAmount(BigDecimal("3.4"))
                .withTags(tag1, tag2)
                .withTimestamp(last30Days.startMillis + 1)
        val transaction30DaysAgo3 = aTransaction()
                .withAmount(BigDecimal("5.6"))
                .withTags(tag1, tag2, tag3)
                .withTimestamp(last30Days.startMillis + 2)
        val transaction15DaysAgo1 = aTransaction()
                .withAmount(BigDecimal("7.8"))
                .withTags(tag1)
                .withTimestamp(last30Days.withEnd(startOfTomorrow.minusDays(15)).endMillis)
        val transaction15DaysAgo2 = aTransaction()
                .withAmount(BigDecimal("9"))
                .withTags()
                .withTimestamp(last30Days.withEnd(startOfTomorrow.minusDays(15)).endMillis + 1)
        val transaction0DaysAgo = aTransaction()
                .withAmount(BigDecimal("10"))
                .withTags(tag2, tag3)
                .withTimestamp(last30Days.endMillis - 1)

        whenever(transactionsProvider.transactions(TransactionsFilter(NONE, last30Days))).thenReturn(just(listOf(
                transaction30DaysAgo1,
                transaction30DaysAgo2,
                transaction30DaysAgo3,
                transaction15DaysAgo1,
                transaction15DaysAgo2,
                transaction0DaysAgo)))
    }
}