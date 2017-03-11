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
import com.mvcoding.expensius.feature.ItemsView
import com.mvcoding.expensius.feature.LoadingView
import com.mvcoding.expensius.feature.ModelDisplayType
import com.mvcoding.expensius.model.Transaction
import com.mvcoding.mvp.Presenter
import rx.Observable

class TransactionsPresenter(
        private val modelDisplayType: ModelDisplayType,
        //        private val transactionsService: TransactionsService,
        private val schedulers: RxSchedulers) : Presenter<TransactionsPresenter.View>() {

    override fun onViewAttached(view: View) {
        super.onViewAttached(view)

        view.showModelDisplayType(modelDisplayType)
        view.showLoading()
//        transactionsService.items()
//                .first()
//                .subscribeOn(schedulers.io)
//                .observeOn(schedulers.main)
//                .doOnNext { view.hideLoading() }
//                .subscribeUntilDetached { view.showItems(it) }
//
//        transactionsService.addedItems()
//                .subscribeOn(schedulers.io)
//                .observeOn(schedulers.main)
//                .subscribeUntilDetached { view.showAddedItems(it.position, it.items) }
//
//        transactionsService.changedItems()
//                .subscribeOn(schedulers.io)
//                .observeOn(schedulers.main)
//                .subscribeUntilDetached { view.showChangedItems(it.position, it.items) }
//
//        transactionsService.removedItems()
//                .subscribeOn(schedulers.io)
//                .observeOn(schedulers.main)
//                .subscribeUntilDetached { view.showRemovedItems(it.position, it.items) }
//
//        transactionsService.movedItem()
//                .subscribeOn(schedulers.io)
//                .observeOn(schedulers.main)
//                .subscribeUntilDetached { view.showMovedItem(it.fromPosition, it.toPosition, it.item) }

        view.archivedTransactionsRequests().subscribeUntilDetached { view.displayArchivedTransactions() }
        view.createTransactionRequests().subscribeUntilDetached { view.displayCalculator() }
        view.transactionSelects().subscribeUntilDetached { view.displayTransactionEdit(it) }
    }

    interface View : Presenter.View, ItemsView<Transaction>, LoadingView {
        fun transactionSelects(): Observable<Transaction>
        fun createTransactionRequests(): Observable<Unit>
        fun archivedTransactionsRequests(): Observable<Unit>

        fun showModelDisplayType(modelDisplayType: ModelDisplayType)

        fun displayTransactionEdit(transaction: Transaction)
        fun displayCalculator()
        fun displayArchivedTransactions()
    }
}