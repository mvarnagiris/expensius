/*
 * Copyright (C) 2015 Mantas Varnagiris.
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

package com.mvcoding.financius.extension

import com.mvcoding.financius.cache.Query
import com.mvcoding.financius.cache.sqlite.SqliteColumn
import com.mvcoding.financius.cache.sqlite.SqliteTable

fun column(table: SqliteTable, name: String, type: SqliteColumn.Type, defaultValue: String = ""): SqliteColumn {
    return SqliteColumn("${table.name}_$name", type, defaultValue)
}

fun select(columns: Array<SqliteColumn>): Query.Select = Query.Select(columns);