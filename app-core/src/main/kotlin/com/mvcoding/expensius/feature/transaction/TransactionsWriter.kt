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

package com.mvcoding.expensius.feature.transaction

import com.mvcoding.expensius.data.DataSource
import com.mvcoding.expensius.data.DataWriter
import com.mvcoding.expensius.model.AppUser
import com.mvcoding.expensius.model.Transaction
import com.mvcoding.expensius.model.UserId

class TransactionsWriter(
        private val appUserSource: DataSource<AppUser>,
        private val updateTransactions: (UserId, Set<Transaction>) -> Unit) : DataWriter<Set<Transaction>> {

    override fun write(data: Set<Transaction>) {
        appUserSource.data().first().map { it.userId }.subscribe { updateTransactions(it, data) }
    }
}