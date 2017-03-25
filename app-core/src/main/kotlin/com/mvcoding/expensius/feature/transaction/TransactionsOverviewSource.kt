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
import com.mvcoding.expensius.data.RealtimeList
import com.mvcoding.expensius.data.RealtimeListDataSource
import com.mvcoding.expensius.model.AppUser
import com.mvcoding.expensius.model.Transaction
import com.mvcoding.expensius.model.UserId
import rx.Observable
import java.util.concurrent.atomic.AtomicReference

class TransactionsOverviewSource(
        private val appUserSource: DataSource<AppUser>,
        private val createRealtimeList: (UserId) -> RealtimeList<Transaction>) : DataSource<List<Transaction>> {

    private val userIdAndRealtimeListDataSource = AtomicReference<Pair<UserId, RealtimeListDataSource<Transaction>>?>()

    override fun data(): Observable<List<Transaction>> = appUserSource.data()
            .map { it.userId }
            .distinctUntilChanged()
            .switchMap {
                val currentUserIdAndRealtimeListDataSource = userIdAndRealtimeListDataSource.get()
                val currentUserId = currentUserIdAndRealtimeListDataSource?.first
                val currentRealtimeListDataSource = currentUserIdAndRealtimeListDataSource?.second

                val shouldUseSameRealtimeListDataSource = currentUserId != null && currentRealtimeListDataSource != null && currentUserId == it
                if (shouldUseSameRealtimeListDataSource) currentRealtimeListDataSource!!.data()
                else {
                    currentRealtimeListDataSource?.close()
                    val realtimeListDataSource = RealtimeListDataSource(createRealtimeList(it)) { it.transactionId.id }
                    userIdAndRealtimeListDataSource.set(Pair(it, realtimeListDataSource))
                    realtimeListDataSource.data()
                }
            }
            .map { emptyList<Transaction>() }
}