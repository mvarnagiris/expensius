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

class RealtimeListDataSourceTest {

//    val allItemsSubject = PublishSubject<RawRealtimeData.AllItems<Item>>()
//    val addedItemsSubject = PublishSubject<RawRealtimeData.AddedItems<Item>>()
//    val changedItemsSubject = PublishSubject<RawRealtimeData.ChangedItems<Item>>()
//    val removedItemsSubject = PublishSubject<RawRealtimeData.RemovedItems<Item>>()
//    val movedItemSubject = PublishSubject<RawRealtimeData.MovedItems<Item>>()
//
//    val items = listOf(item(2), item(4), item(6))
//    val otherItems = listOf(item(8), item(10), item(12))
//
//    val realtimeList = mock<RealtimeList<Item>>()
//    val itemToKey = mock<(Item) -> String>()
//    val realtimeListDataSource = RealtimeListDataSource(realtimeList, itemToKey)
//    val subscriber = TestSubscriber<RealtimeData<Item>>()
//    val otherSubscriber = TestSubscriber<RealtimeData<Item>>()
//    val otherSubscriber2 = TestSubscriber<RealtimeData<Item>>()
//    val otherSubscriber3 = TestSubscriber<RealtimeData<Item>>()
//
//    @Before
//    fun setUp() {
//        whenever(realtimeList.getAllItems()).thenReturn(allItemsSubject)
//        whenever(realtimeList.getAddedItems()).thenReturn(addedItemsSubject)
//        whenever(realtimeList.getChangedItems()).thenReturn(changedItemsSubject)
//        whenever(realtimeList.getRemovedItems()).thenReturn(removedItemsSubject)
//        whenever(realtimeList.getMovedItem()).thenReturn(movedItemSubject)
//        whenever(itemToKey.invoke(any())).thenAnswer { it.getArgument<Item>(0).id.toString() }
//    }
//
//    @Test
//    fun `emits current items first and ignores other events until current items come`() {
//        val changedItems = items.map { it.value(0) }
//        val changedItemsAfterMove = changedItems.drop(1).plus(changedItems[0])
//        realtimeListDataSource.data().subscribe(subscriber)
//
//        receiveAddedItems(otherItems)
//        receiveChangedItems(changedItems)
//        receiveRemovedItems(otherItems)
//        receiveMovedItems(listOf(changedItems.first()), changedItems.last().id.toString())
//        subscriber.assertNoValues()
//
//        receiveCurrentItems(items)
//        subscriber.assertValue(AllItems(items))
//
//        receiveAddedItems(otherItems)
//        receiveChangedItems(changedItems)
//        receiveRemovedItems(otherItems)
//        receiveMovedItems(listOf(changedItems.first()), changedItems.last().id.toString())
//        subscriber.assertValues(
//                AllItems(items),
//                AddedItems(otherItems + items, otherItems, 0),
//                ChangedItems(otherItems + changedItems, changedItems, 3),
//                RemovedItems(changedItems, otherItems, 0),
//                MovedItems(changedItemsAfterMove, listOf(changedItems.first()), 0, changedItems.size - 1))
//    }
//
//    @Test
//    fun `only takes first result from current values`() {
//        realtimeListDataSource.data().subscribe(subscriber)
//
//        receiveCurrentItems(items)
//        receiveCurrentItems(listOf(item(1), item(3)))
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
//        val addedItems1 = listOf(item(1))
//        val addedItems2 = listOf(item(3))
//        val addedItems3 = listOf(item(5))
//        val itemsWithAdded1 = addedItems1 + items
//        val itemsWithAdded2 = itemsWithAdded1.take(2) + addedItems2 + itemsWithAdded1.drop(2)
//        val itemsWithAdded3 = itemsWithAdded2.dropLast(1) + addedItems3 + itemsWithAdded2.takeLast(1)
//        realtimeListDataSource.data().subscribe(subscriber)
//        receiveCurrentItems(items)
//
//        receiveAddedItems(addedItems1, null)
//        realtimeListDataSource.data().subscribe(otherSubscriber)
//
//        receiveAddedItems(addedItems2, "2")
//        realtimeListDataSource.data().subscribe(otherSubscriber2)
//
//        receiveAddedItems(addedItems3, "4")
//        realtimeListDataSource.data().subscribe(otherSubscriber3)
//
//        subscriber.assertValues(AllItems(items), AddedItems(itemsWithAdded1, addedItems1, 0), AddedItems(itemsWithAdded2, addedItems2, 2), AddedItems(itemsWithAdded3, addedItems3, 4))
//        otherSubscriber.assertValues(AllItems(listOf(item(1), item(2), item(4), item(6))), AddedItems(itemsWithAdded2, addedItems2, 2), AddedItems(itemsWithAdded3, addedItems3, 4))
//        otherSubscriber2.assertValues(AllItems(listOf(item(1), item(2), item(3), item(4), item(6))), AddedItems(itemsWithAdded3, addedItems3, 4))
//        otherSubscriber3.assertValues(AllItems(listOf(item(1), item(2), item(3), item(4), item(5), item(6))))
//    }
//
//    @Test
//    fun `handles changed items`() {
//        val changedItems = listOf(item(4).value(0), item(6).value(0))
//        realtimeListDataSource.data().subscribe(subscriber)
//        receiveCurrentItems(items)
//
//        receiveChangedItems(changedItems)
//        realtimeListDataSource.data().subscribe(otherSubscriber)
//
//        subscriber.assertValues(AllItems(items), ChangedItems(items.take(1) + changedItems, changedItems, 1))
//        otherSubscriber.assertValues(AllItems(listOf(item(2), item(4).value(0), item(6).value(0))))
//    }
//
//    @Test
//    fun `does not emit changed items when they are not changed`() {
//        val notChangedItems = listOf(item(4), item(6))
//        realtimeListDataSource.data().subscribe(subscriber)
//        receiveCurrentItems(items)
//
//        receiveChangedItems(notChangedItems)
//        realtimeListDataSource.data().subscribe(otherSubscriber)
//
//        subscriber.assertValues(AllItems(items))
//        otherSubscriber.assertValues(AllItems(items))
//    }
//
//    @Test
//    fun `handles removed items`() {
//        val removedItems1 = listOf(item(4))
//        val removedItems2 = listOf(item(6))
//        val removedItems3 = listOf(item(2))
//        realtimeListDataSource.data().subscribe(subscriber)
//        receiveCurrentItems(items)
//
//        receiveRemovedItems(removedItems1)
//        realtimeListDataSource.data().subscribe(otherSubscriber)
//
//        receiveRemovedItems(removedItems2)
//        realtimeListDataSource.data().subscribe(otherSubscriber2)
//
//        receiveRemovedItems(removedItems3)
//        realtimeListDataSource.data().subscribe(otherSubscriber3)
//
//        subscriber.assertValues(AllItems(items), RemovedItems(items.take(1) + items.takeLast(1), removedItems1, 1), RemovedItems(items.take(1), removedItems2, 1), RemovedItems(emptyList(), removedItems3, 0))
//        otherSubscriber.assertValues(AllItems(listOf(item(2), item(6))), RemovedItems(items.take(1), removedItems2, 1), RemovedItems(emptyList(), removedItems3, 0))
//        otherSubscriber2.assertValues(AllItems(listOf(item(2))), RemovedItems(emptyList(), removedItems3, 0))
//        otherSubscriber3.assertValues(AllItems(listOf()))
//    }
//
//    @Test
//    fun `handles moved items`() {
//        realtimeListDataSource.data().subscribe(subscriber)
//        receiveCurrentItems(items)
//
//        receiveMovedItems(listOf(item(2)), "6")
//        realtimeListDataSource.data().subscribe(otherSubscriber)
//
//        receiveMovedItems(listOf(item(2)), null)
//        realtimeListDataSource.data().subscribe(otherSubscriber2)
//
//        subscriber.assertValues(AllItems(items), MovedItems(items.drop(1) + items[0], listOf(item(2)), 0, 2), MovedItems(items, listOf(item(2)), 2, 0))
//        otherSubscriber.assertValues(AllItems(listOf(item(4), item(6), item(2))), MovedItems(items, listOf(item(2)), 2, 0))
//        otherSubscriber2.assertValues(AllItems(listOf(item(2), item(4), item(6))))
//    }
//
//    @Test
//    fun `does not emit moved item when position does not change`() {
//        realtimeListDataSource.data().subscribe(subscriber)
//        receiveCurrentItems(items)
//
//        receiveMovedItems(listOf(item(2)), null)
//        realtimeListDataSource.data().subscribe(otherSubscriber)
//
//        subscriber.assertValues(RealtimeData.AllItems(items))
//        otherSubscriber.assertValues(RealtimeData.AllItems(items))
//    }
//
//    @Test
//    fun `closes underlying realtime list when this data source is closed`() {
//        realtimeListDataSource.close()
//
//        verify(realtimeList).close()
//    }
//
//    fun receiveCurrentItems(items: List<Item>) = allItemsSubject.onNext(RawRealtimeData.AllItems(items))
//    fun receiveAddedItems(items: List<Item>, previousKey: String? = null) = addedItemsSubject.onNext(RawRealtimeData.AddedItems(items, previousKey))
//    fun receiveChangedItems(items: List<Item>) = changedItemsSubject.onNext(RawRealtimeData.ChangedItems(items))
//    fun receiveRemovedItems(items: List<Item>) = removedItemsSubject.onNext(RawRealtimeData.RemovedItems(items))
//    fun receiveMovedItems(items: List<Item>, previousKey: String? = null) = movedItemSubject.onNext(RawRealtimeData.MovedItems(items, previousKey))
//    fun item(value: Int) = Item(value, value)
//    fun Item.value(value: Int) = copy(value = value)
//
//    data class Item(val id: Int, val value: Int)
}