/*
 * Copyright (C) 2016 Mantas Varnagiris.
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

import android.support.multidex.MultiDexApplication
import com.crashlytics.android.Crashlytics
import com.google.firebase.database.FirebaseDatabase
import com.memoizrlabs.ShankModuleInitializer.initializeModules
import com.mvcoding.expensius.feature.calculator.CalculatorModule
import com.mvcoding.expensius.feature.currency.CurrenciesModule
import com.mvcoding.expensius.feature.login.LoginModule
import com.mvcoding.expensius.feature.overview.OverviewModule
import com.mvcoding.expensius.feature.premium.PremiumModule
import com.mvcoding.expensius.feature.report.ReportsModule
import com.mvcoding.expensius.feature.settings.SettingsModule
import com.mvcoding.expensius.feature.splash.SplashModule
import com.mvcoding.expensius.feature.tag.TagsModule
import com.mvcoding.expensius.feature.transaction.TransactionModule
import io.fabric.sdk.android.Fabric
import net.danlew.android.joda.JodaTimeAndroid

class App : MultiDexApplication() {
    private var isFirebaseSetupComplete = false

    override fun onCreate() {
        super.onCreate()
        JodaTimeAndroid.init(this)
        Fabric.with(this, Crashlytics())
        initializeModules(
                AppModule(this),
                ServicesModule(),
                SplashModule(),
                LoginModule(),
                CalculatorModule(),
                OverviewModule(),
                TagsModule(),
                TransactionModule(),
                ReportsModule(),
                CurrenciesModule(),
                SettingsModule(),
                PremiumModule())
    }

    fun setupFirebase() {
        if (isFirebaseSetupComplete) return
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        isFirebaseSetupComplete = true
    }
}