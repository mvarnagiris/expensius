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
import android.database.MatrixCursor;
import android.support.annotation.NonNull;

import com.mvcoding.financius.BaseTest;
import com.mvcoding.financius.data.database.Database;
import com.mvcoding.financius.data.database.DatabaseQuery;

import org.junit.Test;
import org.mockito.Mock;

import java.util.concurrent.atomic.AtomicInteger;

import rx.functions.Action1;
import rx.subjects.PublishSubject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PageLoaderTest extends BaseTest {
    private final ItemDataConverter dataConverter = new ItemDataConverter();
    private final PublishSubject<Page> pageSubject = PublishSubject.create();
    private final PublishSubject<Cursor> cursorSubject = PublishSubject.create();

    @Mock private Database database;
    @Mock private DatabaseQuery databaseQuery;

    private PageLoader<Item> pageLoader;

    @Override public void setUp() {
        super.setUp();
        when(database.load(any(DatabaseQuery.class))).thenReturn(cursorSubject);
        pageLoader = new PageLoader<>(database);
    }

    @Test public void load_emitsEmptyList_whenCursorIsEmpty() throws Exception {
        testLoad(0, 1, 1);
    }

    @Test public void load_emitsLessItemsThanRequested_whenCursorContainsLessItemsThanRequested() throws Exception {
        testLoad(5, 10, 1);
    }

    @Test public void load_emitsLessItemsThanRequested_whenCursorContainsLessItemsThanRequestedAndRequestingSecondPage() throws Exception {
        testLoad(10, 7, 2);
    }

    @Test public void load_emitsRequestedAmountOfItems_whenCursorContainsEnoughItems() throws Exception {
        testLoad(10, 5, 1);
    }

    @Test public void load_emitsRequestedAmountOfItems_whenCursorContainsEnoughItemsAndRequestingSecondPage() throws Exception {
        testLoad(10, 5, 2);
    }

    @Test public void resetCache_whenCursorIsReloaded() throws Exception {
        final AtomicInteger count = new AtomicInteger(0);
        pageLoader.load(dataConverter, databaseQuery, pageSubject).subscribe(new Action1<PageResult<Item>>() {
            @Override public void call(PageResult<Item> itemPageResult) {
                if (count.get() == 0) {
                    assertPageResult(itemPageResult, 5, 5, false, true);
                } else if (count.get() == 1) {
                    assertPageResult(itemPageResult, 5, 10, true, false);
                } else {
                    assertPageResult(itemPageResult, 5, 5, true, false);
                }
            }
        });

        prepareCursor(10);

        pageSubject.onNext(new Page(0, 5));
        count.getAndIncrement();
        pageSubject.onNext(new Page(5, 10));
        count.getAndIncrement();
        prepareCursor(10);
    }

    private void testLoad(int cursorCount, int pageSize, int pagesCount) {
        final AtomicInteger page = new AtomicInteger(0);
        pageLoader.load(dataConverter, databaseQuery, pageSubject).subscribe(new Action1<PageResult<Item>>() {
            @Override public void call(PageResult<Item> itemPageResult) {
                final int expectedPageCount = Math.min(pageSize, cursorCount - (page.get() * pageSize));
                final int expectedTotalCount = Math.min((page.get() + 1) * pageSize, cursorCount);
                final boolean hasMoreBefore = page.get() > 0;
                final boolean hasMoreAfter = expectedTotalCount < cursorCount;
                assertPageResult(itemPageResult, expectedPageCount, expectedTotalCount, hasMoreBefore, hasMoreAfter);
            }
        });

        prepareCursor(cursorCount);

        for (int i = 0; i < pagesCount; i++) {
            pageSubject.onNext(new Page(i * pageSize, pageSize));
            page.getAndIncrement();
        }
    }

    private void assertPageResult(PageResult pageResult, int pageCount, int totalCount, boolean hasItemsBefore, boolean hasItemsAfter) {
        assertThat(pageResult).isNotNull();
        assertThat(pageResult.getPageItems()).isNotNull();
        assertThat(pageResult.getPageItems()).hasSize(pageCount);
        assertThat(pageResult.getAllItems()).isNotNull();
        assertThat(pageResult.getAllItems().size()).isEqualTo(totalCount);
        assertThat(pageResult.hasMoreBefore()).isEqualTo(hasItemsBefore);
        assertThat(pageResult.hasMoreAfter()).isEqualTo(hasItemsAfter);
    }

    private void prepareCursor(int count) {
        final MatrixCursor cursor = mock(MatrixCursor.class);
        when(cursor.getCount()).thenReturn(count);
        cursorSubject.onNext(cursor);
    }

    private static class Item {
    }

    private static class ItemDataConverter implements DataConverter<Item> {
        @Override public Item from(@NonNull Cursor cursor) {
            return new Item();
        }
    }
}