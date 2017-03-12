package com.mvcoding.expensius.data

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Test
import rx.Observable
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
}

fun <DATA> testMemoryCache(data: DATA, otherData: DATA, createCache: (DataSource<DATA>) -> Cache<DATA>) {
    testMemoryDataSource(data, createCache)
    testPutsInMemoryAndEmitsValueThatHasJustBeenWritten(data, otherData, createCache)
}

private fun <DATA, DS> testPutsInMemoryAndEmitsValueThatHasJustBeenWritten(
        data: DATA,
        otherData: DATA,
        createDataSource: (DataSource<DATA>) -> DS)
        where DS : DataSource<DATA>, DS : DataWriter<DATA> {

    val dataSource = mock<DataSource<DATA>>()
    whenever(dataSource.data()).thenReturn(Observable.just(data))
    val writableMemoryDataSource = createDataSource(dataSource)

    val initialSubscriber = TestSubscriber<DATA>()
    writableMemoryDataSource.data().subscribe(initialSubscriber)

    writableMemoryDataSource.write(otherData)
    val afterWriteSubscriber = TestSubscriber<DATA>()
    writableMemoryDataSource.data().subscribe(afterWriteSubscriber)

    initialSubscriber.assertValues(data, otherData)
    afterWriteSubscriber.assertValue(otherData)
}