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

class FirebaseModule : ShankModule {
    override fun registerFactories() {
//        firebaseAppUserService()
//        firebaseTagsService()
//        firebaseTransactionsService()
    }

//    private fun firebaseAppUserService() = registerFactory(FirebaseAppUserService::class) { -> FirebaseAppUserService(provideRxSchedulers().io) }
//    private fun firebaseTagsService() = registerFactory(FirebaseTagsService::class, ::FirebaseTagsService)
//    private fun firebaseTransactionsService() = registerFactory(FirebaseTransactionsService::class, ::FirebaseTransactionsService)
}

//fun provideFirebaseAppUserService() = provideNew<FirebaseAppUserService>()
//fun provideFirebaseTagsService() = provideNew<FirebaseTagsService>()
//fun provideFirebaseTransactionsService() = provideNew<FirebaseTransactionsService>()