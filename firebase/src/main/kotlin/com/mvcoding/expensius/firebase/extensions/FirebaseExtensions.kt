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

package com.mvcoding.expensius.firebase.extensions

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Scheduler

private val firebaseDatabaseInstance by lazy { FirebaseDatabase.getInstance().apply { setPersistenceEnabled(true) } }
fun getFirebaseDatabase(): FirebaseDatabase = firebaseDatabaseInstance

fun Query.observeSingleValue(scheduler: Scheduler): Observable<DataSnapshot> = Observable.defer {
    Observable.create<DataSnapshot> { subscriber ->
        addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                subscriber.onError(databaseError.toException())
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                subscriber.onNext(dataSnapshot)
                subscriber.onComplete()
            }
        })
    }
}.observeOn(scheduler)

fun <T> Task<T>.observeSuccessfulComplete(scheduler: Scheduler): Observable<T> = Observable.defer {
    Observable.create<T> { subscriber ->
        addOnCompleteListener {
            if (it.isSuccessful) {
                subscriber.onNext(it.result)
                subscriber.onComplete()
            } else {
                subscriber.onError(it.exception!!)
            }
        }
    }
}.observeOn(scheduler)

fun FirebaseAuth.observeSingleCurrentFirebaseUser(scheduler: Scheduler): Maybe<FirebaseUser> = Maybe.defer {
    Maybe.create<FirebaseUser> { subscriber ->
        val authStateListener = object : FirebaseAuth.AuthStateListener {
            override fun onAuthStateChanged(firebaseAuth: FirebaseAuth) {
                firebaseAuth.removeAuthStateListener(this)
                val user = firebaseAuth.currentUser
                if (user != null) subscriber.onSuccess(user)
                else subscriber.onComplete()
            }
        }
        addAuthStateListener(authStateListener)
    }
}.observeOn(scheduler)

fun FirebaseUser.getAppUserDatabaseReference(): DatabaseReference = getFirebaseDatabase()
        .getReference("users")
        .child(uid)

