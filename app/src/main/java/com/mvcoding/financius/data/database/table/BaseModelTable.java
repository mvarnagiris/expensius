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

import android.provider.BaseColumns;
import android.support.annotation.NonNull;

import com.mvcoding.financius.core.model.ModelState;

public abstract class BaseModelTable extends BaseTable {
    private final Column _id;
    private final Column id;
    private final Column modelState;

    protected BaseModelTable(@NonNull String tableName) {
        super(tableName);
        _id = new Column(tableName, BaseColumns._ID, Column.Type.IntegerAutoIncrement);
        id = new Column(tableName, "id", Column.Type.TextPrimaryKey);
        modelState = new Column(tableName, "modelState", Column.Type.Text, ModelState.Normal.name());
    }

    @NonNull public String[] getQueryColumns() {
        final Column[] modelColumns = getModelColumns();
        final String[] queryColumns = new String[modelColumns.length + 2];
        queryColumns[0] = id.selectName();
        queryColumns[1] = modelState.selectName();

        int index = 2;
        for (Column column : modelColumns) {
            queryColumns[index++] = column.selectName();
        }

        return queryColumns;
    }

    @NonNull @Override protected Column[] getColumns() {
        final Column[] modelColumns = getModelColumns();
        final Column[] allColumns = new Column[modelColumns.length + 3];
        allColumns[0] = _id;
        allColumns[1] = id;
        allColumns[2] = modelState;
        System.arraycopy(modelColumns, 0, allColumns, 3, modelColumns.length);

        return allColumns;
    }

    @NonNull protected abstract Column[] getModelColumns();

    @NonNull public final Column id() {
        return id;
    }

    @NonNull public final Column modelState() {
        return modelState;
    }
}
