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

import com.mvcoding.financius.core.BaseTest;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class NotEmptyValidatorTest extends BaseTest {
    private final NotEmptyValidator validator = new NotEmptyValidator();

    @Test public void isValid_returnsTrue_whenValueIsNotEmpty() throws Exception {
        assertThat(validator.isValid("notEmpty")).isTrue();
    }

    @Test public void isValid_returnsFalse_whenValueIsNullOrEmpty() throws Exception {
        assertThat(validator.isValid("")).isFalse();
        assertThat(validator.isValid(null)).isFalse();
    }
}