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

package com.mvcoding.expensius.feature.transaction

import com.mvcoding.expensius.BusinessConstants
import com.mvcoding.expensius.data.DataSource
import com.mvcoding.expensius.model.Transaction
import io.reactivex.Observable

class TransactionsOverviewSource(private val transactionsSource: DataSource<List<Transaction>>) : DataSource<List<Transaction>> {

    override fun data(): Observable<List<Transaction>> = transactionsSource.data().map { it.take(BusinessConstants.TRANSACTIONS_IN_OVERVIEW) }
}