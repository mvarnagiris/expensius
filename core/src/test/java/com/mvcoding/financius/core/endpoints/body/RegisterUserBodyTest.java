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

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

public class RegisterUserBodyTest {
    private RegisterUserBody registerUserBody;

    @Before public void setUp() {
        registerUserBody = new RegisterUserBody();
    }

    @Test public void validate_throwsRuntimeException_whenAtLeastOneFieldIsInvalid() {
        try {
            makeAllFieldsValid(registerUserBody);
            registerUserBody.setGoogleId(null);
            registerUserBody.validate();
            Assertions.failBecauseExceptionWasNotThrown(RuntimeException.class);
        } catch (RuntimeException ignore) {
        }
    }

    @Test public void validate_doesNotThrowException_whenAllFieldsAreValid() {
        makeAllFieldsValid(registerUserBody);

        registerUserBody.validate();
    }

    private void makeAllFieldsValid(RegisterUserBody registerUserBody) {
        registerUserBody.setGoogleId("any");
    }
}
