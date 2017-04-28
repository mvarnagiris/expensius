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

import android.view.View
import com.memoizrlabs.Scope
import com.memoizrlabs.ShankModule
import com.memoizrlabs.shankkotlin.provideGlobalSingleton
import com.memoizrlabs.shankkotlin.provideNew
import com.memoizrlabs.shankkotlin.provideSingletonFor
import com.memoizrlabs.shankkotlin.registerFactory
import com.mvcoding.expensius.feature.BaseActivity
import com.mvcoding.expensius.feature.ModelDisplayType
import com.mvcoding.expensius.feature.ModelDisplayType.VIEW_ARCHIVED
import com.mvcoding.expensius.feature.ModelDisplayType.VIEW_NOT_ARCHIVED
import com.mvcoding.expensius.feature.currency.provideCurrenciesSource
import com.mvcoding.expensius.feature.filter.provideFilterSource
import com.mvcoding.expensius.feature.tag.provideAllTagsSource
import com.mvcoding.expensius.model.Transaction
import com.mvcoding.expensius.provideAppUserSource
import com.mvcoding.expensius.provideFirebaseTransactionsService
import com.mvcoding.expensius.provideRxSchedulers
import memoizrlabs.com.shankandroid.withActivityScope
import memoizrlabs.com.shankandroid.withThisScope

class TransactionModule : ShankModule {
    override fun registerFactories() {
        transactionsSource()
        createTransactionsWriter()
        transactionsWriter()
        transactionsOverviewSource()
        transactionsPresenter()
        transactionPresenter()
        transactionsOverviewPresenter()
    }

    private fun transactionsSource() = registerFactory(TransactionsSource::class) { modelDisplayType: ModelDisplayType, scope: Scope? ->
        TransactionsSource(provideAllTagsSource(), provideFilterSource(scope)) {
            if (modelDisplayType == VIEW_ARCHIVED) provideFirebaseTransactionsService().getArchivedTransactions(it)
            else provideFirebaseTransactionsService().getTransactions(it)
        }
    }

    private fun createTransactionsWriter() = registerFactory(CreateTransactionsWriter::class) { ->
        CreateTransactionsWriter(provideAppUserSource()) { userId, createTransactions ->
            provideFirebaseTransactionsService().createTransactions(userId, createTransactions)
        }
    }

    private fun transactionsWriter() = registerFactory(TransactionsWriter::class) { ->
        TransactionsWriter(provideAppUserSource()) { userId, transactions ->
            provideFirebaseTransactionsService().updateTransactions(userId, transactions)
        }
    }

    private fun transactionsOverviewSource() = registerFactory(TransactionsOverviewSource::class) { ->
        TransactionsOverviewSource(provideTransactionsSource(VIEW_NOT_ARCHIVED, null))
    }

    private fun transactionsPresenter() = registerFactory(TransactionsPresenter::class) { modelDisplayType: ModelDisplayType, scope: Scope ->
        TransactionsPresenter(
                modelDisplayType,
                provideTransactionsSource(modelDisplayType, scope),
                provideRxSchedulers())
    }

    private fun transactionPresenter() = registerFactory(TransactionPresenter::class) {
        transaction: Transaction ->
        TransactionPresenter(transaction, provideTransactionsWriter(), provideCreateTransactionsWriter(), provideCurrenciesSource(), provideRxSchedulers())
    }

    private fun transactionsOverviewPresenter() = registerFactory(TransactionsOverviewPresenter::class) { ->
        TransactionsOverviewPresenter(provideTransactionsOverviewSource(), provideRxSchedulers())
    }
}

fun provideTransactionsSource(modelDisplayType: ModelDisplayType, scope: Scope?) = provideNew<TransactionsSource>(modelDisplayType, scope)
fun provideCreateTransactionsWriter() = provideNew<CreateTransactionsWriter>()
fun provideTransactionsWriter() = provideNew<TransactionsWriter>()
fun provideTransactionsOverviewSource() = provideGlobalSingleton<TransactionsOverviewSource>()
fun BaseActivity.provideTransactionsPresenter(modelDisplayType: ModelDisplayType) = withThisScope.provideSingletonFor<TransactionsPresenter>(modelDisplayType, scope)
fun BaseActivity.provideTransactionPresenter(transaction: Transaction): TransactionPresenter = withThisScope.provideSingletonFor(transaction)
fun View.provideTransactionsOverviewPresenter() = withActivityScope.provideSingletonFor<TransactionsOverviewPresenter>()