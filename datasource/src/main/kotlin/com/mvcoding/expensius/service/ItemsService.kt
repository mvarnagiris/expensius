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
    fun addedItems(): Observable<AddedItems<ITEM>>
    fun changedItems(): Observable<ChangedItems<ITEM>>
    fun removedItems(): Observable<RemovedItems<ITEM>>
    fun movedItem(): Observable<MovedItem<ITEM>>
}


data class AddedItems<out ITEM>(val position: Int, val items: List<ITEM>)
data class ChangedItems<out ITEM>(val position: Int, val items: List<ITEM>)
data class RemovedItems<out ITEM>(val position: Int, val items: List<ITEM>)
data class MovedItem<out ITEM>(val fromPosition: Int, val toPosition: Int, val item: ITEM)