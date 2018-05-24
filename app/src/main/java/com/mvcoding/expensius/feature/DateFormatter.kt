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

package com.mvcoding.expensius.feature

//class DateFormatter(private val context: Context) {
//
//    val dateFormatter: DateTimeFormatter = DateTimeFormatterBuilder()
//            .appendMonthOfYearText()
//            .appendLiteral(" ")
//            .appendYear(0, 4)
//            .toFormatter()
//
//    fun formatDateRelativeToToday(timestamp: Long): String {
//        val dateTime = DateTime(timestamp)
//
//        if (isToday(dateTime)) {
//            return context.getString(R.string.today)
//        } else if (isYesterday(dateTime)) {
//            return context.getString(R.string.yesterday)
//        } else if (isTomorrow(dateTime)) {
//            return context.getString(R.string.tomorrow)
//        }
//
//        return formatDateTime(context, dateTime, FORMAT_SHOW_DATE or FORMAT_SHOW_WEEKDAY or FORMAT_ABBREV_ALL)
//    }
//
//    fun formatDateShort(dateTime: DateTime): String = formatDateTime(context, dateTime, FORMAT_SHOW_DATE or FORMAT_ABBREV_ALL)
//    fun formatInterval(reportPeriod: ReportPeriod, interval: Interval): String = when (reportPeriod) {
//        ReportPeriod.MONTH -> dateFormatter.print(interval.start)
//    }
//
//    fun formatInterval(reportGroup: ReportGroup, interval: Interval): String = when (reportGroup) {
//        ReportGroup.DAY -> DateTimeFormat.shortDate().print(interval.start)
//    }
//
//    fun formatInterval(interval: Interval): String = when (interval.toPeriod()) {
//        Period.days(1) -> interval.start.dayOfMonth().asText
//        else -> PeriodFormat.getDefault().print(interval.toPeriod())
//    }
//}