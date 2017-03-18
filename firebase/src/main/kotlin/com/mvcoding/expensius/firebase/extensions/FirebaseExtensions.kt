package com.mvcoding.expensius.firebase.extensions

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import rx.Observable
import rx.Scheduler
import rx.lang.kotlin.deferredObservable
import rx.lang.kotlin.observable

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

fun FirebaseUser.getAppUserDatabaseReference(): DatabaseReference = FirebaseDatabase.getInstance()
        .getReference("users")
        .child(uid)