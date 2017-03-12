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

package com.mvcoding.expensius.firebase.service

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.mvcoding.expensius.firebase.extensions.getAppUserDatabaseReference
import com.mvcoding.expensius.firebase.extensions.observeSingleCurrentFirebaseUser
import com.mvcoding.expensius.firebase.extensions.observeSingleValue
import com.mvcoding.expensius.firebase.extensions.observeSuccessfulComplete
import com.mvcoding.expensius.firebase.model.FirebaseAppUser
import com.mvcoding.expensius.model.*
import com.mvcoding.expensius.model.AuthProvider.ANONYMOUS
import com.mvcoding.expensius.model.AuthProvider.GOOGLE
import com.mvcoding.expensius.model.Login.AnonymousLogin
import com.mvcoding.expensius.model.Login.GoogleLogin
import com.mvcoding.expensius.model.NullModels.noAppUser
import com.mvcoding.expensius.model.NullModels.noEmail
import com.mvcoding.expensius.model.NullModels.noImage
import com.mvcoding.expensius.model.NullModels.noName
import rx.Observable
import rx.Observable.error
import rx.Observable.just
import rx.Scheduler

class FirebaseAppUserService(private val scheduler: Scheduler) {

    fun getAppUser(): Observable<AppUser> = FirebaseAuth.getInstance()
            .observeSingleCurrentFirebaseUser(scheduler)
            .switchMap { if (it == null) just(noAppUser) else fetchAppUser(it) }
            .onErrorReturn { noAppUser }

    fun login(login: Login): Observable<LoggedInUserDetails> = FirebaseAuth.getInstance()
            .observeSingleCurrentFirebaseUser(scheduler)
            .switchMap { it.loginOrLinkAccount(login).observeSuccessfulComplete(scheduler) }
            .map { (it.user ?: throw RuntimeException("Failed to log in")).toLoggedInUserDetails() }
            .onErrorResumeNext { error(convertException(it)) }

    fun logout(): Unit = FirebaseAuth.getInstance().signOut()

    private fun fetchAppUser(firebaseUser: FirebaseUser): Observable<AppUser> = firebaseUser.getAppUserDatabaseReference()
            .observeSingleValue(scheduler)
            .map { it.getValue(FirebaseAppUser::class.java).toAppUser(firebaseUser.getAuthProviders()) }

    private fun FirebaseUser.getAuthProviders(): Set<AuthProvider> = providerData.map {
        when (it.providerId) {
            GoogleAuthProvider.PROVIDER_ID -> GOOGLE
            else -> ANONYMOUS
        }
    }.toSet()

    private fun FirebaseUser?.loginOrLinkAccount(login: Login): Task<AuthResult> =
            if (this == null) login.createLoginTask()
            else login.createLinkAccountTask(this)

    private fun FirebaseUser.toLoggedInUserDetails() = LoggedInUserDetails(
            UserId(uid),
            displayName?.let(::Name) ?: noName,
            email?.let(::Email) ?: noEmail,
            photoUrl?.toString()?.let(::UriImage) ?: noImage)

    private fun Login.createLoginTask(): Task<AuthResult> = when (this) {
        is AnonymousLogin -> FirebaseAuth.getInstance().signInAnonymously()
        is GoogleLogin -> FirebaseAuth.getInstance().signInWithCredential(createCredential())
    }

    private fun Login.createLinkAccountTask(firebaseUser: FirebaseUser): Task<AuthResult> = when (this) {
        is AnonymousLogin -> FirebaseAuth.getInstance().signInAnonymously()
        is GoogleLogin -> firebaseUser.linkWithCredential(createCredential())
    }

    private fun GoogleLogin.createCredential() = GoogleAuthProvider.getCredential(googleToken.token, null)

    private fun convertException(exception: Throwable) = when (exception) {
        is FirebaseAuthUserCollisionException -> UserAlreadyLinkedException(exception)
        else -> exception
    }
}