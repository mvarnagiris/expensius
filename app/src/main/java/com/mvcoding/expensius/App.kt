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

package com.mvcoding.expensius

import android.app.Application
import com.memoizrlabs.ShankModuleInitializer.initializeModules
import com.mvcoding.expensius.feature.calculator.CalculatorModule
import com.mvcoding.expensius.feature.currency.CurrenciesModule
import com.mvcoding.expensius.feature.filter.FilterModule
import com.mvcoding.expensius.feature.login.LoginModule
import com.mvcoding.expensius.feature.overview.OverviewModule
import com.mvcoding.expensius.feature.premium.PremiumModule
import com.mvcoding.expensius.feature.reports.ReportsModule
import com.mvcoding.expensius.feature.settings.SettingsModule
import com.mvcoding.expensius.feature.splash.SplashModule
import com.mvcoding.expensius.feature.tag.TagsModule

class App : Application() {
    override fun onCreate() {
        super.onCreate()
//        JodaTimeAndroid.init(this)
        initializeModules(
                AppModule(this),
                FirebaseModule(),
                DataModule(),
                ServicesModule(),
                SplashModule(),
                LoginModule(),
                CalculatorModule(),
                OverviewModule(),
                TagsModule(),
//                TransactionModule(),
                ReportsModule(),
                CurrenciesModule(),
                SettingsModule(),
                PremiumModule(),
                FilterModule())
    }
}