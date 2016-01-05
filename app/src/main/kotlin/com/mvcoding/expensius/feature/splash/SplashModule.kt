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

package com.mvcoding.expensius.feature.splash

import com.memoizrlabs.Shank.registerFactory
import com.memoizrlabs.ShankModule
import com.mvcoding.expensius.Session
import com.mvcoding.expensius.Settings
import com.mvcoding.expensius.extension.provideSingleton

class SplashModule : ShankModule {
    override fun registerFactories() {
        val userSettings = provideSingleton(Settings::class)
        val session = provideSingleton(Session::class)
        registerFactory(SplashPresenter::class.java, { SplashPresenter(userSettings, session) })
    }
}