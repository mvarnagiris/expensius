/*
 * Copyright (C) 2018 Mantas Varnagiris.
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
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import org.joda.time.Interval
import kotlin.Long.Companion.MAX_VALUE
import kotlin.Long.Companion.MIN_VALUE

class FilterOld(/*appUserService: AppUserService,*/ timestampProvider: TimestampProvider) {
    private var filterData: FilterDataOld = FilterDataOld(Interval(MIN_VALUE, MAX_VALUE))
    private val filterDataSubject = BehaviorSubject.createDefault(filterData)

    init {
//        appUserService.appUser()
//                .first()
//                .map { it.settings.reportPeriod }
//                .map { it.interval(timestampProvider.currentTimestamp()) }
//                .subscribe { setInterval(it) }
    }

    fun filterData(): Observable<FilterDataOld> = filterDataSubject

    fun setInterval(interval: Interval): FilterOld {
        filterData = filterData.withInterval(interval)
        filterDataSubject.onNext(filterData)
        return this
    }

    fun setTransactionType(transactionType: TransactionType?): FilterOld {
        filterData = filterData.withTransactionType(transactionType)
        filterDataSubject.onNext(filterData)
        return this
    }

    fun setTransactionState(transactionState: TransactionState?): FilterOld {
        filterData = filterData.withTransactionState(transactionState)
        filterDataSubject.onNext(filterData)
        return this
    }

    fun clearTransactionType(): FilterOld = setTransactionType(null)
    fun clearTransactionState(): FilterOld = setTransactionState(null)
}