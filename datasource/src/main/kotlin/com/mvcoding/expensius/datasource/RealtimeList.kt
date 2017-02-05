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

import com.mvcoding.expensius.datasource.RealtimeData.AddedItems
import com.mvcoding.expensius.datasource.RealtimeData.AllItems
import com.mvcoding.expensius.datasource.RealtimeData.ChangedItems
import com.mvcoding.expensius.datasource.RealtimeData.MovedItems
import com.mvcoding.expensius.datasource.RealtimeData.RemovedItems
import rx.Observable

interface RealtimeList<ITEM> {
    fun getAllItems(): Observable<AllItems<ITEM>>
    fun getAddedItems(): Observable<AddedItems<ITEM>>
    fun getChangedItems(): Observable<ChangedItems<ITEM>>
    fun getRemovedItems(): Observable<RemovedItems<ITEM>>
    fun getMovedItem(): Observable<MovedItems<ITEM>>
}