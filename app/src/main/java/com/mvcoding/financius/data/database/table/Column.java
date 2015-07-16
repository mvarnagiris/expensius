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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public final class Column {
    private final String tableName;
    private final String name;
    private final Type type;
    private final String defaultValue;

    Column(@NonNull String tableName, @NonNull String name, @NonNull Type type) {
        this(tableName, name, type, null);
    }

    Column(@NonNull String tableName, @NonNull String name, @NonNull Type type, @Nullable String defaultValue) {
        this.tableName = tableName;
        this.name = name;
        this.type = type;
        this.defaultValue = defaultValue;
    }

    @NonNull String getCreateScript() {
        return name + " " + type + (defaultValue == null || defaultValue.isEmpty() ? "" : " default " + defaultValue);
    }

    @NonNull public String name() {
        return name;
    }

    enum Type {
        IntegerPrimaryKey("integer primary key autoincrement"),
        Text("text"),
        Integer("integer"),
        Real("real"),
        Boolean("boolean"),
        DateTime("datetime");

        final private String dataType;

        Type(String dataType) {
            this.dataType = dataType;
        }

        @Override public String toString() {
            return dataType;
        }
    }
}
