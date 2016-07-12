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

class FirebaseList<ITEM>(
        private val query: Query,
        private val transformer: (DataSnapshot) -> ITEM) : ValueEventListener, ChildEventListener, Closeable {

    private val childrenDataSnapshots = arrayListOf<DataSnapshot>()
    private val itemsSubject = BehaviorSubject<List<ITEM>>()
    private val addedItemsSubject = PublishSubject<FirebaseItemsAdded<ITEM>>()
    private val changedItemsSubject = PublishSubject<FirebaseItemsChanged<ITEM>>()
    private val removedItemsSubject = PublishSubject<FirebaseItemsRemoved<ITEM>>()
    private val movedItemsSubject = PublishSubject<FirebaseItemMoved<ITEM>>()
    private var ignoreChildEvents = true

    init {
        query.addChildEventListener(this)
        query.addListenerForSingleValueEvent(this)
    }

    fun items(): Observable<List<ITEM>> = itemsSubject.onBackpressureBuffer()
    fun addedItems(): Observable<FirebaseItemsAdded<ITEM>> = addedItemsSubject.onBackpressureBuffer()
    fun changedItems(): Observable<FirebaseItemsChanged<ITEM>> = changedItemsSubject.onBackpressureBuffer()
    fun removedItems(): Observable<FirebaseItemsRemoved<ITEM>> = removedItemsSubject.onBackpressureBuffer()
    fun movedItems(): Observable<FirebaseItemMoved<ITEM>> = movedItemsSubject.onBackpressureBuffer()

    override fun onDataChange(dataSnapshot: DataSnapshot) {
        childrenDataSnapshots.clear()
        childrenDataSnapshots.addAll(dataSnapshot.children)
        itemsSubject.onNext(childrenDataSnapshots.map { it.let(transformer) })
        ignoreChildEvents = false
    }

    override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildKey: String?) {
        if (ignoreChildEvents) return
        val position = previousChildKey?.let { getPositionForKey(it) + 1 } ?: 0
        childrenDataSnapshots.add(position, dataSnapshot)
        val item = dataSnapshot.let(transformer)
        addedItemsSubject.onNext(FirebaseItemsAdded(position, listOf(item)))
        itemsSubject.onNext(childrenDataSnapshots.map { it.let(transformer) })
    }

    override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildKey: String?) {
        if (ignoreChildEvents) return
        val position = getPositionForKey(dataSnapshot.key)
        childrenDataSnapshots[position] = dataSnapshot
        val item = dataSnapshot.let(transformer)
        changedItemsSubject.onNext(FirebaseItemsChanged(position, listOf(item)))
        itemsSubject.onNext(childrenDataSnapshots.map { it.let(transformer) })
    }

    override fun onChildRemoved(dataSnapshot: DataSnapshot) {
        if (ignoreChildEvents) return
        val position = getPositionForKey(dataSnapshot.key)
        childrenDataSnapshots.removeAt(position)
        val item = dataSnapshot.let(transformer)
        removedItemsSubject.onNext(FirebaseItemsRemoved(position, listOf(item)))
        itemsSubject.onNext(childrenDataSnapshots.map { it.let(transformer) })
    }

    override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildKey: String?) {
        if (ignoreChildEvents) return
        val oldPosition = getPositionForKey(dataSnapshot.key)
        childrenDataSnapshots.removeAt(oldPosition)
        val newPosition = previousChildKey?.let { getPositionForKey(it) + 1 } ?: 0
        childrenDataSnapshots.add(newPosition, dataSnapshot)
        val item = dataSnapshot.let(transformer)
        movedItemsSubject.onNext(FirebaseItemMoved(oldPosition, newPosition, item))
        itemsSubject.onNext(childrenDataSnapshots.map { it.let(transformer) })
    }

    override fun onCancelled(databaseError: DatabaseError) {
        Log.e("Firebase", databaseError.toString())
        itemsSubject.onNext(emptyList())
    }

    override fun close() = query.removeEventListener(this as ChildEventListener)

    private fun getPositionForKey(key: String): Int = childrenDataSnapshots.indexOfFirst { it.key == key }

    data class FirebaseItemsAdded<out ITEM>(val position: Int, val items: List<ITEM>)
    data class FirebaseItemsChanged<out ITEM>(val position: Int, val items: List<ITEM>)
    data class FirebaseItemsRemoved<out ITEM>(val position: Int, val items: List<ITEM>)
    data class FirebaseItemMoved<out ITEM>(val fromPosition: Int, val toPosition: Int, val item: ITEM)
}