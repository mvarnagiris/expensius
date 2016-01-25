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

import com.mvcoding.expensius.feature.Presenter
import com.mvcoding.expensius.feature.transaction.TransactionsPresenter.PagingEdge.END
import com.mvcoding.expensius.paging.Page
import rx.Observable
import rx.Observable.just
import rx.Observable.merge
import rx.lang.kotlin.PublishSubject

class TransactionsPresenter(transactionsProvider: TransactionsProvider) : Presenter<TransactionsPresenter.View>() {
    internal companion object {
        const val PAGE_SIZE = 50
    }

    private val pagingEdges = PublishSubject<PagingEdge>()
    private val transactions: Observable<List<Transaction>>
    private val endTransaction: Observable<List<Transaction>>
    private val transactionsCache = arrayListOf<Transaction>()

    private var endPage = Page(0, PAGE_SIZE)
    private var hasNextPage = true

    init {
        val endPages = pagingEdges.filter { it == END }.filter { hasNextPage }.map { endPage.nextPage() }.doOnNext { endPage = it }
        val pages = merge(just(endPage), endPages)

        val allTransactionsUpdates = transactionsProvider.transactions(pages).doOnNext {
            if (it.isInvalidated) {
                transactionsCache.clear();
            }
            transactionsCache.addAll(it.items)
        }
        transactions = allTransactionsUpdates.filter { it.isInvalidated }.map { it.items }
        endTransaction = allTransactionsUpdates
                .filter { it.isInvalidated.not() }
                .doOnNext {
                    hasNextPage = it.hasNextPage()
                }
                .map { it.items }
    }

    override fun onAttachView(view: View) {
        super.onAttachView(view)

        if (transactionsCache.size > 0) {
            view.showTransactions(transactionsCache)
        }

        unsubscribeOnDetach(view.onPagingEdgeReached().subscribe(pagingEdges))
        unsubscribeOnDetach(transactions.subscribe { view.showTransactions(it) })
        unsubscribeOnDetach(endTransaction.subscribe { view.addTransactions(it, transactionsCache.size - it.size) })
    }

    enum class PagingEdge {
        START, END
    }

    interface View : Presenter.View {
        fun onPagingEdgeReached(): Observable<PagingEdge>
        fun showTransactions(transactions: List<Transaction>)
        fun addTransactions(transactions: List<Transaction>, position: Int)
    }
}