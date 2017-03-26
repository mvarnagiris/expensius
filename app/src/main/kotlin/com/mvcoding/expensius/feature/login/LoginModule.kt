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

package com.mvcoding.expensius.feature.login

import android.app.Activity
import com.memoizrlabs.ShankModule
import com.memoizrlabs.shankkotlin.provideNew
import com.memoizrlabs.shankkotlin.provideSingletonFor
import com.memoizrlabs.shankkotlin.registerFactory
import com.mvcoding.expensius.feature.login.LoginPresenter.Destination
import com.mvcoding.expensius.feature.tag.provideAllTagsSource
import com.mvcoding.expensius.feature.tag.provideCreateTagsWriter
import com.mvcoding.expensius.provideAppUserSource
import com.mvcoding.expensius.provideDefaultTagsSource
import com.mvcoding.expensius.provideFirebaseAppUserService
import com.mvcoding.expensius.provideRxSchedulers
import memoizrlabs.com.shankandroid.withThisScope

class LoginModule : ShankModule {
    override fun registerFactories() {
        loginSource()
        loginPresenter()
    }

    private fun loginSource() = registerFactory(LoginSource::class) { ->
        val firebaseAppUserService = provideFirebaseAppUserService()
        LoginSource(
                { firebaseAppUserService.login(it) },
                { firebaseAppUserService.logout() },
                { firebaseAppUserService.getAppUser() },
                provideAppUserSource(),
                provideAllTagsSource(),
                provideDefaultTagsSource(),
                provideCreateTagsWriter())
    }

    private fun loginPresenter() = registerFactory(LoginPresenter::class) { destination: Destination ->
        LoginPresenter(destination, provideLoginSource(), provideRxSchedulers())
    }
}

fun provideLoginSource() = provideNew<LoginSource>()
fun Activity.provideLoginPresenter(destination: Destination) = withThisScope.provideSingletonFor<LoginPresenter>(destination)