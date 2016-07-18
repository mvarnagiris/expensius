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

package com.mvcoding.expensius.feature

import com.mvcoding.expensius.model.TransactionState.PENDING
import com.mvcoding.expensius.model.TransactionType.EXPENSE
import org.joda.time.Interval
import org.junit.Test
import rx.observers.TestSubscriber

class FilterTest {
    val subscriber: TestSubscriber<FilterData> = TestSubscriber.create<FilterData>()
    val filter = Filter()

    @Test
    fun initiallyGivesEmptyFilterData() {
        filter.filterData().subscribe(subscriber)

        subscriber.assertValue(FilterData())
    }

    @Test
    fun changingFilterValuesEmitsUpdatedFilter() {
        filter.filterData().subscribe(subscriber)

        filter.setTransactionType(EXPENSE)
        filter.setTransactionState(PENDING)
        filter.setInterval(Interval(0, 1))

        subscriber.assertValues(FilterData(), FilterData(EXPENSE), FilterData(EXPENSE, PENDING), FilterData(EXPENSE, PENDING, Interval(0, 1)))
    }

    @Test
    fun clearingFilterValuesEmitsUpdatedFilter() {
        filter.filterData().subscribe(subscriber)

        filter.setTransactionType(EXPENSE)
        filter.setTransactionState(PENDING)
        filter.setInterval(Interval(0, 1))
        filter.clearTransactionType()
        filter.clearTransactionState()
        filter.clearInterval()

        subscriber.assertValues(
                FilterData(),
                FilterData(EXPENSE),
                FilterData(EXPENSE, PENDING),
                FilterData(EXPENSE, PENDING, Interval(0, 1)),
                FilterData(transactionState = PENDING, interval = Interval(0, 1)),
                FilterData(interval = Interval(0, 1)),
                FilterData())
    }
}