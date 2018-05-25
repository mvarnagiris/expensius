/*
 * Copyright (C) 2018 Mantas Varnagiris.
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
import com.mvcoding.expensius.model.extensions.aRemoteFilter
import com.mvcoding.expensius.model.extensions.withInterval
import org.joda.time.DateTime
import org.junit.Test

class RemoteFilterTest {

    @Test
    fun `creates a copy of filter with next period`() {
        val reportPeriod = ReportPeriod.MONTH
        val february = DateTime(2016, 2, 3, 1, 1)
        val februaryInterval = reportPeriod.interval(february.millis)
        val marchInterval = reportPeriod.nextInterval(februaryInterval)
        val aprilInterval = reportPeriod.nextInterval(marchInterval)

        val filter = aRemoteFilter().withInterval(februaryInterval)
        val nextInterval = filter.withNextInterval(reportPeriod).interval
        val nextNextInterval = filter.withNextInterval(reportPeriod).withNextInterval(reportPeriod).interval

        expect that nextInterval isEqualTo marchInterval
        expect that nextNextInterval isEqualTo aprilInterval
    }

    @Test
    fun `creates a copy of filter with previous period`() {
        val reportPeriod = ReportPeriod.MONTH
        val february = DateTime(2016, 2, 3, 1, 1)
        val februaryInterval = reportPeriod.interval(february.millis)
        val januaryInterval = reportPeriod.previousInterval(februaryInterval)
        val december2015Interval = reportPeriod.previousInterval(januaryInterval)

        val filter = aRemoteFilter().withInterval(februaryInterval)
        val previousInterval = filter.withPreviousInterval(reportPeriod).interval
        val previousNextInterval = filter.withPreviousInterval(reportPeriod).withPreviousInterval(reportPeriod).interval

        expect that previousInterval isEqualTo januaryInterval
        expect that previousNextInterval isEqualTo december2015Interval
    }
}