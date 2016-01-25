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
import com.mvcoding.expensius.feature.transaction.TransactionsPresenter.PagingEdge.END
import com.mvcoding.expensius.paging.Page
import com.mvcoding.expensius.paging.PageLoader
import com.mvcoding.expensius.paging.PageResult
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.*
import org.mockito.Mockito.mock
import rx.Observable
import rx.lang.kotlin.BehaviourSubject
import rx.lang.kotlin.PublishSubject

class TransactionsPresenterTest {
    val pagingEdgeSubject = PublishSubject<TransactionsPresenter.PagingEdge>()
    val pageLoader = PageLoaderForTest()
    val view = mock(TransactionsPresenter.View::class.java)
    val presenter = TransactionsPresenter(TransactionsProviderForTest(pageLoader))

    @Before
    fun setUp() {
        given(view.onPagingEdgeReached()).willReturn(pagingEdgeSubject)
    }

    @Test
    fun showsTransactions() {
        pageLoader.size = 1

        presenter.onAttachView(view)

        verify(view).showTransactions(anyListOf(Transaction::class.java))
    }

    @Test
    fun addsNextPageWhenEndEdgeIsReached() {
        pageLoader.size = PAGE_SIZE + 1
        presenter.onAttachView(view)

        pagingEdgeEnd()

        verify(view).addTransactions(anyListOf(Transaction::class.java), eq(PAGE_SIZE))
    }

    @Test
    fun addsNextPageWhenEndEdgeIsReachedMoreThanOnce() {
        pageLoader.size = PAGE_SIZE * 2 + 1
        presenter.onAttachView(view)

        pagingEdgeEnd()
        pagingEdgeEnd()

        verify(view).addTransactions(anyListOf(Transaction::class.java), eq(PAGE_SIZE * 2))
    }

    @Test
    fun doesNotAddNextPageWhenCurrentPageIsTheLastOne() {
        pageLoader.size = PAGE_SIZE * 2 - 1
        presenter.onAttachView(view)

        pagingEdgeEnd()
        pagingEdgeEnd()

        verify(view).addTransactions(anyListOf(Transaction::class.java), anyInt())
    }

    @Test
    fun doesNotAddNextPageWhenNextPageIsEmpty() {
        pageLoader.size = PAGE_SIZE * 2
        presenter.onAttachView(view)

        pagingEdgeEnd()
        pagingEdgeEnd()

        verify(view).addTransactions(anyListOf(Transaction::class.java), anyInt())
    }

    @Test
    fun showsCachedTransactionsAfterReattach() {
        var lastShownSize = 0;
        willAnswer {
            lastShownSize = it.getArgumentAt(0, List::class.java).size
            null
        }.given(view).showTransactions(anyListOf(Transaction::class.java))
        pageLoader.size = PAGE_SIZE + 1
        presenter.onAttachView(view)
        pagingEdgeEnd()

        presenter.onDetachView(view)
        presenter.onAttachView(view)

        verify(view, times(2)).showTransactions(anyListOf(Transaction::class.java))
        assertThat(lastShownSize, equalTo(PAGE_SIZE + 1))
    }

    private fun pagingEdgeEnd() {
        pagingEdgeSubject.onNext(END)
    }

    class TransactionsProviderForTest(private val pageLoader: PageLoaderForTest) : TransactionsProvider {
        override fun transactions(pages: Observable<Page>): Observable<PageResult<Transaction>> {
            return pageLoader.load({ aTransaction() }, Any(), pages)
        }

        override fun save(transactions: Set<Transaction>) {
            throw UnsupportedOperationException()
        }
    }

    class PageLoaderForTest : PageLoader<Transaction, Any, Any, Any>() {
        private val loadSubject = BehaviourSubject(Any())
        var size = 0;

        override fun load(query: Any) = loadSubject
        override fun sizeOf(data: Any) = size;
        override fun dataItemAtPosition(data: Any, position: Int) = Any()
    }
}