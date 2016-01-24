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

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.Test

class PageResultTest {
    @Test
    fun doesNotHavePreviousPageWhenPageStartsAt0() {
        val pageResult = PageResult<Unit>(Page(0, 5), listOf(), true)

        assertThat(pageResult.hasPreviousPage(), equalTo(false))
    }

    @Test
    fun hasPreviousPageWhenPageStartsAtMoreThan0() {
        val pageResult = PageResult<Unit>(Page(5, 5), listOf(), true)

        assertThat(pageResult.hasPreviousPage(), equalTo(true))
    }

    @Test
    fun doesNotHaveNextPageWhenItemsSizeIsLessThanPageSize() {
        val pageResult = PageResult<Unit>(Page(0, 5), listOf(), true)

        assertThat(pageResult.hasNextPage(), equalTo(false))
    }

    @Test
    fun hasNextPageWhenItemsSizeIsSameAsPageSize() {
        val pageResult = PageResult(Page(0, 2), listOf(Unit, Unit), true)

        assertThat(pageResult.hasNextPage(), equalTo(true))
    }

    @Test
    fun positionIsSameAsPageFirstPosition() {
        val pageResult = PageResult(Page(1, 2), listOf(Unit, Unit), true)

        assertThat(pageResult.position, equalTo(1))
    }
}