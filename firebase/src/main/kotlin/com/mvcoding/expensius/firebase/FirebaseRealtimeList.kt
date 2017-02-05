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

package com.mvcoding.expensius.firebase

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.mvcoding.expensius.datasource.RealtimeData.AddedItems
import com.mvcoding.expensius.datasource.RealtimeData.AllItems
import com.mvcoding.expensius.datasource.RealtimeData.ChangedItems
import com.mvcoding.expensius.datasource.RealtimeData.MovedItems
import com.mvcoding.expensius.datasource.RealtimeData.RemovedItems
import com.mvcoding.expensius.datasource.RealtimeList
import rx.Observable
import rx.lang.kotlin.PublishSubject
import java.io.Closeable

class FirebaseRealtimeList<ITEM>(private val query: Query) : RealtimeList<ITEM>, Closeable {

    private val allItemsSubject = PublishSubject<AllItems<ITEM>>()
    private val addedItemsSubject = PublishSubject<AddedItems<ITEM>>()
    private val changedItemsSubject = PublishSubject<ChangedItems<ITEM>>()
    private val removedItemsSubject = PublishSubject<RemovedItems<ITEM>>()
    private val movedItemSubject = PublishSubject<MovedItems<ITEM>>()
    private val keys = arrayListOf<String>()

    private val valueEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // TODO: Implement
        }
    }

    private val childEventListener = object : ChildEventListener {
        override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildKey: String?) {
        }

        override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildKey: String?) {
        }

        override fun onChildRemoved(dataSnapshot: DataSnapshot) {
        }

        override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildKey: String?) {
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // TODO: Implement
        }
    }

    init {
        query.addChildEventListener(childEventListener)
        query.addListenerForSingleValueEvent(valueEventListener)
    }

    override fun getAllItems(): Observable<AllItems<ITEM>> = allItemsSubject
    override fun getAddedItems(): Observable<AddedItems<ITEM>> = addedItemsSubject
    override fun getChangedItems(): Observable<ChangedItems<ITEM>> = changedItemsSubject
    override fun getRemovedItems(): Observable<RemovedItems<ITEM>> = removedItemsSubject
    override fun getMovedItem(): Observable<MovedItems<ITEM>> = movedItemSubject
    override fun close(): Unit = query.removeEventListener(childEventListener)
}