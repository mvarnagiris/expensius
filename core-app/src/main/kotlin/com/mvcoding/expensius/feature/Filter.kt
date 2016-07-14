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

import com.mvcoding.expensius.model.TransactionType
import org.joda.time.Interval
import rx.Observable
import rx.lang.kotlin.BehaviorSubject

class Filter {
    private var filterData: FilterData = FilterData()
    private val filterDataSubject = BehaviorSubject(filterData)

    fun filterData(): Observable<FilterData> = filterDataSubject

    fun setTransactionType(transactionType: TransactionType) {
        filterData = filterData.copy(transactionType = transactionType)
        filterDataSubject.onNext(filterData)
    }

    fun clearTransactionType() {
        filterData = filterData.copy(transactionType = null)
        filterDataSubject.onNext(filterData)
    }

    fun setInterval(interval: Interval) {
        filterData = filterData.copy(interval = interval)
        filterDataSubject.onNext(filterData)
    }

    fun clearInterval() {
        filterData = filterData.copy(interval = null)
        filterDataSubject.onNext(filterData)
    }
}