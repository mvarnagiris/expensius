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

package com.mvcoding.expensius.extensions

import com.mvcoding.expensius.model.ReportPeriod
import com.mvcoding.expensius.model.ReportPeriod.MONTH
import org.joda.time.DateTime
import org.joda.time.Interval
import org.joda.time.Period.months

fun ReportPeriod.interval(millis: Long): Interval = when (this) {
    MONTH -> DateTime(millis).let { Interval(it.withDayOfMonth(1).withTimeAtStartOfDay(), months(1)) }
}

fun ReportPeriod.nextInterval(interval: Interval): Interval = interval.let {
    when (this) {
        MONTH -> Interval(interval.end, months(1))
    }
}

fun ReportPeriod.previousInterval(interval: Interval): Interval = interval.let {
    when (this) {
        MONTH -> Interval(interval.start.minusMonths(1), months(1))
    }
}