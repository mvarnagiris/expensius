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

import com.mvcoding.expensius.firebase.model.FirebaseTag
import com.mvcoding.expensius.model.CreateTag
import com.mvcoding.expensius.model.ModelState.ARCHIVED
import com.mvcoding.expensius.model.ModelState.NONE
import com.mvcoding.expensius.model.Tag
import com.mvcoding.expensius.service.AppUserService
import com.mvcoding.expensius.service.TagsWriteService
import rx.Observable
import rx.lang.kotlin.deferredObservable
import rx.lang.kotlin.observable

class FirebaseTagsWriteService(private val appUserService: AppUserService) : TagsWriteService {

    override fun createTags(createTags: Set<CreateTag>): Observable<Unit> = deferredObservable {
        observable<Unit> { subscriber ->
            val tagsReference = tagsDatabaseReference(appUserService.getCurrentAppUser().userId)
            createTags.forEach {
                val newTagReference = tagsReference.push()
                val firebaseTag = it.toFirebaseTag(newTagReference.key)
                newTagReference.setValue(firebaseTag)
            }

            subscriber.onNext(Unit)
            subscriber.onCompleted()
        }
    }

    override fun updateTags(updateTags: Set<Tag>): Observable<Unit> = deferredObservable {
        observable<Unit> { subscriber ->
            val tagsToUpdate = updateTags.filter { it.modelState == NONE }.associateBy({ it.tagId.id }, { it.toMap() })
            val archivedTagsToUpdate = updateTags.filter { it.modelState == ARCHIVED }.associateBy({ it.tagId.id }, { it.toMap() })

            val appUserId = appUserService.getCurrentAppUser().userId
            if (tagsToUpdate.isNotEmpty()) tagsDatabaseReference(appUserId).updateChildren(tagsToUpdate)
            if (archivedTagsToUpdate.isNotEmpty()) archivedTagsDatabaseReference(appUserId).updateChildren(tagsToUpdate)

            subscriber.onNext(Unit)
            subscriber.onCompleted()
        }
    }

    private fun CreateTag.toFirebaseTag(id: String) = FirebaseTag(id, NONE.name, title.text, color.rgb, order.value)
    private fun Tag.toMap() = mapOf(
            "id" to tagId.id,
            "modelState" to modelState.name,
            "title" to title.text,
            "color" to color.rgb,
            "order" to order.value)
}