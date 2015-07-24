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

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data @EqualsAndHashCode(callSuper = true) public class PlaceBody extends ModelBody {
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
}
