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

package com.mvcoding.expensius.provider.database.table

import com.mvcoding.expensius.provider.database.table.ValueColumn.Type.TEXT

class TransactionTagsTable : RelationshipTable("transaction_tags") {
    val transactionId = ValueColumn(this, "transactionId", TEXT);
    val tagId = ValueColumn(this, "tagId", TEXT);

    override fun idColumns() = arrayOf(transactionId, tagId)
    override fun columns() = arrayOf(transactionId, tagId)
}