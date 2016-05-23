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

    fun step(): Observable<Step> = stepSubject
    fun setStep(step: Step) = stepSubject.onNext(step)

    enum class Step {
        HOUR, DAY, WEEK, FORTNIGHT, MONTH, QUARTER_YEAR, HALF_YEAR, YEAR;

        fun toPeriod() = when (this) {
            HOUR -> Period.hours(1)
            DAY -> Period.days(1)
            WEEK -> Period.weeks(1)
            FORTNIGHT -> Period.weeks(2)
            MONTH -> Period.months(1)
            QUARTER_YEAR -> Period.months(3)
            HALF_YEAR -> Period.months(6)
            YEAR -> Period.years(1)
        }

        fun toNumberOfSteps(interval: Interval) = when (this) {
            HOUR -> Hours.hoursIn(interval).hours
            DAY -> Days.daysIn(interval).days
            WEEK -> Weeks.weeksIn(interval).weeks
            FORTNIGHT -> Weeks.weeksIn(interval).weeks / 2
            MONTH -> Months.monthsIn(interval).months
            QUARTER_YEAR -> Months.monthsIn(interval).months / 3
            HALF_YEAR -> Months.monthsIn(interval).months / 6
            YEAR -> Years.yearsIn(interval).years
        }
    }
}