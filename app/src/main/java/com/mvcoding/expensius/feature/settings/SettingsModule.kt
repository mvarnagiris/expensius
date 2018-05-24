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

package com.mvcoding.expensius.feature.settings

import com.memoizrlabs.ShankModule

class SettingsModule : ShankModule {
    override fun registerFactories() {
//        settingsPresenter()
//        reportSettingsSource()
    }

//    private fun settingsPresenter() = registerFactory(SettingsPresenter::class) { ->
//        SettingsPresenter(provideAppUserSource(), provideAppUserSource(), provideCurrenciesSource(), provideRxSchedulers())
//    }
//
//    private fun reportSettingsSource() = registerFactory(ReportSettingsSource::class) { ->
//        val appUserSource = provideAppUserSource()
//        ReportSettingsSource { appUserSource.data().map { ReportSettings(it.settings.reportPeriod, it.settings.reportGroup, it.settings.mainCurrency) } }
//    }
}

//fun Activity.provideSettingsPresenter() = withThisScope.provideSingletonFor<SettingsPresenter>()
//fun provideReportSettingsSource(scope: Scope) = Shank.with(scope).provideSingletonFor<ReportSettingsSource>()