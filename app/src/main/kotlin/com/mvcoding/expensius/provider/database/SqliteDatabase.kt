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

package com.mvcoding.expensius.provider.database

import android.content.ContentValues
import android.database.Cursor
import com.mvcoding.expensius.provider.database.table.Table
import com.squareup.sqlbrite.BriteDatabase
import rx.Observable

class SqliteDatabase(private val database: BriteDatabase) : Database {
    override fun save(databaseActions: List<DatabaseAction>) {
        val transaction = database.newTransaction()
        try {
            databaseActions.forEach {
                when (it) {
                    is SaveDatabaseAction -> updateOrInsert(it.table, it.contentValues)
                    is DeleteDatabaseAction -> delete(it.table, it.whereClause, it.whereArgs)
                    else -> throw IllegalArgumentException("$it is not supported.")
                }

            }
            transaction.markSuccessful()
        } finally {
            transaction.end()
        }
    }

    override fun query(queryRequest: QueryRequest): Observable<Cursor> {
        val tables: Iterable<String> = queryRequest.tables.map { it.name }
        val sql = queryRequest.sql()
        val arguments = queryRequest.arguments
        return database.createQuery(tables, sql, *arguments).map { it.run() }
    }

    private fun updateOrInsert(table: Table, contentValues: ContentValues) {
        val where = "${table.idColumns().joinToString(separator = " AND ", transform = { "${it.name}=?" })}"
        val query = "SELECT ${table.idColumns().joinToString { it.toString() }} " +
                    "FROM ${table.name} " +
                    "WHERE $where " +
                    "LIMIT 1"
        val args = table.idColumns().map {
            contentValues.getAsString(it.name).let { value -> if (value.isBlank()) "0" else value }
        }.toTypedArray()
        val cursor = database.query(query, *args)
        if (cursor.moveToFirst()) {
            database.update(table.name, contentValues, where, *args)
        } else {
            database.insert(table.name, contentValues)
        }
    }

    private fun delete(table: Table, whereClause: String?, whereArgs: Array<String>?) {
        database.delete(table.name, whereClause, *whereArgs.orEmpty())
    }
}