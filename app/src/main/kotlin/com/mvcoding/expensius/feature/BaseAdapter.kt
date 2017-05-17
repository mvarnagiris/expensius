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

package com.mvcoding.expensius.feature

import android.support.v7.widget.RecyclerView

abstract class BaseAdapter<ITEM, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {
    val items = arrayListOf<ITEM>()

    override fun getItemCount(): Int = items.size

    fun getItem(position: Int) = items[position]

    fun setItems(items: List<ITEM>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun addItems(position: Int, item: ITEM) {
        items.add(position, item)
        notifyItemInserted(position)
    }

    fun addItems(position: Int, items: List<ITEM>) {
        this.items.addAll(position, items)
        notifyItemRangeInserted(position, items.size)
    }

    fun addItems(items: List<ITEM>) {
        val size = itemCount
        this.items.addAll(items)
        notifyItemRangeInserted(size, items.size)
    }

    fun changeItems(position: Int, items: List<ITEM>) {
        (position..(position + items.size - 1)).forEachIndexed { index, positionToReplace -> this.items[positionToReplace] = items[index] }
        notifyItemRangeChanged(position, items.size)
    }

    fun removeItem(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    fun removeItems(position: Int, count: Int) {
        count.downTo(1).forEach { items.removeAt(position + it - 1) }
        notifyItemRangeRemoved(position, count)
    }

    fun removeItem(item: ITEM) {
        val position = items.indexOf(item)
        items.remove(item)
        notifyItemRemoved(position)
    }

    fun removeItems(items: List<ITEM>) {
        val positions = items.map { this.items.indexOf(it) }.sorted()
        this.items.removeAll(items)
        positions.forEach { notifyItemRemoved(it) }
    }

    fun moveItem(fromPosition: Int, toPosition: Int) {
        val item = items.removeAt(fromPosition)
        items.add(toPosition, item)
        notifyItemMoved(fromPosition, toPosition)
    }
}