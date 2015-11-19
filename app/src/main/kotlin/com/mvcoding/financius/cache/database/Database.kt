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
import android.database.Cursor
import com.mvcoding.financius.cache.database.table.Table
import com.squareup.sqlbrite.BriteDatabase
import rx.Observable

class Database(private val database: BriteDatabase) {
    fun save(table: Table, contentValues: ContentValues) {
        val transaction = database.newTransaction()
        try {
            updateOrInsert(table, contentValues)
            transaction.markSuccessful()
        } finally {
            transaction.end()
        }
    }

    fun query(queryRequest: QueryRequest): Observable<Cursor> {
        val tables: Iterable<String> = queryRequest.getTables().map { it.name }
        val sql = queryRequest.getSql()
        val arguments = queryRequest.getArguments()
        return database.createQuery(tables, sql, *arguments).map { it.run() }
    }

    private fun updateOrInsert(table: Table, contentValues: ContentValues) {
        val where = "WHERE ${table.idColumns().joinToString(separator = " AND ", transform = { "${it.name}=?" })}"
        val query = "SELECT ${table.idColumns().joinToString { it.name }} " +
                "FROM ${table.name} " +
                "$where " +
                "LIMIT 1"
        val args = table.idColumns().map { contentValues.getAsString(it.name) }.toTypedArray()
        database.createQuery(table.name, query, *args)
                .mapToOneOrDefault({ true }, false)
                .subscribe {
                    if (it)
                        database.update(table.name, contentValues, where, *args)
                    else
                        database.insert(table.name, contentValues)
                }
    }
}