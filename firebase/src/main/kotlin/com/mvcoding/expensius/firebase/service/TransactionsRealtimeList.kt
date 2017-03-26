/*
 * Copyright (C) 2017 Mantas Varnagiris.
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

import com.google.firebase.database.Query
import com.mvcoding.expensius.data.DataSource
import com.mvcoding.expensius.data.RawRealtimeData
import com.mvcoding.expensius.data.RealtimeData
import com.mvcoding.expensius.data.RealtimeList
import com.mvcoding.expensius.firebase.FirebaseRealtimeList
import com.mvcoding.expensius.firebase.model.FirebaseTransaction
import com.mvcoding.expensius.model.ModelState
import com.mvcoding.expensius.model.Tag
import com.mvcoding.expensius.model.Transaction
import rx.Observable
import rx.Observable.combineLatest

internal class TransactionsRealtimeList(
        query: Query,
        private val modelState: ModelState,
        tagsRealtimeListDataSource: DataSource<RealtimeData<Tag>>,
        archivedTagsRealtimeListDataSource: DataSource<RealtimeData<Tag>>) : RealtimeList<Transaction> {

    private val firebaseTransactionsRealtimeList = FirebaseRealtimeList(query) { it.getValue(FirebaseTransaction::class.java) }
    private val tagsCache = combineLatest(
            tagsRealtimeListDataSource.data().map { it.allItems },
            archivedTagsRealtimeListDataSource.data().map { it.allItems }) { tags, archivedTags -> tags.plus(archivedTags) }
            .map { it.associateBy { it.tagId.id } }
            .share()

    override fun getAllItems(): Observable<RawRealtimeData.AllItems<Transaction>> =
            combineLatest(firebaseTransactionsRealtimeList.getAllItems(), tagsCache) { firebaseTransactions, tagsCache ->
                RawRealtimeData.AllItems(firebaseTransactions.items.map { it.toTransaction(modelState, tagsCache) })
            }

    override fun getAddedItems(): Observable<RawRealtimeData.AddedItems<Transaction>> =
            combineLatest(firebaseTransactionsRealtimeList.getAddedItems(), tagsCache) { firebaseTransactions, tagsCache ->
                RawRealtimeData.AddedItems(firebaseTransactions.items.map { it.toTransaction(modelState, tagsCache) }, firebaseTransactions.previousKey)
            }

    override fun getChangedItems(): Observable<RawRealtimeData.ChangedItems<Transaction>> =
            combineLatest(firebaseTransactionsRealtimeList.getChangedItems(), tagsCache) { firebaseTransactions, tagsCache ->
                RawRealtimeData.ChangedItems(firebaseTransactions.items.map { it.toTransaction(modelState, tagsCache) })
            }

    override fun getRemovedItems(): Observable<RawRealtimeData.RemovedItems<Transaction>> =
            combineLatest(firebaseTransactionsRealtimeList.getRemovedItems(), tagsCache) { firebaseTransactions, tagsCache ->
                RawRealtimeData.RemovedItems(firebaseTransactions.items.map { it.toTransaction(modelState, tagsCache) })
            }

    override fun getMovedItem(): Observable<RawRealtimeData.MovedItems<Transaction>> =
            combineLatest(firebaseTransactionsRealtimeList.getMovedItem(), tagsCache) { firebaseTransactions, tagsCache ->
                RawRealtimeData.MovedItems(firebaseTransactions.items.map { it.toTransaction(modelState, tagsCache) }, firebaseTransactions.previousKey)
            }

    override fun close() {
        firebaseTransactionsRealtimeList.close()
    }
}