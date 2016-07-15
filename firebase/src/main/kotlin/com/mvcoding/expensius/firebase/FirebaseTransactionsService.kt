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
import com.mvcoding.expensius.firebase.model.FirebaseTransaction
import com.mvcoding.expensius.model.Tag
import com.mvcoding.expensius.model.Transaction
import com.mvcoding.expensius.model.UserId
import com.mvcoding.expensius.service.AppUserService
import com.mvcoding.expensius.service.ItemMoved
import com.mvcoding.expensius.service.ItemsAdded
import com.mvcoding.expensius.service.ItemsChanged
import com.mvcoding.expensius.service.ItemsRemoved
import com.mvcoding.expensius.service.TagsService
import com.mvcoding.expensius.service.TransactionsService
import rx.Observable
import rx.Observable.combineLatest
import rx.lang.kotlin.BehaviorSubject
import java.util.concurrent.atomic.AtomicReference

class FirebaseTransactionsService(
        appUserService: AppUserService,
        archived: Boolean,
        tagsService: TagsService,
        archivedTagsService: TagsService) : TransactionsService {

    private val allTags = combineLatest(
            tagsService.items(),
            archivedTagsService.items(),
            { tags, archivedTags -> tags.union(archivedTags).associateBy { it.tagId.id } })
            .replay(1)
            .autoConnect()

    private val transformation: (DataSnapshot) -> DataSnapshot = { it }
    private val oldFirebaseList = AtomicReference<FirebaseList<DataSnapshot>>()
    private val firebaseListSubject = BehaviorSubject<FirebaseList<DataSnapshot>>()

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

    override fun items(): Observable<List<Transaction>> = firebaseListSubject
            .flatMap { it.items() }
            .withLatestFrom(allTags) { firebaseItems, items -> firebaseItems.toTransactions(items) }

    override fun addedItems(): Observable<ItemsAdded<Transaction>> = firebaseListSubject
            .flatMap { it.addedItems().map { ItemsAdded(it.position, it.items) } }
            .withLatestFrom(allTags) { added, items -> ItemsAdded(added.position, added.items.toTransactions(items)) }

    override fun changedItems(): Observable<ItemsChanged<Transaction>> = firebaseListSubject
            .flatMap { it.changedItems().map { ItemsChanged(it.position, it.items) } }
            .withLatestFrom(allTags) { changed, items -> ItemsChanged(changed.position, changed.items.toTransactions(items)) }

    override fun removedItems(): Observable<ItemsRemoved<Transaction>> = firebaseListSubject
            .flatMap { it.removedItems().map { ItemsRemoved(it.position, it.items) } }
            .withLatestFrom(allTags) { removed, transactions -> ItemsRemoved(removed.position, removed.items.toTransactions(transactions)) }

    override fun movedItems(): Observable<ItemMoved<Transaction>> = firebaseListSubject
            .flatMap { it.movedItems().map { ItemMoved(it.fromPosition, it.toPosition, it.item) } }
            .withLatestFrom(allTags) { moved, tags -> ItemMoved(moved.fromPosition, moved.toPosition, moved.item.toTransaction(tags)) }

    private fun List<DataSnapshot>.toTransactions(tagsCache: Map<String, Tag>) = map { it.toTransaction(tagsCache) }
    private fun DataSnapshot.toTransaction(tagsCache: Map<String, Tag>) = getValue(FirebaseTransaction::class.java).toTransaction(tagsCache)
    private fun query(userId: UserId, archived: Boolean) =
            (if (archived) archivedTransactionsDatabaseReference(userId) else transactionsDatabaseReference(userId)).orderByChild("timestampInverse")
}