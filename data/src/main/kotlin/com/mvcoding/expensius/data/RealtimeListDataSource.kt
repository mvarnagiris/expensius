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

package com.mvcoding.expensius.data

import rx.Observable
import rx.Observable.just
import rx.Subscriber
import rx.lang.kotlin.deferredObservable
import rx.lang.kotlin.filterNotNull
import rx.lang.kotlin.observable
import java.lang.Math.max
import java.lang.Math.min

class RealtimeListDataSource<ITEM>(
        private val realtimeList: RealtimeList<ITEM>,
        private val itemToKey: (ITEM) -> String) : DataSource<RealtimeData<ITEM>> {

    private var items: List<ITEM>? = null

    private val observable = deferredObservable {
        observable<RealtimeData<ITEM>> { subscriber ->
            val allItems = realtimeList.getAllItems().first()
            handleAllItems(allItems, subscriber)
            handleAddedItems(allItems, subscriber)
            handleChangedItems(allItems, subscriber)
            handleRemovedItems(allItems, subscriber)
            handleMovedItems(allItems, subscriber)
        }
    }.share()

    private fun handleAllItems(allItems: Observable<AllItems<ITEM>>, subscriber: Subscriber<in RawRealtimeData<ITEM>>) {
        allItems.doOnNext { items = it.items }.subscribe({ subscriber.onNext(it) }, { subscriber.onError(it) })
    }

    private fun handleAddedItems(allItems: Observable<AllItems<ITEM>>, subscriber: Subscriber<in RawRealtimeData<ITEM>>) {
        realtimeList.getAddedItems()
                .skipUntil(allItems)
                .doOnNext { items = items.orEmpty().insert(it.items, keyToPosition(it.previousKey) + 1) }
                .subscribe({ subscriber.onNext(it) }, { subscriber.onError(it) })
    }

    private fun handleChangedItems(allItems: Observable<AllItems<ITEM>>, subscriber: Subscriber<in RawRealtimeData<ITEM>>) {
        realtimeList.getChangedItems()
                .skipUntil(allItems)
                .doOnNext { items = items.orEmpty().replace(it.items, keyToPosition(itemToKey(it.items.first()))) }
                .subscribe({ subscriber.onNext(it) }, { subscriber.onError(it) })
    }

    private fun handleRemovedItems(allItems: Observable<AllItems<ITEM>>, subscriber: Subscriber<in RawRealtimeData<ITEM>>) {
        realtimeList.getRemovedItems()
                .skipUntil(allItems)
                .doOnNext { items = items.orEmpty().remove(it.items, keyToPosition(itemToKey(it.items.first()))) }
                .subscribe({ subscriber.onNext(it) }, { subscriber.onError(it) })
    }

    private fun handleMovedItems(allItems: Observable<AllItems<ITEM>>, subscriber: Subscriber<in RawRealtimeData<ITEM>>) {
        realtimeList.getMovedItem()
                .skipUntil(allItems)
                .doOnNext { items = items.orEmpty().move(keyToPosition(itemToKey(it.items.first())), keyToPosition(it.previousKey) + 1) }
                .subscribe({ subscriber.onNext(it) }, { subscriber.onError(it) })
    }

    override fun data(): Observable<RealtimeData<ITEM>> = observable.mergeWith(currentState())

    private fun currentState() = just(items).filterNotNull().map { AllItems(it) }
    private fun keyToPosition(previousKey: String?) = previousKey?.let { key -> items.orEmpty().indexOfFirst { itemToKey(it) == key } } ?: -1

    private fun <T> List<T>.insert(items: List<T>, position: Int) = take(position).plus(items).plus(takeLast(size - position))
    private fun <T> List<T>.replace(items: List<T>, position: Int) = take(position).plus(items).plus(takeLast(max(size - position - items.size, 0)))
    private fun <T> List<T>.remove(items: List<T>, position: Int) = take(position).plus(takeLast(max(size - position - items.size, 0)))
    private fun <T> List<T>.move(fromPosition: Int, toPosition: Int) = take(min(fromPosition, toPosition))
            .plus(if (toPosition <= fromPosition) listOf(get(fromPosition)) else slice((fromPosition + 1)..toPosition))
            .plus(if (toPosition <= fromPosition) slice(toPosition..(fromPosition - 1)) else listOf(get(fromPosition)))

}