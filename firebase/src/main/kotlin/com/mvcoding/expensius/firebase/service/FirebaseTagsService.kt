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

package com.mvcoding.expensius.firebase.service

class FirebaseTagsService {

//    private val REF_TAGS = "tags"
//    private val REF_ARCHIVED_TAGS = "archivedTags"
//
//    fun getTags(userId: UserId): RealtimeList<Tag> = FirebaseRealtimeList(userId.tagsReference().orderByChild("order"), { it.toTag(NONE) })
//    fun getArchivedTags(userId: UserId): RealtimeList<Tag> = FirebaseRealtimeList(userId.archivedTagsReference().orderByChild("order"), { it.toTag(ARCHIVED) })
//
//    fun createTags(userId: UserId, createTags: Set<CreateTag>) {
//        val tagsReference = userId.tagsReference()
//        createTags.forEach {
//            val newTagReference = tagsReference.push()
//            val firebaseTag = it.toFirebaseTag(newTagReference.key)
//            newTagReference.setValue(firebaseTag)
//        }
//    }
//
//    fun updateTags(userId: UserId, updateTags: Set<Tag>) {
//        val tagsToUpdate = updateTags.associateBy({ it.tagId.id }, { if (it.modelState == NONE) it.toFirebaseMap() else null })
//        val archivedTagsToUpdate = updateTags.associateBy({ it.tagId.id }, { if (it.modelState == ARCHIVED) it.toFirebaseMap() else null })
//
//        if (tagsToUpdate.isNotEmpty()) userId.tagsReference().updateChildren(tagsToUpdate)
//        if (archivedTagsToUpdate.isNotEmpty()) userId.archivedTagsReference().updateChildren(archivedTagsToUpdate)
//    }
//
//    private fun UserId.tagsReference() = getFirebaseDatabase().getReference(REF_TAGS).child(this.id)
//    private fun UserId.archivedTagsReference() = getFirebaseDatabase().getReference(REF_ARCHIVED_TAGS).child(this.id)
//    private fun DataSnapshot.toTag(modelState: ModelState) = getValue(FirebaseTag::class.java)!!.toTag(modelState)
}