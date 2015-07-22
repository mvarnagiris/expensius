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

import org.junit.Before;

public abstract class BaseBodyTest<B extends Body> extends BaseTest {
    protected B body;

    @Before public void setUp() throws Exception {
        super.setUp();
        body = createBody();
        makeAllFieldsValid(body);
    }

    protected abstract B createBody();

    protected abstract void makeAllFieldsValid(B body);
}
