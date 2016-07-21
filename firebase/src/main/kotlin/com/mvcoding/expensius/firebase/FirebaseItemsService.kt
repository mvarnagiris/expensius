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
import com.google.firebase.database.Query
import com.mvcoding.expensius.service.AddedItems
import com.mvcoding.expensius.service.ChangedItems
import com.mvcoding.expensius.service.ItemsService
import com.mvcoding.expensius.service.MovedItem
import com.mvcoding.expensius.service.RemovedItems
import rx.Observable
import rx.Observable.Transformer
import rx.lang.kotlin.BehaviorSubject
import rx.lang.kotlin.toSingletonObservable
import java.util.concurrent.atomic.AtomicReference

class FirebaseItemsService<ITEM>(
        queries: Observable<Query>,
        private val transformer: Transformer<List<DataSnapshot>, List<ITEM>>) : ItemsService<ITEM> {

    private val oldFirebaseList = AtomicReference<FirebaseList>()
    private val firebaseListSubject = BehaviorSubject<FirebaseList>()

    init {
        queries.map { FirebaseList(it) }.subscribe { closeOldListAndEmitNewList(it) }
    }

    override fun items(): Observable<List<ITEM>> = firebaseListSubject.flatMap { it.items() }.compose(transformer)

    override fun addedItems(): Observable<AddedItems<ITEM>> = firebaseListSubject.flatMap { it.addedItems() }.flatMap { addedItems ->
        addedItems.items.toSingletonObservable().compose(transformer).map { AddedItems(addedItems.position, it) }
    }

    override fun changedItems(): Observable<ChangedItems<ITEM>> = firebaseListSubject.flatMap { it.changedItems() }.flatMap { changedItems ->
        changedItems.items.toSingletonObservable().compose(transformer).map { ChangedItems(changedItems.position, it) }
    }

    override fun removedItems(): Observable<RemovedItems<ITEM>> = firebaseListSubject.flatMap { it.removedItems() }.flatMap { removedItems ->
        removedItems.items.toSingletonObservable().compose(transformer).map { RemovedItems(removedItems.position, it) }
    }

    override fun movedItem(): Observable<MovedItem<ITEM>> = firebaseListSubject.flatMap { it.movedItem() }.flatMap { movedItem ->
        listOf(movedItem.item).toSingletonObservable().compose(transformer).map { MovedItem(movedItem.fromPosition, movedItem.toPosition, it.first()) }
    }

    private fun closeOldListAndEmitNewList(firebaseList: FirebaseList) {
        oldFirebaseList.getAndSet(firebaseList)?.close()
        firebaseListSubject.onNext(firebaseList)
    }
}