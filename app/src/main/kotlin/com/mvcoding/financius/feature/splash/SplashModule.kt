package com.mvcoding.financius.feature.splash

import com.memoizrlabs.Shank
import com.memoizrlabs.ShankModule
import com.mvcoding.financius.Session
import com.mvcoding.financius.Settings
import com.mvcoding.financius.extension.provideSingleton

class SplashModule : ShankModule {
    override fun registerFactories() {
        val userSettings = provideSingleton(Settings::class)
        val session = provideSingleton(Session::class)
        Shank.registerFactory(SplashPresenter::class.java, { SplashPresenter(userSettings, session) })
    }
}