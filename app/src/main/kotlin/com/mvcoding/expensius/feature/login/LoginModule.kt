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

package com.mvcoding.expensius.feature.login

import android.app.Activity
import com.memoizrlabs.ShankModule
import com.memoizrlabs.shankkotlin.provideNew
import com.memoizrlabs.shankkotlin.provideSingletonFor
import com.memoizrlabs.shankkotlin.registerFactory
import com.mvcoding.expensius.*
import com.mvcoding.expensius.feature.login.LoginPresenter.Destination
import com.mvcoding.expensius.feature.tag.provideCreateTagsWriter
import memoizrlabs.com.shankandroid.withThisScope

class LoginModule : ShankModule {
    override fun registerFactories() {
        tagsSnapshotSource()
        loginSource()
        loginPresenter()
    }

    private fun tagsSnapshotSource() = registerFactory(TagsSnapshotSource::class) { ->
        TagsSnapshotSource(provideAppUserSource()) { provideFirebaseTagsService().getTags(it) }
    }

    private fun loginSource() = registerFactory(LoginSource::class) { ->
        val firebaseAppUserService = provideFirebaseAppUserService()
        LoginSource(
                { firebaseAppUserService.login(it) },
                { firebaseAppUserService.logout() },
                { firebaseAppUserService.getAppUser() },
                provideAppUserSource(),
                provideTagsSnapshotSource(),
                provideDefaultTagsSource(),
                provideCreateTagsWriter())
    }

    private fun loginPresenter() = registerFactory(LoginPresenter::class) { destination: Destination ->
        LoginPresenter(destination, provideLoginSource(), provideRxSchedulers())
    }
}

fun provideTagsSnapshotSource() = provideNew<TagsSnapshotSource>()
fun provideLoginSource() = provideNew<LoginSource>()
fun Activity.provideLoginPresenter(destination: Destination) = withThisScope.provideSingletonFor<LoginPresenter>(destination)