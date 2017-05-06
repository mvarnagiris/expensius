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

enum class ReportPeriod { MONTH;

    fun interval(timestamp: Timestamp): Interval = interval(timestamp.millis)

    fun interval(millis: Long): Interval = when (this) {
        MONTH -> DateTime(millis).let { Interval(it.withDayOfMonth(1).withTimeAtStartOfDay(), Period.months(1)) }
    }

    fun interval(interval: Interval, numberOfIntervalsToAdd: Int): Interval = interval.let {
        when {
            numberOfIntervalsToAdd > 1 -> interval(nextInterval(interval), numberOfIntervalsToAdd - 1)
            numberOfIntervalsToAdd < -1 -> interval(previousInterval(interval), numberOfIntervalsToAdd + 1)
            numberOfIntervalsToAdd == 1 -> nextInterval(interval)
            numberOfIntervalsToAdd == -1 -> previousInterval(interval)
            else -> interval
        }
    }

    private fun nextInterval(interval: Interval): Interval = interval.let {
        when (this) {
            MONTH -> Interval(it.end, Period.months(1))
        }
    }

    private fun previousInterval(interval: Interval): Interval = interval.let {
        when (this) {
            MONTH -> Interval(interval.start.minusMonths(1), Period.months(1))
        }
    }
}