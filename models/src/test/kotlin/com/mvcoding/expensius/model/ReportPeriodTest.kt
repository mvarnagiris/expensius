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

import org.hamcrest.CoreMatchers
import org.joda.time.DateTime
import org.junit.Assert
import org.junit.Test

class ReportPeriodTest {
    @Test
    fun `converts timestamp to interval`() {
        val reportPeriod = ReportPeriod.MONTH
        val expectedIntervalStart = DateTime(2016, 2, 1, 0, 0, 0, 0)
        val expectedIntervalEnd = DateTime(2016, 3, 1, 0, 0, 0, 0)
        val february = DateTime(2016, 2, 3, 1, 1)

        val interval = reportPeriod.interval(february.millis)

        Assert.assertThat(interval.start, CoreMatchers.equalTo(expectedIntervalStart))
        Assert.assertThat(interval.end, CoreMatchers.equalTo(expectedIntervalEnd))
    }

    @Test
    fun `calculates next interval`() {
        val reportPeriod = ReportPeriod.MONTH
        val february = DateTime(2016, 2, 3, 1, 1)
        val februaryInterval = reportPeriod.interval(february.millis)
        val expectedIntervalStart = DateTime(2016, 3, 1, 0, 0, 0, 0)
        val expectedIntervalEnd = DateTime(2016, 4, 1, 0, 0, 0, 0)

        val marchInterval = reportPeriod.nextInterval(februaryInterval)

        Assert.assertThat(marchInterval.start, CoreMatchers.equalTo(expectedIntervalStart))
        Assert.assertThat(marchInterval.end, CoreMatchers.equalTo(expectedIntervalEnd))
    }

    @Test
    fun `calculates previous interval`() {
        val reportPeriod = ReportPeriod.MONTH
        val february = DateTime(2016, 2, 3, 1, 1)
        val februaryInterval = reportPeriod.interval(february.millis)
        val expectedIntervalStart = DateTime(2016, 1, 1, 0, 0, 0, 0)
        val expectedIntervalEnd = DateTime(2016, 2, 1, 0, 0, 0, 0)

        val januaryInterval = reportPeriod.previousInterval(februaryInterval)

        Assert.assertThat(januaryInterval.start, CoreMatchers.equalTo(expectedIntervalStart))
        Assert.assertThat(januaryInterval.end, CoreMatchers.equalTo(expectedIntervalEnd))
    }
}