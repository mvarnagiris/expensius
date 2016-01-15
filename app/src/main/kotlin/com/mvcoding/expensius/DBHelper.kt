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

package com.mvcoding.expensius

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.support.v4.content.ContextCompat
import com.mvcoding.expensius.extension.toContentValues
import com.mvcoding.expensius.feature.tag.Tag
import com.mvcoding.expensius.provider.database.table.TagsTable

class DBHelper(private val context: Context, private val tagsTable: TagsTable) : SQLiteOpenHelper(context, "expensius.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(tagsTable.createScript())
        insertDefaultValues(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }

    fun insertDefaultValues(db: SQLiteDatabase) {
        db.beginTransaction();

        try {
            val fixed = Tag(title = context.getString(R.string.fixed), color = ContextCompat.getColor(context, R.color.green_500));
            val essential = Tag(title = context.getString(R.string.essential), color = ContextCompat.getColor(context, R.color.light_blue_500));
            val nonEssential = Tag(title = context.getString(R.string.non_essential), color = ContextCompat.getColor(context, R.color.red_500));

            listOf(fixed, essential, nonEssential).forEach { db.insert(tagsTable.name, null, it.toContentValues(tagsTable)) }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }
}