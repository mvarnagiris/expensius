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

import com.mvcoding.expensius.extensions.splitIntoGroupIntervals
import com.mvcoding.expensius.extensions.toInterval
import com.mvcoding.expensius.model.NullModels.noTag
import com.mvcoding.expensius.model.ReportGroup
import com.mvcoding.expensius.model.Tag
import com.mvcoding.expensius.model.Transaction
import org.joda.time.Interval
import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.util.*

class AmountGrouping {

    fun groupAmountsInIntervals(transactions: List<Transaction>, reportGroup: ReportGroup, interval: Interval): List<AmountGroup<Interval>> {
        val defaultValue = { ZERO }
        val groupedAmounts = groupAmountsIntoIntervals(reportGroup, transactions)
        return reportGroup.splitIntoGroupIntervals(interval)
                .associateBy({ it }, { groupedAmounts.getOrElse(it, defaultValue) })
                .map { AmountGroup(it.key, it.value) }
    }

    private fun groupAmountsIntoIntervals(reportGroup: ReportGroup, transactions: List<Transaction>): Map<Interval, BigDecimal> = transactions
            .groupBy({ reportGroup.toInterval(it.timestamp) }, { it.amount })
            .sumAmounts()

    fun groupAmountsInTags(transactions: List<Transaction>): List<AmountGroup<Tag>> = transactions
            .fold(hashMapOf<Tag, ArrayList<BigDecimal>>()) { map, transaction -> transaction.appendAmountToTags(map) }
            .sumAmounts()
            .map { AmountGroup(it.key, it.value) }
            .sortedBy { -it.amount }

    private fun Transaction.appendAmountToTags(map: HashMap<Tag, ArrayList<BigDecimal>>) = tagsOrNoTag()
            .forEach { map.appendAmountToTag(it, amount) }
            .let { map }

    private fun Transaction.tagsOrNoTag() = tags.let { if (it.isEmpty()) setOf(noTag) else it }

    private fun HashMap<Tag, ArrayList<BigDecimal>>.appendAmountToTag(tag: Tag, amount: BigDecimal) = getOrPut(tag, { arrayListOf() }).add(amount)
    private fun <KEY> Map<KEY, List<BigDecimal>>.sumAmounts() = mapValues { it.value.fold(ZERO) { totalAmount, amount -> totalAmount + amount } }
}

data class AmountGroup<out GROUP>(val group: GROUP, val amount: BigDecimal)