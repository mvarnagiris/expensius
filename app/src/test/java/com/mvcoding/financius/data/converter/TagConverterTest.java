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

import com.mvcoding.financius.core.endpoints.body.TagBody;
import com.mvcoding.financius.data.database.table.TagTable;
import com.mvcoding.financius.data.model.Tag;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class TagConverterTest extends BaseModelConverterTest<TagConverter, Tag, TagTable, TagBody> {
    @Override protected TagConverter createConverter() {
        return new TagConverter();
    }

    @Override protected Tag createModel() {
        return new Tag();
    }

    @Override protected TagTable getTable() {
        return TagTable.get();
    }

    @Override protected int prepareModelCursor(TagTable table, Cursor cursor) {
        final int startIndex = super.prepareModelCursor(table, cursor);
        when(cursor.getColumnIndexOrThrow(table.title().name())).thenReturn(startIndex);
        when(cursor.getColumnIndexOrThrow(table.color().name())).thenReturn(startIndex + 1);
        when(cursor.getString(startIndex)).thenReturn("title");
        when(cursor.getInt(startIndex + 1)).thenReturn(1);
        return startIndex + 2;
    }

    @Override protected void prepareModel(Tag model) {
        super.prepareModel(model);
        model.setTitle("title");
        model.setColor(1);
    }

    @Override protected void assertModel(Tag model) {
        super.assertModel(model);
        assertThat(model.getTitle()).isEqualTo("title");
        assertThat(model.getColor()).isEqualTo(1);
    }

    @Override protected void assertBody(TagBody body) {
        super.assertBody(body);
        assertThat(body.getTitle()).isEqualTo("title");
        assertThat(body.getColor()).isEqualTo(1);
    }
}