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

import com.mvcoding.expensius.provider.database.table.ValueColumn.Type.*

class TransactionsTable : ModelTable("transactions") {
    val transactionType = ValueColumn(this, "transactionType", TEXT)
    val transactionState = ValueColumn(this, "transactionState", TEXT)
    val timestamp = ValueColumn(this, "timestamp", DATE_TIME)
    val currency = ValueColumn(this, "currency", TEXT)
    val exchangeRate = ValueColumn(this, "exchange_rate", REAL)
    val amount = ValueColumn(this, "amount", REAL)
    val tags = ValueColumn(this, "tags", TEXT)
    val note = ValueColumn(this, "note", TEXT)

    override fun modelColumns() = arrayOf(transactionType, transactionState, timestamp, currency, exchangeRate, amount, tags, note)
}