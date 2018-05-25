/*
 * Copyright (C) 2018 Mantas Varnagiris.
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
import com.mvcoding.expensius.feature.EmptyView
import com.mvcoding.expensius.feature.updateEmptyView
import com.mvcoding.expensius.model.Transaction
import com.mvcoding.mvp.Presenter

class TransactionsOverviewPresenter(
        private val transactionsOverviewSource: DataSource<List<Transaction>>,
        private val schedulers: RxSchedulers) : Presenter<TransactionsOverviewPresenter.View>() {

    override fun onViewAttached(view: View) {
        super.onViewAttached(view)
        transactionsOverviewSource.data()
                .subscribeOn(schedulers.io)
                .observeOn(schedulers.main)
                .subscribeUntilDetached {
                    view.showTransactions(it)
                    view.updateEmptyView(it)
                }
    }

    interface View : Presenter.View, EmptyView {
        fun showTransactions(transactions: List<Transaction>)
    }
}