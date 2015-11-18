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

package com.mvcoding.financius.cache.database

import com.mvcoding.financius.cache.database.table.Column
import com.mvcoding.financius.cache.database.table.Table

abstract class QueryRequest {
    protected abstract val tables: List<Table>
    protected abstract val columns: List<Column>

    class Select(private val columns: List<Column>) {
        fun from(table: Table): From = From(columns, table)
    }

    class From(override val columns: List<Column>, table: Table) : QueryRequest() {
        override val tables = listOf(table)
    }

    fun getTables(): Iterable<Table> {
        return tables
    }

    fun getSql(): String {
        return "SELECT ${columns.joinToString { it.name }} FROM ${tables.first().name}"
    }

    fun getArguments(): Array<String> {
        return emptyArray()
    }
}