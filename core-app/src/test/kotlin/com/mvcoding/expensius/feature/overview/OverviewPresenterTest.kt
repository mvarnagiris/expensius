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

import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.*
import rx.lang.kotlin.PublishSubject

class OverviewPresenterTest {
    val addNewTransactionSubject = PublishSubject<Unit>()
    val startTagsSubject = PublishSubject<Unit>()
    val view = mock(OverviewPresenter.View::class.java)
    val presenter = OverviewPresenter()

    @Before
    fun setUp() {
        given(view.onAddNewTransaction()).willReturn(addNewTransactionSubject)
        given(view.onStartTags()).willReturn(startTagsSubject)
    }

    @Test
    fun startsTransactionEditOnAddNewTransaction() {
        presenter.onAttachView(view)

        addNewTransaction()

        verify(view).startTransactionEdit()
    }

    @Test
    fun startsTagsOnStartTags() {
        presenter.onAttachView(view)

        startTags()

        verify(view).startTags()
    }

    private fun addNewTransaction() {
        addNewTransactionSubject.onNext(Unit)
    }

    private fun startTags() {
        startTagsSubject.onNext(Unit)
    }
}