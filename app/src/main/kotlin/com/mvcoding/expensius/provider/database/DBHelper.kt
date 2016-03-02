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

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.support.v4.content.ContextCompat.getColor
import com.mvcoding.expensius.R
import com.mvcoding.expensius.extension.toContentValues
import com.mvcoding.expensius.model.ModelState.NONE
import com.mvcoding.expensius.model.Tag
import com.mvcoding.expensius.model.generateModelId
import com.mvcoding.expensius.provider.database.table.TagsTable
import com.mvcoding.expensius.provider.database.table.TransactionTagsTable
import com.mvcoding.expensius.provider.database.table.TransactionsTable

class DBHelper(
        private val context: Context,
        private val tagsTable: TagsTable,
        private val transactionsTable: TransactionsTable,
        private val transactionTagsTable: TransactionTagsTable) : SQLiteOpenHelper(context, "expensius.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(tagsTable.createScript())
        db.execSQL(transactionsTable.createScript())
        db.execSQL(transactionTagsTable.createScript())
        insertDefaultValues(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }

    fun insertDefaultValues(db: SQLiteDatabase) {
        db.beginTransaction();

        try {

            listOf(
                    Tag(generateModelId(), NONE, getString(R.string.tag_fixed), getColor(context, R.color.red_300)),
                    Tag(generateModelId(), NONE, getString(R.string.tag_essential), getColor(context, R.color.red_500)),
                    Tag(generateModelId(), NONE, getString(R.string.tag_non_essential), getColor(context, R.color.red_900)),
                    Tag(generateModelId(), NONE, getString(R.string.tag_food), getColor(context, R.color.light_green_500)),
                    Tag(generateModelId(), NONE, getString(R.string.tag_leisure), getColor(context, R.color.light_blue_500)),
                    Tag(generateModelId(), NONE, getString(R.string.tag_clothes), getColor(context, R.color.orange_500)),
                    Tag(generateModelId(), NONE, getString(R.string.tag_transport), getColor(context, R.color.yellow_500)),
                    Tag(generateModelId(), NONE, getString(R.string.tag_health_and_beauty), getColor(context, R.color.pink_500)),
                    Tag(generateModelId(), NONE, getString(R.string.tag_bills_and_utilities), getColor(context, R.color.brown_500)),
                    Tag(generateModelId(), NONE, getString(R.string.tag_pets), getColor(context, R.color.teal_500)))
                    .forEach { db.insert(tagsTable.name, null, it.toContentValues(tagsTable)) }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    private fun getString(resId: Int) = context.getString(resId)
}