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

package com.mvcoding.expensius.feature.filter

import com.mvcoding.expensius.data.Cache
import com.mvcoding.expensius.model.Filter
import com.mvcoding.expensius.model.aFilter
import com.mvcoding.expensius.rxSchedulers
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Test
import rx.Observable.from
import rx.Observable.just
import rx.lang.kotlin.PublishSubject

class FilterPresenterTest {

    val nextIntervalRequestsSubject = PublishSubject<Unit>()
    val previousIntervalRequestsSubject = PublishSubject<Unit>()

    val filter = aFilter()
    val filterCache = mock<Cache<Filter>>()
    val view = mock<FilterPresenter.View>()
    val presenter = FilterPresenter(filterCache, rxSchedulers())

    @Before
    fun setUp() {
        whenever(filterCache.data()).thenReturn(just(filter))
        whenever(view.nextIntervalRequests()).thenReturn(nextIntervalRequestsSubject)
        whenever(view.previousIntervalRequests()).thenReturn(previousIntervalRequestsSubject)
    }

    @Test
    fun `shows interval`() {
        presenter.attach(view)

        verify(view).showInterval(filter.interval, filter.reportPeriod)
    }

    @Test
    fun `shows updated interval when filter changes`() {
        val changedFilter = filter.withNextInterval()
        whenever(filterCache.data()).thenReturn(from(listOf(filter, changedFilter)))

        presenter.attach(view)

        verify(view).showInterval(filter.interval, filter.reportPeriod)
        verify(view).showInterval(changedFilter.interval, filter.reportPeriod)
    }

    @Test
    fun `writes filter with next interval when next interval is requested`() {
        val changedFilter = filter.withNextInterval()
        presenter.attach(view)

        requestNextInterval()

        verify(filterCache).write(changedFilter)
    }

    @Test
    fun `writes filter with previous interval when previous interval is requested`() {
        val changedFilter = filter.withPreviousInterval()
        presenter.attach(view)

        requestPreviousInterval()

        verify(filterCache).write(changedFilter)
    }

    fun requestNextInterval() = nextIntervalRequestsSubject.onNext(Unit)
    fun requestPreviousInterval() = previousIntervalRequestsSubject.onNext(Unit)
}