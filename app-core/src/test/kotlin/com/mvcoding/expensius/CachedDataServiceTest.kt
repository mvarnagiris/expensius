/*
 * Copyright (C) 2016 Mantas Varnagiris.
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

package com.mvcoding.expensius

import com.mvcoding.expensius.CachedData.Companion.cachedData
import com.mvcoding.expensius.EmptyState.EMPTY
import com.mvcoding.expensius.EmptyState.NOT_EMPTY
import com.mvcoding.expensius.LoadingState.LOADED
import com.mvcoding.expensius.LoadingState.LOADING
import org.junit.Test
import rx.Observable.error
import rx.Observable.just
import rx.observers.TestSubscriber
import rx.schedulers.TestScheduler
import java.util.concurrent.TimeUnit.DAYS

class CachedDataServiceTest {
    @Test
    fun canSupportMultipleSubscribers() {
        val result = "result"
        val cachedDataService = CachedDataService<String>({ just(result) })
        val subscriber1 = TestSubscriber.create<CachedData<String>>()
        val subscriber2 = TestSubscriber.create<CachedData<String>>()

        cachedDataService.cachedData.subscribe(subscriber1)
        cachedDataService.refresh()
        cachedDataService.cachedData.subscribe(subscriber2)

        subscriber1.assertValues(cachedData(result), cachedData(result))
        subscriber2.assertValue(cachedData(result))
    }

    @Test
    fun cachesPreviousValue() {
        val result1 = "result1"
        val result2 = "result2"
        var result = result1
        val cachedDataService = CachedDataService<String>({ just(result) })
        val subscriber1 = TestSubscriber.create<CachedData<String>>()
        val subscriber2 = TestSubscriber.create<CachedData<String>>()

        cachedDataService.cachedData.subscribe(subscriber1)
        result = result2
        cachedDataService.cachedData.subscribe(subscriber2)

        subscriber1.assertValues(cachedData(result1))
        subscriber2.assertValue(cachedData(result1))
    }

    @Test
    fun refreshInvalidatesCache() {
        val result1 = "result1"
        val result2 = "result2"
        var result = result1
        val cachedDataService = CachedDataService<String>({ just(result) })
        val subscriber1 = TestSubscriber.create<CachedData<String>>()
        val subscriber2 = TestSubscriber.create<CachedData<String>>()

        cachedDataService.cachedData.subscribe(subscriber1)
        result = result2
        cachedDataService.refresh()
        cachedDataService.cachedData.subscribe(subscriber2)

        subscriber1.assertValues(cachedData(result1), cachedData(result2))
        subscriber2.assertValue(cachedData(result2))
    }

    @Test
    fun returnsLastSuccessfulResultAfterFailure() {
        var isFailure = true
        val result = "result"
        val throwable = Throwable()
        val cachedDataService = CachedDataService<String>({ if (isFailure) error(throwable) else just(result) })
        val subscriber1 = TestSubscriber.create<CachedData<String>>()
        val subscriber2 = TestSubscriber.create<CachedData<String>>()

        cachedDataService.cachedData.subscribe(subscriber1)
        isFailure = false
        cachedDataService.refresh()
        cachedDataService.cachedData.subscribe(subscriber2)

        subscriber1.assertValues(cachedData(throwable), cachedData(result))
        subscriber2.assertValue(cachedData(result))
    }

    @Test
    fun showsLoadingUntilSuccess() {
        val cachedDataService = CachedDataService<String>({ just("result") })
        val loadingSubscriber = TestSubscriber.create<LoadingState>()
        val dataSubscriber = TestSubscriber.create<CachedData<String>>()
        cachedDataService.loadingStates.subscribe(loadingSubscriber)

        cachedDataService.cachedData.subscribe(dataSubscriber)

        loadingSubscriber.assertValues(LOADING, LOADED)
    }

    @Test
    fun showsLoadingUntilFailure() {
        val cachedDataService = CachedDataService<String>({ error(Throwable()) })
        val loadingSubscriber = TestSubscriber.create<LoadingState>()
        val dataSubscriber = TestSubscriber.create<CachedData<String>>()
        cachedDataService.loadingStates.subscribe(loadingSubscriber)

        cachedDataService.cachedData.subscribe(dataSubscriber)

        loadingSubscriber.assertValues(LOADING, LOADED)
    }

    @Test
    fun showsLoadingOnRefreshUntilSuccess() {
        val cachedDataService = CachedDataService<String>({ just("result") })
        val loadingSubscriber = TestSubscriber.create<LoadingState>()
        val dataSubscriber = TestSubscriber.create<CachedData<String>>()
        cachedDataService.loadingStates.subscribe(loadingSubscriber)

        cachedDataService.cachedData.subscribe(dataSubscriber)
        cachedDataService.refresh()

        loadingSubscriber.assertValues(LOADING, LOADED, LOADING, LOADED)
    }

    @Test
    fun showsEmptyStateWhenResultIsEmpty() {
        val cachedDataService = CachedDataService<List<String>>({ just(emptyList<String>()) })
        val emptyStateSubscriber = TestSubscriber.create<EmptyState>()
        val dataSubscriber = TestSubscriber.create<CachedData<List<String>>>()
        cachedDataService.emptyStates.subscribe(emptyStateSubscriber)

        cachedDataService.cachedData.subscribe(dataSubscriber)

        emptyStateSubscriber.assertValues(EMPTY)
    }

    @Test
    fun showsNotEmptyStateWhenResultIsNotEmpty() {
        val cachedDataService = CachedDataService<List<String>>({ just(listOf("result")) })
        val emptyStateSubscriber = TestSubscriber.create<EmptyState>()
        val dataSubscriber = TestSubscriber.create<CachedData<List<String>>>()
        cachedDataService.emptyStates.subscribe(emptyStateSubscriber)

        cachedDataService.cachedData.subscribe(dataSubscriber)

        emptyStateSubscriber.assertValues(NOT_EMPTY)
    }

    @Test
    fun showsNotEmptyStateWhenItWasEmptyAndCacheIsUpdatedToNotEmptyData() {
        val cachedDataService = CachedDataService<List<String>>({ just(emptyList<String>()) })
        val emptyStateSubscriber = TestSubscriber.create<EmptyState>()
        val dataSubscriber = TestSubscriber.create<CachedData<List<String>>>()
        cachedDataService.emptyStates.subscribe(emptyStateSubscriber)
        cachedDataService.cachedData.subscribe(dataSubscriber)

        cachedDataService.updateCacheButDoNotEmit(listOf("value"))

        emptyStateSubscriber.assertValues(EMPTY, NOT_EMPTY)
    }

    @Test
    fun doesNotShowNotEmptyStateWhenItWasAlreadyNotEmptyAndCacheIsUpdatedToNotEmptyData() {
        val cachedDataService = CachedDataService<List<String>>({ just(listOf("value")) })
        val emptyStateSubscriber = TestSubscriber.create<EmptyState>()
        val dataSubscriber = TestSubscriber.create<CachedData<List<String>>>()
        cachedDataService.emptyStates.subscribe(emptyStateSubscriber)
        cachedDataService.cachedData.subscribe(dataSubscriber)

        cachedDataService.updateCacheButDoNotEmit(listOf("value2"))

        emptyStateSubscriber.assertValues(NOT_EMPTY)
    }

    @Test
    fun showsEmptyStateWhenItWasNotEmptyAndCacheIsUpdatedToEmptyData() {
        val cachedDataService = CachedDataService<List<String>>({ just(listOf("value")) })
        val emptyStateSubscriber = TestSubscriber.create<EmptyState>()
        val dataSubscriber = TestSubscriber.create<CachedData<List<String>>>()
        cachedDataService.emptyStates.subscribe(emptyStateSubscriber)
        cachedDataService.cachedData.subscribe(dataSubscriber)

        cachedDataService.updateCacheButDoNotEmit(emptyList())

        emptyStateSubscriber.assertValues(NOT_EMPTY, EMPTY)
    }

    @Test
    fun doesNotShowEmptyStateWhenItWasAlreadyEmptyAndCacheIsUpdatedToEmptyData() {
        val cachedDataService = CachedDataService<List<String>>({ just(emptyList<String>()) })
        val emptyStateSubscriber = TestSubscriber.create<EmptyState>()
        val dataSubscriber = TestSubscriber.create<CachedData<List<String>>>()
        cachedDataService.emptyStates.subscribe(emptyStateSubscriber)
        cachedDataService.cachedData.subscribe(dataSubscriber)

        cachedDataService.updateCacheButDoNotEmit(emptyList())

        emptyStateSubscriber.assertValues(EMPTY)
    }

    @Test
    fun noChangesToEmptyStateWhenErrorHappens() {
        val cachedDataService = CachedDataService<List<String>>({ error(Throwable()) })
        val emptyStateSubscriber = TestSubscriber.create<EmptyState>()
        val dataSubscriber = TestSubscriber.create<CachedData<List<String>>>()
        cachedDataService.emptyStates.subscribe(emptyStateSubscriber)

        cachedDataService.cachedData.subscribe(dataSubscriber)

        emptyStateSubscriber.assertNoValues()
    }

    @Test
    fun cacheCanBeUpdatedButUpdatedValueIsEmittedOnlyToSubsequentSubscribers() {
        val cachedDataService = CachedDataService<String>({ just("value") })
        val dataSubscriber = TestSubscriber.create<CachedData<String>>()
        val anotherDataSubscriber = TestSubscriber.create<CachedData<String>>()
        cachedDataService.cachedData.subscribe(dataSubscriber)

        cachedDataService.updateCacheButDoNotEmit("updated value")
        cachedDataService.cachedData.subscribe(anotherDataSubscriber)

        dataSubscriber.assertValue(cachedData("value"))
        anotherDataSubscriber.assertValue(cachedData("updated value"))
    }

    @Test
    fun lastSuccessfulResultIsUpdatedWhenCacheIsUpdated() {
        val throwable = Throwable()
        val cachedDataService = CachedDataService<String>({ error(throwable) })
        val dataSubscriber = TestSubscriber.create<CachedData<String>>()
        val anotherDataSubscriber = TestSubscriber.create<CachedData<String>>()
        cachedDataService.cachedData.subscribe(dataSubscriber)

        cachedDataService.updateCacheButDoNotEmit("updated value")
        cachedDataService.cachedData.subscribe(anotherDataSubscriber)

        dataSubscriber.assertValue(cachedData(throwable))
        anotherDataSubscriber.assertValue(cachedData("updated value"))
    }

    @Test
    fun twoQuickSubscriptionsDoesOnlyOneWebRequest() {
        val testScheduler = TestScheduler()
        var callCount = 0
        val valueSubject = just("value").delay(100, DAYS, testScheduler)
        val cachedDataService = CachedDataService<String>({ callCount++; valueSubject })
        val firstSubscriber = TestSubscriber.create<CachedData<String>>()
        val secondSubscriber = TestSubscriber.create<CachedData<String>>()

        cachedDataService.cachedData.subscribe(firstSubscriber)
        cachedDataService.cachedData.subscribe(secondSubscriber)
        testScheduler.advanceTimeBy(100, DAYS)

        firstSubscriber.assertValue(cachedData("value"))
        secondSubscriber.assertValue(cachedData("value"))
        assert(callCount == 1)
    }
}