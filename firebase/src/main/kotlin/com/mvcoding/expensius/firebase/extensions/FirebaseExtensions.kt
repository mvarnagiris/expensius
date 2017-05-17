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

package com.mvcoding.expensius.firebase.extensions

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import rx.Observable
import rx.Scheduler
import rx.lang.kotlin.deferredObservable
import rx.lang.kotlin.observable

private val firebaseDatabaseInstance by lazy { FirebaseDatabase.getInstance().apply { setPersistenceEnabled(true) } }
fun getFirebaseDatabase(): FirebaseDatabase = firebaseDatabaseInstance

fun Query.observeSingleValue(scheduler: Scheduler): Observable<DataSnapshot> = deferredObservable {
    observable<DataSnapshot> { subscriber ->
        addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                subscriber.onError(databaseError.toException())
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                subscriber.onNext(dataSnapshot)
                subscriber.onCompleted()
            }
        })
    }
}.observeOn(scheduler)

fun <T> Task<T>.observeSuccessfulComplete(scheduler: Scheduler): Observable<T> = deferredObservable {
    observable<T> { subscriber ->
        addOnCompleteListener {
            if (it.isSuccessful) {
                subscriber.onNext(it.result)
                subscriber.onCompleted()
            } else {
                subscriber.onError(it.exception)
            }
        }
    }
}.observeOn(scheduler)

fun FirebaseAuth.observeSingleCurrentFirebaseUser(scheduler: Scheduler): Observable<FirebaseUser?> = deferredObservable {
    observable<FirebaseUser?> { subscriber ->
        val authStateListener = object : FirebaseAuth.AuthStateListener {
            override fun onAuthStateChanged(firebaseAuth: FirebaseAuth) {
                firebaseAuth.removeAuthStateListener(this)
                subscriber.onNext(firebaseAuth.currentUser)
                subscriber.onCompleted()
            }
        }
        addAuthStateListener(authStateListener)
    }
}.observeOn(scheduler)

fun FirebaseUser.getAppUserDatabaseReference(): DatabaseReference = getFirebaseDatabase()
        .getReference("users")
        .child(uid)