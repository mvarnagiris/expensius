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
import com.mvcoding.expensius.model.Transaction
import com.mvcoding.expensius.paging.Page
import com.mvcoding.expensius.paging.PageResult
import rx.Observable
import rx.lang.kotlin.PublishSubject

class TransactionsPresenter(transactionsProvider: TransactionsProvider) : Presenter<TransactionsPresenter.View>() {
    internal companion object {
        const val PAGE_SIZE = 50
    }

    private val pagingEdges = PublishSubject<PagingEdge>()
    private val transactions: Observable<PageResult<Transaction>>
    private val transactionsCache = arrayListOf<Transaction>()

    private var endPage = Page(0, PAGE_SIZE)
    private var hasNextPage = true

    init {
        val pages = pagingEdges
                .filter { it == END && hasNextPage }
                .map { endPage.nextPage() }
                .doOnNext { endPage = it }
                .startWith(endPage)
        transactions = transactionsProvider.transactions(pages)
    }

    override fun onAttachView(view: View) {
        super.onAttachView(view)

        if (transactionsCache.isNotEmpty()) {
            view.showTransactions(transactionsCache)
        }

        unsubscribeOnDetach(view.onPagingEdgeReached().subscribe(pagingEdges))
        unsubscribeOnDetach(transactions.subscribe { showTransactions(view, it) })
        unsubscribeOnDetach(view.onAddNewTransaction().subscribe { view.startTransactionEdit() })
    }

    private fun showTransactions(view: View, pageResult: PageResult<Transaction>) {
        if (pageResult.isInvalidated) {
            transactionsCache.clear();
            view.showTransactions(pageResult.items)
        } else if (pageResult.items.isNotEmpty()) {
            view.addTransactions(pageResult.items, transactionsCache.size)
        }

        transactionsCache.addAll(pageResult.items)
        hasNextPage = pageResult.hasNextPage()
    }

    enum class PagingEdge {
        START, END
    }

    interface View : Presenter.View {
        fun onPagingEdgeReached(): Observable<PagingEdge>
        fun onAddNewTransaction(): Observable<Unit>
        fun showTransactions(transactions: List<Transaction>)
        fun addTransactions(transactions: List<Transaction>, position: Int)
        fun startTransactionEdit()
    }
}