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

interface Element {
    fun elementPartSql(): String
}

class Select(private val columns: Array<Column>) : Element {
    constructor(vararg table: Table) : this(table.map { it.columns() }.flatten().toTypedArray())

    fun from(table: Table) = From(this, columns, table)

    override fun elementPartSql() = "SELECT ${columns.joinToString { it.name }}"
}

abstract class Sql(private val previousElement: Element, val columns: Array<Column>, val tables: Array<Table>) : Element {
    fun sql() = "${previousElement.elementPartSql()} ${elementPartSql()}"
}

abstract class BaseFrom(previousElement: Element, columns: Array<Column>, tables: Array<Table>) : Sql(previousElement, columns, tables) {
    override fun elementPartSql() = "${keyword()} ${tables.last()}"
    abstract fun keyword(): String
    fun leftJoin(table: Table) = LeftJoin(this, columns, tables.plus(table))
    fun where(clause: String) = Where(this, columns, tables, "WHERE", clause)
}

class From(previousElement: Element, columns: Array<Column>, table: Table) : BaseFrom(previousElement, columns, arrayOf(table)) {
    override fun keyword() = "FROM"
}

class LeftJoin(previousElement: Element, columns: Array<Column>, tables: Array<Table>) : BaseFrom(previousElement, columns, tables) {
    override fun keyword() = "LEFT JOIN"
    fun on(clause: String) = On(this, columns, tables, clause)
}

class On(
        previousElement: Element,
        columns: Array<Column>,
        tables: Array<Table>,
        private val clause: String) : BaseFrom(previousElement, columns, tables) {

    override fun elementPartSql(): String {
        return "${keyword()} $clause"
    }

    override fun keyword() = "ON"
}

class From2(
        previousElement: Element,
        columns: Array<Column>,
        table: Table) : Sql(previousElement, columns, arrayOf(table)) {

    override fun elementPartSql() = "FROM ${tables.last().name}"
}

class Join

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