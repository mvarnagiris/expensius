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
import org.joda.time.Days
import org.joda.time.Interval
import org.joda.time.Months
import org.joda.time.Period
import org.joda.time.Weeks
import org.joda.time.Years

fun ReportGroup.toPeriod(): Period = when (this) {
    ReportGroup.DAY -> Period.days(1)
    ReportGroup.WEEK -> Period.weeks(1)
    ReportGroup.MONTH -> Period.months(1)
    ReportGroup.YEAR -> Period.years(1)
}

fun ReportGroup.toNumberOfGroups(interval: Interval) = when (this) {
    ReportGroup.DAY -> Days.daysIn(interval).days
    ReportGroup.WEEK -> Weeks.weeksIn(interval).weeks
    ReportGroup.MONTH -> Months.monthsIn(interval).months
    ReportGroup.YEAR -> Years.yearsIn(interval).years
}

fun ReportGroup.splitIntoGroupIntervals(interval: Interval): List<Interval> {
    val period = toPeriod()
    if (interval.endMillis < Interval(interval.start, period).endMillis) return emptyList()

    val startInterval = toInterval(interval.startMillis)
    val normalizedInterval =
            if (interval.startMillis != startInterval.startMillis)
                when (this) {
                    ReportGroup.DAY -> interval.withStart(interval.start.plusDays(1).withTimeAtStartOfDay())
                    ReportGroup.WEEK -> interval.withStart(interval.start.plusWeeks(1).withDayOfWeek(1).withTimeAtStartOfDay())
                    ReportGroup.MONTH -> interval.withStart(interval.start.plusMonths(1).withDayOfMonth(1).withTimeAtStartOfDay())
                    ReportGroup.YEAR -> interval.withStart(interval.start.plusYears(1).withMonthOfYear(1).withDayOfMonth(1).withTimeAtStartOfDay())
                }
            else interval

    val numberOfSteps = toNumberOfGroups(normalizedInterval)
    return (0..numberOfSteps - 1).map {
        toInterval(normalizedInterval.start.plus(period.multipliedBy(it)).millis)
    }
}

fun ReportGroup.toInterval(timestamp: Long) = org.joda.time.DateTime(timestamp).let {
    when (this) {
        ReportGroup.DAY -> it.withTimeAtStartOfDay()
        ReportGroup.WEEK -> it.withDayOfWeek(1).withTimeAtStartOfDay()
        ReportGroup.MONTH -> it.withDayOfMonth(1).withTimeAtStartOfDay()
        ReportGroup.YEAR -> it.withMonthOfYear(1).withDayOfMonth(1).withTimeAtStartOfDay()
    }
}.let { Interval(it, toPeriod()) }