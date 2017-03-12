package com.mvcoding.expensius.data

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Test
import rx.lang.kotlin.PublishSubject
import rx.observers.TestSubscriber

class MemoryDataSourceTest {
    @Test
    fun `access to main data source is made only once and cached data is returned to other subscribers`() {
        testAccessToMainDataSourceIsMadeOnlyOnceAndCachedDataIsReturnedToOtherSubscribers(1) { MemoryDataSource(it) }
    }

    @Test
    fun `returns data to late subscribers when initial subscriber unsubscribes before data is delivered`() {
        testReturnsDataToLateSubscribersWhenInitialSubscriberUnsubscribesBeforeDataIsDelivered(1) { MemoryDataSource(it) }
    }

    @Test
    fun `resets observable when error happens`() {
        testResetsObservableWhenErrorHappens(1) { MemoryDataSource(it) }
    }
}

fun <T> testMemoryDataSource(data: T, createDataSource: (DataSource<T>) -> DataSource<T>) {
    testAccessToMainDataSourceIsMadeOnlyOnceAndCachedDataIsReturnedToOtherSubscribers(data, createDataSource)
    testReturnsDataToLateSubscribersWhenInitialSubscriberUnsubscribesBeforeDataIsDelivered(data, createDataSource)
    testResetsObservableWhenErrorHappens(data, createDataSource)
}

private fun <T> testAccessToMainDataSourceIsMadeOnlyOnceAndCachedDataIsReturnedToOtherSubscribers(
        data: T,
        createDataSource: (DataSource<T>) -> DataSource<T>) {

    val initialSubscriber1 = TestSubscriber.create<T>()
    val initialSubscriber2 = TestSubscriber.create<T>()
    val otherSubscriber = TestSubscriber.create<T>()
    val dataSubject = PublishSubject<T>()
    val mainDataSource = mock<DataSource<T>>()
    val memoryDataSource = createDataSource(mainDataSource)
    whenever(mainDataSource.data()).thenReturn(dataSubject)

    memoryDataSource.data().subscribe(initialSubscriber1)
    memoryDataSource.data().subscribe(initialSubscriber2)
    dataSubject.onNext(data)
    memoryDataSource.data().subscribe(otherSubscriber)

    initialSubscriber1.assertValue(data)
    initialSubscriber2.assertValue(data)
    otherSubscriber.assertValue(data)
    verify(mainDataSource, times(1)).data()
}

private fun <T> testReturnsDataToLateSubscribersWhenInitialSubscriberUnsubscribesBeforeDataIsDelivered(
        data: T,
        createDataSource: (DataSource<T>) -> DataSource<T>) {

    val initialSubscriber = TestSubscriber.create<T>()
    val otherSubscriber = TestSubscriber.create<T>()
    val dataSubject = PublishSubject<T>()
    val mainDataSource = mock<DataSource<T>>()
    val memoryDataSource = createDataSource(mainDataSource)
    whenever(mainDataSource.data()).thenReturn(dataSubject)

    memoryDataSource.data().subscribe(initialSubscriber)
    initialSubscriber.unsubscribe()
    dataSubject.onNext(data)
    memoryDataSource.data().subscribe(otherSubscriber)

    initialSubscriber.assertNoValues()
    otherSubscriber.assertValue(data)
    verify(mainDataSource, times(1)).data()
}

private fun <T> testResetsObservableWhenErrorHappens(data: T, createDataSource: (DataSource<T>) -> DataSource<T>) {
    val initialSubscriber = TestSubscriber.create<T>()
    val otherSubscriber = TestSubscriber.create<T>()
    val dataSubject1 = PublishSubject<T>()
    val dataSubject2 = PublishSubject<T>()
    val mainDataSource = mock<DataSource<T>>()
    val memoryDataSource = createDataSource(mainDataSource)
    whenever(mainDataSource.data()).thenReturn(dataSubject1)

    memoryDataSource.data().subscribe(initialSubscriber)
    dataSubject1.onError(Throwable())
    whenever(mainDataSource.data()).thenReturn(dataSubject2)
    memoryDataSource.data().subscribe(otherSubscriber)
    dataSubject2.onNext(data)

    initialSubscriber.assertNoValues()
    otherSubscriber.assertValue(data)
    verify(mainDataSource, times(2)).data()
}