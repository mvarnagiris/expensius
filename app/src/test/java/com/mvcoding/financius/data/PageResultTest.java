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
import com.mvcoding.financius.data.paging.PageResult;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class PageResultTest extends BaseTest {
    @Test public void hasPrevious_returnsFalse_whenPageStartIs0() throws Exception {
        final Page page = new Page(0, 1);
        final PageResult<Object> pageResult = new PageResult<>(page, Collections.emptyList(), false);

        assertThat(pageResult.hasPrevious()).isFalse();
    }

    @Test public void hasPrevious_returnsTrue_whenPageStartMoreThan0() throws Exception {
        final Page page = new Page(1, 1);
        final PageResult<Object> pageResult = new PageResult<>(page, Collections.emptyList(), false);

        assertThat(pageResult.hasPrevious()).isTrue();
    }

    @Test public void hasNext_returnsFalse_whenReturnedItemSizeIsLessThanRequestedSize() throws Exception {
        final Page page = new Page(0, 2);
        final PageResult<Object> pageResult = new PageResult<>(page, Collections.singletonList(new Object()), false);

        assertThat(pageResult.hasNext()).isFalse();
    }

    @Test public void hasNext_returnsTrue_whenReturnedItemSizeEqualsRequestedSize() throws Exception {
        final Page page = new Page(0, 2);
        final PageResult<Object> pageResult = new PageResult<>(page, Arrays.asList(new Object(), new Object()), false);

        assertThat(pageResult.hasNext()).isTrue();
    }
}