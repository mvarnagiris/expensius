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

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.mvcoding.expensius.firebase.FirebaseItemsService
import com.mvcoding.expensius.firebase.model.FirebaseTag
import com.mvcoding.expensius.model.ModelState.NONE
import com.mvcoding.expensius.model.Tag
import com.mvcoding.expensius.model.UserId
import com.mvcoding.expensius.service.AddedItems
import com.mvcoding.expensius.service.AppUserService
import com.mvcoding.expensius.service.ChangedItems
import com.mvcoding.expensius.service.MovedItem
import com.mvcoding.expensius.service.RemovedItems
import com.mvcoding.expensius.service.TagsService
import rx.Observable
import rx.Observable.Transformer

class FirebaseTagsService(appUserService: AppUserService, private val databaseReference: (UserId) -> DatabaseReference) : TagsService {

    private val firebaseItemsService = FirebaseItemsService(queries(appUserService), transformer())

    override fun items(): Observable<List<Tag>> = firebaseItemsService.items()
    override fun addedItems(): Observable<AddedItems<Tag>> = firebaseItemsService.addedItems()
    override fun changedItems(): Observable<ChangedItems<Tag>> = firebaseItemsService.changedItems()
    override fun removedItems(): Observable<RemovedItems<Tag>> = firebaseItemsService.removedItems()
    override fun movedItem(): Observable<MovedItem<Tag>> = firebaseItemsService.movedItem()

    private fun queries(appUserService: AppUserService) = appUserService.appUser().map { it.userId }.distinctUntilChanged().map { query(it) }
    private fun query(userId: UserId) = databaseReference(userId).orderByChild("order")
    private fun transformer(): Transformer<List<DataSnapshot>, List<Tag>> = Transformer {
        it.map { dataSnapshots -> dataSnapshots.map { dataSnapshot -> dataSnapshot.getValue(FirebaseTag::class.java).toTag(NONE) } }
    }
}