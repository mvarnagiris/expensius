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
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.util.SparseArrayCompat;

import com.mvcoding.financius.data.database.Database;
import com.mvcoding.financius.data.database.DatabaseQuery;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import lombok.Getter;
import rx.Observable;

public class PageLoader<T> {
    private final Database database;

    @Inject public PageLoader(@NonNull Database database) {
        this.database = database;
    }

    @NonNull
    public Observable<PageResult<T>> load(@NonNull DataConverter<T> dataConverter, @NonNull DatabaseQuery databaseQuery, @NonNull Observable<Page> pageObservable) {
        final SparseArrayCompat<T> allItems = new SparseArrayCompat<>();
        final Observable<Cursor> cursorObservable = database.load(databaseQuery).doOnNext(cursor -> allItems.clear());

        return Observable.combineLatest(pageObservable, cursorObservable, (page, cursor) -> {
            final List<T> pageItems = new ArrayList<>();
            for (int i = page.start, size = Math.min(cursor.getCount(), page.start + page.count); i < size; i++) {
                T item = allItems.get(i);
                if (item == null) {
                    cursor.moveToPosition(i);
                    item = dataConverter.from(cursor);
                    allItems.put(i, item);
                }
                pageItems.add(item);
            }

            return new PageResult<>(cursor, allItems, pageItems, page);
        });
    }

    @Getter public static class Page {
        private final int start;
        private final int count;

        public Page(@IntRange(from = 0) int start, @IntRange(from = 1) int count) {
            this.start = start;
            this.count = count;
        }
    }
}
