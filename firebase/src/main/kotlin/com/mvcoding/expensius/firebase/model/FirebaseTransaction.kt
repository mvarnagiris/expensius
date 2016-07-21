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

package com.mvcoding.expensius.firebase.model

import com.mvcoding.expensius.model.ModelState
import com.mvcoding.expensius.model.Note
import com.mvcoding.expensius.model.NullModels.noMoney
import com.mvcoding.expensius.model.NullModels.noNote
import com.mvcoding.expensius.model.NullModels.noTimestamp
import com.mvcoding.expensius.model.NullModels.noTransactionId
import com.mvcoding.expensius.model.Tag
import com.mvcoding.expensius.model.Timestamp
import com.mvcoding.expensius.model.Transaction
import com.mvcoding.expensius.model.TransactionId
import com.mvcoding.expensius.model.TransactionState
import com.mvcoding.expensius.model.TransactionState.PENDING
import com.mvcoding.expensius.model.TransactionType
import com.mvcoding.expensius.model.TransactionType.EXPENSE

data class FirebaseTransaction(
        val id: String? = null,
        val transactionType: String? = null,
        val transactionState: String? = null,
        val timestamp: Long? = 0,
        val timestampInverse: Long? = 0,
        val money: FirebaseMoney? = null,
        val tags: List<String>? = null,
        val note: String? = null) {

    fun toTransaction(modelState: ModelState, tagsCache: Map<String, Tag>) = Transaction(
            id?.let { TransactionId(it) } ?: noTransactionId,
            modelState,
            transactionType?.let { TransactionType.valueOf(it) } ?: EXPENSE,
            transactionState?.let { TransactionState.valueOf(it) } ?: PENDING,
            timestamp?.let { Timestamp(it) } ?: noTimestamp,
            money?.let { it.toMoney() } ?: noMoney,
            (tags ?: emptyList()).filter { tagsCache.containsKey(it) }.map { tagsCache[it] }.filterNotNull().toSet(),
            note?.let { Note(it) } ?: noNote
    )
}