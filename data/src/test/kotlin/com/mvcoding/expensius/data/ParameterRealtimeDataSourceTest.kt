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

package com.mvcoding.expensius.data

import java.io.Closeable

class ParameterRealtimeDataSourceTest {
//    @Test
//    fun `creates new realtime list when parameter changes`() {
//        createsNewRealtimeListWhenParameterChanges(1, 2, createDataSource())
//    }
//
//    @Test
//    fun `does not create new realtime list when same parameter is emitted`() {
//        doesNotCreateNewRealtimeListWhenSameParameterIsEmitted(1, createDataSource())
//    }
//
//    @Test
//    fun `closes previously created realtime list when new one is created`() {
//        closesPreviouslyCreatedRealtimeListWhenNewOneIsCreated(1, 2, createDataSource())
//    }
//
//    @Test
//    fun `does not create new realtime list for another subscriber`() {
//        doesNotCreateNewRealtimeListForAnotherSubscriber(1, createDataSource())
//    }
//
//    @Test
//    fun `closes current realtime list when close is requested`() {
//        closesCurrentRealtimeListWhenCloseIsRequested(1, createDataSource())
//    }

//    private fun createDataSource() = { parameterSource: DataSource<Int>, createRealtimeList: (Int) -> RealtimeList<Int> -> ParameterRealtimeDataSource(parameterSource, createRealtimeList, Int::toString) }
}

fun <PARAMETER, ITEM, DATASOURCE> testParameterRealtimeDataSource(
        parameter1: PARAMETER,
        parameter2: PARAMETER,
        createDataSource: (DataSource<PARAMETER>, (PARAMETER) -> RealtimeList<ITEM>) -> DATASOURCE)
        where DATASOURCE : DataSource<*>, DATASOURCE : Closeable {
//    createsNewRealtimeListWhenParameterChanges(parameter1, parameter2, createDataSource)
//    doesNotCreateNewRealtimeListWhenSameParameterIsEmitted(parameter1, createDataSource)
//    closesPreviouslyCreatedRealtimeListWhenNewOneIsCreated(parameter1, parameter2, createDataSource)
//    doesNotCreateNewRealtimeListForAnotherSubscriber(parameter1, createDataSource)
//    closesCurrentRealtimeListWhenCloseIsRequested(parameter1, createDataSource)
}

//private fun <PARAMETER, ITEM> createsNewRealtimeListWhenParameterChanges(
//        parameter1: PARAMETER,
//        parameter2: PARAMETER,
//        createDataSource: (DataSource<PARAMETER>, (PARAMETER) -> RealtimeList<ITEM>) -> DataSource<*>) {
//
//    val parameterSubject = PublishSubject<PARAMETER>()
//    val parameterSource = mock<DataSource<PARAMETER>>()
//    val createRealtimeList = mock<(PARAMETER) -> RealtimeList<ITEM>>()
//    val subscriber = TestSubscriber<Any>()
//    whenever(parameterSource.data()).thenReturn(parameterSubject)
//    setupRealtimeLists(parameter1, parameter2, createRealtimeList, mock())
//
//    val dataSource = createDataSource(parameterSource, createRealtimeList)
//
//    dataSource.data().subscribe(subscriber)
//    parameterSubject.onNext(parameter1)
//    parameterSubject.onNext(parameter2)
//
//    subscriber.assertNoErrors()
//    verify(createRealtimeList).invoke(parameter1)
//    verify(createRealtimeList).invoke(parameter2)
//}
//
//private fun <PARAMETER, ITEM> doesNotCreateNewRealtimeListWhenSameParameterIsEmitted(
//        parameter1: PARAMETER,
//        createDataSource: (DataSource<PARAMETER>, (PARAMETER) -> RealtimeList<ITEM>) -> DataSource<*>) {
//
//    val parameterSubject = PublishSubject<PARAMETER>()
//    val parameterSource = mock<DataSource<PARAMETER>>()
//    val createRealtimeList = mock<(PARAMETER) -> RealtimeList<ITEM>>()
//    val subscriber = TestSubscriber<Any>()
//    whenever(parameterSource.data()).thenReturn(parameterSubject)
//    setupRealtimeLists(parameter1, null, createRealtimeList, mock())
//
//    val dataSource = createDataSource(parameterSource, createRealtimeList)
//
//    dataSource.data().subscribe(subscriber)
//    parameterSubject.onNext(parameter1)
//    parameterSubject.onNext(parameter1)
//
//    verify(createRealtimeList, times(1)).invoke(parameter1)
//}
//
//private fun <PARAMETER, ITEM> closesPreviouslyCreatedRealtimeListWhenNewOneIsCreated(
//        parameter1: PARAMETER,
//        parameter2: PARAMETER,
//        createDataSource: (DataSource<PARAMETER>, (PARAMETER) -> RealtimeList<ITEM>) -> DataSource<*>) {
//
//    val parameterSubject = PublishSubject<PARAMETER>()
//    val appUserSource = mock<DataSource<PARAMETER>>()
//    val createRealtimeList = mock<(PARAMETER) -> RealtimeList<ITEM>>()
//    val subscriber = TestSubscriber<Any>()
//    val realtimeList = mock<RealtimeList<ITEM>>()
//    whenever(appUserSource.data()).thenReturn(parameterSubject)
//    setupRealtimeLists(parameter1, parameter2, createRealtimeList, realtimeList)
//
//    val dataSource = createDataSource(appUserSource, createRealtimeList)
//
//    dataSource.data().subscribe(subscriber)
//    parameterSubject.onNext(parameter1)
//    parameterSubject.onNext(parameter2)
//
//    subscriber.assertNoErrors()
//    verify(realtimeList).close()
//}
//
//private fun <PARAMETER, ITEM> doesNotCreateNewRealtimeListForAnotherSubscriber(
//        parameter1: PARAMETER,
//        createDataSource: (DataSource<PARAMETER>, (PARAMETER) -> RealtimeList<ITEM>) -> DataSource<*>) {
//
//    val parameterSource = mock<DataSource<PARAMETER>>()
//    val createRealtimeList = mock<(PARAMETER) -> RealtimeList<ITEM>>()
//    val subscriber = TestSubscriber<Any>()
//    val anotherSubscriber = TestSubscriber<Any>()
//    whenever(parameterSource.data()).thenReturn(just(parameter1))
//    setupRealtimeLists(parameter1, null, createRealtimeList, mock())
//
//    val dataSource = createDataSource(parameterSource, createRealtimeList)
//
//    dataSource.data().subscribe(subscriber)
//    dataSource.data().subscribe(anotherSubscriber)
//
//    verify(createRealtimeList, times(1)).invoke(parameter1)
//}
//
//private fun <PARAMETER, ITEM, DATASOURCE> closesCurrentRealtimeListWhenCloseIsRequested(
//        parameter1: PARAMETER,
//        createDataSource: (DataSource<PARAMETER>, (PARAMETER) -> RealtimeList<ITEM>) -> DATASOURCE)
//        where DATASOURCE : DataSource<*>, DATASOURCE : Closeable {
//
//    val parameterSubject = PublishSubject<PARAMETER>()
//    val appUserSource = mock<DataSource<PARAMETER>>()
//    val createRealtimeList = mock<(PARAMETER) -> RealtimeList<ITEM>>()
//    val subscriber = TestSubscriber<Any>()
//    val realtimeList = mock<RealtimeList<ITEM>>()
//    whenever(appUserSource.data()).thenReturn(parameterSubject)
//    setupRealtimeLists(parameter1, null, createRealtimeList, realtimeList)
//
//    val dataSource = createDataSource(appUserSource, createRealtimeList)
//
//    dataSource.data().subscribe(subscriber)
//    parameterSubject.onNext(parameter1)
//    dataSource.close()
//
//    subscriber.assertNoErrors()
//    verify(realtimeList).close()
//}
//
//private fun <PARAMETER, ITEM> setupRealtimeLists(
//        parameter1: PARAMETER,
//        parameter2: PARAMETER?,
//        createRealtimeList: (PARAMETER) -> RealtimeList<ITEM>,
//        realtimeList: RealtimeList<ITEM>,
//        vararg otherRealtimeList: RealtimeList<ITEM>) {
//
//    whenever(createRealtimeList(parameter1)).thenReturn(realtimeList, *otherRealtimeList)
//    if (parameter2 != null) whenever(createRealtimeList(parameter2)).thenReturn(realtimeList, *otherRealtimeList)
//
//    whenever(realtimeList.getAllItems()).thenReturn(Observable.never())
//    whenever(realtimeList.getAddedItems()).thenReturn(Observable.never())
//    whenever(realtimeList.getChangedItems()).thenReturn(Observable.never())
//    whenever(realtimeList.getRemovedItems()).thenReturn(Observable.never())
//    whenever(realtimeList.getMovedItem()).thenReturn(Observable.never())
//    otherRealtimeList.forEach {
//        whenever(it.getAllItems()).thenReturn(Observable.never())
//        whenever(it.getAddedItems()).thenReturn(Observable.never())
//        whenever(it.getChangedItems()).thenReturn(Observable.never())
//        whenever(it.getRemovedItems()).thenReturn(Observable.never())
//        whenever(it.getMovedItem()).thenReturn(Observable.never())
//    }
//}