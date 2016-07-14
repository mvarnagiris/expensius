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
import com.mvcoding.expensius.service.AppUserService
import com.mvcoding.expensius.service.ItemMoved
import com.mvcoding.expensius.service.ItemsAdded
import com.mvcoding.expensius.service.ItemsChanged
import com.mvcoding.expensius.service.ItemsRemoved
import com.mvcoding.expensius.service.TagsService
import com.mvcoding.expensius.service.TransactionsService
import rx.Observable
import rx.Observable.combineLatest

class FirebaseTransactionsService(
        appUserService: AppUserService,
        archived: Boolean,
        limit: Int,
        private val tagsService: TagsService,
        private val archivedTagsService: TagsService) : TransactionsService {

    private val allTags = combineLatest(
            tagsService.items(),
            archivedTagsService.items(),
            { tags, archivedTags -> tags.union(archivedTags).associateBy { it.tagId.id } })
            .replay(1)
            .autoConnect()

    private val firebaseList by lazy {
        FirebaseList(
                (if (archived) archivedTransactionsDatabaseReference(appUserService.getCurrentAppUser().userId)
                else transactionsDatabaseReference(appUserService.getCurrentAppUser().userId))
                        .orderByChild("timestampInverse")
                        .apply { if (limit > 0) limitToLast(limit) }) { it }
    }

    override fun close() {
        firebaseList.close()
        tagsService.close()
        archivedTagsService.close()
    }

    override fun items(): Observable<List<Transaction>> = firebaseList.items().withLatestFrom(allTags) { firebaseTransactions, tags ->
        firebaseTransactions.toTransactions(tags)
    }

    override fun addedItems(): Observable<ItemsAdded<Transaction>> = firebaseList.addedItems().withLatestFrom(allTags) { firebaseItemsAdded, tags ->
        ItemsAdded(firebaseItemsAdded.position, firebaseItemsAdded.items.toTransactions(tags))
    }

    override fun changedItems(): Observable<ItemsChanged<Transaction>> = firebaseList.changedItems().withLatestFrom(allTags) { firebaseItemsChanged, tags ->
        ItemsChanged(firebaseItemsChanged.position, firebaseItemsChanged.items.toTransactions(tags))
    }

    override fun removedItems(): Observable<ItemsRemoved<Transaction>> = firebaseList.removedItems().withLatestFrom(allTags) { firebaseItemsRemoved, tags ->
        ItemsRemoved(firebaseItemsRemoved.position, firebaseItemsRemoved.items.toTransactions(tags))
    }

    override fun movedItems(): Observable<ItemMoved<Transaction>> = firebaseList.movedItems().withLatestFrom(allTags) { firebaseItemMoved, tags ->
        ItemMoved(firebaseItemMoved.fromPosition, firebaseItemMoved.toPosition, firebaseItemMoved.item.toTransaction(tags))
    }

    private fun List<DataSnapshot>.toTransactions(tagsCache: Map<String, Tag>) = map { it.toTransaction(tagsCache) }
    private fun DataSnapshot.toTransaction(tagsCache: Map<String, Tag>) = getValue(FirebaseTransaction::class.java).toTransaction(tagsCache)
}