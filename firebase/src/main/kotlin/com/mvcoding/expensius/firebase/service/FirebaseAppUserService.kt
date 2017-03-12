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

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.mvcoding.expensius.firebase.extensions.getAppUserDatabaseReference
import com.mvcoding.expensius.firebase.extensions.observeSingleCurrentFirebaseUser
import com.mvcoding.expensius.firebase.extensions.observeSingleValue
import com.mvcoding.expensius.firebase.model.FirebaseAppUser
import com.mvcoding.expensius.model.AppUser
import com.mvcoding.expensius.model.AuthProvider
import com.mvcoding.expensius.model.AuthProvider.ANONYMOUS
import com.mvcoding.expensius.model.AuthProvider.GOOGLE
import com.mvcoding.expensius.model.NullModels.noAppUser
import rx.Observable
import rx.Observable.just
import rx.Scheduler

class FirebaseAppUserService(private val scheduler: Scheduler) {

//    private val firebaseUserSubject = BehaviorSubject<FirebaseUser?>()
//    private val firebaseUserDataSubject = BehaviorSubject<FirebaseAppUser?>()

    fun getAppUser(): Observable<AppUser> = FirebaseAuth.getInstance()
            .observeSingleCurrentFirebaseUser(scheduler)
            .switchMap { if (it == null) just(noAppUser) else fetchAppUser(it) }
            .onErrorReturn { noAppUser }

    private fun fetchAppUser(firebaseUser: FirebaseUser): Observable<AppUser> = firebaseUser.getAppUserDatabaseReference()
            .observeSingleValue(scheduler)
            .map { it.getValue(FirebaseAppUser::class.java).toAppUser(firebaseUser.getAuthProviders()) }

    private fun FirebaseUser.getAuthProviders(): Set<AuthProvider> = providerData.map {
        when (it.providerId) {
            GoogleAuthProvider.PROVIDER_ID -> GOOGLE
            else -> ANONYMOUS
        }
    }.toSet()

    //    private val appUserObservable: Observable<AppUser>
//
//    private val authStateListener = AuthStateListener { onFirebaseUserChanged(it.currentUser) }
//
//    private var userId: UserId = noUserId
//    private var userDataListener: ValueEventListener? = null
//
//    init {
//        firebaseAuth.addAuthStateListener(authStateListener)
//        appUserObservable = combineLatest(firebaseUserSubject, firebaseUserDataSubject) { firebaseUser, firebaseUserData ->
//            val settings = firebaseUserData?.settings?.toSettings() ?: noSettings.copy(mainCurrency = defaultCurrency())
//            firebaseUser?.toAppUser(settings) ?: noAppUser.copy(settings = settings)
//        }.distinctUntilChanged()
//    }
//
//    override fun appUser(): Observable<AppUser> = appUserObservable
//
//    fun loginAnonymously(): Observable<Unit> = deferredObservable {
//        observable<Unit> { subscriber ->
//            firebaseAuth.signInAnonymously()
//                    .addOnSuccessListener {
//                        subscriber.onNext(Unit)
//                        subscriber.onCompleted()
//                    }
//                    .addOnFailureListener { subscriber.onError(it) }
//        }
//    }
//
//    fun loginWithGoogle(googleToken: GoogleToken, forceLoginAndLoseLocalDataIfUserAlreadyExists: Boolean): Observable<Unit> = appUser().first().switchMap {
//        if (it.isNotLoggedIn() || forceLoginAndLoseLocalDataIfUserAlreadyExists) {
//            observable<Unit> { subscriber ->
//                val credential = GoogleAuthProvider.getCredential(googleToken.token, null)
//                firebaseAuth.signInWithCredential(credential)
//                        .addOnSuccessListener {
//                            subscriber.onNext(Unit)
//                            subscriber.onCompleted()
//                        }
//                        .addOnFailureListener { subscriber.onError(it) }
//            }
//        } else {
//            observable<Unit> { subscriber ->
//                val credential = GoogleAuthProvider.getCredential(googleToken.token, null)
//                firebaseAuth.currentUser!!.linkWithCredential(credential)
//                        .addOnSuccessListener {
//                            subscriber.onNext(Unit)
//                            subscriber.onCompleted()
//                        }
//                        .addOnFailureListener {
//                            subscriber.onError(convertException(it))
//                        }
//            }
//        }
//    }
//
//    private fun convertException(exception: Exception) = when (exception) {
//        is FirebaseAuthUserCollisionException -> UserAlreadyLinkedException(exception)
//        else -> exception
//    }
//
//    private fun onFirebaseUserChanged(firebaseUser: FirebaseUser?) {
//        firebaseUserSubject.onNext(firebaseUser)
//        val oldUserId = userId
//        val newUserId = firebaseUser?.let { UserId(it.uid) } ?: noUserId
//        userId = newUserId
//        if (firebaseUser == null) firebaseUserDataSubject.onNext(null)
//        else setupUserDataListener(oldUserId, newUserId)
//    }
//
//    private fun setupUserDataListener(oldUserId: UserId, userId: UserId) {
//        if (oldUserId != noUserId && userDataListener != null) {
//            userDatabaseReference(oldUserId).removeEventListener(userDataListener)
//        }
//
//        userDataListener = object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                firebaseUserDataSubject.onNext(dataSnapshot.getValue(FirebaseAppUser::class.java))
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                // TODO Handle error
//            }
//        }
//        userDatabaseReference(userId).addValueEventListener(userDataListener)
//    }
//
//    private fun FirebaseUser.toAppUser(settings: Settings) = AppUser(
//            UserId(uid),
//            email?.let { Email(it) } ?: noEmail,
//            settings,
//            providerData.map { it.providerId.toAuthProvider() }.toSet())
}