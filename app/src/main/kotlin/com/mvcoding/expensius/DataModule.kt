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

package com.mvcoding.expensius

import com.memoizrlabs.ShankModule
import com.memoizrlabs.shankkotlin.provideNew
import com.memoizrlabs.shankkotlin.registerFactory
import com.mvcoding.expensius.data.DataSource
import com.mvcoding.expensius.data.DataWriter
import com.mvcoding.expensius.feature.login.LoginWriterAndStateSource
import com.mvcoding.expensius.feature.login.TranslatedDefaultTagsSource
import com.mvcoding.expensius.model.CreateTag
import com.mvcoding.expensius.model.Tag

class DataModule : ShankModule {
    override fun registerFactories() {
        translatedDefaultTagsSource()
//        loginWriterAndStateSource()
    }

    private fun translatedDefaultTagsSource() = registerFactory(TranslatedDefaultTagsSource::class) { -> TranslatedDefaultTagsSource(provideContext()) }

//    private fun loginWriterAndStateSource() = registerFactory(LoginWriterAndStateSource::class) { ->
//        LoginWriterAndStateSource(
//                { provideFirebaseAppUserService().loginAnonymously() },
//                { googleToken, forceLoginAndLoseLocalDataIfUserAlreadyExists ->
//                    provideFirebaseAppUserService().loginWithGoogle(googleToken, forceLoginAndLoseLocalDataIfUserAlreadyExists)
//                },
//                provideTagsSource(),
//                provideDefaultTagsSource(),
//                provideCreateTagsWriter())
//    }
}

fun provideLoginWriterAndStateSource() = provideNew<LoginWriterAndStateSource>()
fun provideTagsSource() = provideNew<DataSource<List<Tag>>>() // TODO Provide proper tags source
fun provideDefaultTagsSource() = provideNew<TranslatedDefaultTagsSource>()
fun provideCreateTagsWriter() = provideNew<DataWriter<List<CreateTag>>>() // TODO Provide proper tags writer