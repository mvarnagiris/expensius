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

package com.mvcoding.expensius.provider

import android.content.ContentValues
import com.mvcoding.expensius.extension.map
import com.mvcoding.expensius.extension.toContentValues
import com.mvcoding.expensius.extension.toTransaction
import com.mvcoding.expensius.feature.transaction.TransactionsFilter
import com.mvcoding.expensius.feature.transaction.TransactionsProvider
import com.mvcoding.expensius.model.ModelState
import com.mvcoding.expensius.model.ModelState.NONE
import com.mvcoding.expensius.model.Tag
import com.mvcoding.expensius.model.Transaction
import com.mvcoding.expensius.paging.Page
import com.mvcoding.expensius.paging.PageResult
import com.mvcoding.expensius.provider.database.*
import com.mvcoding.expensius.provider.database.OrderDirection.DESC
import com.mvcoding.expensius.provider.database.table.TagsTable
import com.mvcoding.expensius.provider.database.table.TransactionTagsTable
import com.mvcoding.expensius.provider.database.table.TransactionsTable
import rx.Observable

class DatabaseTransactionsProvider(
        private val database: Database,
        private val pageLoader: DatabasePageLoader<Transaction>,
        private val transactionsTable: TransactionsTable,
        private val transactionTagsTable: TransactionTagsTable,
        private val tagsTable: TagsTable) : TransactionsProvider {

    override fun save(transactions: Set<Transaction>) {
        val saveTransactions = transactions.map { SaveDatabaseAction(transactionsTable, it.toContentValues(transactionsTable)) }
        val saveRelationships = transactions
                .map { transaction -> transaction.tags.map { relationshipSaveRecord(transaction, it) } }
                .flatten()
        val deleteRelationships = transactions.map {
            DeleteDatabaseAction(transactionTagsTable, "${transactionTagsTable.transactionId}=?", arrayOf(it.id))
        }

        database.save(saveTransactions.plus(deleteRelationships).plus(saveRelationships))
    }

    override fun transactions(pages: Observable<Page>, transactionsFilter: TransactionsFilter): Observable<PageResult<Transaction>> {
        return transactions(pages, transactionsFilter.modelState)
    }

    override fun transactions(transactionsFilter: TransactionsFilter): Observable<List<Transaction>> {
        return database.query(query(NONE)).map { it.map { it.toTransaction(transactionsTable, tagsTable) } }
    }

    fun transactions(pages: Observable<Page>, modelState: ModelState): Observable<PageResult<Transaction>> {
        return pageLoader.load({ it.toTransaction(transactionsTable, tagsTable) }, query(modelState), pages)
    }

    private fun query(modelState: ModelState): QueryRequest {
        return select(arrayOf(*transactionsTable.columns(), tagsTable.transactionTags))
                .from(transactionsTable)
                .leftJoin(transactionTagsTable, "${transactionsTable.id}=${transactionTagsTable.transactionId}")
                .leftJoin(tagsTable, "${transactionTagsTable.tagId}=${tagsTable.id}")
                .where("${transactionsTable.modelState}=?", modelState.name)
                .groupBy(transactionsTable.id)
                .orderBy(Order(transactionsTable.timestamp, DESC))
    }

    private fun relationshipSaveRecord(transaction: Transaction, tag: Tag): SaveDatabaseAction {
        val contentValues = ContentValues()
        contentValues.put(transactionTagsTable.transactionId.name, transaction.id)
        contentValues.put(transactionTagsTable.tagId.name, tag.id)
        return SaveDatabaseAction(transactionTagsTable, contentValues)
    }
}