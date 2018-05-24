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

package com.mvcoding.expensius.feature.filter

import com.memoizrlabs.ShankModule

class FilterModule : ShankModule {
    override fun registerFactories() {
//        remoteFilterSource()
//        localFilterSource()
//        remoteFilterMemoryCache()
//        secondaryRemoteFilterMemoryCache()
//        localFilterMemoryCache()
//        secondaryLocalFilterMemoryCache()
//        filterPresenter()
    }

//    private fun remoteFilterSource() = registerFactory(RemoteFilterSource::class) { -> RemoteFilterSource(provideAppUserSource(), provideTimestampProvider()) }
//    private fun localFilterSource() = registerFactory(LocalFilterSource::class) { -> LocalFilterSource() }
//    private fun remoteFilterMemoryCache() = registerNamedFactory(RemoteFilterMemoryCache::class, NAME_PRIMARY_CACHE) { -> RemoteFilterMemoryCache(provideRemoteFilterSource()) }
//    private fun secondaryRemoteFilterMemoryCache() = registerNamedFactory(RemoteFilterMemoryCache::class, NAME_SECONDARY_CACHE) { -> RemoteFilterMemoryCache(provideRemoteFilterSource()) }
//    private fun localFilterMemoryCache() = registerNamedFactory(LocalFilterMemoryCache::class, NAME_PRIMARY_CACHE) { -> LocalFilterMemoryCache(provideLocalFilterSource()) }
//    private fun secondaryLocalFilterMemoryCache() = registerNamedFactory(LocalFilterMemoryCache::class, NAME_SECONDARY_CACHE) { -> LocalFilterMemoryCache(provideLocalFilterSource()) }

//    private fun filterPresenter() = registerFactory(FilterPresenter::class) { scope: Scope ->
//        FilterPresenter(provideRemoteFilterCache(scope), provideReportSettingsSource(scope), provideRxSchedulers())
//    }
}

//private val NAME_PRIMARY_CACHE = "NAME_PRIMARY_CACHE"
//private val NAME_SECONDARY_CACHE = "NAME_SECONDARY_CACHE"

//private fun provideRemoteFilterSource() = provideNew<RemoteFilterSource>()
//private fun provideLocalFilterSource() = provideNew<LocalFilterSource>()

//fun provideRemoteFilterCache(scope: Scope) = Shank.named(NAME_PRIMARY_CACHE).with(scope).provideSingletonFor<RemoteFilterMemoryCache>()
//fun provideSecondaryRemoteFilterCache(scope: Scope) = Shank.named(NAME_SECONDARY_CACHE).with(scope).provideSingletonFor<RemoteFilterMemoryCache>()

//fun provideLocalFilterCache(scope: Scope) = Shank.named(NAME_PRIMARY_CACHE).with(scope).provideSingletonFor<LocalFilterMemoryCache>()
//fun provideSecondaryLocalFilterCache(scope: Scope) = Shank.named(NAME_SECONDARY_CACHE).with(scope).provideSingletonFor<LocalFilterMemoryCache>()

//fun View.provideFilterPresenter() = withActivityScope.provideSingletonFor<FilterPresenter>(activityScope)