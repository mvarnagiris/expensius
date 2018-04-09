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

class MemoryDataSourceTest {
//    @Test
//    fun `access to main data source is made only once and cached data is returned to other subscribers`() {
//        testAccessToMainDataSourceIsMadeOnlyOnceAndCachedDataIsReturnedToOtherSubscribers(1) { MemoryDataSource(it) }
//    }
//
//    @Test
//    fun `returns data to late subscribers when initial subscriber unsubscribes before data is delivered`() {
//        testReturnsDataToLateSubscribersWhenInitialSubscriberUnsubscribesBeforeDataIsDelivered(1) { MemoryDataSource(it) }
//    }
//
//    @Test
//    fun `resets observable when error happens`() {
//        testResetsObservableWhenErrorHappens(1) { MemoryDataSource(it) }
//    }
}

fun <T> testMemoryDataSource(data: T, createDataSource: (DataSource<T>) -> DataSource<T>) {
//    testAccessToMainDataSourceIsMadeOnlyOnceAndCachedDataIsReturnedToOtherSubscribers(data, createDataSource)
//    testReturnsDataToLateSubscribersWhenInitialSubscriberUnsubscribesBeforeDataIsDelivered(data, createDataSource)
//    testResetsObservableWhenErrorHappens(data, createDataSource)
}

//private fun <T> testAccessToMainDataSourceIsMadeOnlyOnceAndCachedDataIsReturnedToOtherSubscribers(
//        data: T,
//        createDataSource: (DataSource<T>) -> DataSource<T>) {
//
//    val initialSubscriber1 = TestSubscriber.create<T>()
//    val initialSubscriber2 = TestSubscriber.create<T>()
//    val otherSubscriber = TestSubscriber.create<T>()
//    val dataSubject = PublishSubject<T>()
//    val mainDataSource = mock<DataSource<T>>()
//    val memoryDataSource = createDataSource(mainDataSource)
//    whenever(mainDataSource.data()).thenReturn(dataSubject)
//
//    memoryDataSource.data().subscribe(initialSubscriber1)
//    memoryDataSource.data().subscribe(initialSubscriber2)
//    dataSubject.onNext(data)
//    memoryDataSource.data().subscribe(otherSubscriber)
//
//    initialSubscriber1.assertValue(data)
//    initialSubscriber2.assertValue(data)
//    otherSubscriber.assertValue(data)
//    verify(mainDataSource, times(1)).data()
//}
//
//private fun <T> testReturnsDataToLateSubscribersWhenInitialSubscriberUnsubscribesBeforeDataIsDelivered(
//        data: T,
//        createDataSource: (DataSource<T>) -> DataSource<T>) {
//
//    val initialSubscriber = TestSubscriber.create<T>()
//    val otherSubscriber = TestSubscriber.create<T>()
//    val dataSubject = PublishSubject<T>()
//    val mainDataSource = mock<DataSource<T>>()
//    val memoryDataSource = createDataSource(mainDataSource)
//    whenever(mainDataSource.data()).thenReturn(dataSubject)
//
//    memoryDataSource.data().subscribe(initialSubscriber)
//    initialSubscriber.unsubscribe()
//    dataSubject.onNext(data)
//    memoryDataSource.data().subscribe(otherSubscriber)
//
//    initialSubscriber.assertNoValues()
//    otherSubscriber.assertValue(data)
//    verify(mainDataSource, times(1)).data()
//}
//
//private fun <T> testResetsObservableWhenErrorHappens(data: T, createDataSource: (DataSource<T>) -> DataSource<T>) {
//    val initialSubscriber = TestSubscriber.create<T>()
//    val otherSubscriber = TestSubscriber.create<T>()
//    val dataSubject1 = PublishSubject<T>()
//    val dataSubject2 = PublishSubject<T>()
//    val mainDataSource = mock<DataSource<T>>()
//    val memoryDataSource = createDataSource(mainDataSource)
//    whenever(mainDataSource.data()).thenReturn(dataSubject1)
//
//    memoryDataSource.data().subscribe(initialSubscriber)
//    dataSubject1.onError(Throwable())
//    whenever(mainDataSource.data()).thenReturn(dataSubject2)
//    memoryDataSource.data().subscribe(otherSubscriber)
//    dataSubject2.onNext(data)
//
//    initialSubscriber.assertNoValues()
//    otherSubscriber.assertValue(data)
//    verify(mainDataSource, times(2)).data()
//}