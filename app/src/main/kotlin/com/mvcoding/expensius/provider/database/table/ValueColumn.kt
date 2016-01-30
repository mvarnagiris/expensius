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

package com.mvcoding.expensius.provider.database.table

data class ValueColumn(override val query: String, private val type: ValueColumn.Type, private val defaultValue: String = "") : Column {
    override val name = query

    constructor(table: Table, name: String, type: ValueColumn.Type, defaultValue: String = "") : this(
            "${table.name}_$name",
            type,
            defaultValue)

    fun createScript() = "$name $type ${if (defaultValue.isBlank()) "" else "default $defaultValue"}"

    override fun toString() = if (name == query) name else "$query as $name"

    enum class Type(val dataType: String) {
        TEXT("text"),
        TEXT_PRIMARY_KEY("text primary key"),
        INTEGER("integer"),
        REAL("real"),
        BOOLEAN("boolean"),
        DATE_TIME("datetime");

        override fun toString(): String {
            return dataType
        }
    }
}