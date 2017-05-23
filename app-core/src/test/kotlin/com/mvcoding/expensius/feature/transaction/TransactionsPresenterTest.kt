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

package com.mvcoding.expensius.feature.transaction

import com.mvcoding.expensius.data.DataSource
import com.mvcoding.expensius.data.RealtimeData
import com.mvcoding.expensius.feature.ModelDisplayType
import com.mvcoding.expensius.feature.ModelDisplayType.VIEW_ARCHIVED
import com.mvcoding.expensius.feature.ModelDisplayType.VIEW_NOT_ARCHIVED
import com.mvcoding.expensius.model.Note
import com.mvcoding.expensius.model.Transaction
import com.mvcoding.expensius.model.extensions.aString
import com.mvcoding.expensius.model.extensions.aTransaction
import com.mvcoding.expensius.model.extensions.someTransactions
import com.mvcoding.expensius.rxSchedulers
import com.nhaarman.mockito_kotlin.*
import org.junit.Before
import org.junit.Test
import rx.lang.kotlin.BehaviorSubject
import rx.lang.kotlin.PublishSubject

class TransactionsPresenterTest {

    val transactionsSubject = BehaviorSubject<RealtimeData<Transaction>>()
    val transactionSelectsSubject = PublishSubject<Transaction>()
    val createTransactionRequestsSubject = PublishSubject<Unit>()
    val displayArchivedTransactionsSubject = PublishSubject<Unit>()

    val transactionsSource = mock<DataSource<RealtimeData<Transaction>>>()
    val view: TransactionsPresenter.View = mock()
    val inOrder = inOrder(view)

    @Before
    fun setUp() {
        whenever(view.transactionSelects()).thenReturn(transactionSelectsSubject)
        whenever(view.createTransactionRequests()).thenReturn(createTransactionRequestsSubject)
        whenever(view.archivedTransactionsRequests()).thenReturn(displayArchivedTransactionsSubject)
        whenever(transactionsSource.data()).thenReturn(transactionsSubject)
    }

    @Test
    fun `shows model display type not archived and show archived transactions request`() {
        presenter(VIEW_NOT_ARCHIVED).attach(view)

        verify(view).showModelDisplayType(VIEW_NOT_ARCHIVED)
        verify(view).showArchivedTransactionsRequest()
    }

    @Test
    fun `shows model display type archived and does not show archived transactions request`() {
        presenter(VIEW_ARCHIVED).attach(view)

        verify(view).showModelDisplayType(VIEW_ARCHIVED)
        verify(view, never()).showArchivedTransactionsRequest()
    }

    @Test
    fun `shows loading until first data comes and then displays all types of realtime data as it comes`() {
        val transactions = someTransactions()
        val addedTransactions = someTransactions()
        val changedTransactions = listOf(addedTransactions.first().copy(note = Note(aString())))
        val removedTransactions = listOf(addedTransactions.last())
        val movedTransactions = listOf(transactions.first())

        presenter().attach(view)
        inOrder.verify(view).showLoading()

        receiveTransactions(transactions)
        inOrder.verify(view).hideLoading()
        inOrder.verify(view).showItems(transactions)

        receiveTransactionsAdded(addedTransactions, 3)
        inOrder.verify(view).showAddedItems(addedTransactions, 3)

        receiveTransactionsChanged(changedTransactions, 3)
        inOrder.verify(view).showChangedItems(changedTransactions, 3)

        receiveTransactionsChanged(changedTransactions, 3)
        inOrder.verify(view).showChangedItems(changedTransactions, 3)

        receiveTransactionsRemoved(removedTransactions, 5)
        inOrder.verify(view).showRemovedItems(removedTransactions, 5)

        receiveTransactionsMoved(movedTransactions, 0, 5)
        inOrder.verify(view).showMovedItems(movedTransactions, 0, 5)
    }

    @Test
    fun `shows empty when there are no transactions`() {
        presenter().attach(view)

        receiveTransactions(emptyList())
        inOrder.verify(view).showEmptyView()

        receiveTransactions(someTransactions())
        inOrder.verify(view).hideEmptyView()

        receiveTransactionsRemoved(emptyList(), 0)
        inOrder.verify(view).showEmptyView()
    }

    @Test
    fun `displays transaction edit when selecting a transaction and display type is view`() {
        val transaction = aTransaction()
        presenter().attach(view)

        selectTransaction(transaction)

        verify(view).displayTransactionEdit(transaction)
    }

    @Test
    fun `displays calculator when create transaction is requested`() {
        presenter().attach(view)

        requestCreateTransaction()

        verify(view).displayCalculator()
    }

    @Test
    fun `displays archived transactions`() {
        presenter().attach(view)

        requestArchivedTransactions()

        verify(view).displayArchivedTransactions()
    }

    private fun receiveTransactions(transactions: List<Transaction>) = transactionsSubject.onNext(RealtimeData.AllItems(transactions))
    private fun requestCreateTransaction() = createTransactionRequestsSubject.onNext(Unit)
    private fun requestArchivedTransactions() = displayArchivedTransactionsSubject.onNext(Unit)
    private fun receiveTransactionsAdded(transactions: List<Transaction>, position: Int) = transactionsSubject.onNext(RealtimeData.AddedItems(transactions, transactions, position))
    private fun receiveTransactionsChanged(transactions: List<Transaction>, position: Int) = transactionsSubject.onNext(RealtimeData.ChangedItems(transactions, transactions, position))
    private fun receiveTransactionsRemoved(transactions: List<Transaction>, position: Int) = transactionsSubject.onNext(RealtimeData.RemovedItems(transactions, transactions, position))
    private fun receiveTransactionsMoved(transactions: List<Transaction>, fromPosition: Int, toPosition: Int) = transactionsSubject.onNext(RealtimeData.MovedItems(transactions, transactions, fromPosition, toPosition))
    private fun selectTransaction(transaction: Transaction) = transactionSelectsSubject.onNext(transaction)
    private fun presenter(modelViewType: ModelDisplayType = VIEW_NOT_ARCHIVED) = TransactionsPresenter(modelViewType, transactionsSource, rxSchedulers())
}