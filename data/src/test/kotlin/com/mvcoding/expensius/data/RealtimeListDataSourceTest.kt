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

import com.nhaarman.mockito_kotlin.*
import org.junit.Before
import org.junit.Test
import rx.lang.kotlin.PublishSubject
import rx.observers.TestSubscriber

class RealtimeListDataSourceTest {

    val allItemsSubject = PublishSubject<RawRealtimeData.AllItems<Int>>()
    val addedItemsSubject = PublishSubject<RawRealtimeData.AddedItems<Int>>()
    val changedItemsSubject = PublishSubject<RawRealtimeData.ChangedItems<Int>>()
    val removedItemsSubject = PublishSubject<RawRealtimeData.RemovedItems<Int>>()
    val movedItemSubject = PublishSubject<RawRealtimeData.MovedItems<Int>>()

    val items = listOf(2, 4, 6)

    val realtimeList = mock<RealtimeList<Int>>()
    val itemToKey = mock<(Int) -> String>()
    val realtimeListDataSource = RealtimeListDataSource(realtimeList, itemToKey)
    val subscriber = TestSubscriber<RealtimeData<Int>>()
    val otherSubscriber = TestSubscriber<RealtimeData<Int>>()
    val otherSubscriber2 = TestSubscriber<RealtimeData<Int>>()
    val otherSubscriber3 = TestSubscriber<RealtimeData<Int>>()

    @Before
    fun setUp() {
        whenever(realtimeList.getAllItems()).thenReturn(allItemsSubject)
        whenever(realtimeList.getAddedItems()).thenReturn(addedItemsSubject)
        whenever(realtimeList.getChangedItems()).thenReturn(changedItemsSubject)
        whenever(realtimeList.getRemovedItems()).thenReturn(removedItemsSubject)
        whenever(realtimeList.getMovedItem()).thenReturn(movedItemSubject)
        whenever(itemToKey.invoke(any())).thenAnswer { it.getArgument<Int>(0).toString() }
    }

    @Test
    fun `emits current items first and ignores other events until current items come`() {
        realtimeListDataSource.data().subscribe(subscriber)

        receiveAddedItems(items)
        receiveRemovedItems(items)
        receiveChangedItems(items)
        receiveMovedItems(items)
        subscriber.assertNoValues()

        receiveCurrentItems(items)
        subscriber.assertValue(RealtimeData.AllItems(items))

        receiveRemovedItems(items)
        receiveAddedItems(items)
        receiveChangedItems(items)
        receiveMovedItems(items)
        subscriber.assertValues(
                RealtimeData.AllItems(items),
                RealtimeData.RemovedItems(items, 0),
                RealtimeData.AddedItems(items, 0),
                RealtimeData.ChangedItems(items, 0),
                RealtimeData.MovedItems(items, 0, 0))
    }

    @Test
    fun `only takes first result from current values`() {
        realtimeListDataSource.data().subscribe(subscriber)

        receiveCurrentItems(items)
        receiveCurrentItems(listOf(1, 3))

        subscriber.assertValue(RealtimeData.AllItems(items))
    }

    @Test
    fun `does not subscribe to current items more than once`() {
        realtimeListDataSource.data().subscribe(subscriber)
        realtimeListDataSource.data().subscribe(otherSubscriber)

        receiveCurrentItems(items)
        realtimeListDataSource.data().subscribe(otherSubscriber2)

        subscriber.assertValue(RealtimeData.AllItems(items))
        otherSubscriber.assertValue(RealtimeData.AllItems(items))
        otherSubscriber2.assertValue(RealtimeData.AllItems(items))
        verify(realtimeList, times(1)).getAllItems()
    }

    @Test
    fun `handles added items`() {
        val addedItems1 = listOf(1)
        val addedItems2 = listOf(3)
        val addedItems3 = listOf(5)
        realtimeListDataSource.data().subscribe(subscriber)
        receiveCurrentItems(items)

        receiveAddedItems(addedItems1, null)
        realtimeListDataSource.data().subscribe(otherSubscriber)

        receiveAddedItems(addedItems2, "2")
        realtimeListDataSource.data().subscribe(otherSubscriber2)

        receiveAddedItems(addedItems3, "4")
        realtimeListDataSource.data().subscribe(otherSubscriber3)

        subscriber.assertValues(RealtimeData.AllItems(items), RealtimeData.AddedItems(addedItems1, 0), RealtimeData.AddedItems(addedItems2, 2), RealtimeData.AddedItems(addedItems3, 4))
        otherSubscriber.assertValues(RealtimeData.AllItems(listOf(1, 2, 4, 6)), RealtimeData.AddedItems(addedItems2, 2), RealtimeData.AddedItems(addedItems3, 4))
        otherSubscriber2.assertValues(RealtimeData.AllItems(listOf(1, 2, 3, 4, 6)), RealtimeData.AddedItems(addedItems3, 4))
        otherSubscriber3.assertValues(RealtimeData.AllItems(listOf(1, 2, 3, 4, 5, 6)))
    }

    @Test
    fun `handles changed items`() {
        val changedItems = listOf(4, 6)
        realtimeListDataSource.data().subscribe(subscriber)
        receiveCurrentItems(items)

        receiveChangedItems(changedItems)
        realtimeListDataSource.data().subscribe(otherSubscriber)

        subscriber.assertValues(RealtimeData.AllItems(items), RealtimeData.ChangedItems(changedItems, 1))
        otherSubscriber.assertValues(RealtimeData.AllItems(listOf(2, 4, 6)))
    }

    @Test
    fun `handles removed items`() {
        val removedItems1 = listOf(4)
        val removedItems2 = listOf(6)
        val removedItems3 = listOf(2)
        realtimeListDataSource.data().subscribe(subscriber)
        receiveCurrentItems(items)

        receiveRemovedItems(removedItems1)
        realtimeListDataSource.data().subscribe(otherSubscriber)

        receiveRemovedItems(removedItems2)
        realtimeListDataSource.data().subscribe(otherSubscriber2)

        receiveRemovedItems(removedItems3)
        realtimeListDataSource.data().subscribe(otherSubscriber3)

        subscriber.assertValues(RealtimeData.AllItems(items), RealtimeData.RemovedItems(removedItems1, 1), RealtimeData.RemovedItems(removedItems2, 1), RealtimeData.RemovedItems(removedItems3, 0))
        otherSubscriber.assertValues(RealtimeData.AllItems(listOf(2, 6)), RealtimeData.RemovedItems(removedItems2, 1), RealtimeData.RemovedItems(removedItems3, 0))
        otherSubscriber2.assertValues(RealtimeData.AllItems(listOf(2)), RealtimeData.RemovedItems(removedItems3, 0))
        otherSubscriber3.assertValues(RealtimeData.AllItems(listOf()))
    }

    @Test
    fun `handles moved items`() {
        realtimeListDataSource.data().subscribe(subscriber)
        receiveCurrentItems(items)

        receiveMovedItems(listOf(2), "6")
        realtimeListDataSource.data().subscribe(otherSubscriber)

        receiveMovedItems(listOf(2), null)
        realtimeListDataSource.data().subscribe(otherSubscriber2)

        subscriber.assertValues(RealtimeData.AllItems(items), RealtimeData.MovedItems(listOf(2), 0, 2), RealtimeData.MovedItems(listOf(2), 2, 0))
        otherSubscriber.assertValues(RealtimeData.AllItems(listOf(4, 6, 2)), RealtimeData.MovedItems(listOf(2), 2, 0))
        otherSubscriber2.assertValues(RealtimeData.AllItems(listOf(2, 4, 6)))
    }

    fun receiveCurrentItems(items: List<Int>) = allItemsSubject.onNext(RawRealtimeData.AllItems(items))
    fun receiveAddedItems(items: List<Int>, previousKey: String? = null) = addedItemsSubject.onNext(RawRealtimeData.AddedItems(items, previousKey))
    fun receiveChangedItems(items: List<Int>) = changedItemsSubject.onNext(RawRealtimeData.ChangedItems(items))
    fun receiveRemovedItems(items: List<Int>) = removedItemsSubject.onNext(RawRealtimeData.RemovedItems(items))
    fun receiveMovedItems(items: List<Int>, previousKey: String? = null) = movedItemSubject.onNext(RawRealtimeData.MovedItems(items, previousKey))
}