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

import android.content.ContentValues
import com.mvcoding.financius.cache.database.table.Table
import com.squareup.sqlbrite.BriteDatabase
import com.squareup.sqlbrite.SqlBrite
import rx.Observable

class Database(private val database: BriteDatabase) {
    fun save(table: Table, contentValues: ContentValues) {
        // TODO: Update or insert.
        database.insert(table.name, contentValues)
    }

    fun query(queryRequest: QueryRequest): Observable<SqlBrite.Query> {
        val tables: Iterable<String> = queryRequest.getTables().map { it.name }
        val sql = queryRequest.getSql()
        val arguments = queryRequest.getArguments()
        return database.createQuery(tables, sql, *arguments)
    }
}