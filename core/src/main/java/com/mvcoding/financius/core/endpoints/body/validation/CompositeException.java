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

package com.mvcoding.financius.core.endpoints.body.validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CompositeException extends RuntimeException {
    private final List<RuntimeException> exceptions;

    public CompositeException(RuntimeException... exceptions) {
        if (exceptions == null || exceptions.length == 0) {
            this.exceptions = Collections.emptyList();
        } else {
            this.exceptions = new ArrayList<RuntimeException>();
            Collections.addAll(this.exceptions, exceptions);
        }
    }

    public List<RuntimeException> getExceptions() {
        return exceptions;
    }
}
