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
import com.mvcoding.expensius.paging.PageResult
import com.mvcoding.expensius.paging.pageResult
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.verify
import org.mockito.Mockito.mock
import rx.Observable
import rx.lang.kotlin.BehaviourSubject

class TransactionsPresenterTest {
    val pagingEdgeSubject = BehaviourSubject(START)


    val defaultFirstPage = Page(0, PAGE_SIZE)
    val defaultFirstPageResult = pageResult(defaultFirstPage, PAGE_SIZE, { aTransaction() })
    val transactionsProvider = TransactionsProviderForTest()
    val view = mock(TransactionsPresenter.View::class.java)
    val presenter = TransactionsPresenter(transactionsProvider)

    @Before
    fun setUp() {
        given(view.onPagingEdgeReached()).willReturn(pagingEdgeSubject)
    }

    @Test
    fun initiallyLoadsFirstPage() {
        transactionsProvider.preparePagesCount(1)
        presenter.onAttachView(view)

        verify(view).showTransactions(transactionsProvider.getPageResultsAtPage(0))
    }

    //    @Test
    //    fun loadsNextPageWhenEndEdgeIsReached() {
    //
    //    }

    class TransactionsProviderForTest : TransactionsProvider {
        private val pageResultsSubject = BehaviourSubject<PageResult<Transaction>>()
        private val pageResults = hashMapOf<Page, PageResult<Transaction>>()

        override fun transactions(pages: Observable<Page>): Observable<PageResult<Transaction>> {
            pages.subscribe {
                if (pageResults.containsKey(it)) {
                    pageResultsSubject.onNext(pageResults.get(it))
                }
            }
            return pageResultsSubject
        }

        override fun save(transactions: Set<Transaction>) {
            throw UnsupportedOperationException()
        }

        fun preparePagesCount(pagesCount: Int) {
            var page = Page(0, PAGE_SIZE).previousPage();
            pagesCount.downTo(1).forEach {
                page = page.nextPage()
                pageResults.put(page, pageResult(page, PAGE_SIZE, { aTransaction() }))
            }
        }

        fun getPageResultsAtPage(pagePosition: Int) = pageResults.values.elementAt(pagePosition).items
    }
}