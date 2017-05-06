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

package com.mvcoding.expensius.feature.settings

import android.app.Activity
import com.memoizrlabs.Scope
import com.memoizrlabs.Shank
import com.memoizrlabs.ShankModule
import com.memoizrlabs.shankkotlin.provideGlobalSingleton
import com.memoizrlabs.shankkotlin.provideSingletonFor
import com.memoizrlabs.shankkotlin.registerFactory
import com.mvcoding.expensius.feature.currency.provideCurrenciesSource
import com.mvcoding.expensius.model.ReportSettings
import com.mvcoding.expensius.provideAppUserSource
import com.mvcoding.expensius.provideRxSchedulers
import memoizrlabs.com.shankandroid.withThisScope

class SettingsModule : ShankModule {
    override fun registerFactories() {
        settingsPresenter()
        reportSettingsSource()
    }

    private fun settingsPresenter() = registerFactory(SettingsPresenter::class) { ->
        SettingsPresenter(provideAppUserSource(), provideAppUserSource(), provideCurrenciesSource(), provideRxSchedulers())
    }

    private fun reportSettingsSource() = registerFactory(ReportSettingsSource::class) { ->
        val appUserSource = provideAppUserSource()
        ReportSettingsSource { appUserSource.data().map { ReportSettings(it.settings.reportPeriod, it.settings.reportGroup) } }
    }
}

fun Activity.provideSettingsPresenter() = withThisScope.provideSingletonFor<SettingsPresenter>()
fun provideReportSettingsSource(scope: Scope? = null) =
        if (scope == null) provideGlobalSingleton<ReportSettingsSource>()
        else Shank.with(scope).provideSingletonFor<ReportSettingsSource>()