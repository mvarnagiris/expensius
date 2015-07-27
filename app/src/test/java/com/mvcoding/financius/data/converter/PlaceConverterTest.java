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

package com.mvcoding.financius.data.converter;

import android.database.Cursor;

import com.mvcoding.financius.core.endpoints.body.PlaceBody;
import com.mvcoding.financius.data.database.table.PlaceTable;
import com.mvcoding.financius.data.model.Place;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class PlaceConverterTest extends BaseModelConverterTest<PlaceConverter, Place, PlaceTable, PlaceBody> {
    @Override protected PlaceConverter createConverter() {
        return new PlaceConverter();
    }

    @Override protected Place createModel() {
        return new Place();
    }

    @Override protected PlaceTable getTable() {
        return PlaceTable.get();
    }

    @Override protected int prepareModelCursor(PlaceTable table, Cursor cursor) {
        final int startIndex = super.prepareModelCursor(table, cursor);
        when(cursor.getColumnIndexOrThrow(table.placeId().selectName())).thenReturn(startIndex);
        when(cursor.getColumnIndexOrThrow(table.name().selectName())).thenReturn(startIndex + 1);
        when(cursor.getColumnIndexOrThrow(table.address().selectName())).thenReturn(startIndex + 2);
        when(cursor.getColumnIndexOrThrow(table.latitude().selectName())).thenReturn(startIndex + 3);
        when(cursor.getColumnIndexOrThrow(table.longitude().selectName())).thenReturn(startIndex + 4);
        when(cursor.getString(startIndex)).thenReturn("placeId");
        when(cursor.getString(startIndex + 1)).thenReturn("name");
        when(cursor.getString(startIndex + 2)).thenReturn("address");
        when(cursor.getDouble(startIndex + 3)).thenReturn(1.2);
        when(cursor.getDouble(startIndex + 4)).thenReturn(3.4);
        return startIndex + 5;
    }

    @Override protected void prepareModel(Place model) {
        super.prepareModel(model);
        model.setPlaceId("placeId");
        model.setName("name");
        model.setAddress("address");
        model.setLatitude(1.2);
        model.setLongitude(3.4);
    }

    @Override protected void assertModel(Place model) {
        super.assertModel(model);
        assertThat(model.getPlaceId()).isEqualTo("placeId");
        assertThat(model.getName()).isEqualTo("name");
        assertThat(model.getAddress()).isEqualTo("address");
        assertThat(model.getLatitude()).isEqualTo(1.2);
        assertThat(model.getLongitude()).isEqualTo(3.4);
    }

    @Override protected void assertBody(PlaceBody body) {
        super.assertBody(body);
        assertThat(body.getPlaceId()).isEqualTo("placeId");
        assertThat(body.getName()).isEqualTo("name");
        assertThat(body.getAddress()).isEqualTo("address");
        assertThat(body.getLatitude()).isEqualTo(1.2);
        assertThat(body.getLongitude()).isEqualTo(3.4);
    }
}