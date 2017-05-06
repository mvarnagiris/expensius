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

import java.io.Serializable

data class Filter(
        val userId: UserId,
        val reportPeriod: ReportPeriod,
        val reportGroup: ReportGroup,
        internal val intervalBaseTimestamp: Timestamp,
        internal val intervalPosition: Int = 0) : Serializable {

    val interval = reportPeriod.interval(reportPeriod.interval(intervalBaseTimestamp), intervalPosition)

    fun withNextInterval() = copy(intervalPosition = intervalPosition + 1)
    fun withPreviousInterval() = copy(intervalPosition = intervalPosition - 1)
}