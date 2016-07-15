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

package com.mvcoding.expensius.service

import rx.Observable

interface ItemsService<ITEM> {
    fun items(): Observable<List<ITEM>>
    fun addedItems(): Observable<ItemsAdded<ITEM>>
    fun changedItems(): Observable<ItemsChanged<ITEM>>
    fun removedItems(): Observable<ItemsRemoved<ITEM>>
    fun movedItems(): Observable<ItemMoved<ITEM>>
}


data class ItemsAdded<out ITEM>(val position: Int, val items: List<ITEM>)
data class ItemsChanged<out ITEM>(val position: Int, val items: List<ITEM>)
data class ItemsRemoved<out ITEM>(val position: Int, val items: List<ITEM>)
data class ItemMoved<out ITEM>(val fromPosition: Int, val toPosition: Int, val item: ITEM)