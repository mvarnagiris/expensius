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
import com.mvcoding.expensius.feature.Filter
import com.mvcoding.expensius.feature.transaction.provideTransactionsProvider
import com.mvcoding.expensius.model.TransactionState.CONFIRMED
import com.mvcoding.expensius.model.TransactionType
import com.mvcoding.expensius.model.TransactionType.EXPENSE
import com.mvcoding.expensius.provideAppUserService
import com.mvcoding.expensius.provideRxSchedulers
import com.mvcoding.expensius.provideSettings
import com.mvcoding.expensius.provideTimestampProvider
import com.mvcoding.expensius.provideTransactionsService
import memoizrlabs.com.shankandroid.withActivityScope
import org.joda.time.DateTime
import org.joda.time.Interval

class ReportsModule : ShankModule {
    override fun registerFactories() {
        tagsReportPresenter()
        trendsPresenter()
        tagTotalsReportPresenter()
    }

    private fun tagsReportPresenter() = registerFactory(TagsReportPresenter::class, { transactionType: TransactionType ->
        // TODO: This is a temporary filter until global filter comes.
        val startOfTomorrow = DateTime.now().plusDays(1).withTimeAtStartOfDay()
        val last30Days = Interval(startOfTomorrow.minusDays(30), startOfTomorrow)
        val filter = Filter()
        filter.setInterval(last30Days)
        filter.setTransactionType(transactionType)
        TagsReportPresenter(filter, provideTransactionsProvider(), provideSettings(), provideRxSchedulers())
    })

    private fun trendsPresenter() = registerFactory(TrendPresenter::class) { ->
        TrendPresenter(
                provideAppUserService(),
                provideTransactionsService(),
                Filter().setTransactionType(EXPENSE).setTransactionState(CONFIRMED),
                provideTimestampProvider(),
                provideRxSchedulers())
    }

    private fun tagTotalsReportPresenter() = registerFactory(TagsTotalsReportPresenter::class) { ->
        TagsTotalsReportPresenter(
                provideAppUserService(),
                provideTransactionsService(),
                Filter().setTransactionState(CONFIRMED),
                provideRxSchedulers())
    }
}

fun View.provideTagsReportPresenter(transactionType: TransactionType): TagsReportPresenter =
        withActivityScope.provideSingletonFor(transactionType)

fun View.provideExpensesTrendsPresenter() = withActivityScope.provideSingletonFor<TrendPresenter>()
fun View.provideTagTotalsReportPresenter() = withActivityScope.provideSingletonFor<TagsTotalsReportPresenter>()