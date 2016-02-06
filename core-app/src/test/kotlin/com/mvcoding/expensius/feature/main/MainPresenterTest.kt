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

package com.mvcoding.expensius.feature.main

import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito
import org.mockito.BDDMockito.mock
import org.mockito.BDDMockito.verify
import rx.lang.kotlin.PublishSubject

class MainPresenterTest {
    val addNewTransactionSubject = PublishSubject<Unit>()
    val view = mock(MainPresenter.View::class.java)
    val presenter = MainPresenter()

    @Before
    fun setUp() {
        BDDMockito.given(view.onAddNewTransaction()).willReturn(addNewTransactionSubject)
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