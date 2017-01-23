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
import com.mvcoding.expensius.datasource.RealtimeListDataSource.RealtimeData.AllItems
import com.mvcoding.expensius.datasource.RealtimeListDataSource.RealtimeData.ChangedItems
import com.mvcoding.expensius.datasource.RealtimeListDataSource.RealtimeData.MovedItem
import com.mvcoding.expensius.datasource.RealtimeListDataSource.RealtimeData.RemovedItems
import rx.Observable
import rx.Observable.just
import rx.Subscriber
import rx.lang.kotlin.deferredObservable
import rx.lang.kotlin.filterNotNull
import rx.lang.kotlin.observable
import java.lang.Math.max
import java.lang.Math.min

class RealtimeListDataSource<ITEM>(
        private val getAllItems: () -> Observable<AllItems<ITEM>>,
        private val getAddedItems: () -> Observable<AddedItems<ITEM>>,
        private val getChangedItems: () -> Observable<ChangedItems<ITEM>>,
        private val getRemovedItems: () -> Observable<RemovedItems<ITEM>>,
        private val getMovedItem: () -> Observable<MovedItem<ITEM>>) : DataSource<RealtimeData<ITEM>> {

    private var items: List<ITEM>? = null

    private val observable = deferredObservable {
        observable<RealtimeData<ITEM>> { subscriber ->
            val allItems = getAllItems().first()
            handleAllItems(allItems, subscriber)
            handleAddedItems(allItems, subscriber)
            handleChangedItems(allItems, subscriber)
            handleRemovedItems(allItems, subscriber)
            handleMovedItems(allItems, subscriber)
        }
    }.share()

    private fun handleAllItems(allItems: Observable<AllItems<ITEM>>, subscriber: Subscriber<in RealtimeData<ITEM>>) {
        allItems.doOnNext { items = it.items }.subscribe({ subscriber.onNext(it) }, { subscriber.onError(it) })
    }

    private fun handleAddedItems(allItems: Observable<AllItems<ITEM>>, subscriber: Subscriber<in RealtimeData<ITEM>>) {
        getAddedItems()
                .skipUntil(allItems)
                .doOnNext { items = items.orEmpty().insert(it.items, it.position) }
                .subscribe({ subscriber.onNext(it) }, { subscriber.onError(it) })
    }

    private fun handleChangedItems(allItems: Observable<AllItems<ITEM>>, subscriber: Subscriber<in RealtimeData<ITEM>>) {
        getChangedItems().skipUntil(allItems)
                .doOnNext { items = items.orEmpty().replace(it.items, it.position) }
                .subscribe({ subscriber.onNext(it) }, { subscriber.onError(it) })
    }

    private fun handleRemovedItems(allItems: Observable<AllItems<ITEM>>, subscriber: Subscriber<in RealtimeData<ITEM>>) {
        getRemovedItems()
                .skipUntil(allItems)
                .doOnNext { items = items.orEmpty().remove(it.items, it.position) }
                .subscribe({ subscriber.onNext(it) }, { subscriber.onError(it) })
    }

    private fun handleMovedItems(allItems: Observable<AllItems<ITEM>>, subscriber: Subscriber<in RealtimeData<ITEM>>) {
        getMovedItem()
                .skipUntil(allItems)
                .doOnNext { items = items.orEmpty().move(it.fromPosition, it.toPosition) }
                .subscribe({ subscriber.onNext(it) }, { subscriber.onError(it) })
    }

    override fun data(): Observable<RealtimeData<ITEM>> = observable.mergeWith(currentState())

    private fun currentState() = just(items).filterNotNull().map { AllItems(it) }

    private fun <T> List<T>.insert(items: List<T>, position: Int) = take(position).plus(items).plus(takeLast(size - position))
    private fun <T> List<T>.replace(items: List<T>, position: Int) = take(position).plus(items).plus(takeLast(max(size - position - items.size, 0)))
    private fun <T> List<T>.remove(items: List<T>, position: Int) = take(position).plus(takeLast(max(size - position - items.size, 0)))
    private fun <T> List<T>.move(fromPosition: Int, toPosition: Int) = take(min(fromPosition, toPosition))
            .plus(if (toPosition <= fromPosition) listOf(get(fromPosition)) else slice((fromPosition + 1)..toPosition))
            .plus(if (toPosition <= fromPosition) slice(toPosition..(fromPosition - 1)) else listOf(get(fromPosition)))

    sealed class RealtimeData<out ITEM> {
        data class AllItems<out ITEM>(val items: List<ITEM>) : RealtimeData<ITEM>()
        data class AddedItems<out ITEM>(val items: List<ITEM>, val position: Int) : RealtimeData<ITEM>()
        data class ChangedItems<out ITEM>(val items: List<ITEM>, val position: Int) : RealtimeData<ITEM>()
        data class RemovedItems<out ITEM>(val items: List<ITEM>, val position: Int) : RealtimeData<ITEM>()
        data class MovedItem<out ITEM>(val item: ITEM, val fromPosition: Int, val toPosition: Int) : RealtimeData<ITEM>()
    }
}