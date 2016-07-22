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

package com.mvcoding.expensius.feature.report

import com.mvcoding.expensius.anAlwaysOneExchangeRateProvider
import com.mvcoding.expensius.extensions.interval
import com.mvcoding.expensius.feature.Filter
import com.mvcoding.expensius.model.ReportPeriod
import com.mvcoding.expensius.model.aFixedTimestampProvider
import com.mvcoding.expensius.model.anAppUser
import com.mvcoding.expensius.rxSchedulers
import com.mvcoding.expensius.service.AppUserService
import com.mvcoding.expensius.service.TransactionsService
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argThat
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.joda.time.DateTime
import org.joda.time.Interval
import org.junit.Before
import org.junit.Test
import rx.Observable.just

class TrendReportPresenterTest {
    val appUserService: AppUserService = mock<AppUserService>().apply { whenever(appUser()).thenReturn(just(anAppUser())) }
    val transactionsService: TransactionsService = mock()
    val filter: Filter = Filter(appUserService, aFixedTimestampProvider())
    val view: TrendReportPresenter.View = mock()
    val presenter = TrendReportPresenter(appUserService, transactionsService, anAlwaysOneExchangeRateProvider(), filter, rxSchedulers())

    @Before
    fun setUp() {
        whenever(appUserService.appUser()).thenReturn(just(anAppUser()))
        whenever(transactionsService.items()).thenReturn(just(emptyList()))
    }

    @Test
    fun showsTrends() {
        presenter.attach(view)

        verify(view).showTrends(any(), any(), any())
    }

    @Test
    fun updatesTrendsWhenDataWithinFilterChanges() {
        presenter.attach(view)

        filter.setInterval(Interval(0, 1))

        verify(view, times(2)).showTrends(any(), any(), any())
    }

    @Test
    fun normalizesTheAmountOfItemsToMax() {
        val february = DateTime(2016, 2, 3, 1, 1)
        val interval = ReportPeriod.MONTH.interval(february.millis)
        filter.setInterval(interval)

        presenter.attach(view)

        verify(view).showTrends(any(), argThat { size == 31 }, argThat { size == 31 })
    }
}