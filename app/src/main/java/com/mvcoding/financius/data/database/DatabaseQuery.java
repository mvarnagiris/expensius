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

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DatabaseQuery {
    private final List<String> columns = new ArrayList<>();
    private final List<String> args = new ArrayList<>();
    private final StringBuilder selection = new StringBuilder();
    private String table;

    public DatabaseQuery select(String... columns) {
        Collections.addAll(this.columns, columns);
        return this;
    }

    public DatabaseQuery from(@NonNull String table) {
        if (this.table != null) {
            throw new IllegalStateException("Table already set to " + this.table);
        }
        this.table = table;
        return this;
    }

    public DatabaseQuery where(@NonNull String selection, String... args) {
        if (this.selection.length() > 0) {
            throw new IllegalStateException("Where clause already set to " + this.selection);
        }

        this.selection.append(selection);
        Collections.addAll(this.args, args);
        return this;
    }

    @NonNull public List<String> getTables() {
        validate();
        return Collections.singletonList(table);
    }

    @NonNull public String getSql() {
        validate();
        final StringBuilder query = new StringBuilder();

        query.append("SELECT ");
        if (columns.isEmpty()) {
            query.append("*");
        } else {
            boolean isNotFirstItem = false;
            for (String column : columns) {
                if (isNotFirstItem) {
                    query.append(",");
                }
                query.append(column);
                isNotFirstItem = true;
            }
        }

        query.append(" FROM ").append(table);
        query.append(" WHERE ").append(selection);

        return query.toString();
    }

    @NonNull public String[] getArgs() {
        return args.toArray(new String[args.size()]);
    }

    private void validate() {
        if (table == null || table.isEmpty()) {
            throw new IllegalStateException("Table cannot be empty.");
        }
    }
}
