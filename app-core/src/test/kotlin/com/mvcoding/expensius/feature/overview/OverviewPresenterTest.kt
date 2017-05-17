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

package com.mvcoding.expensius.feature.overview

import com.mvcoding.expensius.data.DataSource
import com.mvcoding.expensius.model.RemoteFilter
import com.mvcoding.expensius.model.ReportSettings
import com.mvcoding.expensius.model.extensions.aRemoteFilter
import com.mvcoding.expensius.model.extensions.aReportSettings
import com.mvcoding.expensius.rxSchedulers
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Test
import rx.Observable.just
import rx.lang.kotlin.PublishSubject

class OverviewPresenterTest {
    val newTransactionSelectedSubject = PublishSubject<Unit>()
    val transactionsSelectedSubject = PublishSubject<Unit>()
    val tagsSelectedSubject = PublishSubject<Unit>()
    val trendsReportSelectsSubject = PublishSubject<Unit>()
    val tagsReportSelectedSubject = PublishSubject<Unit>()
    val settingsSelectedSubject = PublishSubject<Unit>()
    val filter = aRemoteFilter()
    val reportSettings = aReportSettings()

    val filterSource = mock<DataSource<RemoteFilter>>()
    val reportSettingsSource = mock<DataSource<ReportSettings>>()
    val view = mock<OverviewPresenter.View>()
    val presenter = OverviewPresenter(filterSource, reportSettingsSource, rxSchedulers())

    @Before
    fun setUp() {
        whenever(view.createTransactionSelects()).thenReturn(newTransactionSelectedSubject)
        whenever(view.transactionsSelects()).thenReturn(transactionsSelectedSubject)
        whenever(view.tagsSelects()).thenReturn(tagsSelectedSubject)
        whenever(view.trendsReportSelects()).thenReturn(trendsReportSelectsSubject)
        whenever(view.tagsReportSelects()).thenReturn(tagsReportSelectedSubject)
        whenever(view.settingsSelects()).thenReturn(settingsSelectedSubject)
        whenever(filterSource.data()).thenReturn(just(filter))
        whenever(reportSettingsSource.data()).thenReturn(just(reportSettings))
    }

    @Test
    fun `shows interval`() {
        presenter.attach(view)

        verify(view).showInterval(filter.interval, reportSettings.reportPeriod)
    }

    @Test
    fun `shows create transaction`() {
        presenter.attach(view)

        selectNewTransaction()

        verify(view).displayCreateTransaction()
    }

    @Test
    fun `displays transactions`() {
        presenter.attach(view)

        selectTransactions()

        verify(view).displayTransactions()
    }

    @Test
    fun `displays tags`() {
        presenter.attach(view)

        selectTags()

        verify(view).displayTags()
    }

    @Test
    fun `displays trends report`() {
        presenter.attach(view)

        selectTrendsReport()

        verify(view).displayTrendsReport()
    }

    @Test
    fun `displays tags report`() {
        presenter.attach(view)

        selectTagsReport()

        verify(view).displayTagsReport()
    }

    @Test
    fun `displays settings`() {
        presenter.attach(view)

        selectSettings()

        verify(view).displaySettings()
    }

    private fun selectNewTransaction() = newTransactionSelectedSubject.onNext(Unit)
    private fun selectTransactions() = transactionsSelectedSubject.onNext(Unit)
    private fun selectTags() = tagsSelectedSubject.onNext(Unit)
    private fun selectTrendsReport() = trendsReportSelectsSubject.onNext(Unit)
    private fun selectTagsReport() = tagsReportSelectedSubject.onNext(Unit)
    private fun selectSettings() = settingsSelectedSubject.onNext(Unit)
}