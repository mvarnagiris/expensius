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

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

@Singleton public class Database {
    private final BriteDatabase database;

    @Inject public Database(@NonNull BriteDatabase database) {
        this.database = database;
    }

    @NonNull public Observable<Cursor> load(@NonNull DatabaseQuery databaseQuery) {
        return database.createQuery(databaseQuery.getTables(), databaseQuery.getSql(), databaseQuery.getArgs()).map(SqlBrite.Query::run);
    }
}
