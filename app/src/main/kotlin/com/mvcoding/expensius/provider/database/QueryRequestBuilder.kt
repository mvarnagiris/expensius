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

import com.mvcoding.expensius.provider.database.table.Column
import com.mvcoding.expensius.provider.database.table.Table
import com.mvcoding.expensius.provider.database.table.TransactionsTable

interface Element {
    fun sql(): String
}

abstract class Sql(private var elements: List<Element>) : Element {
    override fun sql() = elements.joinToString(separator = " ", transform = { it.sql() })
}

class Select(private vararg val columns: Column) : Sql(emptyList()) {
    constructor(vararg table: Table) : this(*table.map { it.columns() }.flatten().toTypedArray())

    fun from(table: Table) = From(table)

    override fun sql() = "SELECT ${columns.joinToString { it.name }}"
}

class From(val table: Table) : Element {
    override fun sql() = table.name
}

fun test() {
    Select(TransactionsTable()).from()
}