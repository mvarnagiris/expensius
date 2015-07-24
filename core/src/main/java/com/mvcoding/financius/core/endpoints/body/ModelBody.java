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

import lombok.Data;

@Data public abstract class ModelBody implements Body {
    private String id;
    private ModelState modelState;

    @Override public void validate() throws ValidationException {
        if (!isValidId()) {
            throw new ValidationException("Id cannot be empty.");
        }

        if (!isValidModelState()) {
            throw new ValidationException("ModelState cannot be null.");
        }
    }

    public boolean isValidId() throws ValidationException {
        return NotEmptyValidator.get().isValid(id);
    }

    public boolean isValidModelState() throws ValidationException {
        return NotNullValidator.get().isValid(modelState);
    }
}
