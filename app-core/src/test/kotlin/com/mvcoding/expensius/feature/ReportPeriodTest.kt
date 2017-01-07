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

package com.mvcoding.expensius.feature

import com.mvcoding.expensius.extensions.interval
import com.mvcoding.expensius.extensions.nextInterval
import com.mvcoding.expensius.extensions.previousInterval
import com.mvcoding.expensius.model.ReportPeriod.MONTH
import org.hamcrest.CoreMatchers.equalTo
import org.joda.time.DateTime
import org.junit.Assert.assertThat
import org.junit.Test

class ReportPeriodTest {
    @Test
    fun convertsTimestampToInterval() {
        val reportPeriod = MONTH
        val expectedIntervalStart = DateTime(2016, 2, 1, 0, 0, 0, 0)
        val expectedIntervalEnd = DateTime(2016, 3, 1, 0, 0, 0, 0)
        val february = DateTime(2016, 2, 3, 1, 1)

        val interval = reportPeriod.interval(february.millis)

        assertThat(interval.start, equalTo(expectedIntervalStart))
        assertThat(interval.end, equalTo(expectedIntervalEnd))
    }

    @Test
    fun calculatesNextInterval() {
        val reportPeriod = MONTH
        val february = DateTime(2016, 2, 3, 1, 1)
        val februaryInterval = reportPeriod.interval(february.millis)
        val expectedIntervalStart = DateTime(2016, 3, 1, 0, 0, 0, 0)
        val expectedIntervalEnd = DateTime(2016, 4, 1, 0, 0, 0, 0)

        val marchInterval = reportPeriod.nextInterval(februaryInterval)

        assertThat(marchInterval.start, equalTo(expectedIntervalStart))
        assertThat(marchInterval.end, equalTo(expectedIntervalEnd))
    }

    @Test
    fun calculatesPreviousInterval() {
        val reportPeriod = MONTH
        val february = DateTime(2016, 2, 3, 1, 1)
        val februaryInterval = reportPeriod.interval(february.millis)
        val expectedIntervalStart = DateTime(2016, 1, 1, 0, 0, 0, 0)
        val expectedIntervalEnd = DateTime(2016, 2, 1, 0, 0, 0, 0)

        val januaryInterval = reportPeriod.previousInterval(februaryInterval)

        assertThat(januaryInterval.start, equalTo(expectedIntervalStart))
        assertThat(januaryInterval.end, equalTo(expectedIntervalEnd))
    }
}