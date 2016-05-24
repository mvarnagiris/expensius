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

import com.mvcoding.expensius.feature.ReportStep.Step.DAY
import org.joda.time.*
import rx.Observable
import rx.lang.kotlin.BehaviorSubject

class ReportStep {
    private val stepSubject = BehaviorSubject(DAY)

    fun step(): Observable<Step> = stepSubject.asObservable()
    fun setStep(step: Step) = stepSubject.onNext(step)

    enum class Step {
        DAY, WEEK, MONTH, YEAR;

        fun toPeriod() = when (this) {
            DAY -> Period.days(1)
            WEEK -> Period.weeks(1)
            MONTH -> Period.months(1)
            YEAR -> Period.years(1)
        }

        fun toNumberOfSteps(interval: Interval) = when (this) {
            DAY -> Days.daysIn(interval).days
            WEEK -> Weeks.weeksIn(interval).weeks
            MONTH -> Months.monthsIn(interval).months
            YEAR -> Years.yearsIn(interval).years
        }

        fun splitIntoStepIntervals(interval: Interval): List<Interval> {
            val startInterval = toInterval(interval.startMillis)

            val normalizedInterval =
                    if (interval.startMillis != startInterval.startMillis)
                        when (this) {
                            DAY -> interval.withStart(interval.start.plusDays(1).withTimeAtStartOfDay())
                            WEEK -> interval.withStart(interval.start.plusWeeks(1).withDayOfWeek(1).withTimeAtStartOfDay())
                            MONTH -> interval.withStart(interval.start.plusMonths(1).withDayOfMonth(1).withTimeAtStartOfDay())
                            YEAR -> interval.withStart(interval.start.plusYears(1).withMonthOfYear(1).withDayOfMonth(1).withTimeAtStartOfDay())
                        }
                    else interval

            val period = toPeriod()
            val numberOfSteps = toNumberOfSteps(normalizedInterval)
            return (0..numberOfSteps - 1).map {
                toInterval(normalizedInterval.start.plus(period.multipliedBy(it)).millis)
            }
        }

        fun toInterval(timestamp: Long) = DateTime(timestamp).let {
            when (this) {
                DAY -> it.withTimeAtStartOfDay()
                WEEK -> it.withDayOfWeek(1).withTimeAtStartOfDay()
                MONTH -> it.withDayOfMonth(1).withTimeAtStartOfDay()
                YEAR -> it.withMonthOfYear(1).withDayOfMonth(1).withTimeAtStartOfDay()
            }
        }.let { Interval(it, toPeriod()) }
    }
}