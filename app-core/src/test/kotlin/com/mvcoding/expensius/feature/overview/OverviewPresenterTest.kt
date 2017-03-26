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

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Test
import rx.lang.kotlin.PublishSubject

class OverviewPresenterTest {
    val newTransactionSelectedSubject = PublishSubject<Unit>()
    val transactionsSelectedSubject = PublishSubject<Unit>()
    val tagsSelectedSubject = PublishSubject<Unit>()
//    val tagsReportSelectedSubject = PublishSubject<Unit>()
val settingsSelectedSubject = PublishSubject<Unit>()

//    val interval = Interval(DateTime.now().minusDays(1), DateTime.now())
//    val appUser = anAppUser()
//    val appUserService: AppUserService = mock<AppUserService>().apply { whenever(appUser()).thenReturn(just(appUser)) }
//    val filter = Filter(appUserService, aFixedTimestampProvider()).setInterval(interval)
val view = mock<OverviewPresenter.View>()
    val presenter = OverviewPresenter()

    @Before
    fun setUp() {
        whenever(view.createTransactionSelects()).thenReturn(newTransactionSelectedSubject)
        whenever(view.transactionsSelects()).thenReturn(transactionsSelectedSubject)
        whenever(view.tagsSelects()).thenReturn(tagsSelectedSubject)
//        whenever(view.tagsReportSelects()).thenReturn(tagsReportSelectedSubject)
        whenever(view.settingsSelects()).thenReturn(settingsSelectedSubject)
    }

//    @Test
//    fun `shows interval`() {
//        presenter.attach(view)
//
//        verify(view).showInterval(appUser.settings.reportPeriod, interval)
//    }

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

//    @Test
//    fun `displays tags report`() {
//        presenter.attach(view)
//
//        selectTagsReport()
//
//        verify(view).displayTagsReport()
//    }

    @Test
    fun `displays settings`() {
        presenter.attach(view)

        selectSettings()

        verify(view).displaySettings()
    }

    private fun selectNewTransaction() = newTransactionSelectedSubject.onNext(Unit)
    private fun selectTransactions() = transactionsSelectedSubject.onNext(Unit)
    private fun selectTags() = tagsSelectedSubject.onNext(Unit)
//    private fun selectTagsReport() = tagsReportSelectedSubject.onNext(Unit)
private fun selectSettings() = settingsSelectedSubject.onNext(Unit)
}