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

package com.mvcoding.expensius.feature.reports

import com.mvcoding.expensius.data.DataSource
import com.mvcoding.expensius.feature.reports.trends.TrendsPresenter
import com.mvcoding.expensius.model.Trends
import com.mvcoding.expensius.model.extensions.aTrends
import com.mvcoding.expensius.rxSchedulers
import com.nhaarman.mockito_kotlin.*
import org.junit.Before
import org.junit.Test
import rx.Observable.error
import rx.Observable.just

class TrendsPresenterTest {

    val trends = aTrends()
    val trendsSource = mock<DataSource<Trends>>()
    val view = mock<TrendsPresenter.View>()
    val presenter = TrendsPresenter(trendsSource, rxSchedulers())

    @Before
    fun setUp() {
        whenever(trendsSource.data()).thenReturn(just(trends))
    }

    @Test
    fun `shows trends`() {
        presenter.attach(view)

        verify(view).showTrends(trends)
    }

    @Test
    fun `ignores errors`() {
        whenever(trendsSource.data()).thenReturn(error(Throwable()))

        presenter.attach(view)

        verify(view, never()).showTrends(any())
    }
}