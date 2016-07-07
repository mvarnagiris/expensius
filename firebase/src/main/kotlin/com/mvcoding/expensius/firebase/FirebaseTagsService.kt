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
import com.mvcoding.expensius.model.Tag
import com.mvcoding.expensius.service.AppUserService
import com.mvcoding.expensius.service.ItemMoved
import com.mvcoding.expensius.service.ItemsAdded
import com.mvcoding.expensius.service.ItemsChanged
import com.mvcoding.expensius.service.ItemsRemoved
import com.mvcoding.expensius.service.TagsService
import rx.Observable

class FirebaseTagsService(appUserService: AppUserService, archived: Boolean) : TagsService {

    private val firebaseList = FirebaseList(
            if (archived) archivedTagsDatabaseReference(appUserService.getCurrentAppUser().userId).orderByChild("order")
            else tagsDatabaseReference(appUserService.getCurrentAppUser().userId).orderByChild("order")) {
        it.getValue(FirebaseTag::class.java).toTag()
    }

    override fun close(): Unit = firebaseList.close()
    override fun items(): Observable<List<Tag>> = firebaseList.items()
    override fun addedItems(): Observable<ItemsAdded<Tag>> = firebaseList.addedItems().map { ItemsAdded(it.position, it.items) }
    override fun changedItems(): Observable<ItemsChanged<Tag>> = firebaseList.changedItems().map { ItemsChanged(it.position, it.items) }
    override fun removedItems(): Observable<ItemsRemoved<Tag>> = firebaseList.removedItems().map { ItemsRemoved(it.position, it.items) }
    override fun movedItems(): Observable<ItemMoved<Tag>> = firebaseList.movedItems().map {
        ItemMoved(it.fromPosition, it.toPosition, it.item)
    }
}