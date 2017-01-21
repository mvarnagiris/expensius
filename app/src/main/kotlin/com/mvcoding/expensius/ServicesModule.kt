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

import com.memoizrlabs.ShankModule
import com.memoizrlabs.shankkotlin.provideGlobalSingleton
import com.memoizrlabs.shankkotlin.provideNew
import com.memoizrlabs.shankkotlin.registerFactory
import com.mvcoding.expensius.firebase.service.FirebaseAppUserService
import com.mvcoding.expensius.firebase.service.FirebaseAppUserWriteService
import com.mvcoding.expensius.firebase.service.FirebaseArchivedTagsService
import com.mvcoding.expensius.firebase.service.FirebaseArchivedTransactionsService
import com.mvcoding.expensius.firebase.service.FirebaseTagsService
import com.mvcoding.expensius.firebase.service.FirebaseTagsWriteService
import com.mvcoding.expensius.firebase.service.FirebaseTransactionsService
import com.mvcoding.expensius.firebase.service.FirebaseTransactionsWriteService
import com.mvcoding.expensius.service.AppUserService
import com.mvcoding.expensius.service.AppUserWriteService
import com.mvcoding.expensius.service.TagsService
import com.mvcoding.expensius.service.TagsWriteService
import com.mvcoding.expensius.service.TransactionsService
import com.mvcoding.expensius.service.TransactionsWriteService

class ServicesModule : ShankModule {
    override fun registerFactories() {
        firebaseAppUserWriteService()
        firebaseAppUserService()
        firebaseTagsWriteService()
        firebaseTagsService()
        firebaseArchivedTagsService()
        firebaseTransactionsWriteService()
        firebaseTransactionsService()
        firebaseArchivedTransactionsService()
    }

    private fun firebaseAppUserWriteService() = registerFactory(FirebaseAppUserWriteService::class) { -> FirebaseAppUserWriteService(provideAppUserService()) }
    private fun firebaseAppUserService() = registerFactory(FirebaseAppUserService::class) { -> FirebaseAppUserService() }
    private fun firebaseTagsWriteService() = registerFactory(FirebaseTagsWriteService::class) { -> FirebaseTagsWriteService(provideAppUserService()) }
    private fun firebaseTagsService() = registerFactory(FirebaseTagsService::class) { -> FirebaseTagsService(provideAppUserService()) }
    private fun firebaseArchivedTagsService() = registerFactory(FirebaseArchivedTagsService::class) { -> FirebaseArchivedTagsService(provideAppUserService()) }

    private fun firebaseTransactionsWriteService() = registerFactory(FirebaseTransactionsWriteService::class) { ->
        FirebaseTransactionsWriteService(provideAppUserService())
    }

    private fun firebaseTransactionsService() = registerFactory(FirebaseTransactionsService::class) { ->
        FirebaseTransactionsService(provideAppUserService(), provideTagsService(), provideArchivedTagsService())
    }

    private fun firebaseArchivedTransactionsService() = registerFactory(FirebaseArchivedTransactionsService::class) { ->
        FirebaseArchivedTransactionsService(provideAppUserService(), provideTagsService(), provideArchivedTagsService())
    }
}

fun provideAppUserWriteService(): AppUserWriteService = provideFirebaseAppUserWriteService()
fun provideAppUserService(): AppUserService = provideFirebaseAppUserService()
fun provideTagsWriteService(): TagsWriteService = provideFirebaseTagsWriteService()
fun provideTagsService(): TagsService = provideFirebaseTagsService()
fun provideArchivedTagsService(): TagsService = provideFirebaseArchivedTagsService()
fun provideTransactionsWriteService(): TransactionsWriteService = provideFirebaseTransactionsWriteService()
fun provideTransactionsService(): TransactionsService = provideFirebaseTransactionsService()
fun provideArchivedTransactionsService(): TransactionsService = provideFirebaseArchivedTransactionsService()

private fun provideFirebaseAppUserWriteService() = provideNew<FirebaseAppUserWriteService>()
fun provideFirebaseAppUserService() = provideGlobalSingleton<FirebaseAppUserService>()
private fun provideFirebaseTagsWriteService() = provideNew<FirebaseTagsWriteService>()
private fun provideFirebaseTagsService() = provideGlobalSingleton<FirebaseTagsService>()
private fun provideFirebaseArchivedTagsService() = provideGlobalSingleton<FirebaseArchivedTagsService>()
private fun provideFirebaseTransactionsWriteService() = provideNew<FirebaseTransactionsWriteService>()
private fun provideFirebaseTransactionsService() = provideGlobalSingleton<FirebaseTransactionsService>()
private fun provideFirebaseArchivedTransactionsService() = provideGlobalSingleton<FirebaseArchivedTransactionsService>()
