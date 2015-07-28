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

public class DynamicColumn {
    private final String query;
    private final String selectName;

    DynamicColumn(@NonNull String query, @NonNull String selectName) {
        this.query = query + " as " + selectName;
        this.selectName = selectName;
    }

    @NonNull public String query() {
        return query;
    }

    @NonNull public String selectName() {
        return selectName;
    }

    @Override public String toString() {
        return query;
    }
}
