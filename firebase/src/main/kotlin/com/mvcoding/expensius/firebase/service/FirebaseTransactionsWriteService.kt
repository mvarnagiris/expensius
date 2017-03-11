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

//class FirebaseTransactionsWriteService(private val appUserService: AppUserService) : TransactionsWriteService {
//
//    override fun createTransactions(createTransactions: Set<CreateTransaction>): Observable<Unit> = appUserService.appUser()
//            .first()
//            .map {
//                val transactionsReference = transactionsDatabaseReference(it.userId)
//                createTransactions.forEach {
//                    val newTransactionReference = transactionsReference.push()
//                    val firebaseTransaction = it.toFirebaseTransaction(newTransactionReference.key)
//                    newTransactionReference.setValue(firebaseTransaction)
//                }
//            }
//
//    override fun saveTransactions(updateTransactions: Set<Transaction>): Observable<Unit> = appUserService.appUser()
//            .first()
//            .map {
//                val transactions = updateTransactions.associateBy({ it.transactionId.id }, { if (it.modelState == NONE) it.toMap() else null })
//                val archivedTransactions = updateTransactions.associateBy({ it.transactionId.id }, { if (it.modelState == ARCHIVED) it.toMap() else null })
//
//                val appUserId = it.userId
//                if (transactions.isNotEmpty()) transactionsDatabaseReference(appUserId).updateChildren(transactions)
//                if (archivedTransactions.isNotEmpty()) archivedTransactionsDatabaseReference(appUserId).updateChildren(archivedTransactions)
//            }
//
//    private fun CreateTransaction.toFirebaseTransaction(id: String) = FirebaseTransaction(
//            id,
//            transactionType.name,
//            transactionState.name,
//            timestamp.millis,
//            -timestamp.millis,
//            money.amount.toPlainString(),
//            money.currency.code,
//            tags.map { it.tagId.id },
//            note.text)
//
//    private fun Transaction.toMap() = mapOf(
//            "id" to transactionId.id,
//            "transactionType" to transactionType.name,
//            "transactionState" to transactionState.name,
//            "timestamp" to timestamp.millis,
//            "timestampInverse" to -timestamp.millis,
//            "amount" to money.amount.toPlainString(),
//            "currency" to money.currency.code,
//            "tags" to tags.map { it.tagId.id },
//            "note" to note.text)
//}