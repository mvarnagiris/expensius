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

class Paging(private val pagingThreshold: Int) {

    var hasEnteredStartPagingArea = false
        private set
    var hasLeftStartPagingArea = false
        private set
    var hasEnteredEndPagingArea = false
        private set
    var hasLeftEndPagingArea = false
        private set

    private var isInStartPagingArea = false
    private var isInEndPagingArea = false

    fun updateValues(firstVisibleItemPosition: Int, visibleItemCount: Int, totalItemCount: Int) {
        if (firstVisibleItemPosition <= pagingThreshold) {
            hasEnteredStartPagingArea = !isInStartPagingArea
            hasLeftStartPagingArea = false
            isInStartPagingArea = true
        } else {
            hasLeftStartPagingArea = isInStartPagingArea
            hasEnteredStartPagingArea = false
            isInStartPagingArea = false
        }

        if (firstVisibleItemPosition + visibleItemCount >= totalItemCount - pagingThreshold) {
            hasEnteredEndPagingArea = !isInEndPagingArea
            hasLeftEndPagingArea = false
            isInEndPagingArea = true
        } else {
            hasLeftEndPagingArea = isInEndPagingArea
            hasEnteredEndPagingArea = false
            isInEndPagingArea = false
        }
    }

    fun reset() {
        isInStartPagingArea = false
        isInEndPagingArea = false
        hasEnteredStartPagingArea = false
        hasLeftStartPagingArea = false
        hasEnteredEndPagingArea = false
        hasLeftEndPagingArea = false
    }
}