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

import com.mvcoding.expensius.anInt
import org.joda.time.DateTimeConstants
import org.joda.time.Interval

fun anInterval(reportPeriod: ReportPeriod = ReportPeriod.MONTH) = reportPeriod.interval(System.currentTimeMillis() - DateTimeConstants.MILLIS_PER_DAY.toLong() * 30 * anInt(1000))
fun aRemoteFilter(reportPeriod: ReportPeriod = ReportPeriod.MONTH) = RemoteFilter(aUserId(), anInterval(reportPeriod))
fun RemoteFilter.withInterval(interval: Interval) = copy(interval = interval)