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

import com.mvcoding.expensius.data.DataSource
import com.mvcoding.expensius.model.Transaction
import com.mvcoding.expensius.model.extensions.aTransaction
import com.mvcoding.expensius.rxSchedulers
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Test
import rx.Observable.just

class TransactionsOverviewPresenterTest {

    val transactions = listOf(aTransaction(), aTransaction(), aTransaction())

    val transactionsOverviewSource = mock<DataSource<List<Transaction>>>()
    val view = mock<TransactionsOverviewPresenter.View>()
    val presenter = TransactionsOverviewPresenter(transactionsOverviewSource, rxSchedulers())

    @Test
    fun `displays overview transactions`() {
        whenever(transactionsOverviewSource.data()).thenReturn(just(transactions))

        presenter.attach(view)

        verify(view).showTransactions(transactions)
        verify(view).hideEmptyView()
    }

    @Test
    fun `shows empty view when transactions are empty`() {
        whenever(transactionsOverviewSource.data()).thenReturn(just(emptyList()))

        presenter.attach(view)

        verify(view).showTransactions(emptyList())
        verify(view).showEmptyView()
    }
}