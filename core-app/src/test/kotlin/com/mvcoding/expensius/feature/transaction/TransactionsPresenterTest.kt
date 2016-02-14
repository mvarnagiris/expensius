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
import com.mvcoding.expensius.feature.transaction.TransactionsPresenter.Companion.PAGE_SIZE
import com.mvcoding.expensius.feature.transaction.TransactionsPresenter.PagingEdge.END
import com.mvcoding.expensius.model.ModelState.ARCHIVED
import com.mvcoding.expensius.model.Transaction
import com.mvcoding.expensius.paging.Page
import com.mvcoding.expensius.paging.PageLoader
import com.mvcoding.expensius.paging.PageResult
import com.nhaarman.mockito_kotlin.*
import org.junit.Before
import org.junit.Test
import org.mockito.Matchers.anyInt
import org.mockito.Matchers.anyListOf
import org.mockito.Mockito.times
import rx.Observable
import rx.Observable.just
import rx.lang.kotlin.PublishSubject

class TransactionsPresenterTest {
    val addNewTransactionSubject = PublishSubject<Unit>()
    val displayArchivedTransactionsSubject = PublishSubject<Unit>()
    val pagingEdgeSubject = PublishSubject<TransactionsPresenter.PagingEdge>()
    val pageLoader = PageLoaderForTest()
    val view = mock<TransactionsPresenter.View>()

    @Before
    fun setUp() {
        whenever(view.onAddNewTransaction()).thenReturn(addNewTransactionSubject)
        whenever(view.onDisplayArchivedTransactions()).thenReturn(displayArchivedTransactionsSubject)
        whenever(view.onPagingEdgeReached()).thenReturn(pagingEdgeSubject)
    }

    @Test
    fun showsModelDisplayType() {
        val presenter = presenterWithModelDisplayTypeArchived()

        presenter.onAttachView(view)

        verify(view).showModelDisplayType(VIEW_ARCHIVED)
    }

    @Test
    fun showsTransactions() {
        pageLoader.size = 1

        presenterWithModelDisplayTypeView().onAttachView(view)

        verify(view).showTransactions(argThat { size == 1 })
    }

    @Test
    fun showsArchivedTransactions() {
        pageLoader.size = 1
        val presenter = presenterWithModelDisplayTypeArchived()

        presenter.onAttachView(view)

        verify(view).showTransactions(argThat { size == 1 && get(0).modelState == ARCHIVED })
    }

    @Test
    fun addsNextPageWhenEndEdgeIsReached() {
        pageLoader.size = PAGE_SIZE + 1
        presenterWithModelDisplayTypeView().onAttachView(view)

        pagingEdgeEnd()

        verify(view).addTransactions(argThat { size == 1 }, eq(PAGE_SIZE))
    }

    @Test
    fun addsNextPageWhenEndEdgeIsReachedMoreThanOnce() {
        pageLoader.size = PAGE_SIZE * 2 + 1
        presenterWithModelDisplayTypeView().onAttachView(view)

        pagingEdgeEnd()
        pagingEdgeEnd()

        verify(view).addTransactions(argThat { size == 1 }, eq(PAGE_SIZE * 2))
    }

    @Test
    fun doesNotAddNextPageWhenCurrentPageIsTheLastOne() {
        pageLoader.size = PAGE_SIZE * 2 - 1
        presenterWithModelDisplayTypeView().onAttachView(view)

        pagingEdgeEnd()
        pagingEdgeEnd()

        verify(view, times(1)).addTransactions(anyListOf(Transaction::class.java), anyInt())
    }

    @Test
    fun doesNotAddNextPageWhenNextPageIsEmpty() {
        pageLoader.size = PAGE_SIZE * 2
        presenterWithModelDisplayTypeView().onAttachView(view)

        pagingEdgeEnd()
        pagingEdgeEnd()

        verify(view).addTransactions(anyListOf(Transaction::class.java), anyInt())
    }

    @Test
    fun showsCachedTransactionsAfterReattach() {
        pageLoader.size = PAGE_SIZE + 1
        val presenter = presenterWithModelDisplayTypeView()
        presenter.onAttachView(view)
        pagingEdgeEnd()

        presenter.onDetachView(view)
        presenter.onAttachView(view)

        verify(view).showTransactions(argThat { size == PAGE_SIZE + 1 })
    }

    @Test
    fun displaysTransactionEditOnAddNewTransaction() {
        presenterWithModelDisplayTypeView().onAttachView(view)

        addNewTransaction()

        verify(view).displayTransactionEdit()
    }

    @Test
    fun displaysArchivedTransactions() {
        presenterWithModelDisplayTypeView().onAttachView(view)

        displayArchivedTransactions()

        verify(view).displayArchivedTransactions()
    }

    private fun pagingEdgeEnd() {
        pagingEdgeSubject.onNext(END)
    }

    private fun addNewTransaction() {
        addNewTransactionSubject.onNext(Unit)
    }

    private fun displayArchivedTransactions() {
        displayArchivedTransactionsSubject.onNext(Unit)
    }

    private fun presenterWithModelDisplayTypeView() = TransactionsPresenter(TransactionsProviderForTest(pageLoader), VIEW_NOT_ARCHIVED)

    private fun presenterWithModelDisplayTypeArchived() = TransactionsPresenter(TransactionsProviderForTest(pageLoader), VIEW_ARCHIVED)

    class TransactionsProviderForTest(private val pageLoader: PageLoaderForTest) : TransactionsProvider {
        override fun transactions(pages: Observable<Page>): Observable<PageResult<Transaction>> {
            return pageLoader.load({ aTransaction() }, Any(), pages)
        }

        override fun archivedTransactions(pages: Observable<Page>): Observable<PageResult<Transaction>> {
            return pageLoader.load({ aTransaction().withModelState(ARCHIVED) }, Any(), pages)
        }

        override fun save(transactions: Set<Transaction>) {
            throw UnsupportedOperationException()
        }
    }

    class PageLoaderForTest : PageLoader<Transaction, Any, Any, Any>() {
        var size = 0;

        override fun load(query: Any) = just(Any())
        override fun sizeOf(data: Any) = size;
        override fun dataItemAtPosition(data: Any, position: Int) = Any()
    }
}