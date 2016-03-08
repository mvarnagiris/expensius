/*
 * Copyright (C) 2016 Mantas Varnagiris.
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

package com.mvcoding.expensius.feature

import android.support.v7.widget.RecyclerView
import com.mvcoding.expensius.feature.transaction.TransactionsPresenter
import com.mvcoding.expensius.feature.transaction.TransactionsPresenter.PagingEdge.END
import com.mvcoding.expensius.feature.transaction.TransactionsPresenter.PagingEdge.START

class PagingOnScrollListener(private val pagingListener: PagingListener, private val paging: Paging) : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val layoutManager = recyclerView.layoutManager
        val firstVisibleItemPosition = layoutManager.getPosition(layoutManager.getChildAt(0))
        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount

        paging.updateValues(firstVisibleItemPosition, visibleItemCount, totalItemCount)

        if (paging.hasEnteredStartPagingArea) {
            pagingListener.onEnteredPagingArea(START)
        }

        if (paging.hasLeftStartPagingArea) {
            pagingListener.onLeftPagingArea(START)
        }

        if (paging.hasEnteredEndPagingArea) {
            pagingListener.onEnteredPagingArea(END)
        }

        if (paging.hasLeftEndPagingArea) {
            pagingListener.onLeftPagingArea(END)
        }
    }

    fun reset() = paging.reset()

    interface PagingListener {
        fun onEnteredPagingArea(pagingEdge: TransactionsPresenter.PagingEdge)
        fun onLeftPagingArea(pagingEdge: TransactionsPresenter.PagingEdge)
    }
}