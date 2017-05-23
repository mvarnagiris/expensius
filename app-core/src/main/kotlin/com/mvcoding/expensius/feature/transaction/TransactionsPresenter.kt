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
import com.mvcoding.expensius.feature.*
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
                .subscribeUntilDetached { realtimeData ->
                    when (realtimeData) {
                        is RealtimeData.AllItems -> view.showItems(realtimeData.allItems).also { view.updateEmptyView(realtimeData.allItems) }
                        is RealtimeData.AddedItems -> view.showAddedItems(realtimeData.addedItems, realtimeData.position)
                        is RealtimeData.ChangedItems -> view.showChangedItems(realtimeData.changedItems, realtimeData.position)
                        is RealtimeData.RemovedItems -> view.showRemovedItems(realtimeData.removedItems, realtimeData.position).also { view.updateEmptyView(realtimeData.allItems) }
                        is RealtimeData.MovedItems -> view.showMovedItems(realtimeData.movedItems, realtimeData.fromPosition, realtimeData.toPosition)
                    }
                }

        view.createTransactionRequests().subscribeUntilDetached { view.displayCalculator() }
        view.transactionSelects().subscribeUntilDetached { view.displayTransactionEdit(it) }
        view.archivedTransactionsRequests().subscribeUntilDetached { view.displayArchivedTransactions() }
    }

    interface View : Presenter.View, RealtimeItemsView<Transaction>, LoadingView, EmptyView {
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