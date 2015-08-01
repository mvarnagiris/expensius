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

package com.mvcoding.financius.data.paging;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v4.util.SparseArrayCompat;

import com.mvcoding.financius.data.DataConverter;
import com.mvcoding.financius.data.database.Database;
import com.mvcoding.financius.data.database.DatabaseQuery;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

public class PageLoader<T> {
    private final Database database;

    @Inject public PageLoader(@NonNull Database database) {
        this.database = database;
    }

    @NonNull
    public Observable<PageResult<T>> load(@NonNull DataConverter<T> dataConverter, @NonNull DatabaseQuery databaseQuery, @NonNull Observable<Page> pageObservable) {
        final SparseArrayCompat<T> cache = new SparseArrayCompat<>();
        final Observable<Cursor> cursorObservable = database.load(databaseQuery).doOnNext(cursor -> cache.clear());

        return Observable.combineLatest(pageObservable, cursorObservable, (page, cursor) -> {
            final List<T> items = new ArrayList<>();
            for (int i = page.getStart(), size = Math.min(cursor.getCount(), page.getStart() + page.getSize()); i < size; i++) {
                T item = cache.get(i);
                if (item == null) {
                    cursor.moveToPosition(i);
                    item = dataConverter.from(cursor);
                    cache.put(i, item);
                }
                items.add(item);
            }

            return new PageResult<>(page, items, cache.size() == items.size());
        });
    }
}
