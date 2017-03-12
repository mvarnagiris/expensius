package com.mvcoding.expensius

import com.memoizrlabs.ShankModule
import com.memoizrlabs.shankkotlin.provideNew
import com.memoizrlabs.shankkotlin.registerFactory
import com.mvcoding.expensius.firebase.service.FirebaseAppUserService

class FirebaseModule : ShankModule {
    override fun registerFactories() {
        firebaseAppUserService()
    }

    private fun firebaseAppUserService() = registerFactory(FirebaseAppUserService::class) { -> FirebaseAppUserService(provideRxSchedulers().io) }
}

fun provideFirebaseAppUserService() = provideNew<FirebaseAppUserService>()