package com.mvcoding.expensius

import com.memoizrlabs.ShankModule
import com.memoizrlabs.shankkotlin.provideNew
import com.memoizrlabs.shankkotlin.registerFactory
import com.mvcoding.expensius.firebase.service.FirebaseAppUserService
import com.mvcoding.expensius.firebase.service.FirebaseTagsService

class FirebaseModule : ShankModule {
    override fun registerFactories() {
        firebaseAppUserService()
        firebaseTagsService()
    }

    private fun firebaseAppUserService() = registerFactory(FirebaseAppUserService::class) { -> FirebaseAppUserService(provideRxSchedulers().io) }
    private fun firebaseTagsService() = registerFactory(FirebaseTagsService::class, ::FirebaseTagsService)
}

fun provideFirebaseAppUserService() = provideNew<FirebaseAppUserService>()
fun provideFirebaseTagsService() = provideNew<FirebaseTagsService>()