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

package com.mvcoding.expensius.feature.transaction

import com.mvcoding.expensius.data.DataSource
import com.mvcoding.expensius.data.ParameterRealtimeDataSource
import com.mvcoding.expensius.data.RealtimeData
import com.mvcoding.expensius.data.RealtimeData.*
import com.mvcoding.expensius.data.RealtimeList
import com.mvcoding.expensius.model.BasicTransaction
import com.mvcoding.expensius.model.RemoteFilter
import com.mvcoding.expensius.model.Tag
import com.mvcoding.expensius.model.Transaction
import rx.Observable
import rx.Observable.combineLatest
import java.io.Closeable

class TransactionsSource(
        private val allTagsSource: DataSource<List<Tag>>,
        remoteFilterSource: DataSource<RemoteFilter>,
        createRealtimeList: (RemoteFilter) -> RealtimeList<BasicTransaction>) : DataSource<RealtimeData<Transaction>>, Closeable {

    private val dataSource = ParameterRealtimeDataSource(remoteFilterSource, createRealtimeList) { it.transactionId.id }

    override fun data(): Observable<RealtimeData<Transaction>> = combineLatest(dataSource.data(), allTagsSource.data()) { basicTransactions, tags ->
        when (basicTransactions) {
            is AllItems -> AllItems(allItems(basicTransactions, tags))
            is AddedItems -> AddedItems(allItems(basicTransactions, tags), basicTransactions.addedItems.map { it.toTransaction(tags) }, basicTransactions.position)
            is ChangedItems -> ChangedItems(allItems(basicTransactions, tags), basicTransactions.changedItems.map { it.toTransaction(tags) }, basicTransactions.position)
            is RemovedItems -> RemovedItems(allItems(basicTransactions, tags), basicTransactions.removedItems.map { it.toTransaction(tags) }, basicTransactions.position)
            is MovedItems -> MovedItems(allItems(basicTransactions, tags), basicTransactions.movedItems.map { it.toTransaction(tags) }, basicTransactions.fromPosition, basicTransactions.toPosition)
        }
    }

    override fun close(): Unit = dataSource.close()

    private fun allItems(basicTransactions: RealtimeData<BasicTransaction>, tags: List<Tag>) = basicTransactions.allItems.map { it.toTransaction(tags) }
}