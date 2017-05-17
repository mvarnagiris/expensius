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

package com.mvcoding.expensius.feature.filter

import com.mvcoding.expensius.data.Cache
import com.mvcoding.expensius.data.DataSource
import com.mvcoding.expensius.data.MemoryCache
import com.mvcoding.expensius.model.LocalFilter
import rx.Observable

class LocalFilterMemoryCache(localFilterSource: DataSource<LocalFilter>) : Cache<LocalFilter> {

    private val memoryCache = MemoryCache(localFilterSource)

    override fun data(): Observable<LocalFilter> = memoryCache.data()
    override fun write(data: LocalFilter) = memoryCache.write(data)
}