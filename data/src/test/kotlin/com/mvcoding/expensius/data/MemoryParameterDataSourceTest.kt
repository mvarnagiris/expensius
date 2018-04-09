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

class MemoryParameterDataSourceTest {

//    val parameter1 = Parameter(1)
//    val parameter2 = Parameter(2)
//    val data1 = Data("1")
//    val data2 = Data("2")

//    @Test
//    fun `returns data from dataSource`() {
//        returnsDataFromDataSource(parameter1, data1) { MemoryParameterDataSource(it) }
//    }
//
//    @Test
//    fun `does not request data from dataSource again when another subscriber subscribes before result comes`() {
//        doesNotRequestDataFromDataSourceAgainWhenAnotherSubscriberSubscribesBeforeResultComes(parameter1, data1) { MemoryParameterDataSource(it) }
//    }
//
//    @Test
//    fun `returns data from dataSource again when asking data for different parameter`() {
//        returnsDataFromDataSourceAgainWhenAskingDataForDifferentParameter(parameter1, parameter2, data1, data2) { MemoryParameterDataSource(it) }
//    }
//
//    @Test
//    fun `data is cached even when different parameter was requested before`() {
//        dataIsCachedEvenWhenDifferentParameterWasRequestedBefore(parameter1, parameter2, data1, data2) { MemoryParameterDataSource(it) }
//    }
//
//    @Test
//    fun `requests data from dataSource again when error happens`() {
//        requestsDataFromDataSourceAgainWhenErrorHappens(parameter1, data1) { MemoryParameterDataSource(it) }
//    }
//
//    data class Parameter(val value: Int)
//    data class Data(val value: String)
}

fun <PARAMETER, DATA> testMemoryParameterDataSource(
        parameter1: PARAMETER,
        parameter2: PARAMETER,
        data1: DATA,
        data2: DATA,
        createMemoryParameterDataSource: (ParameterDataSource<PARAMETER, DATA>) -> ParameterDataSource<PARAMETER, DATA>) {

//    returnsDataFromDataSource(parameter1, data1, createMemoryParameterDataSource)
//    doesNotRequestDataFromDataSourceAgainWhenAnotherSubscriberSubscribesBeforeResultComes(parameter1, data1, createMemoryParameterDataSource)
//    returnsDataFromDataSourceAgainWhenAskingDataForDifferentParameter(parameter1, parameter2, data1, data2, createMemoryParameterDataSource)
//    dataIsCachedEvenWhenDifferentParameterWasRequestedBefore(parameter1, parameter2, data1, data2, createMemoryParameterDataSource)
//    requestsDataFromDataSourceAgainWhenErrorHappens(parameter1, data1, createMemoryParameterDataSource)
}

//private fun <PARAMETER, DATA> returnsDataFromDataSource(
//        parameter1: PARAMETER,
//        data1: DATA,
//        createMemoryParameterDataSource: (ParameterDataSource<PARAMETER, DATA>) -> ParameterDataSource<PARAMETER, DATA>) {
//
//    val dataSource = mock<ParameterDataSource<PARAMETER, DATA>>()
//    val memoryParameterDataSource = createMemoryParameterDataSource(dataSource)
//    val subscriber1 = TestSubscriber<DATA>()
//    whenever(dataSource.data(parameter1)).thenReturn(just(data1))
//
//    memoryParameterDataSource.data(parameter1).subscribe(subscriber1)
//
//    subscriber1.assertValue(data1)
//    verify(dataSource, times(1)).data(parameter1)
//}
//
//private fun <PARAMETER, DATA> doesNotRequestDataFromDataSourceAgainWhenAnotherSubscriberSubscribesBeforeResultComes(
//        parameter1: PARAMETER,
//        data1: DATA,
//        createMemoryParameterDataSource: (ParameterDataSource<PARAMETER, DATA>) -> ParameterDataSource<PARAMETER, DATA>) {
//
//    val dataSubject = PublishSubject<DATA>()
//    val dataSource = mock<ParameterDataSource<PARAMETER, DATA>>()
//    val memoryParameterDataSource = createMemoryParameterDataSource(dataSource)
//    val subscriber1 = TestSubscriber<DATA>()
//    val subscriber2 = TestSubscriber<DATA>()
//    whenever(dataSource.data(parameter1)).thenReturn(dataSubject)
//
//    memoryParameterDataSource.data(parameter1).subscribe(subscriber1)
//    memoryParameterDataSource.data(parameter1).subscribe(subscriber2)
//    dataSubject.onNext(data1)
//
//    subscriber1.assertValue(data1)
//    subscriber2.assertValue(data1)
//    verify(dataSource, times(1)).data(parameter1)
//}
//
//private fun <PARAMETER, DATA> returnsDataFromDataSourceAgainWhenAskingDataForDifferentParameter(
//        parameter1: PARAMETER,
//        parameter2: PARAMETER,
//        data1: DATA,
//        data2: DATA,
//        createMemoryParameterDataSource: (ParameterDataSource<PARAMETER, DATA>) -> ParameterDataSource<PARAMETER, DATA>) {
//
//    val dataSource = mock<ParameterDataSource<PARAMETER, DATA>>()
//    val memoryParameterDataSource = createMemoryParameterDataSource(dataSource)
//    val subscriber1 = TestSubscriber<DATA>()
//    val subscriber2 = TestSubscriber<DATA>()
//    whenever(dataSource.data(parameter1)).thenReturn(just(data1))
//    whenever(dataSource.data(parameter2)).thenReturn(just(data2))
//
//    memoryParameterDataSource.data(parameter1).subscribe(subscriber1)
//    memoryParameterDataSource.data(parameter2).subscribe(subscriber2)
//
//    subscriber1.assertValue(data1)
//    subscriber2.assertValue(data2)
//    verify(dataSource, times(1)).data(parameter1)
//    verify(dataSource, times(1)).data(parameter2)
//}
//
//private fun <PARAMETER, DATA> dataIsCachedEvenWhenDifferentParameterWasRequestedBefore(
//        parameter1: PARAMETER,
//        parameter2: PARAMETER,
//        data1: DATA,
//        data2: DATA,
//        createMemoryParameterDataSource: (ParameterDataSource<PARAMETER, DATA>) -> ParameterDataSource<PARAMETER, DATA>) {
//
//    val dataSource = mock<ParameterDataSource<PARAMETER, DATA>>()
//    val memoryParameterDataSource = createMemoryParameterDataSource(dataSource)
//    val subscriber1 = TestSubscriber<DATA>()
//    val subscriber2 = TestSubscriber<DATA>()
//    val subscriber3 = TestSubscriber<DATA>()
//    whenever(dataSource.data(parameter1)).thenReturn(just(data1))
//    whenever(dataSource.data(parameter2)).thenReturn(just(data2))
//
//    memoryParameterDataSource.data(parameter1).subscribe(subscriber1)
//    memoryParameterDataSource.data(parameter2).subscribe(subscriber2)
//    memoryParameterDataSource.data(parameter1).subscribe(subscriber3)
//
//    subscriber3.assertValue(data1)
//    verify(dataSource, times(1)).data(parameter1)
//}
//
//private fun <PARAMETER, DATA> requestsDataFromDataSourceAgainWhenErrorHappens(
//        parameter1: PARAMETER,
//        data1: DATA,
//        createMemoryParameterDataSource: (ParameterDataSource<PARAMETER, DATA>) -> ParameterDataSource<PARAMETER, DATA>) {
//
//    val dataSource = mock<ParameterDataSource<PARAMETER, DATA>>()
//    val memoryParameterDataSource = createMemoryParameterDataSource(dataSource)
//    val throwable = Throwable()
//    val subscriber1 = TestSubscriber<DATA>()
//    val subscriber2 = TestSubscriber<DATA>()
//    whenever(dataSource.data(parameter1)).thenReturn(error(throwable), just(data1))
//
//    memoryParameterDataSource.data(parameter1).subscribe(subscriber1)
//    memoryParameterDataSource.data(parameter1).subscribe(subscriber2)
//
//    subscriber1.assertError(throwable)
//    subscriber2.assertValue(data1)
//    verify(dataSource, times(2)).data(parameter1)
//}