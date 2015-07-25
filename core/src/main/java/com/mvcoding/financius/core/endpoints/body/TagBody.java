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

@Data @EqualsAndHashCode(callSuper = true) public class TagBody extends ModelBody {
    private String title;
    private int color;

    @Override public void validate() throws ValidationException {
        super.validate();

        if (!isValidTitle()) {
            throw new ValidationException("Title cannot be empty.");
        }
    }

    public boolean isValidTitle() throws ValidationException {
        return NotEmptyValidator.get().isValid(title);
    }
}
