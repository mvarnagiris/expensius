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

import com.mvcoding.expensius.firebase.archivedTagsDatabaseReference
import com.mvcoding.expensius.firebase.model.FirebaseTag
import com.mvcoding.expensius.firebase.tagsDatabaseReference
import com.mvcoding.expensius.model.CreateTag
import com.mvcoding.expensius.model.ModelState.ARCHIVED
import com.mvcoding.expensius.model.ModelState.NONE
import com.mvcoding.expensius.model.Tag
import com.mvcoding.expensius.service.AppUserService
import com.mvcoding.expensius.service.TagsWriteService
import rx.Observable

class FirebaseTagsWriteService(private val appUserService: AppUserService) : TagsWriteService {

    override fun createTags(createTags: Set<CreateTag>): Observable<Unit> = appUserService.appUser()
            .first()
            .map {
                val tagsReference = tagsDatabaseReference(it.userId)
                createTags.forEach {
                    val newTagReference = tagsReference.push()
                    val firebaseTag = it.toFirebaseTag(newTagReference.key)
                    newTagReference.setValue(firebaseTag)
                }
            }

    override fun saveTags(updateTags: Set<Tag>): Observable<Unit> = appUserService.appUser()
            .first()
            .map {
                val tagsToUpdate = updateTags.associateBy({ it.tagId.id }, { if (it.modelState == NONE) it.toMap() else null })
                val archivedTagsToUpdate = updateTags.associateBy({ it.tagId.id }, { if (it.modelState == ARCHIVED) it.toMap() else null })

                val appUserId = it.userId
                if (tagsToUpdate.isNotEmpty()) tagsDatabaseReference(appUserId).updateChildren(tagsToUpdate)
                if (archivedTagsToUpdate.isNotEmpty()) archivedTagsDatabaseReference(appUserId).updateChildren(archivedTagsToUpdate)
            }

    private fun CreateTag.toFirebaseTag(id: String) = FirebaseTag(id, title.text, color.rgb, order.value)
    private fun Tag.toMap() = mapOf(
            "id" to tagId.id,
            "title" to title.text,
            "build/intermediates/exploded-aar/com.google.android.gms/play-services-base/9.2.0/res/color" to color.rgb,
            "order" to order.value)
}