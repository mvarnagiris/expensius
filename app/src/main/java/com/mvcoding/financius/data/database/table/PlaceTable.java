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

@Singleton public final class PlaceTable extends BaseModelTable {
    private final Column placeId;
    private final Column name;
    private final Column address;
    private final Column latitude;
    private final Column longitude;

    @Inject PlaceTable() {
        this("place");
    }

    private PlaceTable(@NonNull String tableName) {
        super(tableName);
        placeId = new Column(tableName, "placeId", Column.Type.Text);
        name = new Column(tableName, "name", Column.Type.Text);
        address = new Column(tableName, "address", Column.Type.Text);
        latitude = new Column(tableName, "latitude", Column.Type.Real, "0");
        longitude = new Column(tableName, "longitude", Column.Type.Real, "0");
    }

    @NonNull @Override protected Column[] getModelColumns() {
        return new Column[]{placeId, name, address, latitude, longitude};
    }

    @NonNull public Column placeId() {
        return placeId;
    }

    @NonNull public Column name() {
        return name;
    }

    @NonNull public Column address() {
        return address;
    }

    @NonNull public Column latitude() {
        return latitude;
    }

    @NonNull public Column longitude() {
        return longitude;
    }
}
