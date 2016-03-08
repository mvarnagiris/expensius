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

import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PagingTest {
    val THRESHOLD = 5
    val FIRST_VISIBLE_ITEM_POSITION = 10
    val VISIBLE_ITEM_COUNT = 10
    val TOTAL_ITEM_COUNT = FIRST_VISIBLE_ITEM_POSITION + VISIBLE_ITEM_COUNT + THRESHOLD

    val paging = Paging(THRESHOLD)

    @Test
    fun hasEnteredStartPagingAreaReturnsTrueWhenItWasNotInStartPagingAreaButIsNow() {
        paging.updateValues(THRESHOLD, 0, 0)

        assertTrue { paging.hasEnteredStartPagingArea }
    }

    @Test
    fun hasEnteredStartPagingAreaReturnsTrueWhenItWasInStartPagingAreaAndIsNowAndResetWasCalled() {
        paging.updateValues(THRESHOLD, 0, 0)
        paging.reset()
        paging.updateValues(THRESHOLD, 0, 0)

        assertTrue { paging.hasEnteredStartPagingArea }
    }

    @Test
    fun hasEnteredStartPagingAreaReturnsTrueWhenItWasInStartThenLeftThenCameBack() {
        paging.updateValues(THRESHOLD, 0, 0)
        paging.updateValues(THRESHOLD + 1, 0, 0)
        paging.updateValues(THRESHOLD, 0, 0)

        assertTrue { paging.hasEnteredStartPagingArea }
    }

    @Test
    fun hasEnteredStartPagingAreaReturnsFalseWhenItWasInStartPagingAreaAndIsNow() {
        paging.updateValues(THRESHOLD, 0, 0)
        paging.updateValues(THRESHOLD, 0, 0)

        assertFalse { paging.hasEnteredStartPagingArea }
    }

    @Test
    fun hasEnteredStartPagingAreaReturnsFalseWhenItLeftStartPagingArea() {
        paging.updateValues(THRESHOLD, 0, 0)
        paging.updateValues(THRESHOLD + 1, 0, 0)

        assertFalse { paging.hasEnteredStartPagingArea }
    }

    @Test
    fun hasLeftStartPagingAreaReturnsTrueWhenItWasInStartPagingAreaButItIsNotNow() {
        paging.updateValues(THRESHOLD, 0, 0)
        paging.updateValues(THRESHOLD + 1, 0, 0)

        assertTrue { paging.hasLeftStartPagingArea }
    }

    @Test
    fun hasLeftStartPagingAreaReturnsFalseWhenItWasNotInStartPagingAreaAndItIsNotNow() {
        paging.updateValues(THRESHOLD + 1, 0, 0)

        assertFalse { paging.hasLeftStartPagingArea }
    }

    @Test
    fun hasLeftStartPagingAreaReturnsFalseWhenItEntersStartPagingArea() {
        paging.updateValues(THRESHOLD, 0, 0)

        assertFalse { paging.hasLeftStartPagingArea }
    }

    @Test
    fun hasEnteredEndPagingAreaReturnsTrueWhenItWasNotInEndPagingAreaButIsNow() {
        paging.updateValues(FIRST_VISIBLE_ITEM_POSITION, VISIBLE_ITEM_COUNT, TOTAL_ITEM_COUNT)

        assertTrue { paging.hasEnteredEndPagingArea }
    }

    @Test
    fun hasEnteredEndPagingAreaReturnsTrueWhenItWasInEndPagingAreaAndIsNowAndResetWasCalled() {
        paging.updateValues(FIRST_VISIBLE_ITEM_POSITION, VISIBLE_ITEM_COUNT, TOTAL_ITEM_COUNT)
        paging.reset()
        paging.updateValues(FIRST_VISIBLE_ITEM_POSITION, VISIBLE_ITEM_COUNT, TOTAL_ITEM_COUNT)

        assertTrue { paging.hasEnteredEndPagingArea }
    }

    @Test
    fun hasEnteredEndPagingAreaReturnsTrueWhenItWasInEndThenLeftThenCameBack() {
        paging.updateValues(FIRST_VISIBLE_ITEM_POSITION, VISIBLE_ITEM_COUNT, TOTAL_ITEM_COUNT)
        paging.updateValues(FIRST_VISIBLE_ITEM_POSITION, VISIBLE_ITEM_COUNT, TOTAL_ITEM_COUNT + 1)
        paging.updateValues(FIRST_VISIBLE_ITEM_POSITION, VISIBLE_ITEM_COUNT, TOTAL_ITEM_COUNT)

        assertTrue { paging.hasEnteredEndPagingArea }
    }

    @Test
    fun hasEnteredEndPagingAreaReturnsFalseWhenItWasInEndPagingAreaAndIsNow() {
        paging.updateValues(FIRST_VISIBLE_ITEM_POSITION, VISIBLE_ITEM_COUNT, TOTAL_ITEM_COUNT)
        paging.updateValues(FIRST_VISIBLE_ITEM_POSITION, VISIBLE_ITEM_COUNT, TOTAL_ITEM_COUNT)

        assertFalse { paging.hasEnteredEndPagingArea }
    }

    @Test
    fun hasEnteredEndPagingAreaReturnsFalseWhenItLeftEndPagingArea() {
        paging.updateValues(FIRST_VISIBLE_ITEM_POSITION, VISIBLE_ITEM_COUNT, TOTAL_ITEM_COUNT)
        paging.updateValues(FIRST_VISIBLE_ITEM_POSITION, VISIBLE_ITEM_COUNT, TOTAL_ITEM_COUNT + 1)

        assertFalse { paging.hasEnteredEndPagingArea }
    }

    @Test
    fun hasLeftEndPagingAreaReturnsTrueWhenItWasInEndPagingAreaButItIsNotNow() {
        paging.updateValues(FIRST_VISIBLE_ITEM_POSITION, VISIBLE_ITEM_COUNT, TOTAL_ITEM_COUNT)
        paging.updateValues(FIRST_VISIBLE_ITEM_POSITION, VISIBLE_ITEM_COUNT, TOTAL_ITEM_COUNT + 1)

        assertTrue { paging.hasLeftEndPagingArea }
    }

    @Test
    fun hasLeftEndPagingAreaReturnsFalseWhenItWasNotInEndPagingAreaAndItIsNotNow() {
        paging.updateValues(FIRST_VISIBLE_ITEM_POSITION, VISIBLE_ITEM_COUNT, TOTAL_ITEM_COUNT + 1)

        assertFalse { paging.hasLeftEndPagingArea }
    }

    @Test
    fun hasLeftEndPagingAreaReturnsFalseWhenItEntersEndPagingArea() {
        paging.updateValues(FIRST_VISIBLE_ITEM_POSITION, VISIBLE_ITEM_COUNT, TOTAL_ITEM_COUNT)

        assertFalse { paging.hasLeftEndPagingArea }
    }
}