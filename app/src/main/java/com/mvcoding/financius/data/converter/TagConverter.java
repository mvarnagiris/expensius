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

package com.mvcoding.financius.data.converter;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.mvcoding.financius.core.endpoints.body.TagBody;
import com.mvcoding.financius.data.database.table.BaseModelTable;
import com.mvcoding.financius.data.database.table.TagTable;
import com.mvcoding.financius.data.model.Tag;

import javax.inject.Inject;

public class TagConverter extends ModelConverter<TagBody, Tag> {
    @Inject public TagConverter() {
    }

    @Override public Tag from(@NonNull Cursor cursor) {
        final Tag tag = super.from(cursor);
        final TagTable table = TagTable.get();
        tag.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(table.title().selectName())));
        tag.setColor(cursor.getInt(cursor.getColumnIndexOrThrow(table.color().selectName())));
        return tag;
    }

    @NonNull @Override public TagBody toBody(Tag model) {
        final TagBody body = super.toBody(model);
        body.setTitle(model.getTitle());
        body.setColor(model.getColor());
        return body;
    }

    @NonNull @Override protected Tag createModel() {
        return new Tag();
    }

    @NonNull @Override protected BaseModelTable getModelTable() {
        return TagTable.get();
    }

    @NonNull @Override protected TagBody createBody() {
        return new TagBody();
    }
}
