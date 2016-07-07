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
import com.mvcoding.expensius.model.ModelState.NONE
import com.mvcoding.expensius.model.Tag
import com.mvcoding.expensius.service.AppUserService
import com.mvcoding.expensius.service.TagsWriteService
import rx.Observable
import rx.lang.kotlin.deferredObservable
import rx.lang.kotlin.observable

class FirebaseTagsWriteService(private val appUserService: AppUserService) : TagsWriteService {

    override fun createTags(createTags: Set<CreateTag>): Observable<List<Tag>> = deferredObservable {
        observable<List<Tag>> { subscriber ->
            val tagsReference = tagsDatabaseReference(appUserService.getCurrentAppUser().userId)
            val createdTags = createTags.map {
                val newTagReference = tagsReference.push()
                val firebaseTag = it.toFirebaseTag(newTagReference.key)
                newTagReference.setValue(firebaseTag)
                firebaseTag.toTag()
            }

            subscriber.onNext(createdTags)
            subscriber.onCompleted()
        }
    }

    private fun CreateTag.toFirebaseTag(id: String) = FirebaseTag(id, NONE.name, title.text, color.rgb, order.value)
}