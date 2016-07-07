package com.mvcoding.expensius.extension

import android.content.ContentValues
import android.database.Cursor
import com.mvcoding.expensius.feature.transaction.TransactionState
import com.mvcoding.expensius.feature.transaction.TransactionType
import com.mvcoding.expensius.model.Color
import com.mvcoding.expensius.model.Currency
import com.mvcoding.expensius.model.ModelState
import com.mvcoding.expensius.model.Order
import com.mvcoding.expensius.model.Tag
import com.mvcoding.expensius.model.TagId
import com.mvcoding.expensius.model.Title
import com.mvcoding.expensius.model.Transaction
import com.mvcoding.expensius.provider.database.table.TagsTable
import com.mvcoding.expensius.provider.database.table.TransactionsTable
import java.math.BigDecimal

fun Transaction.toContentValues(transactionsTable: TransactionsTable): ContentValues {
    val contentValues = ContentValues()
    contentValues.put(transactionsTable.id.name, id)
    contentValues.put(transactionsTable.modelState.name, modelState.name)
    contentValues.put(transactionsTable.transactionType.name, transactionType.name)
    contentValues.put(transactionsTable.transactionState.name, transactionState.name)
    contentValues.put(transactionsTable.timestamp.name, timestamp)
    contentValues.put(transactionsTable.currency.name, currency.code)
    contentValues.put(transactionsTable.exchangeRate.name, exchangeRate.toPlainString())
    contentValues.put(transactionsTable.amount.name, amount.toPlainString())
    contentValues.put(transactionsTable.tags.name, tags.joinToString(separator = ",", transform = { it.tagId.id }))
    contentValues.put(transactionsTable.note.name, note)
    return contentValues
}

fun Cursor.toTransaction(transactionsTable: TransactionsTable, tagsTable: TagsTable): Transaction {
    val id = getString(getColumnIndex(transactionsTable.id.name))
    val modelState = ModelState.valueOf(getString(getColumnIndex(transactionsTable.modelState.name)))
    val transactionType = TransactionType.valueOf(getString(getColumnIndex(transactionsTable.transactionType.name)))
    val transactionState = TransactionState.valueOf(getString(getColumnIndex(transactionsTable.transactionState.name)))
    val timestamp = getLong(getColumnIndex(transactionsTable.timestamp.name))
    val currency = Currency(getString(getColumnIndex(transactionsTable.currency.name)))
    val exchangeRate = BigDecimal(getString(getColumnIndex(transactionsTable.exchangeRate.name)))
    val amount = BigDecimal(getString(getColumnIndex(transactionsTable.amount.name)))
    val tagSplitRegex = Regex(TagsTable.COLUMN_SEPARATOR)
    val tagsString = getString(getColumnIndex(tagsTable.transactionTags.name))
    val tags = tagsString?.split(Regex(TagsTable.TAG_SEPARATOR))?.map {
        val tagValues = it.split(tagSplitRegex)
        Tag(TagId(tagValues[0]),
                ModelState.valueOf(tagValues[1]),
                Title(tagValues[2]),
                Color(tagValues[3].toInt()),
                Order(tagValues[4].toInt()))
    }?.toSet() ?: emptySet<Tag>()
    val note = getString(getColumnIndex(transactionsTable.note.name))
    return Transaction(id, modelState, transactionType, transactionState, timestamp, currency, exchangeRate, amount, tags, note)
}