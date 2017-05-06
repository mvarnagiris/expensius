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

fun aFilter() = Filter(aUserId(), aReportPeriod(), aReportGroup(), aTimestamp())
fun Filter.withReportPeriod(reportPeriod: ReportPeriod) = copy(reportPeriod = reportPeriod)
fun Filter.withIntervalBaseTimestamp(timestamp: Timestamp) = copy(intervalBaseTimestamp = timestamp)
fun Filter.withIntervalBaseTimestamp(timestamp: Long) = withIntervalBaseTimestamp(Timestamp(timestamp))