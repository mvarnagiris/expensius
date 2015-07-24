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

import lombok.Data;

@Data public abstract class BaseEntity {
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

        timestamp = System.currentTimeMillis();
    }
}
