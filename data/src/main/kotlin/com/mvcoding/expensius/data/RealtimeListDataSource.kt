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
import java.io.Closeable
import java.lang.Math.max
import java.lang.Math.min

class RealtimeListDataSource<ITEM>(
        private val realtimeList: RealtimeList<ITEM>,
        private val itemToKey: (ITEM) -> String) : DataSource<RealtimeData<ITEM>>, Closeable {

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

    private fun handleAllItems(allItems: Observable<RawRealtimeData.AllItems<ITEM>>, subscriber: Subscriber<in RealtimeData<ITEM>>) {
        allItems.map { RealtimeData.AllItems(it.items) }
                .doOnNext { items = it.items }
                .subscribe({ subscriber.onNext(it) }, { subscriber.onError(it) })
    }

    private fun handleAddedItems(allItems: Observable<RawRealtimeData.AllItems<ITEM>>, subscriber: Subscriber<in RealtimeData<ITEM>>) {
        realtimeList.getAddedItems()
                .skipUntil(allItems)
                .map { RealtimeData.AddedItems(it.items, keyToPosition(it.previousKey) + 1) }
                .doOnNext { items = items.orEmpty().insert(it.items, it.position) }
                .subscribe({ subscriber.onNext(it) }, { subscriber.onError(it) })
    }

    // TODO Write test for not emitting values when changed item is not really changed
    private fun handleChangedItems(allItems: Observable<RawRealtimeData.AllItems<ITEM>>, subscriber: Subscriber<in RealtimeData<ITEM>>) {
        realtimeList.getChangedItems()
                .skipUntil(allItems)
                .map { RealtimeData.ChangedItems(it.items, keyToPosition(itemToKey(it.items.first()))) }
                .filter { items!!.subList(it.position, it.position + it.items.size).containsAll(it.items).not() }
                .doOnNext { items = items.orEmpty().replace(it.items, it.position) }
                .subscribe({ subscriber.onNext(it) }, { subscriber.onError(it) })
    }

    private fun handleRemovedItems(allItems: Observable<RawRealtimeData.AllItems<ITEM>>, subscriber: Subscriber<in RealtimeData<ITEM>>) {
        realtimeList.getRemovedItems()
                .skipUntil(allItems)
                .map { RealtimeData.RemovedItems(it.items, keyToPosition(itemToKey(it.items.first()))) }
                .doOnNext { items = items.orEmpty().remove(it.items, it.position) }
                .subscribe({ subscriber.onNext(it) }, { subscriber.onError(it) })
    }

    private fun handleMovedItems(allItems: Observable<RawRealtimeData.AllItems<ITEM>>, subscriber: Subscriber<in RealtimeData<ITEM>>) {
        realtimeList.getMovedItem()
                .skipUntil(allItems)
                .map {
                    val fromPosition = keyToPosition(itemToKey(it.items.first()))
                    val previousItemPosition = keyToPosition(it.previousKey)
                    val toPosition =
                            if (previousItemPosition < 0) 0
                            else if (previousItemPosition > fromPosition) previousItemPosition
                            else previousItemPosition + 1
                    RealtimeData.MovedItems(it.items, fromPosition, toPosition)
                }
                .filter { it.fromPosition != it.toPosition }
                .doOnNext { items = items.orEmpty().move(it.fromPosition, it.toPosition) }
                .subscribe({ subscriber.onNext(it) }, { subscriber.onError(it) })
    }

    override fun data(): Observable<RealtimeData<ITEM>> = observable.mergeWith(currentState())
    override fun close() = realtimeList.close()

    private fun currentState() = just(items).filterNotNull().map { RealtimeData.AllItems(it) }
    private fun keyToPosition(previousKey: String?) = previousKey?.let { key -> items.orEmpty().indexOfFirst { itemToKey(it) == key } } ?: -1

    private fun <T> List<T>.insert(items: List<T>, position: Int) = take(position).plus(items).plus(takeLast(size - position))
    private fun <T> List<T>.replace(items: List<T>, position: Int) = take(position).plus(items).plus(takeLast(max(size - position - items.size, 0)))
    private fun <T> List<T>.remove(items: List<T>, position: Int) = take(position).plus(takeLast(max(size - position - items.size, 0)))
    private fun <T> List<T>.move(fromPosition: Int, toPosition: Int): List<T> {
        val isMovingUp = toPosition <= fromPosition
        return take(min(fromPosition, toPosition))
                .plus(if (isMovingUp) listOf(get(fromPosition)) else slice((fromPosition + 1)..toPosition))
                .plus(if (isMovingUp) slice(toPosition..(fromPosition - 1)) else listOf(get(fromPosition)))
                .plus(if (isMovingUp) slice((fromPosition + 1)..(size - 1)) else slice((toPosition + 1)..(size - 1)))
    }

}