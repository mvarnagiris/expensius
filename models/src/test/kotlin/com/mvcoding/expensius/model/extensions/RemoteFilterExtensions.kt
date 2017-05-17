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

package com.mvcoding.expensius.model.extensions

import com.mvcoding.expensius.model.RemoteFilter
import com.mvcoding.expensius.model.ReportPeriod
import com.mvcoding.expensius.model.ReportPeriod.MONTH
import org.joda.time.DateTimeConstants

fun anInterval(reportPeriod: ReportPeriod = MONTH) = reportPeriod.interval(System.currentTimeMillis() - DateTimeConstants.MILLIS_PER_DAY.toLong() * 30 * anInt(1000))
fun aRemoteFilter(reportPeriod: ReportPeriod = MONTH) = RemoteFilter(aUserId(), anInterval(reportPeriod))
fun RemoteFilter.withInterval(interval: org.joda.time.Interval) = copy(interval = interval)