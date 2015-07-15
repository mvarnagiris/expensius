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

import android.support.annotation.NonNull;

import com.mvcoding.financius.core.model.ModelState;

import java.util.UUID;

public abstract class Model {
    private long _id;
    private String id;
    private ModelState modelState;

    @NonNull public String getId() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    @NonNull public ModelState getModelState() {
        return modelState;
    }

    public void setModelState(@NonNull ModelState modelState) {
        this.modelState = modelState;
    }
}
