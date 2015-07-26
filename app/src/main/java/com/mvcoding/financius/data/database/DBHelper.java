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

package com.mvcoding.financius.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.mvcoding.financius.AppContext;
import com.mvcoding.financius.data.database.table.BaseTable;
import com.mvcoding.financius.data.database.table.PlaceTable;
import com.mvcoding.financius.data.database.table.TagTable;
import com.mvcoding.financius.data.database.table.TransactionTable;
import com.mvcoding.financius.data.database.table.TransactionTagTable;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton public class DBHelper extends SQLiteOpenHelper {
    private static final String NAME = "financius.db";
    private static final int VERSION = 1;

    private final List<BaseTable> tables;

    @Inject DBHelper(@NonNull @AppContext Context context) {
        super(context, NAME, null, VERSION);

        tables = new ArrayList<>();
        tables.add(PlaceTable.get());
        tables.add(TagTable.get());
        tables.add(TransactionTable.get());
        tables.add(TransactionTagTable.get());
    }

    @Override public void onCreate(SQLiteDatabase db) {
        for (BaseTable table : tables) {
            table.create(db);
        }
    }

    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (BaseTable table : tables) {
            table.upgrade(db, oldVersion, newVersion);
        }
    }
}
