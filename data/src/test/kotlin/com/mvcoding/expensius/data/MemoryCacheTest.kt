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

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Test
import rx.Observable.just
import rx.lang.kotlin.PublishSubject
import rx.observers.TestSubscriber

class MemoryCacheTest {
    @Test
    fun `behaves like memory data source`() {
        testMemoryDataSource(1) { MemoryCache(it) }
    }

    @Test
    fun `puts in memory and emits value that has just been written`() {
        testPutsInMemoryAndEmitsValueThatHasJustBeenWritten(1, 2) { MemoryCache(it) }
    }

    @Test
    fun `emits value from original source when it emits after write`() {
        testEmitsValueFromOriginalSourceWhenItEmitsAfterWrite(1, 2) { MemoryCache(it) }
    }
}

fun <DATA> testMemoryCache(data: DATA, otherData: DATA, createCache: (DataSource<DATA>) -> Cache<DATA>) {
    testMemoryDataSource(data, createCache)
    testPutsInMemoryAndEmitsValueThatHasJustBeenWritten(data, otherData, createCache)
    testEmitsValueFromOriginalSourceWhenItEmitsAfterWrite(data, otherData, createCache)
}

private fun <DATA> testPutsInMemoryAndEmitsValueThatHasJustBeenWritten(
        data: DATA,
        otherData: DATA,
        createCache: (DataSource<DATA>) -> Cache<DATA>) {

    val initialSubscriber = TestSubscriber<DATA>()
    val afterWriteSubscriber = TestSubscriber<DATA>()
    val dataSource = mock<DataSource<DATA>>()
    whenever(dataSource.data()).thenReturn(just(data))
    val memoryCache = createCache(dataSource)

    memoryCache.data().subscribe(initialSubscriber)
    memoryCache.write(otherData)
    memoryCache.data().subscribe(afterWriteSubscriber)

    initialSubscriber.assertValues(data, otherData)
    afterWriteSubscriber.assertValue(otherData)
}

private fun <DATA> testEmitsValueFromOriginalSourceWhenItEmitsAfterWrite(
        data: DATA,
        otherData: DATA,
        createCache: (DataSource<DATA>) -> Cache<DATA>) {

    val dataSourceSubject = PublishSubject<DATA>()
    val dataSource = mock<DataSource<DATA>>()
    val subscriber = TestSubscriber<DATA>()
    whenever(dataSource.data()).thenReturn(dataSourceSubject)

    val memoryCache = createCache(dataSource)
    memoryCache.write(data)
    memoryCache.data().subscribe(subscriber)
    dataSourceSubject.onNext(otherData)

    subscriber.assertValues(data, otherData)
}