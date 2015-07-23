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

import com.google.common.base.Strings;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

class NotEmptyValidator implements Validator<String> {
    private static final NotEmptyValidator INSTANCE = new NotEmptyValidator();

    @NotNull public static NotEmptyValidator get() {
        return INSTANCE;
    }

    @Override public boolean isValid(@Nullable String value) {
        return Strings.isNullOrEmpty(value);
    }
}