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

package com.mvcoding.expensius.feature

//import com.mvcoding.expensius.service.AppUserService

class FilterTest {
//    val subscriber: TestSubscriber<FilterDataOld> = TestSubscriber.create<FilterDataOld>()
//    val appUser = anAppUser()
//    val appUserService: AppUserService = mock<AppUserService>().apply { whenever(this.appUser()).thenReturn(just(appUser)) }
//    val timestampProvider = aFixedTimestampProvider()
//    val defaultFilterData = FilterDataOld(anAppUser().settings.reportPeriod.interval(timestampProvider.currentTimestamp()))
//    val filter = FilterOld(appUserService, timestampProvider)
//
//    @Test
//    fun initiallyGivesEmptyFilterData() {
//        filter.filterData().subscribe(subscriber)
//
//        subscriber.assertValue(defaultFilterData)
//    }
//
//    @Test
//    fun changingFilterValuesEmitsUpdatedFilter() {
//        filter.filterData().subscribe(subscriber)
//
//        filter.setTransactionType(EXPENSE)
//        filter.setTransactionState(PENDING)
//        filter.setInterval(Interval(0, 1))
//
//        subscriber.assertValues(
//                defaultFilterData,
//                defaultFilterData.copy(transactionType = EXPENSE),
//                defaultFilterData.copy(transactionType = EXPENSE, transactionState = PENDING),
//                defaultFilterData.copy(Interval(0, 1), EXPENSE, PENDING))
//    }
//
//    @Test
//    fun clearingFilterValuesEmitsUpdatedFilter() {
//        filter.setInterval(Interval(0, 1))
//        filter.setTransactionType(EXPENSE)
//        filter.setTransactionState(PENDING)
//        filter.filterData().subscribe(subscriber)
//
//        filter.clearTransactionType()
//        filter.clearTransactionState()
//
//        subscriber.assertValues(
//                FilterDataOld(Interval(0, 1), EXPENSE, PENDING),
//                FilterDataOld(Interval(0, 1), transactionState = PENDING),
//                FilterDataOld(Interval(0, 1)))
//    }
}