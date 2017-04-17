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

package com.mvcoding.expensius.firebase.service

import com.google.firebase.database.DataSnapshot
import com.mvcoding.expensius.data.RealtimeList
import com.mvcoding.expensius.firebase.FirebaseRealtimeList
import com.mvcoding.expensius.firebase.extensions.getFirebaseDatabase
import com.mvcoding.expensius.firebase.model.FirebaseTag
import com.mvcoding.expensius.firebase.model.toFirebaseMap
import com.mvcoding.expensius.firebase.model.toFirebaseTag
import com.mvcoding.expensius.model.CreateTag
import com.mvcoding.expensius.model.ModelState
import com.mvcoding.expensius.model.ModelState.ARCHIVED
import com.mvcoding.expensius.model.ModelState.NONE
import com.mvcoding.expensius.model.Tag
import com.mvcoding.expensius.model.UserId

class FirebaseTagsService {

    private val REF_TAGS = "tags"
    private val REF_ARCHIVED_TAGS = "archivedTags"

    fun getTags(userId: UserId): RealtimeList<Tag> = FirebaseRealtimeList(userId.tagsReference().orderByChild("order"), { it.toTag(NONE) })
    fun getArchivedTags(userId: UserId): RealtimeList<Tag> = FirebaseRealtimeList(userId.archivedTagsReference().orderByChild("order"), { it.toTag(ARCHIVED) })

    fun createTags(userId: UserId, createTags: Set<CreateTag>) {
        val tagsReference = userId.tagsReference()
        createTags.forEach {
            val newTagReference = tagsReference.push()
            val firebaseTag = it.toFirebaseTag(newTagReference.key)
            newTagReference.setValue(firebaseTag)
        }
    }

    fun updateTags(userId: UserId, updateTags: Set<Tag>) {
        val tagsToUpdate = updateTags.associateBy({ it.tagId.id }, { if (it.modelState == NONE) it.toFirebaseMap() else null })
        val archivedTagsToUpdate = updateTags.associateBy({ it.tagId.id }, { if (it.modelState == ARCHIVED) it.toFirebaseMap() else null })

        if (tagsToUpdate.isNotEmpty()) userId.tagsReference().updateChildren(tagsToUpdate)
        if (archivedTagsToUpdate.isNotEmpty()) userId.archivedTagsReference().updateChildren(archivedTagsToUpdate)
    }

    private fun UserId.tagsReference() = getFirebaseDatabase().getReference(REF_TAGS).child(this.id)
    private fun UserId.archivedTagsReference() = getFirebaseDatabase().getReference(REF_ARCHIVED_TAGS).child(this.id)
    private fun DataSnapshot.toTag(modelState: ModelState) = getValue(FirebaseTag::class.java).toTag(modelState)
}