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
import com.mvcoding.expensius.paging.Page
import rx.Observable
import rx.Observable.just

class TransactionsPresenter(
        private val transactionsProvider: TransactionsProvider) : Presenter<TransactionsPresenter.View>() {

    internal companion object {
        const val PAGE_SIZE = 50
    }

    internal val pageObservable = just(Page(0, PAGE_SIZE))

    override fun onAttachView(view: View) {
        super.onAttachView(view)

        unsubscribeOnDetach(transactionsProvider.transactions(pageObservable).subscribe { view.showTransactions(it.items) })
    }

    enum class PagingEdge {
        START, END
    }

    interface View : Presenter.View {
        fun onPagingEdgeReached(): Observable<PagingEdge>
        fun showTransactions(transactions: List<Transaction>)
    }
}