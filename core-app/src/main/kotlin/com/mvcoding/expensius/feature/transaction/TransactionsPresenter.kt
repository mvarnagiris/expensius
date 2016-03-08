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

import com.mvcoding.expensius.RxSchedulers
import com.mvcoding.expensius.feature.ModelDisplayType
import com.mvcoding.expensius.feature.ModelDisplayType.VIEW_ARCHIVED
import com.mvcoding.expensius.feature.ModelDisplayType.VIEW_NOT_ARCHIVED
import com.mvcoding.expensius.feature.Presenter
import com.mvcoding.expensius.feature.transaction.TransactionsPresenter.PagingEdge.END
import com.mvcoding.expensius.model.ModelState.ARCHIVED
import com.mvcoding.expensius.model.ModelState.NONE
import com.mvcoding.expensius.model.Transaction
import com.mvcoding.expensius.paging.Page
import com.mvcoding.expensius.paging.PageResult
import rx.Observable
import rx.Observable.just
import rx.lang.kotlin.PublishSubject

class TransactionsPresenter(
        private val transactionsProvider: TransactionsProvider,
        private val modelDisplayType: ModelDisplayType,
        private val schedulers: RxSchedulers) : Presenter<TransactionsPresenter.View>() {
    internal companion object {
        const val PAGE_SIZE = 5
    }

    private val transactionsCache = arrayListOf<Transaction>()
    private val pagingEdgeSubject = PublishSubject<PagingEdge>()
    private val transactions: Observable<PageResult<Transaction>>

    private var endPage = Page(0, PAGE_SIZE)
    private var hasNextPage = true

    init {
        val pages = pagingEdgeSubject
                .filter { it == END && hasNextPage }
                .map { endPage.nextPage() }
                .doOnNext { endPage = it }
                .startWith(just(endPage).filter { transactionsCache.isEmpty() })

        transactions = transactionsProvider.transactions(pages, modelDisplayType.toTransactionsFilter()).cache()
    }

    override fun onViewAttached(view: View) {
        super.onViewAttached(view)

        view.showModelDisplayType(modelDisplayType)

        if (transactionsCache.isNotEmpty()) {
            view.showTransactions(transactionsCache)
        }

        unsubscribeOnDetach(view.onPagingEdgeReached().subscribe(pagingEdgeSubject))
        unsubscribeOnDetach(transactions.subscribeOn(schedulers.io).observeOn(schedulers.main).subscribe { showTransactions(view, it) })
        unsubscribeOnDetach(view.onCreateTransaction().subscribe { view.displayCreateTransaction() })
        unsubscribeOnDetach(view.onDisplayArchivedTransactions().subscribe { view.displayArchivedTransactions() })
        unsubscribeOnDetach(view.onTransactionSelected().subscribe { view.displayTransactionEdit(it) })
    }

    private fun showTransactions(view: View, pageResult: PageResult<Transaction>) {
        if (pageResult.isInvalidated) {
            transactionsCache.clear()
            view.showTransactions(pageResult.items)
        } else if (pageResult.items.isNotEmpty()) {
            view.addTransactions(pageResult.items, transactionsCache.size)
        }

        transactionsCache.addAll(pageResult.items)
        hasNextPage = pageResult.hasNextPage()
    }

    private fun ModelDisplayType.toTransactionsFilter() = when (this) {
        VIEW_NOT_ARCHIVED -> TransactionsFilter(NONE)
        VIEW_ARCHIVED -> TransactionsFilter(ARCHIVED)
    }

    enum class PagingEdge {
        START, END
    }

    interface View : Presenter.View {
        fun onPagingEdgeReached(): Observable<PagingEdge>
        fun onTransactionSelected(): Observable<Transaction>
        fun onCreateTransaction(): Observable<Unit>
        fun onDisplayArchivedTransactions(): Observable<Unit>

        fun showModelDisplayType(modelDisplayType: ModelDisplayType)
        fun showTransactions(transactions: List<Transaction>)
        fun addTransactions(transactions: List<Transaction>, position: Int)

        fun displayCreateTransaction()
        fun displayTransactionEdit(transaction: Transaction)
        fun displayArchivedTransactions()
    }
}