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

import com.mvcoding.financius.BaseTest;
import com.mvcoding.financius.core.endpoints.body.ModelBody;
import com.mvcoding.financius.core.model.ModelState;
import com.mvcoding.financius.data.database.table.BaseModelTable;
import com.mvcoding.financius.data.model.Model;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class BaseModelConverterTest<C extends ModelConverter<B, M>, M extends Model, T extends BaseModelTable, B extends ModelBody> extends BaseTest {
    private C converter;

    @Override public void setUp() {
        super.setUp();
        converter = createConverter();
    }

    @Test public void from_returnsModelWithAllValuesSetFromCursor() throws Exception {
        final Cursor cursor = mock(Cursor.class);
        prepareModelCursor(getTable(), cursor);

        final M model = converter.from(cursor);

        assertModel(model);
    }

    @Test public void toBody_returnsModelBodyWithAllValuesSetFromModel() throws Exception {
        final M model = createModel();
        prepareModel(model);

        final B body = converter.toBody(model);

        assertBody(body);
    }

    protected abstract C createConverter();

    protected abstract M createModel();

    protected abstract T getTable();

    protected int prepareModelCursor(T table, Cursor cursor) {
        when(cursor.getColumnIndexOrThrow(table.id().selectName())).thenReturn(0);
        when(cursor.getColumnIndexOrThrow(table.modelState().selectName())).thenReturn(1);
        when(cursor.getString(0)).thenReturn("id");
        when(cursor.getString(1)).thenReturn("Normal");
        return 2;
    }

    protected void prepareModel(M model) {
        model.setId("id");
        model.setModelState(ModelState.Normal);
    }

    protected void assertModel(M model) {
        assertThat(model).isNotNull();
        assertThat(model.getId()).isEqualTo("id");
        assertThat(model.getModelState()).isEqualTo(ModelState.Normal);
    }

    protected void assertBody(B body) {
        assertThat(body).isNotNull();
        assertThat(body.getId()).isEqualTo("id");
        assertThat(body.getModelState()).isEqualTo(ModelState.Normal);
    }
}
