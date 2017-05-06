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
import org.joda.time.DateTime
import org.junit.Test

class FilterTest {

    @Test
    fun `filter is created with correct interval`() {
        val reportPeriod = ReportPeriod.MONTH
        val february = DateTime(2016, 2, 3, 1, 1)
        val expectedInterval = reportPeriod.interval(february.millis)

        val filter = aFilter().withReportPeriod(reportPeriod).withIntervalBaseTimestamp(february.millis)
        val currentInterval = filter.interval

        expect that currentInterval isEqualTo expectedInterval
    }

    @Test
    fun `creates a copy of filter with next period`() {
        val reportPeriod = ReportPeriod.MONTH
        val february = DateTime(2016, 2, 3, 1, 1)
        val expectedNextInterval = reportPeriod.interval(reportPeriod.interval(february.millis), 1)
        val expectedNextNextInterval = reportPeriod.interval(reportPeriod.interval(february.millis), 2)

        val filter = aFilter().withReportPeriod(reportPeriod).withIntervalBaseTimestamp(february.millis)
        val nextInterval = filter.withNextInterval().interval
        val nextNextInterval = filter.withNextInterval().withNextInterval().interval

        expect that nextInterval isEqualTo expectedNextInterval
        expect that nextNextInterval isEqualTo expectedNextNextInterval
    }

    @Test
    fun `creates a copy of filter with previous period`() {
        val reportPeriod = ReportPeriod.MONTH
        val february = DateTime(2016, 2, 3, 1, 1)
        val expectedPreviousInterval = reportPeriod.interval(reportPeriod.interval(february.millis), -1)
        val expectedPreviousPreviousInterval = reportPeriod.interval(reportPeriod.interval(february.millis), -2)

        val filter = aFilter().withReportPeriod(reportPeriod).withIntervalBaseTimestamp(february.millis)
        val previousInterval = filter.withPreviousInterval().interval
        val previousNextInterval = filter.withPreviousInterval().withPreviousInterval().interval

        expect that previousInterval isEqualTo expectedPreviousInterval
        expect that previousNextInterval isEqualTo expectedPreviousPreviousInterval
    }
}