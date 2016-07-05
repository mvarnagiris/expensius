package com.mvcoding.expensius.feature

import org.joda.time.DateTime
import org.joda.time.Days
import org.joda.time.Interval
import org.joda.time.Months
import org.joda.time.Period
import org.joda.time.Weeks
import org.joda.time.Years

enum class ReportGroup {
    DAY, WEEK, MONTH, YEAR;

    fun toPeriod(): Period = when (this) {
        DAY -> Period.days(1)
        WEEK -> Period.weeks(1)
        MONTH -> Period.months(1)
        YEAR -> Period.years(1)
    }

    fun toNumberOfGroups(interval: Interval) = when (this) {
        DAY -> Days.daysIn(interval).days
        WEEK -> Weeks.weeksIn(interval).weeks
        MONTH -> Months.monthsIn(interval).months
        YEAR -> Years.yearsIn(interval).years
    }

    fun splitIntoGroupIntervals(interval: Interval): List<Interval> {
        val period = toPeriod()
        if (interval.endMillis < Interval(interval.start, period).endMillis) return emptyList()

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

        val numberOfSteps = toNumberOfGroups(normalizedInterval)
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