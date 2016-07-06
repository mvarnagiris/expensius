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

package com.mvcoding.expensius.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.mvcoding.expensius.model.AppUser
import com.mvcoding.expensius.model.AppUser.Companion.noAppUser
import com.mvcoding.expensius.model.AuthProvider
import com.mvcoding.expensius.model.AuthProvider.ANONYMOUS
import com.mvcoding.expensius.model.AuthProvider.GOOGLE
import com.mvcoding.expensius.model.UserId
import com.mvcoding.expensius.service.AppUserService
import com.mvcoding.expensius.service.LoginService
import rx.Observable
import rx.lang.kotlin.BehaviorSubject
import rx.lang.kotlin.deferredObservable
import rx.lang.kotlin.observable

class FirebaseAppUserService : AppUserService, LoginService {
    private val authStateListener = AuthStateListener { appUserSubject.onNext(it.currentUser?.toAppUser() ?: noAppUser) }
    private val firebaseAuth = FirebaseAuth.getInstance().apply { addAuthStateListener { authStateListener } }
    private val appUserSubject = BehaviorSubject<AppUser>()

    override fun appUser(): Observable<AppUser> = appUserSubject.distinctUntilChanged()

    override fun loginAnonymously(): Observable<AppUser> = deferredObservable {
        observable<AppUser> { subscriber ->
            firebaseAuth.signInAnonymously()
                    .addOnSuccessListener { subscriber.onNext(it.user.toAppUser()) }
                    .addOnCompleteListener { subscriber.onError(it.exception) }
        }
    }

    private fun FirebaseUser.toAppUser() = AppUser(UserId(uid), providerData.map { it.providerId.toAuthProvider() }.toSet())
    private fun String.toAuthProvider(): AuthProvider = when (this) {
        GoogleAuthProvider.PROVIDER_ID -> GOOGLE
        else -> ANONYMOUS
    }
}