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
import java.util.*

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
    contentValues.put(transactionsTable.tags.name, tags.joinToString(separator = ",", transform = { it.id }))
    contentValues.put(transactionsTable.note.name, note)
    return contentValues
}

fun Cursor.toTransaction(transactionsTable: TransactionsTable, tagsTable: TagsTable): Transaction {
    val id = getString(this.getColumnIndex(transactionsTable.id.name))
    val modelState = ModelState.valueOf(getString(this.getColumnIndex(transactionsTable.modelState.name)))
    val transactionType = TransactionType.valueOf(getString(this.getColumnIndex(transactionsTable.transactionType.name)))
    val transactionState = TransactionState.valueOf(getString(this.getColumnIndex(transactionsTable.transactionState.name)))
    val timestamp = getLong(this.getColumnIndex(transactionsTable.timestamp.name))
    val currency = Currency(getString(this.getColumnIndex(transactionsTable.currency.name)))
    val exchangeRate = BigDecimal(getString(this.getColumnIndex(transactionsTable.exchangeRate.name)))
    val amount = BigDecimal(getString(this.getColumnIndex(transactionsTable.amount.name)))
    val tagSplitRegex = Regex(TagsTable.COLUMN_SEPARATOR)
    val tagsString = getString(this.getColumnIndex(tagsTable.transactionTags.name))
    val tags = tagsString?.split(Regex(TagsTable.TAG_SEPARATOR))?.map {
        val tagValues = it.split(tagSplitRegex)
        Tag(tagValues[0], ModelState.valueOf(tagValues[1]), tagValues[2], tagValues[3].toInt())
    }?.toSortedSet(Comparator { leftTag, rightTag -> leftTag.order.compareTo(rightTag.order) }) ?: emptySet<Tag>()
    val note = getString(this.getColumnIndex(transactionsTable.note.name))
    return Transaction(id, modelState, transactionType, transactionState, timestamp, currency, exchangeRate, amount, tags, note)
}