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

package com.mvcoding.expensius.feature.reports

import com.memoizrlabs.ShankModule

class ReportsModule : ShankModule {
    override fun registerFactories() {
//        trendsReportSource()
//        tagsReportSource()
//        trendsReportPresenter()
//        tagsReportPresenter()
//        trendsIntervalsPresenter()
    }

//    private fun trendsReportSource() = registerFactory(TrendsReportSource::class) { scope: Scope ->
//        TrendsReportSource(
//                provideTransactionsSource(true, scope),
//                provideTransactionsSource(false, scope),
//                provideLocalFilterCache(scope),
//                provideReportSettingsSource(scope),
//                provideMoneyConversionSource())
//    }
//
//    private fun tagsReportSource() = registerFactory(TagsReportSource::class) { scope: Scope ->
//        TagsReportSource(
//                provideTransactionsSource(true, scope),
//                provideTransactionsSource(false, scope),
//                provideLocalFilterCache(scope),
//                provideReportSettingsSource(scope),
//                provideMoneyConversionSource())
//    }
//
//    private fun trendsReportPresenter() = registerFactory(TrendsReportPresenter::class) { scope: Scope ->
//        TrendsReportPresenter(
//                provideTrendsReportSource(scope),
//                provideReportSettingsSource(scope),
//                provideRemoteFilterCache(scope),
//                provideSecondaryRemoteFilterCache(scope),
//                provideRxSchedulers())
//    }
//
//    private fun tagsReportPresenter() = registerFactory(TagsReportPresenter::class) { scope: Scope ->
//        TagsReportPresenter(provideTagsReportSource(scope), provideReportSettingsSource(scope), provideSecondaryRemoteFilterCache(scope), provideRxSchedulers())
//    }
//
//    private fun trendsIntervalsPresenter() = registerFactory(TrendsIntervalsPresenter::class) { scope: Scope ->
//        TrendsIntervalsPresenter(provideTrendsReportSource(scope), provideRxSchedulers())
//    }
}

//fun provideTrendsReportSource(scope: Scope) = Shank.with(scope).provideSingletonFor<TrendsReportSource>(scope)
//fun provideTagsReportSource(scope: Scope) = Shank.with(scope).provideSingletonFor<TagsReportSource>(scope)
//fun View.provideTrendsReportPresenter() = withActivityScope.provideSingletonFor<TrendsReportPresenter>(activityScope)
//fun View.provideTagsReportPresenter() = withActivityScope.provideSingletonFor<TagsReportPresenter>(activityScope)
//fun View.provideTrendsIntervalsPresenter() = withActivityScope.provideSingletonFor<TrendsIntervalsPresenter>(activityScope)