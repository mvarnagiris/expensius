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

package com.mvcoding.expensius.provider.database

import android.database.Cursor
import com.mvcoding.expensius.paging.PageLoader

class DatabasePageLoader<T>(private val database: Database) : PageLoader<T, QueryRequest, Cursor, Cursor>() {
    override fun load(query: QueryRequest) = database.query(query)

    override fun sizeOf(data: Cursor) = data.count

    override fun dataItemAtPosition(data: Cursor, position: Int): Cursor {
        data.moveToPosition(position)
        return data
    }
}