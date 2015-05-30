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

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

public class PlaceBodyTest {
    private PlaceBody body;

    @Before public void setUp() {
        body = new PlaceBody();
    }

    @Test public void validate_doesNotThrowException_whenAllFieldsAreValid() {
        makeAllFieldsValid(body);

        body.validate();
    }

    @Test public void validate_throwsRuntimeException_whenPlaceIdIsNullOrEmpty() {
        try {
            makeAllFieldsValid(body);
            body.setPlaceId(null);
            body.validate();
            failBecauseExceptionWasNotThrown(RuntimeException.class);
        } catch (RuntimeException ignore) {
        }

        try {
            makeAllFieldsValid(body);
            body.setPlaceId("");
            body.validate();
            failBecauseExceptionWasNotThrown(RuntimeException.class);
        } catch (RuntimeException ignore) {
        }
    }

    @Test public void validate_throwsRuntimeException_whenNameNullOrEmpty() {
        try {
            makeAllFieldsValid(body);
            body.setName(null);
            body.validate();
            failBecauseExceptionWasNotThrown(RuntimeException.class);
        } catch (RuntimeException ignore) {
        }

        try {
            makeAllFieldsValid(body);
            body.setName("");
            body.validate();
            failBecauseExceptionWasNotThrown(RuntimeException.class);
        } catch (RuntimeException ignore) {
        }
    }

    private void makeAllFieldsValid(PlaceBody body) {
        body.setPlaceId("any");
        body.setName("any");
        body.setAddress("any");
        body.setLatitude(0);
        body.setLongitude(0);
    }
}
