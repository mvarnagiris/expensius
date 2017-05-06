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

package com.mvcoding.expensius.feature.filter

import android.view.View
import com.memoizrlabs.Scope
import com.memoizrlabs.Shank
import com.memoizrlabs.ShankModule
import com.memoizrlabs.shankkotlin.provideGlobalSingleton
import com.memoizrlabs.shankkotlin.provideNew
import com.memoizrlabs.shankkotlin.provideSingletonFor
import com.memoizrlabs.shankkotlin.registerFactory
import com.mvcoding.expensius.feature.settings.provideReportSettingsSource
import com.mvcoding.expensius.provideAppUserSource
import com.mvcoding.expensius.provideRxSchedulers
import com.mvcoding.expensius.provideTimestampProvider
import memoizrlabs.com.shankandroid.activityScope
import memoizrlabs.com.shankandroid.withActivityScope

class FilterModule : ShankModule {
    override fun registerFactories() {
        remoteFilterSource()
        memoryRemoteFilterCache()
        filterPresenter()
    }

    private fun remoteFilterSource() = registerFactory(RemoteFilterSource::class) { -> RemoteFilterSource(provideAppUserSource(), provideTimestampProvider()) }
    private fun memoryRemoteFilterCache() = registerFactory(MemoryRemoteFilterCache::class) { -> MemoryRemoteFilterCache(provideRemoteFilterSource()) }

    private fun filterPresenter() = registerFactory(FilterPresenter::class) { scope: Scope ->
        FilterPresenter(provideFilterSource(scope), provideReportSettingsSource(scope), provideRxSchedulers())
    }
}

fun provideRemoteFilterSource() = provideNew<RemoteFilterSource>()

fun provideFilterSource(scope: Scope? = null) =
        if (scope == null) provideGlobalSingleton<MemoryRemoteFilterCache>()
        else Shank.with(scope).provideSingletonFor<MemoryRemoteFilterCache>()

fun View.provideFilterPresenter() = withActivityScope.provideSingletonFor<FilterPresenter>(activityScope)