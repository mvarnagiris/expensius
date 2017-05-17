/*
 * Copyright (C) 2017 Mantas Varnagiris.
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
import com.mvcoding.expensius.data.DataSource
import com.mvcoding.expensius.data.RealtimeData
import com.mvcoding.expensius.feature.LoadingView
import com.mvcoding.expensius.feature.ModelDisplayType
import com.mvcoding.expensius.feature.RealtimeItemsView
import com.mvcoding.expensius.model.Transaction
import com.mvcoding.mvp.Presenter
import rx.Observable

class TransactionsPresenter(
        private val modelDisplayType: ModelDisplayType,
        private val transactionsSource: DataSource<RealtimeData<Transaction>>,
        private val schedulers: RxSchedulers) : Presenter<TransactionsPresenter.View>() {

    override fun onViewAttached(view: View) {
        super.onViewAttached(view)

        view.showModelDisplayType(modelDisplayType)
        view.showLoading()
        if (modelDisplayType == ModelDisplayType.VIEW_NOT_ARCHIVED) view.showArchivedTransactionsRequest()

        transactionsSource.data()
                .subscribeOn(schedulers.io)
                .observeOn(schedulers.main)
                .doOnNext { view.hideLoading() }
                .subscribeUntilDetached {
                    when (it) {
                        is RealtimeData.AllItems -> view.showItems(it.allItems)
                        is RealtimeData.AddedItems -> view.showAddedItems(it.addedItems, it.position)
                        is RealtimeData.ChangedItems -> view.showChangedItems(it.changedItems, it.position)
                        is RealtimeData.RemovedItems -> view.showRemovedItems(it.removedItems, it.position)
                        is RealtimeData.MovedItems -> view.showMovedItems(it.movedItems, it.fromPosition, it.toPosition)
                    }
                }

        view.createTransactionRequests().subscribeUntilDetached { view.displayCalculator() }
        view.transactionSelects().subscribeUntilDetached { view.displayTransactionEdit(it) }
        view.archivedTransactionsRequests().subscribeUntilDetached { view.displayArchivedTransactions() }
    }

    interface View : Presenter.View, RealtimeItemsView<Transaction>, LoadingView {
        fun transactionSelects(): Observable<Transaction>
        fun createTransactionRequests(): Observable<Unit>
        fun archivedTransactionsRequests(): Observable<Unit>

        fun showModelDisplayType(modelDisplayType: ModelDisplayType)
        fun showArchivedTransactionsRequest()

        fun displayTransactionEdit(transaction: Transaction)
        fun displayCalculator()
        fun displayArchivedTransactions()
    }
}