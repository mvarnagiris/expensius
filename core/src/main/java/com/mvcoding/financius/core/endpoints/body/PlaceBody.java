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

package com.mvcoding.financius.core.endpoints.body;

public class PlaceBody extends ModelBody {
    private String placeId;
    private String name;
    private String address;
    private double latitude;
    private double longitude;

    @Override public void validate() throws ValidationException {
        super.validate();

        if (!isValidPlaceId()) {
            throw new ValidationException("Place Id cannot be empty.");
        }

        if (!isValidName()) {
            throw new ValidationException("Name cannot be empty.");
        }
    }

    public boolean isValidPlaceId() throws ValidationException {
        return NotEmptyValidator.get().isValid(placeId);
    }

    public boolean isValidName() throws ValidationException {
        return NotEmptyValidator.get().isValid(name);
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
}
