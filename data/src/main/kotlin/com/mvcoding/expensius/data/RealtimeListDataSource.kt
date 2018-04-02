/*
 * Copyright (C) 2018 Mantas Varnagiris.
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

import io.reactivex.Observable
import io.reactivex.Observable.just
import io.reactivex.ObservableEmitter
import java.io.Closeable
import java.lang.Math.max
import java.lang.Math.min

class RealtimeListDataSource<ITEM>(
        private val realtimeList: RealtimeList<ITEM>,
        private val itemToKey: (ITEM) -> String) : DataSource<RealtimeData<ITEM>>, Closeable {

    private var allItems: List<ITEM>? = null

    private val observable = Observable.defer {
        Observable.create<RealtimeData<ITEM>> { subscriber ->
            val allItems = realtimeList.getAllItems().firstOrError().toObservable()
            handleAllItems(allItems, subscriber)
            handleAddedItems(allItems, subscriber)
            handleChangedItems(allItems, subscriber)
            handleRemovedItems(allItems, subscriber)
            handleMovedItems(allItems, subscriber)
        }
    }.share()

    private fun handleAllItems(allItems: Observable<RawRealtimeData.AllItems<ITEM>>, subscriber: ObservableEmitter<in RealtimeData<ITEM>>) {
        allItems.map { RealtimeData.AllItems(it.items) }
                .doOnNext { this.allItems = it.allItems }
                .subscribe({ subscriber.onNext(it) }, { subscriber.onError(it) })
    }

    private fun handleAddedItems(allItemsObservable: Observable<RawRealtimeData.AllItems<ITEM>>, subscriber: ObservableEmitter<in RealtimeData<ITEM>>) {
        realtimeList.getAddedItems()
                .skipUntil(allItemsObservable)
                .map {
                    val position = keyToPosition(it.previousKey) + 1
                    val newAllItems = allItems.orEmpty().insert(it.items, position)
                    allItems = newAllItems
                    RealtimeData.AddedItems(newAllItems, it.items, position)
                }
                .subscribe({ subscriber.onNext(it) }, { subscriber.onError(it) })
    }

    private fun handleChangedItems(allItemsObservable: Observable<RawRealtimeData.AllItems<ITEM>>, subscriber: ObservableEmitter<in RealtimeData<ITEM>>) {
        realtimeList.getChangedItems()
                .skipUntil(allItemsObservable)
                .map {
                    val position = keyToPosition(itemToKey(it.items.first()))
                    val itemsHaveChanged = allItems!!.subList(position, position + it.items.size).containsAll(it.items).not()
                    if (itemsHaveChanged) {
                        val newAllItems = allItems.orEmpty().replace(it.items, position)
                        allItems = newAllItems
                        RealtimeData.ChangedItems(newAllItems, it.items, position)
                    } else null
                }
                .subscribe({ subscriber.onNext(it as RealtimeData<ITEM>) }, { subscriber.onError(it) })
    }

    private fun handleRemovedItems(allItemsObservable: Observable<RawRealtimeData.AllItems<ITEM>>, subscriber: ObservableEmitter<in RealtimeData<ITEM>>) {
        realtimeList.getRemovedItems()
                .skipUntil(allItemsObservable)
                .map {
                    val position = keyToPosition(itemToKey(it.items.first()))
                    val newAllItems = allItems.orEmpty().remove(it.items, position)
                    allItems = newAllItems
                    RealtimeData.RemovedItems(newAllItems, it.items, position)
                }
                .subscribe({ subscriber.onNext(it) }, { subscriber.onError(it) })
    }

    private fun handleMovedItems(allItemsObservable: Observable<RawRealtimeData.AllItems<ITEM>>, subscriber: ObservableEmitter<in RealtimeData<ITEM>>) {
        realtimeList.getMovedItem()
                .skipUntil(allItemsObservable)
                .map {
                    val fromPosition = keyToPosition(itemToKey(it.items.first()))
                    val previousItemPosition = keyToPosition(it.previousKey)
                    val toPosition =
                            if (previousItemPosition < 0) 0
                            else if (previousItemPosition > fromPosition) previousItemPosition
                            else previousItemPosition + 1
                    val newAllItems = allItems.orEmpty().move(fromPosition, toPosition)
                    allItems = newAllItems
                    RealtimeData.MovedItems(newAllItems, it.items, fromPosition, toPosition)
                }
                .filter { it.fromPosition != it.toPosition }
                .subscribe({ subscriber.onNext(it) }, { subscriber.onError(it) })
    }

    override fun data(): Observable<RealtimeData<ITEM>> = observable.mergeWith(currentState())
    override fun close() = realtimeList.close()

    private fun currentState() = just(allItems).map { RealtimeData.AllItems(it) }
    private fun keyToPosition(previousKey: String?) = previousKey?.let { key -> allItems.orEmpty().indexOfFirst { itemToKey(it) == key } } ?: -1

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