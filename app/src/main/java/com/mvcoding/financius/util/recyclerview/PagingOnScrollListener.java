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
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

public class PagingOnScrollListener extends RecyclerView.OnScrollListener {
    private final PagingListener listener;
    private final Paging paging;

    public PagingOnScrollListener(@NonNull PagingListener listener, @IntRange(from = 0) int pagingThreshold) {
        this.listener = listener;
        paging = new Paging(pagingThreshold);
    }

    @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        final int firstVisibleItemPosition = layoutManager.getPosition(layoutManager.getChildAt(0));
        final int visibleItemCount = layoutManager.getChildCount();
        final int totalItemCount = layoutManager.getItemCount();

        paging.updateValues(firstVisibleItemPosition, visibleItemCount, totalItemCount);

        if (paging.hasEnteredStartPagingArea()) {
            listener.onEnteredPagingArea(recyclerView, PagingEdge.Start);
        }

        if (paging.hasLeftStartPagingArea()) {
            listener.onLeftPagingArea(recyclerView, PagingEdge.Start);
        }

        if (paging.hasEnteredEndPagingArea()) {
            listener.onEnteredPagingArea(recyclerView, PagingEdge.End);
        }

        if (paging.hasLeftEndPagingArea()) {
            listener.onLeftPagingArea(recyclerView, PagingEdge.End);
        }
    }

    public void reset() {
        paging.reset();
    }

    public interface PagingListener {
        void onEnteredPagingArea(@NonNull RecyclerView recyclerView, @NonNull PagingEdge pagingEdge);
        void onLeftPagingArea(@NonNull RecyclerView recyclerView, @NonNull PagingEdge pagingEdge);
    }
}
