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

import org.junit.Test;

import static org.assertj.core.api.Assertions.fail;

public class PageLoaderTest extends BaseTest {
    @Test public void load_emitsEmptyList_whenCursorIsEmpty() throws Exception {
        fail("Not implemented.");
    }

    @Test public void load_emitsLessItemsThanRequested_whenCursorContainsLessItemsThanRequested() throws Exception {
        fail("Not implemented.");
    }

    @Test public void load_emitsRequestedAmountOfItems_whenCursorContainsEnoughItems() throws Exception {
        fail("Not implemented.");
    }

    @Test public void load_emitsSecondPageOfTags_whenCursorContainsEnoughItemsAndSecondPageWasRequested() throws Exception {
        fail("Not implemented.");
    }

    @Test public void load_emitsSameItems_whenSamePageWasRequested() throws Exception {
        fail("Not implemented.");
    }

    @Test public void resetCache_whenCursorIsReloaded() throws Exception {
        fail("Not implemented.");
    }
}