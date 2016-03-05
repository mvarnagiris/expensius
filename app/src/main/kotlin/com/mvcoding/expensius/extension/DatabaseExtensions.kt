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

package com.mvcoding.expensius.extension

import android.content.ContentValues
import android.database.Cursor
import com.mvcoding.expensius.feature.transaction.TransactionState
import com.mvcoding.expensius.feature.transaction.TransactionType
import com.mvcoding.expensius.model.Currency
import com.mvcoding.expensius.model.ModelState
import com.mvcoding.expensius.model.Tag
import com.mvcoding.expensius.model.Transaction
import com.mvcoding.expensius.provider.database.table.TagsTable
import com.mvcoding.expensius.provider.database.table.TransactionsTable
import java.math.BigDecimal

fun Tag.toContentValues(tagsTable: TagsTable): ContentValues {
    val contentValues = ContentValues()
    contentValues.put(tagsTable.id.name, id)
    contentValues.put(tagsTable.modelState.name, modelState.name)
    contentValues.put(tagsTable.title.name, title)
    contentValues.put(tagsTable.color.name, color)
    return contentValues
}

fun Transaction.toContentValues(transactionsTable: TransactionsTable): ContentValues {
    val contentValues = ContentValues()
    contentValues.put(transactionsTable.id.name, id)
    contentValues.put(transactionsTable.modelState.name, modelState.name)
    contentValues.put(transactionsTable.transactionType.name, transactionType.name)
    contentValues.put(transactionsTable.transactionState.name, transactionState.name)
    contentValues.put(transactionsTable.timestamp.name, timestamp)
    contentValues.put(transactionsTable.currency.name, currency.code)
    contentValues.put(transactionsTable.amount.name, amount.toPlainString())
    contentValues.put(transactionsTable.tags.name, tags.joinToString(separator = ",", transform = { it.id }))
    contentValues.put(transactionsTable.note.name, note)
    return contentValues
}

fun Cursor.toTag(tagsTable: TagsTable): Tag {
    val id = getString(this.getColumnIndex(tagsTable.id.name))
    val modelState = ModelState.valueOf(getString(this.getColumnIndex(tagsTable.modelState.name)))
    val title = getString(this.getColumnIndex(tagsTable.title.name))
    val color = getInt(this.getColumnIndex(tagsTable.color.name))
    return Tag(id, modelState, title, color)
}

fun Cursor.toTransaction(transactionsTable: TransactionsTable, tagsTable: TagsTable): Transaction {
    val id = getString(this.getColumnIndex(transactionsTable.id.name))
    val modelState = ModelState.valueOf(getString(this.getColumnIndex(transactionsTable.modelState.name)))
    val transactionType = TransactionType.valueOf(getString(this.getColumnIndex(transactionsTable.transactionType.name)))
    val transactionState = TransactionState.valueOf(getString(this.getColumnIndex(transactionsTable.transactionState.name)))
    val timestamp = getLong(this.getColumnIndex(transactionsTable.timestamp.name))
    val currency = Currency(getString(this.getColumnIndex(transactionsTable.currency.name)))
    val amount = BigDecimal(getString(this.getColumnIndex(transactionsTable.amount.name)))
    val tagSplitRegex = Regex(TagsTable.COLUMN_SEPARATOR)
    val tagsString = getString(this.getColumnIndex(tagsTable.transactionTags.name))
    val tags = tagsString?.split(Regex(TagsTable.TAG_SEPARATOR))?.map {
        val tagValues = it.split(tagSplitRegex)
        Tag(tagValues[0], ModelState.valueOf(tagValues[1]), tagValues[2], tagValues[3].toInt())
    }?.toSet() ?: setOf<Tag>()
    val note = getString(this.getColumnIndex(transactionsTable.note.name))
    return Transaction(id, modelState, transactionType, transactionState, timestamp, currency, amount, tags, note)
}

fun <T> Cursor.map(mapper: ((Cursor) -> T)): List<T> {
    if (!moveToFirst()) {
        return emptyList()
    }

    val items = arrayListOf<T>()
    do {
        items.add(mapper.invoke(this))
    } while (moveToNext())
    return items
}
