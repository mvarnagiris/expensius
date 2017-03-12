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

class TransactionsPresenterTest {
//    val someTransactions = listOf(aTransaction(), aTransaction(), aTransaction())
//    val someOtherTransactions = listOf(aTransaction(), aTransaction())
//
//    val createTransactionRequestsSubject = PublishSubject<Unit>()
//    val transactionSelectsSubject = PublishSubject<Transaction>()
//    val archivedTransactionsRequestsSubject = PublishSubject<Unit>()
//    val transactionsSubject = BehaviorSubject(someTransactions)
//
//    val transactionsService: TransactionsService = mock()
//    val view: TransactionsPresenter.View = mock()
//    val inOrder: InOrder = inOrder(view)
//
//    @Before
//    fun setUp() {
//        whenever(transactionsService.items()).thenReturn(transactionsSubject)
//        whenever(transactionsService.addedItems()).thenReturn(empty())
//        whenever(transactionsService.changedItems()).thenReturn(empty())
//        whenever(transactionsService.removedItems()).thenReturn(empty())
//        whenever(transactionsService.movedItem()).thenReturn(empty())
//
//        whenever(view.transactionSelects()).thenReturn(transactionSelectsSubject)
//        whenever(view.createTransactionRequests()).thenReturn(createTransactionRequestsSubject)
//        whenever(view.archivedTransactionsRequests()).thenReturn(archivedTransactionsRequestsSubject)
//    }
//
//    @Test
//    fun showsModelDisplayType() {
//        presenter(VIEW_ARCHIVED).attach(view)
//
//        verify(view).showModelDisplayType(VIEW_ARCHIVED)
//    }
//
//    @Test
//    fun showsInitialTransactionsOnlyOnce() {
//        presenter().attach(view)
//        transactions(someOtherTransactions)
//
//        inOrder.verify(view).showLoading()
//        inOrder.verify(view).hideLoading()
//        inOrder.verify(view).showItems(someTransactions)
//        verify(view, times(1)).showItems(any())
//    }
//
//    @Test
//    fun showsAddedTransactions() {
//        val someItemsAdded = AddedItems(0, someTransactions)
//        val someOtherItemsAdded = AddedItems(someTransactions.size, someOtherTransactions)
//        whenever(transactionsService.addedItems()).thenReturn(from(listOf(someItemsAdded, someOtherItemsAdded)))
//
//        presenter().attach(view)
//
//        inOrder.verify(view).showAddedItems(someItemsAdded.position, someItemsAdded.items)
//        inOrder.verify(view).showAddedItems(someOtherItemsAdded.position, someOtherItemsAdded.items)
//    }
//
//    @Test
//    fun showsChangedTransactions() {
//        val someItemsChanged = ChangedItems(0, someTransactions)
//        val someOtherItemsChanged = ChangedItems(someTransactions.size, someOtherTransactions)
//        whenever(transactionsService.changedItems()).thenReturn(from(listOf(someItemsChanged, someOtherItemsChanged)))
//
//        presenter().attach(view)
//
//        inOrder.verify(view).showChangedItems(someItemsChanged.position, someItemsChanged.items)
//        inOrder.verify(view).showChangedItems(someOtherItemsChanged.position, someOtherItemsChanged.items)
//    }
//
//    @Test
//    fun showsRemovedTransactions() {
//        val someItemsRemoved = RemovedItems(0, someTransactions)
//        val someOtherItemsRemoved = RemovedItems(someTransactions.size, someOtherTransactions)
//        whenever(transactionsService.removedItems()).thenReturn(from(listOf(someItemsRemoved, someOtherItemsRemoved)))
//
//        presenter().attach(view)
//
//        inOrder.verify(view).showRemovedItems(someItemsRemoved.position, someItemsRemoved.items)
//        inOrder.verify(view).showRemovedItems(someOtherItemsRemoved.position, someOtherItemsRemoved.items)
//    }
//
//    @Test
//    fun showsMovedTransactions() {
//        val inOrder = inOrder(view)
//        val someTransaction = aTransaction()
//        val someOtherTransaction = aTransaction()
//        val movedTransactions = listOf(MovedItem(0, 1, someTransaction), MovedItem(1, 2, someOtherTransaction))
//        whenever(transactionsService.movedItem()).thenReturn(from(movedTransactions))
//
//        presenter().attach(view)
//
//        inOrder.verify(view).showMovedItem(0, 1, someTransaction)
//        inOrder.verify(view).showMovedItem(1, 2, someOtherTransaction)
//    }
//
//    @Test
//    fun displaysTransactionEditWhenSelectingTransaction() {
//        val transaction = aTransaction()
//        presenter().attach(view)
//
//        selectTransaction(transaction)
//
//        verify(view).displayTransactionEdit(transaction)
//    }
//
//    @Test
//    fun displaysCalculatorOnCreateTransaction() {
//        presenter().attach(view)
//
//        createTransaction()
//
//        verify(view).displayCalculator()
//    }
//
//    @Test
//    fun displaysArchivedTransactions() {
//        presenter().attach(view)
//
//        requestArchivedTransactions()
//
//        verify(view).displayArchivedTransactions()
//    }
//
//    private fun selectTransaction(transaction: Transaction) = transactionSelectsSubject.onNext(transaction)
//    private fun createTransaction() = createTransactionRequestsSubject.onNext(Unit)
//    private fun requestArchivedTransactions() = archivedTransactionsRequestsSubject.onNext(Unit)
//    private fun transactions(transactions: List<Transaction>) = transactionsSubject.onNext(transactions)
//    private fun presenter(modelDisplayType: ModelDisplayType = VIEW_NOT_ARCHIVED) = TransactionsPresenter(
//            modelDisplayType,
//            transactionsService,
//            rxSchedulers())
}