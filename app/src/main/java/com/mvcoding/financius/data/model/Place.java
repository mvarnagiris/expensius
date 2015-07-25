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

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.mvcoding.financius.core.endpoints.body.PlaceBody;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @EqualsAndHashCode(callSuper = true) @ToString(callSuper = true) @NoArgsConstructor public class Place extends Model<PlaceBody> {
    public static final Parcelable.Creator<Place> CREATOR = new Parcelable.Creator<Place>() {
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }

        public Place[] newArray(int size) {
            return new Place[size];
        }
    };

    @SerializedName("placeId") private String placeId;
    @SerializedName("name") private String name;
    @SerializedName("address") private String address;
    @SerializedName("latitude") private double latitude;
    @SerializedName("longitude") private double longitude;

    private Place(@NonNull Parcel in) {
        super(in);
        placeId = in.readString();
        name = in.readString();
        address = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public Place(@NonNull com.google.android.gms.location.places.Place place) {
        placeId = place.getId();
        name = place.getName().toString();
        address = place.getAddress().toString();
        latitude = place.getLatLng().latitude;
        longitude = place.getLatLng().longitude;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(placeId);
        dest.writeString(name);
        dest.writeString(address);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }

    @NonNull @Override public PlaceBody toBody() {
        final PlaceBody body = super.toBody();
        body.setPlaceId(placeId);
        body.setName(name);
        body.setAddress(address);
        body.setLatitude(latitude);
        body.setLongitude(longitude);
        return body;
    }

    @NonNull @Override public Place withDefaultValues() {
        super.withDefaultValues();
        return this;
    }

    @NonNull @Override protected PlaceBody createBody() {
        return new PlaceBody();
    }
}
