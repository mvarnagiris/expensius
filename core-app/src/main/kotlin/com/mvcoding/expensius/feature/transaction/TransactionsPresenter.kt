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
import com.mvcoding.expensius.model.ModelState.ARCHIVED
import com.mvcoding.expensius.model.ModelState.NONE
import com.mvcoding.expensius.model.Transaction
import com.mvcoding.mvp.Presenter
import rx.Observable

class TransactionsPresenter(
        private val transactionsProvider: TransactionsProvider,
        private val modelDisplayType: ModelDisplayType,
        private val schedulers: RxSchedulers) : Presenter<TransactionsPresenter.View>() {

    override fun onViewAttached(view: View) {
        super.onViewAttached(view)

        view.showModelDisplayType(modelDisplayType)

        unsubscribeOnDetach(transactionsProvider.transactions(modelDisplayType.toTransactionsFilter())
                .subscribeOn(schedulers.io)
                .observeOn(schedulers.main)
                .subscribe { view.showTransactions(it) })
        unsubscribeOnDetach(view.onCreateTransaction().subscribe { view.displayCreateTransaction() })
        unsubscribeOnDetach(view.onDisplayArchivedTransactions().subscribe { view.displayArchivedTransactions() })
        unsubscribeOnDetach(view.onTransactionSelected().subscribe { view.displayTransactionEdit(it) })
    }

    private fun ModelDisplayType.toTransactionsFilter() = when (this) {
        VIEW_NOT_ARCHIVED -> TransactionsFilter(NONE)
        VIEW_ARCHIVED -> TransactionsFilter(ARCHIVED)
    }

    enum class PagingEdge {
        START, END
    }

    interface View : Presenter.View {
        fun onTransactionSelected(): Observable<Transaction>
        fun onCreateTransaction(): Observable<Unit>
        fun onDisplayArchivedTransactions(): Observable<Unit>

        fun showModelDisplayType(modelDisplayType: ModelDisplayType)
        fun showTransactions(transactions: List<Transaction>)

        fun displayCreateTransaction()
        fun displayTransactionEdit(transaction: Transaction)
        fun displayArchivedTransactions()
    }
}