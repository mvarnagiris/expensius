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

class PageTest {
    @Test
    fun previousPageIsFullWhenThereIsEnoughSize() {
        val page = Page(5, 5)

        val previousPage = page.previousPage()

        assertThat(previousPage.first, equalTo(0))
        assertThat(previousPage.last, equalTo(4))
        assertThat(previousPage.size, equalTo(5))
    }

    @Test
    fun previousPageIsNotFullWhenThereIsNotEnoughSize() {
        val page = Page(3, 5)

        val previousPage = page.previousPage()

        assertThat(previousPage.first, equalTo(0))
        assertThat(previousPage.last, equalTo(2))
        assertThat(previousPage.size, equalTo(3))
    }

    @Test
    fun previousPageIsEmptyWhenItIsFirstPage() {
        val page = Page(0, 5)

        val previousPage = page.previousPage()

        assertThat(previousPage.first, equalTo(0))
        assertThat(previousPage.last, equalTo(0))
        assertThat(previousPage.size, equalTo(0))
    }

    @Test
    fun previousPageIsEmptyWhenItIsEmptyPage() {
        val page = Page(0, 5)

        val previousPage = page.previousPage().previousPage()

        assertThat(previousPage.first, equalTo(0))
        assertThat(previousPage.last, equalTo(0))
        assertThat(previousPage.size, equalTo(0))
    }

    @Test
    fun previousPageSizeIsRestoredWhenItIsEmptyPage() {
        val page = Page(0, 5)

        val previousPage = page.previousPage().nextPage()

        assertThat(previousPage.first, equalTo(0))
        assertThat(previousPage.last, equalTo(4))
        assertThat(previousPage.size, equalTo(5))
    }

    @Test
    fun nextPageIsFullWhenItIsFirstPage() {
        val page = Page(0, 5)

        val nextPage = page.nextPage()

        assertThat(nextPage.first, equalTo(5))
        assertThat(nextPage.last, equalTo(9))
        assertThat(nextPage.size, equalTo(5))
    }

    @Test
    fun nextPageIsFullWhenItIsNotFirstPage() {
        val page = Page(5, 5)

        val nextPage = page.nextPage()

        assertThat(nextPage.first, equalTo(10))
        assertThat(nextPage.last, equalTo(14))
        assertThat(nextPage.size, equalTo(5))
    }

    @Test
    fun nextPageIsFullWhenItIsNotFullFirstPage() {
        val page = Page(3, 5)

        val nextPage = page.previousPage().nextPage()

        assertThat(nextPage.first, equalTo(3))
        assertThat(nextPage.last, equalTo(7))
        assertThat(nextPage.size, equalTo(5))
    }

    @Test
    fun nextPageIsFullWhenItIsEmptyPage() {
        val page = Page(0, 5)

        val nextPage = page.previousPage().nextPage()

        assertThat(nextPage.first, equalTo(0))
        assertThat(nextPage.last, equalTo(4))
        assertThat(nextPage.size, equalTo(5))
    }

    @Test
    fun rangeIsFullWhenThereAreEnoughItems() {
        val page = Page(0, 5)

        val range = page.rangeTo(5)

        assertThat(range.start, equalTo(0))
        assertThat(range.last, equalTo(4))
    }

    @Test
    fun rangeIsNotFullWhenThereAreNotEnoughItems() {
        val page = Page(0, 5)

        val range = page.rangeTo(3)

        assertThat(range.start, equalTo(0))
        assertThat(range.last, equalTo(2))
    }

    @Test
    fun rangeIsEmptyWhenThereAreNoItemsForThisPage() {
        val page = Page(5, 5)

        val range = page.rangeTo(5)

        assertThat(range.isEmpty(), equalTo(true))
    }
}