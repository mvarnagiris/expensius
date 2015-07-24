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

import com.google.gson.annotations.SerializedName;
import com.mvcoding.financius.core.endpoints.body.TagBody;

public class Tag extends Model<TagBody> {
    @SerializedName("title") private String title;
    @SerializedName("color") private int color;

    @NonNull @Override public TagBody toBody() {
        final TagBody body = super.toBody();
        body.setTitle(title);
        body.setColor(color);
        return body;
    }

    @NonNull @Override public Tag withDefaultValues() {
        super.withDefaultValues();
        return this;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @NonNull @Override protected TagBody createBody() {
        return new TagBody();
    }
}
