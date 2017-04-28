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
    fun `creates a copy of filter with next period`() {
        val reportPeriod = ReportPeriod.MONTH
        val february = DateTime(2016, 2, 3, 1, 1)
        val februaryInterval = reportPeriod.interval(february.millis)
        val expectedIntervalStart = DateTime(2016, 3, 1, 0, 0, 0, 0)
        val expectedIntervalEnd = DateTime(2016, 4, 1, 0, 0, 0, 0)

        val marchInterval = Filter(aUserId(), reportPeriod, februaryInterval).withNextInterval().interval

        expect that marchInterval.start isEqualTo expectedIntervalStart
        expect that marchInterval.end isEqualTo expectedIntervalEnd
    }

    @Test
    fun `creates a copy of filter with previous period`() {
        val reportPeriod = ReportPeriod.MONTH
        val february = DateTime(2016, 2, 3, 1, 1)
        val februaryInterval = reportPeriod.interval(february.millis)
        val expectedIntervalStart = DateTime(2016, 1, 1, 0, 0, 0, 0)
        val expectedIntervalEnd = DateTime(2016, 2, 1, 0, 0, 0, 0)

        val januaryInterval = Filter(aUserId(), reportPeriod, februaryInterval).withPreviousInterval().interval

        expect that januaryInterval.start isEqualTo expectedIntervalStart
        expect that januaryInterval.end isEqualTo expectedIntervalEnd
    }
}