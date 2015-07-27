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
import android.support.annotation.NonNull;

import com.mvcoding.financius.core.endpoints.body.PlaceBody;
import com.mvcoding.financius.data.database.table.BaseModelTable;
import com.mvcoding.financius.data.database.table.PlaceTable;
import com.mvcoding.financius.data.model.Place;

import javax.inject.Inject;

public class PlaceConverter extends ModelConverter<PlaceBody, Place> {
    @Inject public PlaceConverter() {
    }

    @Override public Place from(@NonNull Cursor cursor) {
        final Place place = super.from(cursor);
        final PlaceTable table = PlaceTable.get();
        place.setPlaceId(cursor.getString(cursor.getColumnIndexOrThrow(table.placeId().selectName())));
        place.setName(cursor.getString(cursor.getColumnIndexOrThrow(table.name().selectName())));
        place.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(table.address().selectName())));
        place.setLatitude(cursor.getDouble(cursor.getColumnIndexOrThrow(table.latitude().selectName())));
        place.setLongitude(cursor.getDouble(cursor.getColumnIndexOrThrow(table.longitude().selectName())));
        return place;
    }

    @NonNull @Override public PlaceBody toBody(Place model) {
        final PlaceBody body = super.toBody(model);
        body.setPlaceId(model.getPlaceId());
        body.setName(model.getName());
        body.setAddress(model.getAddress());
        body.setLatitude(model.getLatitude());
        body.setLongitude(model.getLongitude());
        return body;
    }

    @NonNull @Override protected Place createModel() {
        return new Place();
    }

    @NonNull @Override protected BaseModelTable getModelTable() {
        return PlaceTable.get();
    }

    @NonNull @Override protected PlaceBody createBody() {
        return new PlaceBody();
    }
}
