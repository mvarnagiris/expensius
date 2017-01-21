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
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Test
import rx.Observable
import rx.lang.kotlin.PublishSubject
import rx.observers.TestSubscriber

class RealtimeListDataSourceTest {

    val currentItemsSubject = PublishSubject<CurrentItems<Int>>()
    val addedItemsSubject = PublishSubject<AddedItems<Int>>()
    val changedItemsSubject = PublishSubject<ChangedItems<Int>>()
    val removedItemsSubject = PublishSubject<RemovedItems<Int>>()
    val movedItemSubject = PublishSubject<MovedItem<Int>>()

    val items = listOf(1, 2, 3)
    val otherItems = listOf(4, 5, 6)

    val currentItems = mock<() -> Observable<CurrentItems<Int>>>()
    val addedItems = mock<() -> Observable<AddedItems<Int>>>()
    val changedItems = mock<() -> Observable<ChangedItems<Int>>>()
    val removedItems = mock<() -> Observable<RemovedItems<Int>>>()
    val movedItem = mock<() -> Observable<MovedItem<Int>>>()
    val realTimeListDataSource = RealtimeListDataSource(currentItems, addedItems, changedItems, removedItems, movedItem)
    val subscriber = TestSubscriber<RealtimeData<Int>>()

    @Before
    fun setUp() {
        whenever(currentItems()).thenReturn(currentItemsSubject)
        whenever(addedItems()).thenReturn(addedItemsSubject)
        whenever(changedItems()).thenReturn(changedItemsSubject)
        whenever(removedItems()).thenReturn(removedItemsSubject)
        whenever(movedItem()).thenReturn(movedItemSubject)
    }

    @Test
    fun `emits current items first and ignores other events until current items come`() {
        realTimeListDataSource.data().subscribe(subscriber)

        receiveAddedItems(items, 0)
        receiveChangedItems(items, 0)
        receiveMovedItem(1, fromPosition = 0, toPosition = 1)
        receiveRemovedItems(items, 0)
        subscriber.assertNoValues()

        receiveCurrentItems(items)
        subscriber.assertValue(CurrentItems(items))

        receiveAddedItems(items, 0)
        receiveChangedItems(items, 0)
        receiveMovedItem(1, fromPosition = 0, toPosition = 1)
        receiveRemovedItems(items, 0)
        subscriber.assertValues(CurrentItems(items), AddedItems(items, 0), ChangedItems(items, 0), MovedItem(1, 0, 1), RemovedItems(items, 0))
    }

    @Test
    fun `only takes first result from current values`() {
        realTimeListDataSource.data().subscribe(subscriber)

        receiveCurrentItems(items)
        receiveCurrentItems(otherItems)

        subscriber.assertValue(CurrentItems(items))
    }

    @Test
    fun `asonly takes first result from current values`() {
        realTimeListDataSource.data().subscribe(subscriber)

        receiveCurrentItems(items)
        receiveCurrentItems(otherItems)

        subscriber.assertValue(CurrentItems(items))
    }

    fun receiveCurrentItems(items: List<Int>) = currentItemsSubject.onNext(CurrentItems(items))
    fun receiveAddedItems(items: List<Int>, position: Int) = addedItemsSubject.onNext(AddedItems(items, position))
    fun receiveChangedItems(items: List<Int>, position: Int) = changedItemsSubject.onNext(ChangedItems(items, position))
    fun receiveRemovedItems(items: List<Int>, position: Int) = removedItemsSubject.onNext(RemovedItems(items, position))
    fun receiveMovedItem(item: Int, fromPosition: Int, toPosition: Int) = movedItemSubject.onNext(MovedItem(item, fromPosition, toPosition))
}