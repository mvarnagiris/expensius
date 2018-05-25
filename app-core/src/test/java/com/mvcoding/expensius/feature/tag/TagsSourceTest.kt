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

package com.mvcoding.expensius.feature.tag

import com.mvcoding.expensius.data.testParameterRealtimeDataSource
import com.mvcoding.expensius.model.Tag
import com.mvcoding.expensius.model.UserId
import com.mvcoding.expensius.model.extensions.aUserId
import org.junit.Test

class TagsSourceTest {

    @Test
    fun `behaves like parameter realtime data source`() {
        testParameterRealtimeDataSource<UserId, Tag, TagsSource>(aUserId(), aUserId()) { parameterDataSource, createRealtimeList ->
            TagsSource(parameterDataSource, createRealtimeList)
        }
    }
}

