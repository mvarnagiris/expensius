/*
 * Copyright (C) 2018 Mantas Varnagiris.
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

package com.mvcoding.expensius.model

import java.io.Serializable

data class LocalFilter(
        val transactionType: TransactionType?,
        val transactionState: TransactionState?,
        val tags: Set<Tag>,
        val note: Note?) : Serializable {

    fun withTransactionType(transactionType: TransactionType?) = copy(transactionType = transactionType)
    fun withTransactionState(transactionState: TransactionState?) = copy(transactionState = transactionState)
    fun withTags(tags: Set<Tag>) = copy(tags = tags)
    fun withNote(note: Note) = copy(note = note)

    fun filter(transactions: List<Transaction>): List<Transaction> = transactions.filter { transaction ->
        transactionType?.let { transaction.transactionType == it } ?: true
                && transactionState?.let { transaction.transactionState == it } ?: true
                && (tags.isEmpty() || transaction.tags.containsAll(tags))
                && note?.let { transaction.note.text.contains(note.text, ignoreCase = true) } ?: true
    }
}