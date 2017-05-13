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

package com.mvcoding.expensius.feature.reports

import android.view.View
import com.memoizrlabs.Scope
import com.memoizrlabs.Shank
import com.memoizrlabs.ShankModule
import com.memoizrlabs.shankkotlin.provideSingletonFor
import com.memoizrlabs.shankkotlin.registerFactory
import com.mvcoding.expensius.feature.currency.provideMoneyConversionSource
import com.mvcoding.expensius.feature.filter.provideLocalFilterCache
import com.mvcoding.expensius.feature.reports.trends.TrendsPresenter
import com.mvcoding.expensius.feature.reports.trends.TrendsSource
import com.mvcoding.expensius.feature.settings.provideReportSettingsSource
import com.mvcoding.expensius.feature.transaction.provideTransactionsSource
import com.mvcoding.expensius.provideRxSchedulers
import memoizrlabs.com.shankandroid.activityScope
import memoizrlabs.com.shankandroid.withActivityScope

class ReportsModule : ShankModule {
    override fun registerFactories() {
        trendsSource()
        trendsPresenter()
//        tagTotalsReportPresenter()
    }

    private fun trendsSource() = registerFactory(TrendsSource::class) { scope: Scope ->
        TrendsSource(
                provideTransactionsSource(scope),
                provideTransactionsSource(scope), // TODO Should be secondary source
                provideLocalFilterCache(scope),
                provideReportSettingsSource(scope),
                provideMoneyConversionSource())
    }

    private fun trendsPresenter() = registerFactory(TrendsPresenter::class) { scope: Scope ->
        TrendsPresenter(provideTrendsSource(scope), provideRxSchedulers())
    }

//    private fun tagTotalsReportPresenter() = registerFactory(TagsTotalsReportPresenter::class) { ->
//        TagsTotalsReportPresenter(
//                provideAppUserService(),
//                provideTransactionsService(),
//                provideExchangeRatesProvider(),
//                provideFilter().setTransactionState(CONFIRMED),
//                provideRxSchedulers())
//    }
}

fun provideTrendsSource(scope: Scope) = Shank.with(scope).provideSingletonFor<TrendsSource>(scope)
fun View.provideTrendsPresenter() = withActivityScope.provideSingletonFor<TrendsPresenter>(activityScope)
fun View.provideTagTotalsReportPresenter() = withActivityScope.provideSingletonFor<TagsTotalsReportPresenter>()