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
import com.mvcoding.expensius.data.DataSource
import com.mvcoding.expensius.model.RemoteFilter
import com.mvcoding.expensius.model.ReportSettings
import com.mvcoding.expensius.model.aRemoteFilter
import com.mvcoding.expensius.model.aReportSettings
import com.mvcoding.expensius.rxSchedulers
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Test
import rx.Observable.just
import rx.lang.kotlin.PublishSubject

class RemoteFilterPresenterTest {

    val nextIntervalRequestsSubject = PublishSubject<Unit>()
    val previousIntervalRequestsSubject = PublishSubject<Unit>()
    val filter = aRemoteFilter()
    val reportSettings = aReportSettings()

    val filterCache = mock<Cache<RemoteFilter>>()
    val reportSettingsSource = mock<DataSource<ReportSettings>>()
    val view = mock<FilterPresenter.View>()
    val presenter = FilterPresenter(filterCache, reportSettingsSource, rxSchedulers())

    @Before
    fun setUp() {
        whenever(filterCache.data()).thenReturn(just(filter))
        whenever(reportSettingsSource.data()).thenReturn(just(reportSettings))
        whenever(view.nextIntervalRequests()).thenReturn(nextIntervalRequestsSubject)
        whenever(view.previousIntervalRequests()).thenReturn(previousIntervalRequestsSubject)
    }

    @Test
    fun `shows interval`() {
        presenter.attach(view)

        verify(view).showInterval(filter.interval, reportSettings.reportPeriod)
    }

    @Test
    fun `shows updated interval when filter changes`() {
        val changedFilter = filter.withNextInterval(reportSettings.reportPeriod)
        val filterSubject = PublishSubject<RemoteFilter>()
        whenever(filterCache.data()).thenReturn(filterSubject)

        presenter.attach(view)
        filterSubject.onNext(filter)
        filterSubject.onNext(changedFilter)

        verify(view).showInterval(filter.interval, reportSettings.reportPeriod)
        verify(view).showInterval(changedFilter.interval, reportSettings.reportPeriod)
    }

    @Test
    fun `writes filter with next interval when next interval is requested`() {
        val changedFilter = filter.withNextInterval(reportSettings.reportPeriod)
        presenter.attach(view)

        requestNextInterval()

        verify(filterCache).write(changedFilter)
    }

    @Test
    fun `writes filter with previous interval when previous interval is requested`() {
        val changedFilter = filter.withPreviousInterval(reportSettings.reportPeriod)
        presenter.attach(view)

        requestPreviousInterval()

        verify(filterCache).write(changedFilter)
    }

    fun requestNextInterval() = nextIntervalRequestsSubject.onNext(Unit)
    fun requestPreviousInterval() = previousIntervalRequestsSubject.onNext(Unit)
}