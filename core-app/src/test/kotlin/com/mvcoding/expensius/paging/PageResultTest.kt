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

import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test

class PageResultTest {
    @Test
    fun doesNotHavePreviousPageWhenPageStartsAt0() {
        val pageResult = PageResult<Unit>(Page(0, 5), listOf())

        assertThat(pageResult.hasPreviousPage(), `is`(false))
    }

    @Test
    fun hasPreviousPageWhenPageStartsAtMoreThan0() {
        val pageResult = PageResult<Unit>(Page(5, 5), listOf())

        assertThat(pageResult.hasPreviousPage(), `is`(true))
    }

    @Test
    fun doesNotHaveNextPageWhenItemsSizeIsLessThanPageSize() {
        val pageResult = PageResult<Unit>(Page(0, 5), listOf())

        assertThat(pageResult.hasNextPage(), `is`(false))
    }

    @Test
    fun hasNextPageWhenItemsSizeIsSameAsPageSize() {
        val pageResult = PageResult(Page(0, 2), listOf(Unit, Unit))

        assertThat(pageResult.hasNextPage(), `is`(true))
    }
}