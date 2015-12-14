/*
 * Copyright (C) 2015 Mantas Varnagiris.
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

package com.mvcoding.expensius.paging

import java.lang.Math.*

data class Page(
        val first: Int,
        val size: Int,
        private val preferredSize: Int = size) {
    val last = max(first + size - 1, 0)

    fun previousPage() = Page(
            max(first - size, 0),
            if (first - size < 0) size - abs(first - size) else size,
            preferredSize)

    fun nextPage() = Page(first + size, max(size, preferredSize), preferredSize)

    fun rangeTo(dataSetSize: Int) = first.rangeTo(min(last, dataSetSize - 1))
}