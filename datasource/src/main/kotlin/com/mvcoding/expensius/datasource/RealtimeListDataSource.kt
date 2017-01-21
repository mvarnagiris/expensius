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

import com.mvcoding.expensius.datasource.RealtimeListDataSource.RealtimeData
import com.mvcoding.expensius.datasource.RealtimeListDataSource.RealtimeData.AddedItems
import com.mvcoding.expensius.datasource.RealtimeListDataSource.RealtimeData.ChangedItems
import com.mvcoding.expensius.datasource.RealtimeListDataSource.RealtimeData.CurrentItems
import com.mvcoding.expensius.datasource.RealtimeListDataSource.RealtimeData.MovedItem
import com.mvcoding.expensius.datasource.RealtimeListDataSource.RealtimeData.RemovedItems
import rx.Observable
import rx.lang.kotlin.deferredObservable
import rx.lang.kotlin.observable

class RealtimeListDataSource<ITEM>(
        private val currentItems: () -> Observable<CurrentItems<ITEM>>,
        private val addedItems: () -> Observable<AddedItems<ITEM>>,
        private val changedItems: () -> Observable<ChangedItems<ITEM>>,
        private val removedItems: () -> Observable<RemovedItems<ITEM>>,
        private val movedItem: () -> Observable<MovedItem<ITEM>>) : DataSource<RealtimeData<ITEM>> {

    override fun data(): Observable<RealtimeData<ITEM>> = deferredObservable {
        observable<RealtimeData<ITEM>> { subscriber ->
            val currentItems = currentItems().first()
            currentItems.subscribe({ subscriber.onNext(it) }, { subscriber.onError(it) })
            addedItems().skipUntil(currentItems).subscribe({ subscriber.onNext(it) }, { subscriber.onError(it) })
            changedItems().skipUntil(currentItems).subscribe({ subscriber.onNext(it) }, { subscriber.onError(it) })
            removedItems().skipUntil(currentItems).subscribe({ subscriber.onNext(it) }, { subscriber.onError(it) })
            movedItem().skipUntil(currentItems).subscribe({ subscriber.onNext(it) }, { subscriber.onError(it) })
        }
    }

    sealed class RealtimeData<out ITEM> {
        data class CurrentItems<out ITEM>(val items: List<ITEM>) : RealtimeData<ITEM>()
        data class AddedItems<out ITEM>(val items: List<ITEM>, val position: Int) : RealtimeData<ITEM>()
        data class ChangedItems<out ITEM>(val items: List<ITEM>, val position: Int) : RealtimeData<ITEM>()
        data class RemovedItems<out ITEM>(val items: List<ITEM>, val position: Int) : RealtimeData<ITEM>()
        data class MovedItem<out ITEM>(val item: ITEM, val fromPosition: Int, val toPosition: Int) : RealtimeData<ITEM>()
    }
}