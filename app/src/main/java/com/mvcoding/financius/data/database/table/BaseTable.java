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

package com.mvcoding.financius.data.database.table;

import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

abstract class BaseTable {
    private final String tableName;

    protected BaseTable(@NonNull String tableName) {
        this.tableName = tableName;
    }

    @NonNull public String getTableName() {
        return tableName;
    }

    public void create(@NonNull SQLiteDatabase database) {
        final StringBuilder sb = new StringBuilder("create table ");
        sb.append(tableName);
        sb.append(" (");

        final Column[] columns = getColumns();
        int index = 0;
        for (Column column : columns) {
            if (index > 0) {
                sb.append(", ");
            }
            sb.append(column.getCreateScript());
            index++;
        }
        sb.append(");");

        database.execSQL(sb.toString());
    }

    @NonNull protected abstract Column[] getColumns();
}
