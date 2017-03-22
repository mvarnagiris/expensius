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
import com.mvcoding.expensius.data.RawRealtimeData
import com.mvcoding.expensius.data.RealtimeList
import com.mvcoding.expensius.data.RealtimeListDataSource
import com.mvcoding.expensius.model.AppUser
import com.mvcoding.expensius.model.Transaction
import com.mvcoding.expensius.model.UserId
import rx.Observable

class TransactionsOverviewSource(
        private val appUserSource: DataSource<AppUser>,
        private val createRealtimeList: (UserId) -> RealtimeList<Transaction>) : DataSource<List<Transaction>> {

    override fun data(): Observable<List<Transaction>> = appUserSource.data()
            .first()
            .map { createRealtimeList(it.userId) }
            .switchMap { realtimeList -> RealtimeListDataSource(realtimeList) { it.transactionId.id }.data().doOnNext { realtimeList.close() } }
            .ofType(RawRealtimeData.AllItems::class.java)
            .map {
                @Suppress("UNCHECKED_CAST")
                it.items as List<Transaction>
            }
            .first()
}