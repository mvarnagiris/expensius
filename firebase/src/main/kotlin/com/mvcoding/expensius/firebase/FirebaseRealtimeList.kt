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

import com.google.firebase.database.*
import com.mvcoding.expensius.data.RawRealtimeData.*
import com.mvcoding.expensius.data.RealtimeList
import rx.Observable
import rx.lang.kotlin.PublishSubject

class FirebaseRealtimeList<ITEM>(
        private val query: Query,
        private val converter: (DataSnapshot) -> ITEM) : RealtimeList<ITEM> {

    private val allItemsSubject = PublishSubject<AllItems<ITEM>>()
    private val addedItemsSubject = PublishSubject<AddedItems<ITEM>>()
    private val changedItemsSubject = PublishSubject<ChangedItems<ITEM>>()
    private val removedItemsSubject = PublishSubject<RemovedItems<ITEM>>()
    private val movedItemSubject = PublishSubject<MovedItems<ITEM>>()

    private val valueEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            allItemsSubject.onNext(dataSnapshot.children.map { converter(it) }.let { AllItems(it) })
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // TODO: Implement
        }
    }

    private val childEventListener = object : ChildEventListener {
        override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildKey: String?) {
            addedItemsSubject.onNext(converter(dataSnapshot).let { AddedItems(listOf(it), previousChildKey) })
        }

        override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildKey: String?) {
            changedItemsSubject.onNext(converter(dataSnapshot).let { ChangedItems(listOf(it)) })
        }

        override fun onChildRemoved(dataSnapshot: DataSnapshot) {
            removedItemsSubject.onNext(converter(dataSnapshot).let { RemovedItems(listOf(it)) })
        }

        override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildKey: String?) {
            movedItemSubject.onNext(converter(dataSnapshot).let { MovedItems(listOf(it), previousChildKey) })
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