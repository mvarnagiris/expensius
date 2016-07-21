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

import android.util.Log
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import rx.Observable
import rx.lang.kotlin.BehaviorSubject
import rx.lang.kotlin.PublishSubject
import java.io.Closeable

class FirebaseList(private val query: Query) : ValueEventListener, ChildEventListener, Closeable {

    private val childrenDataSnapshots = arrayListOf<DataSnapshot>()
    private val itemsSubject = BehaviorSubject<List<DataSnapshot>>()
    private val addedItemsSubject = PublishSubject<FirebaseAddedItems>()
    private val changedItemsSubject = PublishSubject<FirebaseChangedItems>()
    private val removedItemsSubject = PublishSubject<FirebaseRemovedItems>()
    private val movedItemsSubject = PublishSubject<FirebaseMovedItem>()
    private var ignoreChildEvents = true

    init {
        query.addChildEventListener(this)
        query.addListenerForSingleValueEvent(this)
    }

    fun items(): Observable<List<DataSnapshot>> = itemsSubject.onBackpressureBuffer()
    fun addedItems(): Observable<FirebaseAddedItems> = addedItemsSubject.onBackpressureBuffer()
    fun changedItems(): Observable<FirebaseChangedItems> = changedItemsSubject.onBackpressureBuffer()
    fun removedItems(): Observable<FirebaseRemovedItems> = removedItemsSubject.onBackpressureBuffer()
    fun movedItem(): Observable<FirebaseMovedItem> = movedItemsSubject.onBackpressureBuffer()

    override fun onDataChange(dataSnapshot: DataSnapshot) {
        childrenDataSnapshots.clear()
        childrenDataSnapshots.addAll(dataSnapshot.children)
        notifyItemsChanged()
        ignoreChildEvents = false
    }

    override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildKey: String?) {
        if (ignoreChildEvents) return
        val position = previousChildKey?.let { getPositionForKey(it) + 1 } ?: 0
        childrenDataSnapshots.add(position, dataSnapshot)
        addedItemsSubject.onNext(FirebaseAddedItems(position, listOf(dataSnapshot)))
        notifyItemsChanged()
    }

    override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildKey: String?) {
        if (ignoreChildEvents) return
        val position = getPositionForKey(dataSnapshot.key)
        childrenDataSnapshots[position] = dataSnapshot
        changedItemsSubject.onNext(FirebaseChangedItems(position, listOf(dataSnapshot)))
        notifyItemsChanged()
    }

    override fun onChildRemoved(dataSnapshot: DataSnapshot) {
        if (ignoreChildEvents) return
        val position = getPositionForKey(dataSnapshot.key)
        childrenDataSnapshots.removeAt(position)
        removedItemsSubject.onNext(FirebaseRemovedItems(position, listOf(dataSnapshot)))
        notifyItemsChanged()
    }

    override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildKey: String?) {
        if (ignoreChildEvents) return
        val oldPosition = getPositionForKey(dataSnapshot.key)
        childrenDataSnapshots.removeAt(oldPosition)
        val newPosition = previousChildKey?.let { getPositionForKey(it) + 1 } ?: 0
        childrenDataSnapshots.add(newPosition, dataSnapshot)
        movedItemsSubject.onNext(FirebaseMovedItem(oldPosition, newPosition, dataSnapshot))
        notifyItemsChanged()
    }

    override fun onCancelled(databaseError: DatabaseError) {
        Log.e("Firebase", databaseError.toString())
        itemsSubject.onNext(emptyList())
    }

    override fun close(): Unit = query.removeEventListener(this as ChildEventListener)
    private fun getPositionForKey(key: String): Int = childrenDataSnapshots.indexOfFirst { it.key == key }
    private fun notifyItemsChanged(): Unit = itemsSubject.onNext(childrenDataSnapshots)

    data class FirebaseAddedItems(val position: Int, val items: List<DataSnapshot>)
    data class FirebaseChangedItems(val position: Int, val items: List<DataSnapshot>)
    data class FirebaseRemovedItems(val position: Int, val items: List<DataSnapshot>)
    data class FirebaseMovedItem(val fromPosition: Int, val toPosition: Int, val item: DataSnapshot)
}