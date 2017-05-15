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
import com.mvcoding.expensius.feature.filter.provideSecondaryRemoteFilterCache
import com.mvcoding.expensius.feature.reports.trends.TrendsReportPresenter
import com.mvcoding.expensius.feature.reports.trends.TrendsReportSource
import com.mvcoding.expensius.feature.settings.provideReportSettingsSource
import com.mvcoding.expensius.feature.transaction.provideTransactionsSource
import com.mvcoding.expensius.provideRxSchedulers
import memoizrlabs.com.shankandroid.activityScope
import memoizrlabs.com.shankandroid.withActivityScope

class ReportsModule : ShankModule {
    override fun registerFactories() {
        trendsReportSource()
        trendsReportPresenter()
//        tagTotalsReportPresenter()
    }

    private fun trendsReportSource() = registerFactory(TrendsReportSource::class) { scope: Scope ->
        TrendsReportSource(
                provideTransactionsSource(true, scope),
                provideTransactionsSource(false, scope),
                provideLocalFilterCache(scope),
                provideReportSettingsSource(scope),
                provideMoneyConversionSource())
    }

    private fun trendsReportPresenter() = registerFactory(TrendsReportPresenter::class) { scope: Scope ->
        TrendsReportPresenter(provideTrendsReportSource(scope), provideReportSettingsSource(scope), provideSecondaryRemoteFilterCache(scope), provideRxSchedulers())
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

fun provideTrendsReportSource(scope: Scope) = Shank.with(scope).provideSingletonFor<TrendsReportSource>(scope)
fun View.provideTrendsReportPresenter() = withActivityScope.provideSingletonFor<TrendsReportPresenter>(activityScope)
fun View.provideTagTotalsReportPresenter() = withActivityScope.provideSingletonFor<TagsTotalsReportPresenter>()