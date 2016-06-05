/*
 * Copyright (C) 2015 Mantas Varnagiris.
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

import com.mvcoding.expensius.feature.Filter
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.joda.time.DateTime
import org.joda.time.Interval
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.verify
import rx.lang.kotlin.PublishSubject

class OverviewPresenterTest {
    val newTransactionSelectedSubject = PublishSubject<Unit>()
    val transactionsSelectedSubject = PublishSubject<Unit>()
    val tagsSelectedSubject = PublishSubject<Unit>()
    val settingsSelectedSubject = PublishSubject<Unit>()

    val interval = Interval(DateTime.now().minusDays(1), DateTime.now())
    val filter = Filter().apply { setInterval(interval) }
    val view = mock<OverviewPresenter.View>()
    val presenter = OverviewPresenter(filter)

    @Before
    fun setUp() {
        whenever(view.createTransactionSelects()).thenReturn(newTransactionSelectedSubject)
        whenever(view.transactionsSelects()).thenReturn(transactionsSelectedSubject)
        whenever(view.tagsSelects()).thenReturn(tagsSelectedSubject)
        whenever(view.settingsSelects()).thenReturn(settingsSelectedSubject)
    }

    @Test
    fun showsInterval() {
        presenter.onViewAttached(view)

        verify(view).showInterval(interval)
    }

    @Test
    fun displaysCreateTransaction() {
        presenter.onViewAttached(view)

        selectNewTransaction()

        verify(view).displayCreateTransaction()
    }

    @Test
    fun displaysTransactions() {
        presenter.onViewAttached(view)

        selectTransactions()

        verify(view).displayTransactions()
    }

    @Test
    fun displaysTags() {
        presenter.onViewAttached(view)

        selectTags()

        verify(view).displayTags()
    }

    @Test
    fun displaysSettings() {
        presenter.onViewAttached(view)

        selectSettings()

        verify(view).displaySettings()
    }

    private fun selectNewTransaction() = newTransactionSelectedSubject.onNext(Unit)
    private fun selectTransactions() = transactionsSelectedSubject.onNext(Unit)
    private fun selectTags() = tagsSelectedSubject.onNext(Unit)
    private fun selectSettings() = settingsSelectedSubject.onNext(Unit)
}