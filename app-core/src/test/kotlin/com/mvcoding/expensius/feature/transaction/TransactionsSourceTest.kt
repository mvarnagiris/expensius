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
import com.mvcoding.expensius.data.RealtimeData
import com.mvcoding.expensius.model.Transaction
import com.mvcoding.expensius.model.extensions.anInt
import com.mvcoding.expensius.model.extensions.someTransactions
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Test
import rx.lang.kotlin.PublishSubject
import rx.observers.TestSubscriber

class TransactionsSourceTest {

    @Test
    fun `extracts all items from realtime transactions and emits that`() {
        val allItems = someTransactions()
        val addedItems = someTransactions()
        val changedItems = someTransactions()
        val removedItems = someTransactions()
        val movedItems = someTransactions()
        val realtimeTransactionsSource = mock<DataSource<RealtimeData<Transaction>>>()
        val transactionsSource = TransactionsSource(realtimeTransactionsSource)
        val subscriber = TestSubscriber<List<Transaction>>()
        val realtimeSubject = PublishSubject<RealtimeData<Transaction>>()
        whenever(realtimeTransactionsSource.data()).thenReturn(realtimeSubject)

        transactionsSource.data().subscribe(subscriber)
        realtimeSubject.onNext(RealtimeData.AllItems(allItems))
        realtimeSubject.onNext(RealtimeData.AddedItems(addedItems, someTransactions(), anInt()))
        realtimeSubject.onNext(RealtimeData.ChangedItems(changedItems, someTransactions(), anInt()))
        realtimeSubject.onNext(RealtimeData.RemovedItems(removedItems, someTransactions(), anInt()))
        realtimeSubject.onNext(RealtimeData.MovedItems(movedItems, someTransactions(), anInt(), anInt()))

        subscriber.assertValues(allItems, addedItems, changedItems, removedItems, movedItems)
    }
}