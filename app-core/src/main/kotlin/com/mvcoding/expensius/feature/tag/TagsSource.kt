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

package com.mvcoding.expensius.feature.tag

import com.mvcoding.expensius.data.DataSource
import com.mvcoding.expensius.data.ParameterRealtimeDataSource
import com.mvcoding.expensius.data.RealtimeData
import com.mvcoding.expensius.data.RealtimeList
import com.mvcoding.expensius.model.Tag
import com.mvcoding.expensius.model.UserId
import rx.Observable
import java.io.Closeable

class TagsSource(
        appUserIdSource: DataSource<UserId>,
        createRealtimeList: (UserId) -> RealtimeList<Tag>) : DataSource<RealtimeData<Tag>>, Closeable {

    private val dataSource = ParameterRealtimeDataSource(appUserIdSource, createRealtimeList) { it.tagId.id }

    override fun data(): Observable<RealtimeData<Tag>> = dataSource.data()

    override fun close(): Unit = dataSource.close()
}