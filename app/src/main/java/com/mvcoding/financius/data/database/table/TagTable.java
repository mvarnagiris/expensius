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

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton public final class TagTable extends BaseModelTable {
    private final Column title;
    private final Column color;

    @Inject TagTable() {
        this("tag");
    }

    private TagTable(@NonNull String tableName) {
        super(tableName);
        title = new Column(tableName, "title", Column.Type.Text);
        color = new Column(tableName, "color", Column.Type.Integer, "0");
    }

    @NonNull @Override protected Column[] getModelColumns() {
        return new Column[]{title, color};
    }

    @NonNull public Column title() {
        return title;
    }

    @NonNull public Column color() {
        return color;
    }
}
