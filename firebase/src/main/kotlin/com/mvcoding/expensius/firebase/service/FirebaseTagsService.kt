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

import com.google.firebase.database.FirebaseDatabase
import com.mvcoding.expensius.data.RealtimeList
import com.mvcoding.expensius.firebase.FirebaseRealtimeList
import com.mvcoding.expensius.firebase.model.FirebaseTag
import com.mvcoding.expensius.model.CreateTag
import com.mvcoding.expensius.model.ModelState.NONE
import com.mvcoding.expensius.model.Tag
import com.mvcoding.expensius.model.UserId

class FirebaseTagsService {
    fun getTags(userId: UserId): RealtimeList<Tag> = FirebaseRealtimeList(userId.tagsReference(), { it.getValue(FirebaseTag::class.java).toTag(NONE) })

    fun createTags(userId: UserId, createTags: Set<CreateTag>) {
        val tagsReference = userId.tagsReference()
        createTags.forEach {
            val newTagReference = tagsReference.push()
            val firebaseTag = it.toFirebaseTag(newTagReference.key)
            newTagReference.setValue(firebaseTag)
        }
    }

    private fun UserId.tagsReference() = FirebaseDatabase.getInstance().getReference("tags").child(this.id)
    private fun CreateTag.toFirebaseTag(id: String) = FirebaseTag(id, title.text, color.rgb, order.value)
}