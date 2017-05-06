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

import com.google.firebase.database.DataSnapshot
import com.mvcoding.expensius.data.RealtimeList
import com.mvcoding.expensius.firebase.FirebaseRealtimeList
import com.mvcoding.expensius.firebase.extensions.getFirebaseDatabase
import com.mvcoding.expensius.firebase.model.FirebaseTransaction
import com.mvcoding.expensius.firebase.model.toFirebaseMap
import com.mvcoding.expensius.firebase.model.toFirebaseTransaction
import com.mvcoding.expensius.model.*
import com.mvcoding.expensius.model.ModelState.ARCHIVED
import com.mvcoding.expensius.model.ModelState.NONE

class FirebaseTransactionsService {

    private val REF_TRANSACTIONS = "transactions"
    private val REF_ARCHIVED_TRANSACTIONS = "archivedTransactions"

    fun getTransactions(remoteFilter: RemoteFilter): RealtimeList<BasicTransaction> =
            FirebaseRealtimeList(remoteFilter.userId
                    .transactionsReference()
                    .orderByChild("timestampInverse")
                    .startAt(-remoteFilter.interval.endMillis.toDouble())
                    .endAt(-remoteFilter.interval.startMillis.toDouble()), { it.toTransaction(NONE) })

    fun getArchivedTransactions(remoteFilter: RemoteFilter): RealtimeList<BasicTransaction> =
            FirebaseRealtimeList(remoteFilter.userId
                    .archivedTransactionsReference()
                    .orderByChild("timestampInverse")
                    .startAt(-remoteFilter.interval.endMillis.toDouble())
                    .endAt(-remoteFilter.interval.startMillis.toDouble()), { it.toTransaction(ARCHIVED) })

    fun createTransactions(userId: UserId, createTransactions: Set<CreateTransaction>) {
        val transactionsReference = userId.transactionsReference()
        createTransactions.forEach {
            val newTransactionReference = transactionsReference.push()
            val firebaseTransaction = it.toFirebaseTransaction(newTransactionReference.key)
            newTransactionReference.setValue(firebaseTransaction)
        }
    }

    fun updateTransactions(userId: UserId, updateTransactions: Set<Transaction>) {
        val transactionsToUpdate = updateTransactions.associateBy({ it.transactionId.id }, { if (it.modelState == NONE) it.toFirebaseMap() else null })
        val archivedTransactionsToUpdate = updateTransactions.associateBy({ it.transactionId.id }, { if (it.modelState == ARCHIVED) it.toFirebaseMap() else null })

        if (transactionsToUpdate.isNotEmpty()) userId.transactionsReference().updateChildren(transactionsToUpdate)
        if (archivedTransactionsToUpdate.isNotEmpty()) userId.archivedTransactionsReference().updateChildren(archivedTransactionsToUpdate)
    }

    private fun UserId.transactionsReference() = getFirebaseDatabase().getReference(REF_TRANSACTIONS).child(this.id)
    private fun UserId.archivedTransactionsReference() = getFirebaseDatabase().getReference(REF_ARCHIVED_TRANSACTIONS).child(this.id)
    private fun DataSnapshot.toTransaction(modelState: ModelState) = getValue(FirebaseTransaction::class.java).toBasicTransaction(modelState)
}