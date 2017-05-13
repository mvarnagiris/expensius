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

package com.mvcoding.expensius.feature.overview

import com.memoizrlabs.Scope
import com.memoizrlabs.ShankModule
import com.memoizrlabs.shankkotlin.provideSingletonFor
import com.memoizrlabs.shankkotlin.registerFactory
import com.mvcoding.expensius.feature.BaseActivity
import com.mvcoding.expensius.feature.filter.provideRemoteFilterCache
import com.mvcoding.expensius.feature.settings.provideReportSettingsSource
import com.mvcoding.expensius.provideRxSchedulers
import memoizrlabs.com.shankandroid.withThisScope

class OverviewModule : ShankModule {
    override fun registerFactories() {
        overviewPresenter()
    }

    private fun overviewPresenter() = registerFactory(OverviewPresenter::class) { scope: Scope ->
        OverviewPresenter(provideRemoteFilterCache(scope), provideReportSettingsSource(scope), provideRxSchedulers())
    }
}

fun BaseActivity.provideOverviewPresenter() = withThisScope.provideSingletonFor<OverviewPresenter>(scope)