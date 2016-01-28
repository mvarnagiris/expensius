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

fun select(columns: Array<Column>) = Select(columns)
fun select(vararg table: Table) = Select(*table)

interface Element {
    fun elementPartSql(): String
}

abstract class Sql(private val previousElement: Element, val columns: Array<Column>, val tables: Array<Table>) : Element {
    fun sql(): String = "${if (previousElement is Sql) previousElement.sql() else previousElement.elementPartSql() } ${elementPartSql()}"
}

class Select(private val columns: Array<Column>) : Element {
    constructor(vararg table: Table) : this(table.map { it.columns() }.flatten().toTypedArray())

    fun from(table: Table) = From(this, columns, table)
    override fun elementPartSql() = "SELECT ${columns.joinToString { it.name }}"
}

class From(
        previousElement: Element,
        columns: Array<Column>,
        table: Table) : Sql(previousElement, columns, arrayOf(table)) {

    override fun elementPartSql() = "FROM ${tables.last().name}"
    fun leftJoin(table: Table, on: String) = Join(this, columns, tables.plus(table), "LEFT", on)
    fun where(clause: String) = Where(this, columns, tables, "WHERE", clause)
}

class Join(
        previousElement: Element,
        columns: Array<Column>,
        tables: Array<Table>,
        private val joinType: String,
        private val on: String) : Sql(previousElement, columns, tables) {

    override fun elementPartSql() = "$joinType JOIN ${tables.last().name} ON $on"
    fun where(clause: String) = Where(this, columns, tables, "WHERE", clause)
}

class Where(
        previousElement: Element,
        columns: Array<Column>,
        tables: Array<Table>,
        private val keyword: String,
        private val clause: String) : Sql(previousElement, columns, tables) {

    override fun elementPartSql() = "$keyword $clause"

    fun and(clause: String) = Where(this, columns, tables, "AND", clause)
    fun or(clause: String) = Where(this, columns, tables, "OR", clause)
}