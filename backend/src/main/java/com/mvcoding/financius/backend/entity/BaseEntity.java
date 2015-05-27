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

import java.util.UUID;

public class BaseEntity {
    @Id @ApiResourceProperty(name = "id") private String id;
    @Index @ApiResourceProperty(name = "create_ts") private long createTimestamp;
    @Index @ApiResourceProperty(name = "edit_ts") private long editTimestamp;

    public void onCreate() {
        if (Strings.isNullOrEmpty(getId())) {
            setId(UUID.randomUUID().toString());
        }
        final long timestamp = System.currentTimeMillis();
        setCreateTimestamp(timestamp);
        setEditTimestamp(timestamp);
    }

    public void onUpdate() {
        setEditTimestamp(System.currentTimeMillis());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(long createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    public long getEditTimestamp() {
        return editTimestamp;
    }

    public void setEditTimestamp(long editTimestamp) {
        this.editTimestamp = editTimestamp;
    }
}
