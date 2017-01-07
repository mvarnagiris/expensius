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

package com.mvcoding.expensius.feature.transaction

import com.mvcoding.expensius.model.Transaction
import com.mvcoding.expensius.model.aTransaction
import com.mvcoding.expensius.service.TransactionsService
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Test
import rx.lang.kotlin.BehaviorSubject

class TransactionsOverviewPresenterTest {
    val transactionsService: TransactionsService = mock()
    val view: TransactionsOverviewPresenter.View = mock()
    val presenter = TransactionsOverviewPresenter(transactionsService)

    @Test
    fun displaysUpTo3Transactions() {
        val noTransactions = emptyList<Transaction>()
        val lessThan3Transactions = listOf(aTransaction())
        val exactly3Transactions = listOf(aTransaction(), aTransaction(), aTransaction())
        val moreThan3Transactions = listOf(aTransaction(), aTransaction(), aTransaction(), aTransaction())
        val transactionsSubject = BehaviorSubject(noTransactions)
        whenever(transactionsService.items()).thenReturn(transactionsSubject)

        presenter.attach(view)
        transactionsSubject.onNext(lessThan3Transactions)
        transactionsSubject.onNext(exactly3Transactions)
        transactionsSubject.onNext(moreThan3Transactions)

        verify(view).showTransactions(noTransactions)
        verify(view).showTransactions(lessThan3Transactions)
        verify(view).showTransactions(exactly3Transactions)
        verify(view).showTransactions(moreThan3Transactions.take(3))
    }
}