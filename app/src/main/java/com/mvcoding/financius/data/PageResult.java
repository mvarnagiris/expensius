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
import android.support.annotation.NonNull;
import android.support.v4.util.SparseArrayCompat;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor @Getter public class PageResult<T> {
    private final Cursor cursor;
    private final SparseArrayCompat<T> allItems;
    private final List<T> pageItems;
    private final PageLoader.Page page;

    public boolean hasMoreBefore() {
        return page.getStart() > 0;
    }

    public boolean hasMoreAfter() {
        return cursor.getCount() > page.getStart() + page.getCount();
    }

    @NonNull public PageLoader.Page getPreviousPage() {
        return null;
    }

    @NonNull public PageLoader.Page getNextPage() {
        return null;
    }
}
