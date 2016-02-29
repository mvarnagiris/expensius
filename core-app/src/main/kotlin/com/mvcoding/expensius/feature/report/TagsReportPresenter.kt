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

import com.mvcoding.expensius.feature.Presenter
import com.mvcoding.expensius.feature.transaction.TransactionsFilter
import com.mvcoding.expensius.feature.transaction.TransactionsProvider
import com.mvcoding.expensius.model.ModelState.NONE
import com.mvcoding.expensius.model.Tag
import com.mvcoding.expensius.model.Transaction
import org.joda.time.DateTime
import org.joda.time.Interval
import org.joda.time.Period
import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.util.*

class TagsReportPresenter(
        private val interval: Interval,
        private val transactionsProvider: TransactionsProvider) : Presenter<TagsReportPresenter.View>() {

    override fun onViewAttached(view: View) {
        super.onViewAttached(view)

        unsubscribeOnDetach(transactionsProvider
                .transactions(TransactionsFilter(NONE, interval))
                .map { transactions ->
                    val allTags = hashSetOf<Tag>()
                    val resultMap = hashMapOf<Interval, HashMap<Tag, BigDecimal>>()

                    transactions.forEach { transaction ->
                        val tags = transaction.tags.let { tags -> if (tags.isEmpty()) setOf(Tag()) else tags }
                        allTags.addAll(tags)

                        val interval = transaction.timestampToInterval()
                        val amountsMap = resultMap.getOrElse(interval, { hashMapOf<Tag, BigDecimal>() })
                        tags.forEach { tag -> amountsMap.apply { put(tag, getOrElse(tag, { ZERO }) + transaction.amount) } }
                        resultMap.put(interval, amountsMap)
                    }

                    val period = Period.days(1)
                    var interval = interval.withPeriodAfterStart(period)
                    val last30Days = 0..29
                    last30Days.map {
                        val amountsMap = resultMap.getOrElse(interval, { hashMapOf<Tag, BigDecimal>() })
                        amountsMap.apply { allTags.forEach { putIfAbsent(it, ZERO) } }
                        val tagsReportItem = TagsReportItem(interval,
                                amountsMap.toSortedMap(Comparator { left, right -> left.title.compareTo(right.title) }))
                        interval = interval.withStart(interval.end).withPeriodAfterStart(period)
                        tagsReportItem
                    }
                }
                .subscribe { view.showTagsReportItems(it) })
    }

    private fun Transaction.timestampToInterval() = DateTime(timestamp).withTimeAtStartOfDay().let { Interval(it, it.plusDays(1)) }

    data class TagsReportItem(val interval: Interval, val tagsWithAmount: Map<Tag, BigDecimal>)

    interface View : Presenter.View {
        fun showTagsReportItems(tagsReportItems: List<TagsReportItem>)
    }
}