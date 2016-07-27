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

import android.app.Activity
import android.view.View
import com.memoizrlabs.ShankModule
import com.memoizrlabs.shankkotlin.provideSingletonFor
import com.memoizrlabs.shankkotlin.registerFactory
import com.mvcoding.expensius.feature.ModelDisplayType
import com.mvcoding.expensius.feature.currency.provideCurrenciesProvider
import com.mvcoding.expensius.model.Transaction
import com.mvcoding.expensius.provideArchivedTransactionsService
import com.mvcoding.expensius.provideRxSchedulers
import com.mvcoding.expensius.provideTransactionsService
import com.mvcoding.expensius.provideTransactionsWriteService
import memoizrlabs.com.shankandroid.withActivityScope
import memoizrlabs.com.shankandroid.withThisScope

class TransactionModule : ShankModule {
    override fun registerFactories() {
        transactionsPresenter()
        transactionPresenter()
        transactionsOverviewPresenter()
    }

    private fun transactionsPresenter() = registerFactory(TransactionsPresenter::class) { modelDisplayType: ModelDisplayType ->
        TransactionsPresenter(
                modelDisplayType,
                if (modelDisplayType == ModelDisplayType.VIEW_ARCHIVED) provideArchivedTransactionsService() else provideTransactionsService(),
                provideRxSchedulers())
    }

    private fun transactionPresenter() = registerFactory(TransactionPresenter::class) {
        transaction: Transaction ->
        TransactionPresenter(transaction, provideTransactionsWriteService(), provideCurrenciesProvider())
    }

    private fun transactionsOverviewPresenter() = registerFactory(TransactionsOverviewPresenter::class) { ->
        TransactionsOverviewPresenter(provideTransactionsService())
    }
}

fun Activity.provideTransactionsPresenter(modelDisplayType: ModelDisplayType): TransactionsPresenter = withThisScope.provideSingletonFor(modelDisplayType)
fun Activity.provideTransactionPresenter(transaction: Transaction): TransactionPresenter = withThisScope.provideSingletonFor(transaction)
fun View.provideTransactionsOverviewPresenter() = withActivityScope.provideSingletonFor<TransactionsOverviewPresenter>()
