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

class FirebaseRealtimeList(private val query: Query) : ValueEventListener, ChildEventListener {


    override fun onDataChange(dataSnapshot: DataSnapshot) {
    }

    override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildKey: String?) {
    }

    override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildKey: String?) {
    }

    override fun onChildRemoved(dataSnapshot: DataSnapshot) {
    }

    override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildKey: String?) {
    }

    override fun onCancelled(databaseError: DatabaseError) {
    }
}