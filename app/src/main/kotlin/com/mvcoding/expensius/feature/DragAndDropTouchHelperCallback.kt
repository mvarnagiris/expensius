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
import android.support.v7.widget.helper.ItemTouchHelper
import android.support.v7.widget.helper.ItemTouchHelper.DOWN
import android.support.v7.widget.helper.ItemTouchHelper.UP
import rx.subjects.PublishSubject

class DragAndDropTouchHelperCallback(
        private val adapter: BaseAdapter<*, *>,
        private val itemMovedSubject: PublishSubject<ItemMoved>,
        private val canDropOver: (RecyclerView.ViewHolder, RecyclerView.ViewHolder) -> Boolean = { current, target -> true }) :
        ItemTouchHelper.SimpleCallback(UP or DOWN, 0) {

    private var fromPosition: Int = -1
    private var toPosition: Int = -1

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        if (fromPosition == -1) {
            fromPosition = viewHolder.adapterPosition
        }
        toPosition = target.adapterPosition
        adapter.move(viewHolder.adapterPosition, toPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, position: Int) {
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        if (actionState == ItemTouchHelper.ACTION_STATE_IDLE) {
            itemMovedSubject.onNext(ItemMoved(fromPosition, toPosition))
            fromPosition = -1
            toPosition = -1
        }
    }

    override fun canDropOver(recyclerView: RecyclerView, current: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return canDropOver(current, target)
    }

    data class ItemMoved(val fromPosition: Int, val toPosition: Int)
}