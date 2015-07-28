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

import android.database.Cursor;
import android.support.v4.util.SparseArrayCompat;

import com.mvcoding.financius.BaseTest;

import org.junit.Test;
import org.mockito.Mock;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.when;

public class PageResultTest extends BaseTest {
    @Mock private Cursor cursor;
    @Mock private SparseArrayCompat<Object> allItems;
    @Mock private List<Object> pageItems;
    @Mock private PageLoader.Page page;

    @Test public void hasMoreBefore_returnsFalse_whenPageStartIs0() throws Exception {
        final PageLoader.Page page = new PageLoader.Page(0, 1);
        final PageResult<Object> pageResult = new PageResult<>(cursor, allItems, pageItems, page);

        assertThat(pageResult.hasMoreBefore()).isFalse();
    }

    @Test public void hasMoreBefore_returnsTrue_whenPageStartMoreThan0() throws Exception {
        final PageLoader.Page page = new PageLoader.Page(1, 1);
        final PageResult<Object> pageResult = new PageResult<>(cursor, allItems, pageItems, page);

        assertThat(pageResult.hasMoreBefore()).isTrue();
    }

    @Test public void hasMoreAfter_returnsFalse_whenRequestedPageWantsMoreOrSameAmountOfItemsThanThereAreInCursor() throws Exception {
        final PageLoader.Page page = new PageLoader.Page(0, 2);
        final PageResult<Object> pageResult = new PageResult<>(cursor, allItems, pageItems, page);

        when(cursor.getCount()).thenReturn(1);
        assertThat(pageResult.hasMoreAfter()).isFalse();

        when(cursor.getCount()).thenReturn(2);
        assertThat(pageResult.hasMoreAfter()).isFalse();
    }

    @Test public void hasMoreAfter_returnsTrue_whenRequestedPageWantsLessItemsThanThereAreInCursor() throws Exception {
        final PageLoader.Page page = new PageLoader.Page(0, 2);
        final PageResult<Object> pageResult = new PageResult<>(cursor, allItems, pageItems, page);

        when(cursor.getCount()).thenReturn(3);
        assertThat(pageResult.hasMoreAfter()).isTrue();
    }

    @Test public void getPreviousPage() throws Exception {
        fail("Not implemented.");
    }

    @Test public void getNextPage() throws Exception {
        fail("Not implemented.");
    }
}