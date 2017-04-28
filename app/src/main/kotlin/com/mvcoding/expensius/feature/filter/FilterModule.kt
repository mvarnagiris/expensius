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

import com.memoizrlabs.Scope
import com.memoizrlabs.Shank
import com.memoizrlabs.ShankModule
import com.memoizrlabs.shankkotlin.provideGlobalSingleton
import com.memoizrlabs.shankkotlin.provideSingletonFor
import com.memoizrlabs.shankkotlin.registerFactory
import com.mvcoding.expensius.extensions.interval
import com.mvcoding.expensius.model.Filter
import com.mvcoding.expensius.provideAppUserSource

class FilterModule : ShankModule {
    override fun registerFactories() {
        filterSource()
    }

    private fun filterSource() = registerFactory(FilterSource::class) { ->
        val appUserSource = provideAppUserSource()
        FilterSource { appUserSource.data().map { Filter(it.userId, it.settings.reportPeriod.interval(System.currentTimeMillis())) } }
    }
}

fun provideFilterSource(scope: Scope?) =
        if (scope == null) provideGlobalSingleton<FilterSource>()
        else Shank.with(scope).provideSingletonFor<FilterSource>()