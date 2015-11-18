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

import com.mvcoding.financius.cache.Database
import com.mvcoding.financius.cache.DatabaseQueryResult
import com.mvcoding.financius.cache.Query
import com.mvcoding.financius.cache.Table
import com.squareup.sqlbrite.BriteDatabase
import rx.Observable

class SqliteDatabase(private val database: BriteDatabase) : Database<ContentValuesDatabaseRecord, Query> {
    override fun save(table: Table, databaseRecord: ContentValuesDatabaseRecord) {
        // TODO: Do update or insert.
        database.insert(table.name, databaseRecord.contentValues)
    }

    override fun <T> load(query: Query): Observable<DatabaseQueryResult<T>> {
        throw UnsupportedOperationException()
    }
}