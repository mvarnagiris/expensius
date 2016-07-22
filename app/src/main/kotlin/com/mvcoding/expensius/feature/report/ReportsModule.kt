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

package com.mvcoding.expensius.feature.report

import android.view.View
import com.memoizrlabs.ShankModule
import com.memoizrlabs.shankkotlin.provideSingletonFor
import com.memoizrlabs.shankkotlin.registerFactory
import com.mvcoding.expensius.feature.currency.provideExchangeRatesProvider
import com.mvcoding.expensius.model.TransactionState.CONFIRMED
import com.mvcoding.expensius.model.TransactionType.EXPENSE
import com.mvcoding.expensius.provideAppUserService
import com.mvcoding.expensius.provideFilter
import com.mvcoding.expensius.provideRxSchedulers
import com.mvcoding.expensius.provideTransactionsService
import memoizrlabs.com.shankandroid.withActivityScope

class ReportsModule : ShankModule {
    override fun registerFactories() {
        trendsPresenter()
        tagTotalsReportPresenter()
    }

    private fun trendsPresenter() = registerFactory(TrendReportPresenter::class) { ->
        TrendReportPresenter(
                provideAppUserService(),
                provideTransactionsService(),
                provideExchangeRatesProvider(),
                provideFilter().setTransactionType(EXPENSE).setTransactionState(CONFIRMED),
                provideRxSchedulers())
    }

    private fun tagTotalsReportPresenter() = registerFactory(TagsTotalsReportPresenter::class) { ->
        TagsTotalsReportPresenter(
                provideAppUserService(),
                provideTransactionsService(),
                provideExchangeRatesProvider(),
                provideFilter().setTransactionState(CONFIRMED),
                provideRxSchedulers())
    }
}

fun View.provideExpenseTrendReportPresenter() = withActivityScope.provideSingletonFor<TrendReportPresenter>()
fun View.provideTagTotalsReportPresenter() = withActivityScope.provideSingletonFor<TagsTotalsReportPresenter>()