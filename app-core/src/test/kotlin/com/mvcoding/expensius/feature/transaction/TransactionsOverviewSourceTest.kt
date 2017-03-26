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

import com.mvcoding.expensius.BusinessConstants
import com.mvcoding.expensius.data.DataSource
import com.mvcoding.expensius.data.RawRealtimeData
import com.mvcoding.expensius.data.RealtimeList
import com.mvcoding.expensius.data.testUserIdRealtimeDataSource
import com.mvcoding.expensius.model.*
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Test
import rx.Observable.just
import rx.Observable.never
import rx.observers.TestSubscriber

class TransactionsOverviewSourceTest {

    @Test
    fun `behaves like user id realtime list data source`() {
        testUserIdRealtimeDataSource(::TransactionsOverviewSource)
    }

    @Test
    fun `limits returned transactions count`() {
        val appUserSource = mock<DataSource<AppUser>>()
        val createRealtimeList = mock<(UserId) -> RealtimeList<Transaction>>()
        val realtimeList = mock<RealtimeList<Transaction>>()
        val subscriber = TestSubscriber<List<Transaction>>()
        val transactions = (0..BusinessConstants.TRANSACTIONS_IN_OVERVIEW).map { aTransaction() }
        val expectedTransactions = transactions.take(BusinessConstants.TRANSACTIONS_IN_OVERVIEW)
        whenever(appUserSource.data()).thenReturn(just(anAppUser()))
        whenever(createRealtimeList(any())).thenReturn(realtimeList)
        whenever(realtimeList.getAllItems()).thenReturn(just(RawRealtimeData.AllItems(transactions)))
        whenever(realtimeList.getAddedItems()).thenReturn(never())
        whenever(realtimeList.getChangedItems()).thenReturn(never())
        whenever(realtimeList.getRemovedItems()).thenReturn(never())
        whenever(realtimeList.getMovedItem()).thenReturn(never())
        val transactionsOverviewSource = TransactionsOverviewSource(appUserSource, createRealtimeList)

        transactionsOverviewSource.data().subscribe(subscriber)

        subscriber.assertValue(expectedTransactions)
    }
}