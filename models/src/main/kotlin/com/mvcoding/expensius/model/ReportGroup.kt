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

package com.mvcoding.expensius.model

import org.joda.time.DateTime
import org.joda.time.Interval
import org.joda.time.Period
import java.math.BigDecimal

enum class ReportGroup { DAY;

    fun period(): Period = when (this) {
        DAY -> Period.days(1)
    }

    fun interval(timestamp: Timestamp) = interval(timestamp.millis)

    fun interval(millis: Long) = DateTime(millis).let {
        when (this) {
            DAY -> it.withTimeAtStartOfDay()
        }
    }.let { Interval(it, period()) }

    fun group(transactions: List<Transaction>): Map<Interval, Money> {
        fun List<Transaction>.currencyOrNull() = firstOrNull()?.money?.currency
        fun List<Transaction>.groupByInterval() = groupBy { interval(it.timestamp) }
        fun Map<Interval, List<Transaction>>.sumAmounts() = mapValues { it.value.fold(BigDecimal.ZERO) { sum, transaction -> sum + transaction.money.amount } }
        fun Map<Interval, BigDecimal>.convertAmountsToMoney(currency: Currency) = mapValues { Money(it.value, currency) }

        return transactions.currencyOrNull()?.let { currency -> transactions.groupByInterval().sumAmounts().convertAmountsToMoney(currency) } ?: emptyMap()
    }

    fun splitIntoGroupIntervals(interval: Interval): List<Interval> {
        val startInterval = interval(interval.startMillis)
        val groupIntervals = arrayListOf(startInterval)

        val period = period()
        while (groupIntervals.last().endMillis < interval.endMillis) {
            groupIntervals.add(Interval(groupIntervals.last().end, period))
        }

        return groupIntervals
    }
}