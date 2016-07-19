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

import com.mvcoding.expensius.model.TransactionState
import com.mvcoding.expensius.model.TransactionType
import org.joda.time.Interval
import rx.Observable
import rx.lang.kotlin.BehaviorSubject

class Filter {
    private var filterData: FilterData = FilterData()
    private val filterDataSubject = BehaviorSubject(filterData)

    fun filterData(): Observable<FilterData> = filterDataSubject

    fun setTransactionType(transactionType: TransactionType?): Filter {
        filterData = filterData.withTransactionType(transactionType)
        filterDataSubject.onNext(filterData)
        return this
    }

    fun setTransactionState(transactionState: TransactionState?): Filter {
        filterData = filterData.withTransactionState(transactionState)
        filterDataSubject.onNext(filterData)
        return this
    }

    fun setInterval(interval: Interval?): Filter {
        filterData = filterData.withInterval(interval)
        filterDataSubject.onNext(filterData)
        return this
    }

    fun clearTransactionType(): Filter = setTransactionType(null)
    fun clearTransactionState(): Filter = setTransactionState(null)
    fun clearInterval(): Filter = setInterval(null)
}