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

package com.mvcoding.financius.data.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.mvcoding.financius.core.endpoints.body.PlaceBody;

public class Place extends Model<PlaceBody> {
    @SerializedName("placeId") private String placeId;
    @SerializedName("name") private String name;
    @SerializedName("address") private String address;
    @SerializedName("latitude") private double latitude;
    @SerializedName("longitude") private double longitude;

    @NonNull @Override public PlaceBody toBody() {
        final PlaceBody body = super.toBody();
        body.setPlaceId(placeId);
        body.setName(name);
        body.setAddress(address);
        body.setLatitude(latitude);
        body.setLongitude(longitude);
        return body;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @NonNull @Override protected PlaceBody createBody() {
        return new PlaceBody();
    }
}
