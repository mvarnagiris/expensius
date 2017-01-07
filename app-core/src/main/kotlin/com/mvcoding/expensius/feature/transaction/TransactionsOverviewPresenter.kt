/*
 * Copyright (C) 2016 Mantas Varnagiris.
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

import com.mvcoding.expensius.model.Transaction
import com.mvcoding.expensius.service.TransactionsService
import com.mvcoding.mvp.Presenter

class TransactionsOverviewPresenter(private val transactionsService: TransactionsService) : Presenter<TransactionsOverviewPresenter.View>() {

    override fun onViewAttached(view: View) {
        super.onViewAttached(view)

        transactionsService.items().map { it.take(3) }.subscribeUntilDetached { view.showTransactions(it) }
    }

    interface View : Presenter.View {
        fun showTransactions(transactions: List<Transaction>)
    }
}