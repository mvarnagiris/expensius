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

import com.google.firebase.database.DataSnapshot
import com.mvcoding.expensius.firebase.model.FirebaseTag
import com.mvcoding.expensius.model.Tag
import com.mvcoding.expensius.model.UserId
import com.mvcoding.expensius.service.AppUserService
import com.mvcoding.expensius.service.ItemMoved
import com.mvcoding.expensius.service.ItemsAdded
import com.mvcoding.expensius.service.ItemsChanged
import com.mvcoding.expensius.service.ItemsRemoved
import com.mvcoding.expensius.service.TagsService
import rx.Observable
import rx.lang.kotlin.BehaviorSubject
import java.util.concurrent.atomic.AtomicReference

class FirebaseTagsService(appUserService: AppUserService, archived: Boolean) : TagsService {

    private val transformation: (DataSnapshot) -> Tag = { it.getValue(FirebaseTag::class.java).toTag() }
    private val oldFirebaseList = AtomicReference<FirebaseList<Tag>>()
    private val firebaseListSubject = BehaviorSubject<FirebaseList<Tag>>()

    init {
        appUserService.appUser()
                .map { it.userId }
                .distinctUntilChanged()
                .map { query(it, archived) }
                .map { FirebaseList(it, transformation) }
                .subscribe {
                    oldFirebaseList.getAndSet(it)?.close()
                    firebaseListSubject.onNext(it)
                }
    }

    override fun items(): Observable<List<Tag>> = firebaseListSubject.flatMap { it.items() }
    override fun addedItems(): Observable<ItemsAdded<Tag>> = firebaseListSubject.flatMap { it.addedItems().map { ItemsAdded(it.position, it.items) } }
    override fun changedItems(): Observable<ItemsChanged<Tag>> = firebaseListSubject.flatMap { it.changedItems().map { ItemsChanged(it.position, it.items) } }
    override fun removedItems(): Observable<ItemsRemoved<Tag>> = firebaseListSubject.flatMap { it.removedItems().map { ItemsRemoved(it.position, it.items) } }
    override fun movedItems(): Observable<ItemMoved<Tag>> = firebaseListSubject.flatMap {
        it.movedItems().map { ItemMoved(it.fromPosition, it.toPosition, it.item) }
    }

    private fun query(userId: UserId, archived: Boolean) = (if (archived) archivedTagsDatabaseReference(userId) else tagsDatabaseReference(userId))
            .orderByChild("order")
}