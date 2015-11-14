package com.mvcoding.financius.database

import android.content.ContentValues
import android.database.Cursor
import com.mvcoding.financius.database.table.Table
import rx.Observable

interface Database {
    fun save(table: Table, contentValues: ContentValues)
    fun load(query: Query): Observable<Cursor>
}

