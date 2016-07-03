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

package com.mvcoding.expensius.feature.transaction

import com.mvcoding.expensius.feature.ModelDisplayType.VIEW_ARCHIVED
import com.mvcoding.expensius.feature.ModelDisplayType.VIEW_NOT_ARCHIVED
import com.mvcoding.expensius.model.ModelState.ARCHIVED
import com.mvcoding.expensius.model.ModelState.NONE
import com.mvcoding.expensius.model.Transaction
import com.mvcoding.expensius.rxSchedulers
import com.nhaarman.mockito_kotlin.argThat
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Test
import rx.Observable.just
import rx.lang.kotlin.PublishSubject

class TransactionsPresenterTest {
    val createTransactionSubject = PublishSubject<Unit>()
    val selectTransactionSubject = PublishSubject<Transaction>()
    val displayArchivedTransactionsSubject = PublishSubject<Unit>()

    val transactionsProvider = mock<TransactionsProvider>()
    val view = mock<TransactionsPresenter.View>()

    @Before
    fun setUp() {
        whenever(transactionsProvider.transactions(argThat { modelState == NONE })).thenReturn(just(listOf(aTransaction())))
        whenever(transactionsProvider.transactions(argThat { modelState == ARCHIVED }))
                .thenReturn(just(listOf(aTransaction().withModelState(ARCHIVED))))
        whenever(view.onCreateTransaction()).thenReturn(createTransactionSubject)
        whenever(view.onTransactionSelected()).thenReturn(selectTransactionSubject)
        whenever(view.onDisplayArchivedTransactions()).thenReturn(displayArchivedTransactionsSubject)
    }

    @Test
    fun showsModelDisplayType() {
        presenterWithModelDisplayTypeArchived().attach(view)

        verify(view).showModelDisplayType(VIEW_ARCHIVED)
    }

    @Test
    fun showsTransactions() {
        presenterWithModelDisplayTypeView().attach(view)

        verify(view).showTransactions(argThat { size == 1 })
    }

    @Test
    fun showsArchivedTransactions() {
        presenterWithModelDisplayTypeArchived().attach(view)

        verify(view).showTransactions(argThat { size == 1 && first().modelState == ARCHIVED })
    }

    @Test
    fun displaysCreateTransactionOnCreateTransaction() {
        presenterWithModelDisplayTypeView().attach(view)

        createTransaction()

        verify(view).displayCreateTransaction()
    }

    @Test
    fun displaysTransactionEditOnTransactionSelected() {
        val transaction = aTransaction()
        presenterWithModelDisplayTypeView().attach(view)

        selectTransaction(transaction)

        verify(view).displayTransactionEdit(transaction)
    }

    @Test
    fun displaysArchivedTransactions() {
        presenterWithModelDisplayTypeView().attach(view)

        displayArchivedTransactions()

        verify(view).displayArchivedTransactions()
    }

    private fun selectTransaction(transaction: Transaction) = selectTransactionSubject.onNext(transaction)
    private fun createTransaction() = createTransactionSubject.onNext(Unit)
    private fun displayArchivedTransactions() = displayArchivedTransactionsSubject.onNext(Unit)
    private fun presenterWithModelDisplayTypeView() = TransactionsPresenter(transactionsProvider, VIEW_NOT_ARCHIVED, rxSchedulers())
    private fun presenterWithModelDisplayTypeArchived() = TransactionsPresenter(transactionsProvider, VIEW_ARCHIVED, rxSchedulers())
}