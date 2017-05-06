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

class ReportPeriodTest {
    @Test
    fun `converts timestamp to interval`() {
        val reportPeriod = ReportPeriod.MONTH
        val expectedIntervalStart = DateTime(2016, 2, 1, 0, 0, 0, 0)
        val expectedIntervalEnd = DateTime(2016, 3, 1, 0, 0, 0, 0)
        val february = DateTime(2016, 2, 3, 1, 1)

        val interval = reportPeriod.interval(february.millis)

        expect that interval.start isEqualTo expectedIntervalStart
        expect that interval.end isEqualTo expectedIntervalEnd
    }

    @Test
    fun `calculates next intervals for month period`() {
        val reportPeriod = ReportPeriod.MONTH
        val february = DateTime(2016, 2, 3, 1, 1)
        val februaryInterval = reportPeriod.interval(february.millis)
        val marchIntervalStart = DateTime(2016, 3, 1, 0, 0, 0, 0)
        val marchIntervalEnd = DateTime(2016, 4, 1, 0, 0, 0, 0)
        val aprilIntervalStart = DateTime(2016, 4, 1, 0, 0, 0, 0)
        val aprilIntervalEnd = DateTime(2016, 5, 1, 0, 0, 0, 0)

        val marchInterval = reportPeriod.nextInterval(februaryInterval)
        val aprilInterval = reportPeriod.nextInterval(marchInterval)

        expect that marchInterval.start isEqualTo marchIntervalStart
        expect that marchInterval.end isEqualTo marchIntervalEnd
        expect that aprilInterval.start isEqualTo aprilIntervalStart
        expect that aprilInterval.end isEqualTo aprilIntervalEnd
    }

    @Test
    fun `calculates previous intervals for month period`() {
        val reportPeriod = ReportPeriod.MONTH
        val february = DateTime(2016, 2, 3, 1, 1)
        val februaryInterval = reportPeriod.interval(february.millis)
        val januaryIntervalStart = DateTime(2016, 1, 1, 0, 0, 0, 0)
        val januaryIntervalEnd = DateTime(2016, 2, 1, 0, 0, 0, 0)
        val december2015IntervalStart = DateTime(2015, 12, 1, 0, 0, 0, 0)
        val december2015IntervalEnd = DateTime(2016, 1, 1, 0, 0, 0, 0)

        val januaryInterval = reportPeriod.previousInterval(februaryInterval)
        val december2015Interval = reportPeriod.previousInterval(januaryInterval)

        expect that januaryInterval.start isEqualTo januaryIntervalStart
        expect that januaryInterval.end isEqualTo januaryIntervalEnd
        expect that december2015Interval.start isEqualTo december2015IntervalStart
        expect that december2015Interval.end isEqualTo december2015IntervalEnd
    }
}