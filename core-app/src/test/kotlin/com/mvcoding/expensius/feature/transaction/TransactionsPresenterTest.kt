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

import com.mvcoding.expensius.feature.ModelDisplayType
import com.mvcoding.expensius.feature.ModelDisplayType.VIEW_ARCHIVED
import com.mvcoding.expensius.feature.ModelDisplayType.VIEW_NOT_ARCHIVED
import com.mvcoding.expensius.model.NullModels.newTransaction
import com.mvcoding.expensius.model.Transaction
import com.mvcoding.expensius.model.aFixedTimestampProvider
import com.mvcoding.expensius.model.aTransaction
import com.mvcoding.expensius.model.anAppUser
import com.mvcoding.expensius.rxSchedulers
import com.mvcoding.expensius.service.AddedItems
import com.mvcoding.expensius.service.AppUserService
import com.mvcoding.expensius.service.ChangedItems
import com.mvcoding.expensius.service.MovedItem
import com.mvcoding.expensius.service.RemovedItems
import com.mvcoding.expensius.service.TransactionsService
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.inOrder
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Test
import org.mockito.InOrder
import rx.Observable.empty
import rx.Observable.from
import rx.Observable.just
import rx.lang.kotlin.BehaviorSubject
import rx.lang.kotlin.PublishSubject

class TransactionsPresenterTest {
    val someTransactions = listOf(aTransaction(), aTransaction(), aTransaction())
    val someOtherTransactions = listOf(aTransaction(), aTransaction())
    val appUser = anAppUser()

    val createTransactionRequestsSubject = PublishSubject<Unit>()
    val transactionSelectsSubject = PublishSubject<Transaction>()
    val archivedTransactionsRequestsSubject = PublishSubject<Unit>()
    val transactionsSubject = BehaviorSubject(someTransactions)

    val appUserService: AppUserService = mock()
    val transactionsService: TransactionsService = mock()
    val timestampProvider = aFixedTimestampProvider()
    val view: TransactionsPresenter.View = mock()
    val inOrder: InOrder = inOrder(view)

    @Before
    fun setUp() {
        whenever(transactionsService.items()).thenReturn(transactionsSubject)
        whenever(transactionsService.addedItems()).thenReturn(empty())
        whenever(transactionsService.changedItems()).thenReturn(empty())
        whenever(transactionsService.removedItems()).thenReturn(empty())
        whenever(transactionsService.movedItem()).thenReturn(empty())
        whenever(appUserService.appUser()).thenReturn(just(appUser))

        whenever(view.transactionSelects()).thenReturn(transactionSelectsSubject)
        whenever(view.createTransactionRequests()).thenReturn(createTransactionRequestsSubject)
        whenever(view.archivedTransactionsRequests()).thenReturn(archivedTransactionsRequestsSubject)
    }

    @Test
    fun showsModelDisplayType() {
        presenter(VIEW_ARCHIVED).attach(view)

        verify(view).showModelDisplayType(VIEW_ARCHIVED)
    }

    @Test
    fun showsInitialTransactionsOnlyOnce() {
        presenter().attach(view)
        transactions(someOtherTransactions)

        inOrder.verify(view).showLoading()
        inOrder.verify(view).hideLoading()
        inOrder.verify(view).showItems(someTransactions)
        verify(view, times(1)).showItems(any())
    }

    @Test
    fun showsAddedTransactions() {
        val someItemsAdded = AddedItems(0, someTransactions)
        val someOtherItemsAdded = AddedItems(someTransactions.size, someOtherTransactions)
        whenever(transactionsService.addedItems()).thenReturn(from(listOf(someItemsAdded, someOtherItemsAdded)))

        presenter().attach(view)

        inOrder.verify(view).showAddedItems(someItemsAdded.position, someItemsAdded.items)
        inOrder.verify(view).showAddedItems(someOtherItemsAdded.position, someOtherItemsAdded.items)
    }

    @Test
    fun showsChangedTransactions() {
        val someItemsChanged = ChangedItems(0, someTransactions)
        val someOtherItemsChanged = ChangedItems(someTransactions.size, someOtherTransactions)
        whenever(transactionsService.changedItems()).thenReturn(from(listOf(someItemsChanged, someOtherItemsChanged)))

        presenter().attach(view)

        inOrder.verify(view).showChangedItems(someItemsChanged.position, someItemsChanged.items)
        inOrder.verify(view).showChangedItems(someOtherItemsChanged.position, someOtherItemsChanged.items)
    }

    @Test
    fun showsRemovedTransactions() {
        val someItemsRemoved = RemovedItems(0, someTransactions)
        val someOtherItemsRemoved = RemovedItems(someTransactions.size, someOtherTransactions)
        whenever(transactionsService.removedItems()).thenReturn(from(listOf(someItemsRemoved, someOtherItemsRemoved)))

        presenter().attach(view)

        inOrder.verify(view).showRemovedItems(someItemsRemoved.position, someItemsRemoved.items)
        inOrder.verify(view).showRemovedItems(someOtherItemsRemoved.position, someOtherItemsRemoved.items)
    }

    @Test
    fun showsMovedTransactions() {
        val inOrder = inOrder(view)
        val someTransaction = aTransaction()
        val someOtherTransaction = aTransaction()
        val movedTransactions = listOf(MovedItem(0, 1, someTransaction), MovedItem(1, 2, someOtherTransaction))
        whenever(transactionsService.movedItem()).thenReturn(from(movedTransactions))

        presenter().attach(view)

        inOrder.verify(view).showMovedItem(0, 1, someTransaction)
        inOrder.verify(view).showMovedItem(1, 2, someOtherTransaction)
    }

    @Test
    fun displaysTransactionEditWhenSelectingTransaction() {
        val transaction = aTransaction()
        presenter().attach(view)

        selectTransaction(transaction)

        verify(view).displayTransactionEdit(transaction)
    }

    @Test
    fun displaysTransactionEditOnCreateTransaction() {
        presenter().attach(view)

        createTransaction()

        verify(view).displayTransactionEdit(newTransaction(appUser, timestampProvider))
    }

    @Test
    fun displaysArchivedTransactions() {
        presenter().attach(view)

        requestArchivedTransactions()

        verify(view).displayArchivedTransactions()
    }

    private fun selectTransaction(transaction: Transaction) = transactionSelectsSubject.onNext(transaction)
    private fun createTransaction() = createTransactionRequestsSubject.onNext(Unit)
    private fun requestArchivedTransactions() = archivedTransactionsRequestsSubject.onNext(Unit)
    private fun transactions(transactions: List<Transaction>) = transactionsSubject.onNext(transactions)
    private fun presenter(modelDisplayType: ModelDisplayType = VIEW_NOT_ARCHIVED) = TransactionsPresenter(
            modelDisplayType,
            appUserService,
            transactionsService,
            timestampProvider,
            rxSchedulers())
}