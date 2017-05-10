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
import com.mvcoding.expensius.data.RealtimeData
import com.mvcoding.expensius.data.RealtimeData.AddedItems
import com.mvcoding.expensius.data.RealtimeData.AllItems
import com.mvcoding.expensius.model.Transaction
import com.mvcoding.expensius.model.extensions.anInt
import com.mvcoding.expensius.model.extensions.someTransactions
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Test
import rx.Observable.from
import rx.observers.TestSubscriber

class TransactionsOverviewSourceTest {

    @Test
    fun `limits returned transactions count`() {
        val transactionsSource = mock<DataSource<RealtimeData<Transaction>>>()
        val transactions1 = someTransactions()
        val transactions2 = someTransactions()
        whenever(transactionsSource.data()).thenReturn(from(listOf(AllItems(transactions1), AddedItems(transactions2, someTransactions(), anInt()))))
        val subscriber = TestSubscriber<List<Transaction>>()
        val transactionsOverviewSource = TransactionsOverviewSource(transactionsSource)

        transactionsOverviewSource.data().subscribe(subscriber)

        subscriber.assertValues(transactions1.take(BusinessConstants.TRANSACTIONS_IN_OVERVIEW), transactions2.take(BusinessConstants.TRANSACTIONS_IN_OVERVIEW))
    }
}