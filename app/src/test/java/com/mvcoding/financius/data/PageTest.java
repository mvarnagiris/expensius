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

package com.mvcoding.financius.data;

import com.mvcoding.financius.BaseTest;
import com.mvcoding.financius.data.paging.Page;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PageTest extends BaseTest {
    @Test public void getPreviousPage_returnsFullPreviousPage_whenThereAreEnoughSize() throws Exception {
        final Page page = new Page(5, 5);

        final Page previousPage = page.getPreviousPage();

        assertThat(previousPage).isNotNull();
        assertThat(previousPage.getStart()).isEqualTo(0);
        assertThat(previousPage.getSize()).isEqualTo(5);
    }

    @Test public void getPreviousPage_returnsNotAFullPreviousPage_whenThereAreNotEnoughSize() throws Exception {
        final Page page = new Page(3, 5);

        final Page previousPage = page.getPreviousPage();

        assertThat(previousPage).isNotNull();
        assertThat(previousPage.getStart()).isEqualTo(0);
        assertThat(previousPage.getSize()).isEqualTo(3);
    }

    @Test public void getPreviousPage_returnsEmptyPage_whenItIsAlreadyFirstPage() throws Exception {
        final Page page = new Page(0, 5);

        final Page previousPage = page.getPreviousPage();

        assertThat(previousPage).isNotNull();
        assertThat(previousPage.getStart()).isEqualTo(0);
        assertThat(previousPage.getSize()).isEqualTo(0);
    }

    @Test public void getPreviousPage_returnsEmptyPage_whenItIsAlreadyEmptyPage() throws Exception {
        final Page page = new Page(0, 5);

        final Page previousPage = page.getPreviousPage().getPreviousPage();

        assertThat(previousPage).isNotNull();
        assertThat(previousPage.getStart()).isEqualTo(0);
        assertThat(previousPage.getSize()).isEqualTo(0);
    }

    @Test public void getNextPage_returnsNextPage_whenItIsAFirstPage() throws Exception {
        final Page page = new Page(0, 5);

        final Page nextPage = page.getNextPage();

        assertThat(nextPage).isNotNull();
        assertThat(nextPage.getStart()).isEqualTo(5);
        assertThat(nextPage.getSize()).isEqualTo(5);
    }

    @Test public void getNextPage_returnsNextPage_whenItIsNotAFirstPage() throws Exception {
        final Page page = new Page(5, 5);

        final Page nextPage = page.getNextPage();

        assertThat(nextPage).isNotNull();
        assertThat(nextPage.getStart()).isEqualTo(10);
        assertThat(nextPage.getSize()).isEqualTo(5);
    }

    @Test public void getNextPage_returnsFullNextPage_whenItIsNotAFullFirstPage() throws Exception {
        final Page page = new Page(3, 5);

        final Page nextPage = page.getPreviousPage().getNextPage();

        assertThat(nextPage).isNotNull();
        assertThat(nextPage.getStart()).isEqualTo(3);
        assertThat(nextPage.getSize()).isEqualTo(5);
    }

    @Test public void getNextPage_returnsFullNextPage_whenItIsEmptyPage() throws Exception {
        final Page page = new Page(0, 5);

        final Page nextPage = page.getPreviousPage().getNextPage();

        assertThat(nextPage).isNotNull();
        assertThat(nextPage.getStart()).isEqualTo(0);
        assertThat(nextPage.getSize()).isEqualTo(5);
    }
}