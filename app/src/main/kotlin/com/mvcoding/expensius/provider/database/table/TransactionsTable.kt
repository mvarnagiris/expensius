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

import com.mvcoding.expensius.provider.database.table.Column.Type.*

class TransactionsTable : ModelTable("transactions") {
    val transactionType = Column(this, "transactionType", TEXT)
    val transactionState = Column(this, "transactionState", TEXT)
    val timestamp = Column(this, "timestamp", DATE_TIME)
    val currency = Column(this, "currency", TEXT)
    val amount = Column(this, "amount", REAL)
    val tags = Column(this, "tags", TEXT)
    val note = Column(this, "note", TEXT)

    override fun modelColumns(): List<Column> {
        return listOf(transactionType, transactionState, timestamp, currency, amount, tags, note)
    }
}