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

import android.support.annotation.IntRange;

class Paging {
    private final int pagingThreshold;

    private boolean isInStartPagingArea;
    private boolean isInEndPagingArea;
    private boolean hasEnteredStartPagingArea;
    private boolean hasLeftStartPagingArea;
    private boolean hasEnteredEndPagingArea;
    private boolean hasLeftEndPagingArea;

    public Paging(@IntRange(from = 0) int pagingThreshold) {
        this.pagingThreshold = pagingThreshold;
    }

    public void updateValues(int firstVisibleItemPosition, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItemPosition <= pagingThreshold) {
            hasEnteredStartPagingArea = !isInStartPagingArea;
            hasLeftStartPagingArea = false;
            isInStartPagingArea = true;
        } else {
            hasLeftStartPagingArea = isInStartPagingArea;
            hasEnteredStartPagingArea = false;
            isInStartPagingArea = false;
        }

        if (firstVisibleItemPosition + visibleItemCount >= totalItemCount - pagingThreshold) {
            hasEnteredEndPagingArea = !isInEndPagingArea;
            hasLeftEndPagingArea = false;
            isInEndPagingArea = true;
        } else {
            hasLeftEndPagingArea = isInEndPagingArea;
            hasEnteredEndPagingArea = false;
            isInEndPagingArea = false;
        }
    }

    public boolean hasEnteredStartPagingArea() {
        return hasEnteredStartPagingArea;
    }

    public boolean hasLeftStartPagingArea() {
        return hasLeftStartPagingArea;
    }

    public boolean hasEnteredEndPagingArea() {
        return hasEnteredEndPagingArea;
    }

    public boolean hasLeftEndPagingArea() {
        return hasLeftEndPagingArea;
    }

    public void reset() {
        isInStartPagingArea = false;
        isInEndPagingArea = false;
        hasEnteredStartPagingArea = false;
        hasLeftStartPagingArea = false;
        hasEnteredEndPagingArea = false;
        hasLeftEndPagingArea = false;
    }
}
