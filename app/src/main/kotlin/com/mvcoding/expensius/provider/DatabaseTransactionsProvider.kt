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

import com.mvcoding.expensius.ModelState.NONE
import com.mvcoding.expensius.extension.toContentValues
import com.mvcoding.expensius.extension.toTransaction
import com.mvcoding.expensius.feature.transaction.Transaction
import com.mvcoding.expensius.feature.transaction.TransactionsProvider
import com.mvcoding.expensius.paging.Page
import com.mvcoding.expensius.paging.PageResult
import com.mvcoding.expensius.provider.database.Database
import com.mvcoding.expensius.provider.database.DatabasePageLoader
import com.mvcoding.expensius.provider.database.select
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
        database.save(transactionsTable, transactions.map { it.toContentValues(transactionsTable) })
    }

    override fun transactions(pages: Observable<Page>): Observable<PageResult<Transaction>> {
        return pageLoader.load({ it.toTransaction(transactionsTable) },
                               select(arrayOf(*transactionsTable.columns(), tagsTable.transactionTags))
                                       .from(transactionsTable)
                                       .leftJoin(transactionTagsTable, "${transactionsTable.id}=${transactionTagsTable.transactionId}")
                                       .leftJoin(tagsTable, "${transactionTagsTable.tagId}=${tagsTable.id}")
                                       .where("${transactionsTable.modelState}=?", NONE.name),
                //.groupBy(transactionsTable.id)
                               pages)
    }
}