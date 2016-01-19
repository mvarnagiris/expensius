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

import com.mvcoding.expensius.feature.transaction.TransactionsPresenter.Companion.PAGE_SIZE
import com.mvcoding.expensius.feature.transaction.TransactionsPresenter.PagingEdge.START
import com.mvcoding.expensius.paging.Page
import com.mvcoding.expensius.paging.pageResult
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.verify
import org.mockito.Mockito.mock
import rx.Observable.just
import rx.lang.kotlin.BehaviourSubject

class TransactionsPresenterTest {
    val defaultFirstPage = Page(0, PAGE_SIZE)
    val defaultFirstPageResult = pageResult(defaultFirstPage, PAGE_SIZE, { aTransaction() })
    val pagingEdgeSubject = BehaviourSubject(START)
    val transactionsProvider = mock(TransactionsProvider::class.java)
    val view = mock(TransactionsPresenter.View::class.java)
    val presenter = TransactionsPresenter(transactionsProvider)

    @Before
    fun setUp() {
        given(view.onPagingEdgeReached()).willReturn(pagingEdgeSubject)
        given(transactionsProvider.transactions(presenter.pageObservable)).willReturn(just(defaultFirstPageResult))
    }

    @Test
    fun initiallyLoadsFirstPage() {
        presenter.onAttachView(view)

        verify(view).showTransactions(defaultFirstPageResult.items)
    }

    @Test
    fun loadsNextPageWhenEdgeIsReached() {
        
    }
}