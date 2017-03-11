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

import com.mvcoding.expensius.data.RealtimeData.*
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Test
import rx.lang.kotlin.PublishSubject
import rx.observers.TestSubscriber

class RealtimeListDataSourceTest {

    val allItemsSubject = PublishSubject<AllItems<Int>>()
    val addedItemsSubject = PublishSubject<AddedItems<Int>>()
    val changedItemsSubject = PublishSubject<ChangedItems<Int>>()
    val removedItemsSubject = PublishSubject<RemovedItems<Int>>()
    val movedItemSubject = PublishSubject<MovedItems<Int>>()

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
        receiveChangedItems(items)
        receiveMovedItems(items)
        receiveRemovedItems(items)
        subscriber.assertNoValues()

        receiveCurrentItems(items)
        subscriber.assertValue(AllItems(items))

        receiveAddedItems(items)
        receiveChangedItems(items)
        receiveMovedItems(items)
        receiveRemovedItems(items)
        subscriber.assertValues(AllItems(items), AddedItems(items, null), ChangedItems(items), MovedItems(items, null), RemovedItems(items))
    }

//    @Test
//    fun `only takes first result from current values`() {
//        realtimeListDataSource.data().subscribe(subscriber)
//
//        receiveCurrentItems(items)
//        receiveCurrentItems(listOf(1, 3))
//
//        subscriber.assertValue(AllItems(items))
//    }
//
//    @Test
//    fun `does not subscribe to current items more than once`() {
//        realtimeListDataSource.data().subscribe(subscriber)
//        realtimeListDataSource.data().subscribe(otherSubscriber)
//
//        receiveCurrentItems(items)
//        realtimeListDataSource.data().subscribe(otherSubscriber2)
//
//        subscriber.assertValue(AllItems(items))
//        otherSubscriber.assertValue(AllItems(items))
//        otherSubscriber2.assertValue(AllItems(items))
//        verify(realtimeList, times(1)).getAllItems()
//    }
//
//    @Test
//    fun `handles added items`() {
//        val addedItems1 = listOf(1)
//        val addedItems2 = listOf(3)
//        val addedItems3 = listOf(5)
//        realtimeListDataSource.data().subscribe(subscriber)
//        receiveCurrentItems(items)
//
//        receiveAddedItems(addedItems1, position = 0)
//        realtimeListDataSource.data().subscribe(otherSubscriber)
//
//        receiveAddedItems(addedItems2, position = 2)
//        realtimeListDataSource.data().subscribe(otherSubscriber2)
//
//        receiveAddedItems(addedItems3, position = 4)
//        realtimeListDataSource.data().subscribe(otherSubscriber3)
//
//        subscriber.assertValues(AllItems(items), AddedItems(addedItems1, 0), AddedItems(addedItems2, 2), AddedItems(addedItems3, 4))
//        otherSubscriber.assertValues(AllItems(listOf(1, 2, 4, 6)), AddedItems(addedItems2, 2), AddedItems(addedItems3, 4))
//        otherSubscriber2.assertValues(AllItems(listOf(1, 2, 3, 4, 6)), AddedItems(addedItems3, 4))
//        otherSubscriber3.assertValues(AllItems(listOf(1, 2, 3, 4, 5, 6)))
//    }
//
//    @Test
//    fun `handles changed items`() {
//        val changedItems = listOf(1, 3)
//        realtimeListDataSource.data().subscribe(subscriber)
//        receiveCurrentItems(items)
//
//        receiveChangedItems(changedItems, position = 1)
//        realtimeListDataSource.data().subscribe(otherSubscriber)
//
//        subscriber.assertValues(AllItems(items), ChangedItems(changedItems, 1))
//        otherSubscriber.assertValues(AllItems(listOf(2, 1, 3)))
//    }
//
//    @Test
//    fun `handles removed items`() {
//        val removedItems1 = listOf(4)
//        val removedItems2 = listOf(6)
//        val removedItems3 = listOf(2)
//        realtimeListDataSource.data().subscribe(subscriber)
//        receiveCurrentItems(items)
//
//        receiveRemovedItems(removedItems1, position = 1)
//        realtimeListDataSource.data().subscribe(otherSubscriber)
//
//        receiveRemovedItems(removedItems2, position = 1)
//        realtimeListDataSource.data().subscribe(otherSubscriber2)
//
//        receiveRemovedItems(removedItems3, position = 0)
//        realtimeListDataSource.data().subscribe(otherSubscriber3)
//
//        subscriber.assertValues(AllItems(items), RemovedItems(removedItems1, 1), RemovedItems(removedItems2, 1), RemovedItems(removedItems3, 0))
//        otherSubscriber.assertValues(AllItems(listOf(2, 6)), RemovedItems(removedItems2, 1), RemovedItems(removedItems3, 0))
//        otherSubscriber2.assertValues(AllItems(listOf(2)), RemovedItems(removedItems3, 0))
//        otherSubscriber3.assertValues(AllItems(listOf()))
//    }
//
//    @Test
//    fun `handles moved items`() {
//        realtimeListDataSource.data().subscribe(subscriber)
//        receiveCurrentItems(items)
//
//        receiveMovedItem(2, fromPosition = 0, toPosition = 2)
//        realtimeListDataSource.data().subscribe(otherSubscriber)
//
//        receiveMovedItem(2, fromPosition = 2, toPosition = 0)
//        realtimeListDataSource.data().subscribe(otherSubscriber2)
//
//        subscriber.assertValues(AllItems(items), MovedItems(2, 0, 2), MovedItems(2, 2, 0))
//        otherSubscriber.assertValues(AllItems(listOf(4, 6, 2)), MovedItems(2, 2, 0))
//        otherSubscriber2.assertValues(AllItems(listOf(2, 4, 6)))
//    }

    fun receiveCurrentItems(items: List<Int>) = allItemsSubject.onNext(AllItems(items))
    fun receiveAddedItems(items: List<Int>, previousKey: String? = null) = addedItemsSubject.onNext(AddedItems(items, previousKey))
    fun receiveChangedItems(items: List<Int>) = changedItemsSubject.onNext(ChangedItems(items))
    fun receiveRemovedItems(items: List<Int>) = removedItemsSubject.onNext(RemovedItems(items))
    fun receiveMovedItems(items: List<Int>, previousKey: String? = null) = movedItemSubject.onNext(MovedItems(items, previousKey))
}