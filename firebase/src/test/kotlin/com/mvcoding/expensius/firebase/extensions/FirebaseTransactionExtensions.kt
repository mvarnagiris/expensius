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

package com.mvcoding.expensius.firebase.extensions

import com.mvcoding.expensius.aLongTimestamp
import com.mvcoding.expensius.aString
import com.mvcoding.expensius.aStringId
import com.mvcoding.expensius.anAmount
import com.mvcoding.expensius.firebase.model.FirebaseTransaction
import com.mvcoding.expensius.model.*

fun aFirebaseTransaction() = FirebaseTransaction(
        id = aStringId(),
        transactionType = aTransactionType().name,
        transactionState = aTransactionState().name,
        timestamp = aLongTimestamp(),
        timestampInverse = -aLongTimestamp(),
        amount = anAmount().toPlainString(),
        currency = aStringCurrencyCode(),
        tags = listOf(aTag(), aTag(), aTag()).map { it.tagId.id },
        note = aString("note"))

fun FirebaseTransaction.withId(id: String?) = copy(id = id)
fun FirebaseTransaction.withTransactionType(transactionType: TransactionType?) = copy(transactionType = transactionType?.name)
fun FirebaseTransaction.withTransactionType(transactionType: String) = copy(transactionType = transactionType)
fun FirebaseTransaction.withTransactionState(transactionState: TransactionState?) = copy(transactionState = transactionState?.name)
fun FirebaseTransaction.withTransactionState(transactionState: String) = copy(transactionState = transactionState)
fun FirebaseTransaction.withTimestamp(timestamp: Long?) = copy(timestamp = timestamp)
fun FirebaseTransaction.withTimestampInverse(timestampInverse: Long?) = copy(timestampInverse = timestampInverse)
fun FirebaseTransaction.withAmount(amount: String?) = copy(amount = amount)
fun FirebaseTransaction.withCurrency(currency: String?) = copy(currency = currency)
fun FirebaseTransaction.withTags(tags: Collection<Tag>?) = copy(tags = tags?.map { it.tagId.id })
fun FirebaseTransaction.withNote(note: String?) = copy(note = note)