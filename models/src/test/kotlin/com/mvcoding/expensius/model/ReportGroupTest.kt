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

import com.memoizr.assertk.expect
import com.mvcoding.expensius.model.extensions.aTransaction
import com.mvcoding.expensius.model.extensions.withTimestamp
import org.joda.time.DateTime
import org.joda.time.Interval
import org.joda.time.Period
import org.junit.Test

class ReportGroupTest {

    @Test
    fun `can convert to Period`() {
        ReportGroup.values().forEach {
            val period = it.toPeriod()
            val expectedPeriod = when (it) {
                ReportGroup.DAY -> Period.days(1)
            }
            expect that period isEqualTo expectedPeriod
        }
    }

    @Test
    fun `can convert to Interval that wraps timestamp`() {
        val now = DateTime.now()
        val timestampMillis = now.millis
        val timestamp = Timestamp(timestampMillis)

        ReportGroup.values().forEach {
            val intervalFromMillis = it.toInterval(timestampMillis)
            val intervalFromTimestamp = it.toInterval(timestamp)
            val expectedInterval = when (it) {
                ReportGroup.DAY -> Interval(now.withTimeAtStartOfDay(), Period.days(1))
            }
            expect that intervalFromMillis isEqualTo expectedInterval
            expect that intervalFromTimestamp isEqualTo expectedInterval
        }
    }

    @Test
    fun `groups moneys into intervals`() {
        ReportGroup.values().forEach { reportGroup ->
            val period = reportGroup.toPeriod()
            val timestamp = DateTime().withTimeAtStartOfDay().millis
            val groupA1 = aTransaction().withTimestamp(timestamp)
            val groupA2 = aTransaction().withTimestamp(timestamp + 1)
            val groupB1 = aTransaction().withTimestamp(DateTime(timestamp).plus(period).millis)
            val transactions = listOf(groupA1, groupA2, groupB1)
            val expectedCurrency = groupA1.money.currency
            val expectedGroupedMoneys = mapOf(
                    reportGroup.toInterval(groupA1.timestamp) to Money(groupA1.money.amount + groupA2.money.amount, expectedCurrency),
                    reportGroup.toInterval(groupB1.timestamp) to Money(groupB1.money.amount, expectedCurrency))

            val groupedMoneys = reportGroup.group(transactions)

            expect that groupedMoneys isEqualTo expectedGroupedMoneys
        }
    }
}