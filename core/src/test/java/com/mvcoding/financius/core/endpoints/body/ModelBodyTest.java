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

import com.mvcoding.financius.core.model.ModelState;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

public class ModelBodyTest {
    private ModelBody body;

    @Before public void setUp() throws Exception {
        body = new ModelBody() {
        };
    }

    @Test public void validate_doesNotThrowException_whenAllFieldsAreValid() throws Exception {
        makeAllFieldsValid(body);

        body.validate();
    }

    @Test public void validate_throwsRuntimeException_whenIdIsNullOrEmpty() throws Exception {
        try {
            makeAllFieldsValid(body);
            body.setId(null);
            body.validate();
            failBecauseExceptionWasNotThrown(RuntimeException.class);
        } catch (RuntimeException ignore) {
        }

        try {
            makeAllFieldsValid(body);
            body.setId("");
            body.validate();
            failBecauseExceptionWasNotThrown(RuntimeException.class);
        } catch (RuntimeException ignore) {
        }
    }

    @Test public void validate_throwsRuntimeException_whenModelStateIsNull() throws Exception {
        try {
            makeAllFieldsValid(body);
            body.setModelState(null);
            body.validate();
            failBecauseExceptionWasNotThrown(RuntimeException.class);
        } catch (RuntimeException ignore) {
        }
    }

    private void makeAllFieldsValid(ModelBody body) {
        body.setId("id");
        body.setModelState(ModelState.Normal);
    }
}