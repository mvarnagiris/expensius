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

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import lombok.Getter;

public class Page {
    @Getter private final int start;
    @Getter private final int size;
    private final int preferredSize;

    public Page(@IntRange(from = 0) int start, @IntRange(from = 1) int size) {
        this(start, size, size);
    }

    private Page(@IntRange(from = 0) int start, @IntRange(from = 1) int size, @IntRange(from = 1) int preferredSize) {
        this.start = start;
        this.size = size;
        this.preferredSize = preferredSize;
    }

    @NonNull public Page getPreviousPage() {
        return new Page(Math.max(start - size, 0), start - size < 0 ? size - Math.abs(start - size) : size, preferredSize);
    }

    @NonNull public Page getNextPage() {
        return new Page(start + size, Math.max(size, preferredSize));
    }
}
