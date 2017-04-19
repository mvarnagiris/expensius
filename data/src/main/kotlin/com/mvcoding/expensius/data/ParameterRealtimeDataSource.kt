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

package com.mvcoding.expensius.data

import rx.Observable
import java.util.concurrent.atomic.AtomicReference

class ParameterRealtimeDataSource<in PARAMETER, ITEM>(
        private val parameterSource: DataSource<PARAMETER>,
        private val createRealtimeList: (PARAMETER) -> RealtimeList<ITEM>,
        private val getItemId: (ITEM) -> String) : DataSource<RealtimeData<ITEM>> {

    private val userIdAndRealtimeListDataSource = AtomicReference<Pair<PARAMETER, RealtimeListDataSource<ITEM>>?>()

    override fun data(): Observable<RealtimeData<ITEM>> = parameterSource.data()
            .distinctUntilChanged()
            .switchMap {
                val currentUserIdAndRealtimeListDataSource = userIdAndRealtimeListDataSource.get()
                val currentUserId = currentUserIdAndRealtimeListDataSource?.first
                val currentRealtimeListDataSource = currentUserIdAndRealtimeListDataSource?.second

                val shouldUseSameRealtimeListDataSource = currentUserId != null && currentRealtimeListDataSource != null && currentUserId == it
                if (shouldUseSameRealtimeListDataSource) currentRealtimeListDataSource!!.data()
                else {
                    currentRealtimeListDataSource?.close()
                    val realtimeListDataSource = RealtimeListDataSource(createRealtimeList(it)) { getItemId(it) }
                    userIdAndRealtimeListDataSource.set(Pair(it, realtimeListDataSource))
                    realtimeListDataSource.data()
                }
            }
}