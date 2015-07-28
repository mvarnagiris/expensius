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

import com.mvcoding.financius.BaseTest;
import com.mvcoding.financius.core.model.ModelState;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ModelTest extends BaseTest {
    private Model model;

    @Override public void setUp() {
        super.setUp();
        model = new Model() {
        };
    }

    @Test public void withDefaultValues_setsDefaultValuesOnSelf() throws Exception {
        model.withDefaultValues();

        assertThat(model.getId()).isNotNull();
        assertThat(model.getId()).isNotEmpty();
        assertThat(model.getModelState()).isEqualTo(ModelState.Normal);
    }
}