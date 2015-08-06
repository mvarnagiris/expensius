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

package com.mvcoding.financius.util.recyclerview;

import com.mvcoding.financius.BaseTest;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PagingTest extends BaseTest {
    private static final int THRESHOLD = 5;
    private static final int FIRST_VISIBLE_ITEM_POSITION = 10;
    private static final int VISIBLE_ITEM_COUNT = 10;
    private static final int TOTAL_ITEM_COUNT = FIRST_VISIBLE_ITEM_POSITION + VISIBLE_ITEM_COUNT + THRESHOLD;

    private Paging paging;

    @Override public void setUp() {
        super.setUp();
        paging = new Paging(THRESHOLD);
    }

    @Test public void hasEnteredStartPagingArea_returnsTrue_whenItWasNotInStartPagingAreaButIsNow() throws Exception {
        paging.updateValues(THRESHOLD, 0, 0);

        assertThat(paging.hasEnteredStartPagingArea()).isTrue();
    }

    @Test public void hasEnteredStartPagingArea_returnsTrue_whenItWasInStartPagingAreaAndIsNowAndResetWasCalled() throws Exception {
        paging.updateValues(THRESHOLD, 0, 0);
        paging.reset();
        paging.updateValues(THRESHOLD, 0, 0);

        assertThat(paging.hasEnteredStartPagingArea()).isTrue();
    }

    @Test public void hasEnteredStartPagingArea_returnsTrue_whenItWasInStartThenLeftThenCameBack() throws Exception {
        paging.updateValues(THRESHOLD, 0, 0);
        paging.updateValues(THRESHOLD + 1, 0, 0);
        paging.updateValues(THRESHOLD, 0, 0);

        assertThat(paging.hasEnteredStartPagingArea()).isTrue();
    }

    @Test public void hasEnteredStartPagingArea_returnsFalse_whenItWasInStartPagingAreaAndIsNow() throws Exception {
        paging.updateValues(THRESHOLD, 0, 0);
        paging.updateValues(THRESHOLD, 0, 0);

        assertThat(paging.hasEnteredStartPagingArea()).isFalse();
    }

    @Test public void hasEnteredStartPagingArea_returnsFalse_whenItLeftStartPagingArea() throws Exception {
        paging.updateValues(THRESHOLD, 0, 0);
        paging.updateValues(THRESHOLD + 1, 0, 0);

        assertThat(paging.hasEnteredStartPagingArea()).isFalse();
    }

    @Test public void hasLeftStartPagingArea_returnsTrue_whenItWasInStartPagingAreaButItIsNotNow() throws Exception {
        paging.updateValues(THRESHOLD, 0, 0);
        paging.updateValues(THRESHOLD + 1, 0, 0);

        assertThat(paging.hasLeftStartPagingArea()).isTrue();
    }

    @Test public void hasLeftStartPagingArea_returnsFalse_whenItWasNotInStartPagingAreaAndItIsNotNow() throws Exception {
        paging.updateValues(THRESHOLD + 1, 0, 0);

        assertThat(paging.hasLeftStartPagingArea()).isFalse();
    }

    @Test public void hasLeftStartPagingArea_returnsFalse_whenItEntersStartPagingArea() throws Exception {
        paging.updateValues(THRESHOLD, 0, 0);

        assertThat(paging.hasLeftStartPagingArea()).isFalse();
    }

    @Test public void hasEnteredEndPagingArea_returnsTrue_whenItWasNotInEndPagingAreaButIsNow() throws Exception {
        paging.updateValues(FIRST_VISIBLE_ITEM_POSITION, VISIBLE_ITEM_COUNT, TOTAL_ITEM_COUNT);

        assertThat(paging.hasEnteredEndPagingArea()).isTrue();
    }

    @Test public void hasEnteredEndPagingArea_returnsTrue_whenItWasInEndPagingAreaAndIsNowAndResetWasCalled() throws Exception {
        paging.updateValues(FIRST_VISIBLE_ITEM_POSITION, VISIBLE_ITEM_COUNT, TOTAL_ITEM_COUNT);
        paging.reset();
        paging.updateValues(FIRST_VISIBLE_ITEM_POSITION, VISIBLE_ITEM_COUNT, TOTAL_ITEM_COUNT);

        assertThat(paging.hasEnteredEndPagingArea()).isTrue();
    }

    @Test public void hasEnteredEndPagingArea_returnsTrue_whenItWasInEndThenLeftThenCameBack() throws Exception {
        paging.updateValues(FIRST_VISIBLE_ITEM_POSITION, VISIBLE_ITEM_COUNT, TOTAL_ITEM_COUNT);
        paging.updateValues(FIRST_VISIBLE_ITEM_POSITION, VISIBLE_ITEM_COUNT, TOTAL_ITEM_COUNT + 1);
        paging.updateValues(FIRST_VISIBLE_ITEM_POSITION, VISIBLE_ITEM_COUNT, TOTAL_ITEM_COUNT);

        assertThat(paging.hasEnteredEndPagingArea()).isTrue();
    }

    @Test public void hasEnteredEndPagingArea_returnsFalse_whenItWasInEndPagingAreaAndIsNow() throws Exception {
        paging.updateValues(FIRST_VISIBLE_ITEM_POSITION, VISIBLE_ITEM_COUNT, TOTAL_ITEM_COUNT);
        paging.updateValues(FIRST_VISIBLE_ITEM_POSITION, VISIBLE_ITEM_COUNT, TOTAL_ITEM_COUNT);

        assertThat(paging.hasEnteredEndPagingArea()).isFalse();
    }

    @Test public void hasEnteredEndPagingArea_returnsFalse_whenItLeftEndPagingArea() throws Exception {
        paging.updateValues(FIRST_VISIBLE_ITEM_POSITION, VISIBLE_ITEM_COUNT, TOTAL_ITEM_COUNT);
        paging.updateValues(FIRST_VISIBLE_ITEM_POSITION, VISIBLE_ITEM_COUNT, TOTAL_ITEM_COUNT + 1);

        assertThat(paging.hasEnteredEndPagingArea()).isFalse();
    }

    @Test public void hasLeftEndPagingArea_returnsTrue_whenItWasInEndPagingAreaButItIsNotNow() throws Exception {
        paging.updateValues(FIRST_VISIBLE_ITEM_POSITION, VISIBLE_ITEM_COUNT, TOTAL_ITEM_COUNT);
        paging.updateValues(FIRST_VISIBLE_ITEM_POSITION, VISIBLE_ITEM_COUNT, TOTAL_ITEM_COUNT + 1);

        assertThat(paging.hasLeftEndPagingArea()).isTrue();
    }

    @Test public void hasLeftEndPagingArea_returnsFalse_whenItWasNotInEndPagingAreaAndItIsNotNow() throws Exception {
        paging.updateValues(FIRST_VISIBLE_ITEM_POSITION, VISIBLE_ITEM_COUNT, TOTAL_ITEM_COUNT + 1);

        assertThat(paging.hasLeftEndPagingArea()).isFalse();
    }

    @Test public void hasLeftEndPagingArea_returnsFalse_whenItEntersEndPagingArea() throws Exception {
        paging.updateValues(FIRST_VISIBLE_ITEM_POSITION, VISIBLE_ITEM_COUNT, TOTAL_ITEM_COUNT);

        assertThat(paging.hasLeftEndPagingArea()).isFalse();
    }
}