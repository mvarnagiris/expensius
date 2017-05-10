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
import com.mvcoding.expensius.model.ReportGroup.DAY
import org.joda.time.DateTime
import org.joda.time.Interval
import org.joda.time.Period
import org.junit.Test

class ReportGroupTest {
    @Test
    fun `number of steps is 0 when interval is empty or does not fill the period`() {
        val anyDate = DateTime()
        val emptyInterval = Interval(0, 0)

        ReportGroup.values().forEach {
            expect that it.toNumberOfGroups(emptyInterval) isEqualTo 0
            expect that it.toNumberOfGroups(Interval(anyDate, it.toPeriod().minusMinutes(1))) isEqualTo 0
        }
    }

    @Test
    fun `number of steps is 1 when interval is same as step`() {
        val anyDate = DateTime()

        ReportGroup.values().forEach {
            expect that it.toNumberOfGroups(Interval(anyDate, it.toPeriod())) isEqualTo 1
        }
    }

    @Test
    fun `number of steps is 1 when interval does not fill the second period`() {
        val anyDate = DateTime()

        ReportGroup.values().forEach {
            expect that it.toNumberOfGroups(Interval(anyDate, it.toPeriod().plusMinutes(1))) isEqualTo 1
        }
    }

    @Test
    fun `interval wraps given timestamp`() {
        val now = DateTime.now()
        val timestamp = now.millis

        ReportGroup.values().forEach {
            val interval = it.toInterval(timestamp)
            val expectedInterval = when (it) {
                DAY -> Interval(now.withTimeAtStartOfDay(), Period.days(1))
            }
            expect that interval isEqualTo expectedInterval
        }
    }

    @Test
    fun `splits into intervals that fully cover given interval when it can be divided in equal periods`() {
        val timestamp = DateTime.now().millis

        ReportGroup.values().forEach {
            val firstInterval = it.toInterval(timestamp)
            val secondInterval = firstInterval
                    .withStart(firstInterval.end)
                    .withPeriodAfterStart(it.toPeriod())
            val totalInterval = Interval(firstInterval.start, secondInterval.end)
            val splitIntervals = it.splitIntoGroupIntervals(totalInterval)

            expect that splitIntervals isEqualTo listOf(firstInterval, secondInterval)
        }
    }

    @Test
    fun `returns empty list when interval is smaller than period`() {
        val timestamp = DateTime.now().millis

        ReportGroup.values().forEach {
            val interval = it.toInterval(timestamp)
            val totalInterval = Interval(
                    interval.start.plusMinutes(1),
                    interval.end.minusMinutes(1))
            val splitIntervals = it.splitIntoGroupIntervals(totalInterval)

            expect that splitIntervals isEqualTo emptyList()
        }
    }

    @Test
    fun `splits into intervals by cutting out edges when cannot be divided in equal periods`() {
        val timestamp = DateTime.now().millis

        ReportGroup.values().forEach {
            val firstInterval = it.toInterval(timestamp)
            val secondInterval = firstInterval
                    .withStart(firstInterval.end)
                    .withPeriodAfterStart(it.toPeriod())
            val totalInterval = Interval(
                    firstInterval.start.minusMinutes(1),
                    secondInterval.end.plusMinutes(1))
            val splitIntervals = it.splitIntoGroupIntervals(totalInterval)

            expect that splitIntervals isEqualTo listOf(firstInterval, secondInterval)
        }
    }
}