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

import com.mvcoding.expensius.model.TimestampProvider
import com.mvcoding.expensius.model.TransactionState
import com.mvcoding.expensius.model.TransactionType
import org.joda.time.Interval
import rx.Observable
import rx.lang.kotlin.BehaviorSubject
import kotlin.Long.Companion.MAX_VALUE
import kotlin.Long.Companion.MIN_VALUE

class Filter(/*appUserService: AppUserService,*/ timestampProvider: TimestampProvider) {
    private var filterData: FilterData = FilterData(Interval(MIN_VALUE, MAX_VALUE))
    private val filterDataSubject = BehaviorSubject(filterData)

    init {
//        appUserService.appUser()
//                .first()
//                .map { it.settings.reportPeriod }
//                .map { it.interval(timestampProvider.currentTimestamp()) }
//                .subscribe { setInterval(it) }
    }

    fun filterData(): Observable<FilterData> = filterDataSubject

    fun setInterval(interval: Interval): Filter {
        filterData = filterData.withInterval(interval)
        filterDataSubject.onNext(filterData)
        return this
    }

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

    fun clearTransactionType(): Filter = setTransactionType(null)
    fun clearTransactionState(): Filter = setTransactionState(null)
}