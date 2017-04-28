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
import com.mvcoding.expensius.data.testParameterRealtimeDataSource
import com.mvcoding.expensius.model.BasicTransaction
import com.mvcoding.expensius.model.Filter
import com.mvcoding.expensius.model.Tag
import com.mvcoding.expensius.model.aFilter
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Test
import rx.Observable

class TransactionsSourceTest {

    @Test
    fun `behaves like parameter realtime data source`() {
        val allTagsSource = mock<DataSource<List<Tag>>>()
        whenever(allTagsSource.data()).thenReturn(Observable.just(emptyList()))
        testParameterRealtimeDataSource<Filter, BasicTransaction, TransactionsSource>(aFilter(), aFilter()) { parameterDataSource, createRealtimeList ->
            TransactionsSource(allTagsSource, parameterDataSource, createRealtimeList)
        }
    }
}