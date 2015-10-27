/*
 * Copyright (C) 2015 Mantas Varnagiris.
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

package com.mvcoding.financius

import android.content.Context
import com.memoizrlabs.Shank
import com.memoizrlabs.ShankModule
import com.mvcoding.financius.feature.intro.IntroPage
import com.mvcoding.financius.feature.intro.IntroPresenter
import com.mvcoding.financius.feature.splash.SplashPresenter

class DependencyModule(val context: Context) : ShankModule {
    init {
        Shank.registerFactory(Context::class.java, { context })
    }

    override fun registerFactories() {
        userSettings()
        session()

        splashPresenter()
        introPresenter()
    }

    private fun introPresenter() {
        val userSettings = Shank.provide(Settings::class.java)
        val introPages = listOf(IntroPage(), IntroPage(), IntroPage())
        Shank.registerFactory(IntroPresenter::class.java, { IntroPresenter(introPages, userSettings) })
    }

    private fun splashPresenter() {
        val userSettings = Shank.provide(Settings::class.java)
        val session = Shank.provide(Session::class.java)
        Shank.registerFactory(SplashPresenter::class.java, { SplashPresenter(userSettings, session) })
    }

    private fun session() {
        Shank.registerFactory(Session::class.java, { UserSession() })
    }

    private fun userSettings() {
        Shank.registerFactory(Settings::class.java, { UserSettings() })
    }
}

