/*
 * Copyright (C) 2017 Mantas Varnagiris.
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

package com.mvcoding.expensius.datasource

sealed class RealtimeData<out ITEM> {
    data class AllItems<out ITEM>(val items: List<ITEM>) : RealtimeData<ITEM>()
    data class AddedItems<out ITEM>(val items: List<ITEM>, val position: Int) : RealtimeData<ITEM>()
    data class ChangedItems<out ITEM>(val items: List<ITEM>, val position: Int) : RealtimeData<ITEM>()
    data class RemovedItems<out ITEM>(val items: List<ITEM>, val position: Int) : RealtimeData<ITEM>()
    data class MovedItem<out ITEM>(val item: ITEM, val fromPosition: Int, val toPosition: Int) : RealtimeData<ITEM>()
}