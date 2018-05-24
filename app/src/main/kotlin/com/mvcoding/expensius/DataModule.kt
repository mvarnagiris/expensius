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

import com.memoizrlabs.ShankModule

class DataModule : ShankModule {
    override fun registerFactories() {
//        appUserSource()
//        appUserIdSource()
//        translatedDefaultTagsSource()
    }
//
//    private fun appUserSource() = registerFactory(AppUserSource::class) { ->
//        AppUserSource(
//                { provideFirebaseAppUserService().getAppUser() },
//                { provideFirebaseAppUserService().setAppUser(it) })
//    }
//
//    private fun appUserIdSource() = registerFactory(AppUserIdSource::class) { -> AppUserIdSource(provideAppUserSource()) }
//    private fun translatedDefaultTagsSource() = registerFactory(TranslatedDefaultTagsSource::class) { -> TranslatedDefaultTagsSource(provideContext()) }
}

//fun provideAppUserSource() = provideGlobalSingleton<AppUserSource>()
//fun provideAppUserIdSource() = provideGlobalSingleton<AppUserIdSource>()
//fun provideDefaultTagsSource() = provideNew<TranslatedDefaultTagsSource>()