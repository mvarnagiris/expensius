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

package com.mvcoding.financius.feature.overview

import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.*
import rx.subjects.PublishSubject

class OverviewPresenterTest {
    val addNewTransactionSubject = PublishSubject.create<Unit>()
    val view = mock(OverviewPresenter.View::class.java)
    val presenter = OverviewPresenter()

    @Before
    fun setUp() {
        given(view.onAddNewTransaction()).willReturn(addNewTransactionSubject)
    }

    @Test
    fun startsTransactionEditOnAddNewTransaction() {
        presenter.onAttachView(view)

        addNewTransaction()

        verify(view).startTransactionEdit()
    }

    private fun addNewTransaction() {
        addNewTransactionSubject.onNext(Unit)
    }
}