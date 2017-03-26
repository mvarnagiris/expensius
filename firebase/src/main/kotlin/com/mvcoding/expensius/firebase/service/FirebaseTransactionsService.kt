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

import com.google.firebase.database.FirebaseDatabase
import com.mvcoding.expensius.data.DataSource
import com.mvcoding.expensius.data.RealtimeData
import com.mvcoding.expensius.data.RealtimeList
import com.mvcoding.expensius.data.RealtimeListDataSource
import com.mvcoding.expensius.model.ModelState
import com.mvcoding.expensius.model.Tag
import com.mvcoding.expensius.model.Transaction
import com.mvcoding.expensius.model.UserId

class FirebaseTransactionsService {

    private val REF_TRANSACTIONS = "transactions"
    private val REF_ARCHIVED_TRANSACTIONS = "archivedTransactions"

    fun getTransactions(
            userId: UserId,
            tagsRealtimeListDataSource: DataSource<RealtimeData<Tag>>,
            archivedTagsRealtimeListDataSource: DataSource<RealtimeData<Tag>>): RealtimeList<Transaction> =
            TransactionsRealtimeList(
                    userId.transactionsReference().orderByChild("timestampInverse"),
                    ModelState.NONE,
                    tagsRealtimeListDataSource,
                    archivedTagsRealtimeListDataSource)

    fun getArchivedTransactions(
            userId: UserId,
            tagsRealtimeListDataSource: RealtimeListDataSource<Tag>,
            archivedTagsRealtimeListDataSource: RealtimeListDataSource<Tag>): RealtimeList<Transaction> =
            TransactionsRealtimeList(
                    userId.archivedTransactionsReference().orderByChild("timestampInverse"),
                    ModelState.ARCHIVED,
                    tagsRealtimeListDataSource,
                    archivedTagsRealtimeListDataSource)

    private fun UserId.transactionsReference() = FirebaseDatabase.getInstance().getReference(REF_TRANSACTIONS).child(this.id)
    private fun UserId.archivedTransactionsReference() = FirebaseDatabase.getInstance().getReference(REF_ARCHIVED_TRANSACTIONS).child(this.id)
}