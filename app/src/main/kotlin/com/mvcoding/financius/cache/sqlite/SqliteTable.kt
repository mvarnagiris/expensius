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

package com.mvcoding.financius.cache.sqlite

import com.mvcoding.financius.cache.Table

abstract class SqliteTable(override val name: String) : Table {
    fun createScript(): String {
        return "create table $name (${columns().joinToString { it.createScript() }})"
    }

    abstract fun columns(): Array<SqliteColumn>
}