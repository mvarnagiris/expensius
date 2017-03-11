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

data class FirebaseTransaction(
        val id: String? = null,
        val transactionType: String? = null,
        val transactionState: String? = null,
        val timestamp: Long? = 0,
        val timestampInverse: Long? = 0,
        val amount: String? = null,
        val currency: String? = null,
        val tags: List<String>? = null,
        val note: String? = null) {

//    fun toTransaction(modelState: ModelState, tagsCache: Map<String, Tag>) = Transaction(
//            id?.let { TransactionId(it) } ?: noTransactionId,
//            modelState,
//            transactionType?.let { TransactionType.valueOf(it) } ?: EXPENSE,
//            transactionState?.let { TransactionState.valueOf(it) } ?: PENDING,
//            timestamp?.let { Timestamp(it) } ?: noTimestamp,
//            Money(amount?.let { BigDecimal(it) } ?: BigDecimal.ZERO, currency?.let { Currency(it) } ?: defaultCurrency()),
//            (tags ?: emptyList()).filter { tagsCache.containsKey(it) }.map { tagsCache[it] }.filterNotNull().toSet(),
//            note?.let { Note(it) } ?: noNote
//    )
}