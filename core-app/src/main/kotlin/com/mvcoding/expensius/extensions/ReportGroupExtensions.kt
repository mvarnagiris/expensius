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

import com.mvcoding.expensius.model.ReportGroup
import com.mvcoding.expensius.model.ReportGroup.DAY
import com.mvcoding.expensius.model.Timestamp
import org.joda.time.DateTime
import org.joda.time.Days
import org.joda.time.Interval
import org.joda.time.Period

fun ReportGroup.toPeriod(): Period = when (this) {
    DAY -> Period.days(1)
}

fun ReportGroup.toNumberOfGroups(interval: Interval) = when (this) {
    DAY -> Days.daysIn(interval).days
}

fun ReportGroup.splitIntoGroupIntervals(interval: Interval): List<Interval> {
    val period = toPeriod()
    if (interval.endMillis < Interval(interval.start, period).endMillis) return emptyList()

    val startInterval = toInterval(interval.startMillis)
    val normalizedInterval =
            if (interval.startMillis != startInterval.startMillis)
                when (this) {
                    DAY -> interval.withStart(interval.start.plusDays(1).withTimeAtStartOfDay())
                }
            else interval

    val numberOfSteps = toNumberOfGroups(normalizedInterval)
    return (0..numberOfSteps - 1).map {
        toInterval(normalizedInterval.start.plus(period.multipliedBy(it)).millis)
    }
}

fun ReportGroup.toInterval(timestamp: Timestamp) = toInterval(timestamp.millis)
fun ReportGroup.toInterval(millis: Long) = DateTime(millis).let {
    when (this) {
        DAY -> it.withTimeAtStartOfDay()
    }
}.let { Interval(it, toPeriod()) }