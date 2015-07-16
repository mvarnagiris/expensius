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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BaseRelationshipTable extends BaseTable {
    protected BaseRelationshipTable(@NonNull String tableName) {
        super(tableName);
    }

    @Override public void create(@NonNull SQLiteDatabase database) {
        final Column[] relationshipColumns = getRelationshipColumns();
        final Column[] columns = getColumns();

        if (relationshipColumns.length < 2) {
            throw new IllegalStateException("Relationship table must have at least two relationship columns.");
        }

        final StringBuilder sb = new StringBuilder("create table ");
        sb.append(getTableName());
        sb.append(" (");

        final List<Column> allColumns = new ArrayList<>();
        Collections.addAll(allColumns, relationshipColumns);
        Collections.addAll(allColumns, columns);

        int index = 0;
        for (Column column : allColumns) {
            if (index > 0) {
                sb.append(", ");
            }
            sb.append(column.getCreateScript());
            index++;
        }

        index = 0;
        sb.append(",").append(" primary key (");
        for (Column column : relationshipColumns) {
            if (index > 0) {
                sb.append(", ");
            }
            sb.append(column.name());
            index++;
        }
        sb.append(")").append(");");

        database.execSQL(sb.toString());
    }

    @NonNull protected abstract Column[] getRelationshipColumns();
}
