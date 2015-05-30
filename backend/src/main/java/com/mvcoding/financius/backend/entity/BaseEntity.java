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

package com.mvcoding.financius.backend.entity;

import com.google.api.server.spi.config.ApiResourceProperty;
import com.google.common.base.Strings;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.mvcoding.financius.core.model.ModelState;

import java.util.UUID;

public class BaseEntity {
    @Id @ApiResourceProperty(name = "id") private String id;
    @Index @ApiResourceProperty(name = "timestamp") private long timestamp;
    @ApiResourceProperty(name = "modelState") private ModelState modelState;

    public void updateDefaults() {
        if (Strings.isNullOrEmpty(id)) {
            id = UUID.randomUUID().toString();
        }

        if (modelState == null) {
            modelState = ModelState.Normal;
        }

        setTimestamp(System.currentTimeMillis());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public ModelState getModelState() {
        return modelState;
    }

    public void setModelState(ModelState modelState) {
        this.modelState = modelState;
    }
}
