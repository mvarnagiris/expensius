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

package com.mvcoding.expensius.firebase.model

import com.mvcoding.expensius.model.*
import com.mvcoding.expensius.model.NullModels.noNote
import com.mvcoding.expensius.model.NullModels.noTimestamp
import com.mvcoding.expensius.model.NullModels.noTransaction
import java.math.BigDecimal

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

    fun toTransaction(modelState: ModelState): Transaction {
        if (id.isNullOrBlank()) return noTransaction
        return Transaction(
                TransactionId(id!!),
                modelState,
                transactionType(),
                transactionState(),
                timestamp?.let(::Timestamp) ?: noTimestamp,
                Money(amount(), currency?.let { if (it.isBlank()) defaultCurrency() else Currency(it) } ?: defaultCurrency()),
                tags.orEmpty().map(::TagId).toSet(),
                note?.trim()?.let(::Note) ?: noNote
        )
    }

    private fun amount() = try {
        BigDecimal(amount!!)
    } catch (e: Exception) {
        BigDecimal.ZERO
    }

    private fun transactionType() = try {
        TransactionType.valueOf(transactionType!!)
    } catch (e: Exception) {
        TransactionType.EXPENSE
    }

    private fun transactionState() = try {
        TransactionState.valueOf(transactionState!!)
    } catch (e: Exception) {
        TransactionState.PENDING
    }
}

internal fun CreateTransaction.toFirebaseTransaction(id: String) = FirebaseTransaction(
        id,
        transactionType.name,
        transactionState.name,
        timestamp.millis,
        -timestamp.millis,
        money.amount.toPlainString(),
        money.currency.code,
        tagIds.map { it.id },
        note.text)

internal fun Transaction.toFirebaseMap() = mapOf(
        "id" to transactionId.id,
        "transactionType" to transactionType.name,
        "transactionState" to transactionState.name,
        "timestamp" to timestamp.millis,
        "timestampInverse" to -timestamp.millis,
        "amount" to money.amount.toPlainString(),
        "currency" to money.currency.code,
        "tags" to tagIds.map { it.id },
        "note" to note.text)