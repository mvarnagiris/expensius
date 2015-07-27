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

import com.mvcoding.financius.BaseTest;
import com.mvcoding.financius.core.endpoints.body.ModelBody;
import com.mvcoding.financius.core.model.ModelState;
import com.mvcoding.financius.data.database.table.BaseModelTable;
import com.mvcoding.financius.data.database.table.Column;
import com.mvcoding.financius.data.model.Model;

import org.junit.Test;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ModelConverterTest extends BaseTest {
    @Mock private BaseModelTable table;

    private ModelConverter<ModelBody, Model> modelConverter;

    @Override public void setUp() {
        super.setUp();

        final Column idColumn = mock(Column.class);
        final Column modelStateColumn = mock(Column.class);
        when(idColumn.selectName()).thenReturn("table.id");
        when(modelStateColumn.selectName()).thenReturn("table.modelState");
        when(table.id()).thenReturn(idColumn);
        when(table.modelState()).thenReturn(modelStateColumn);
        final String[] columns = new String[]{idColumn.selectName(), modelStateColumn.selectName()};
        when(table.getQueryColumns()).thenReturn(columns);

        modelConverter = new ModelConverter<ModelBody, Model>() {
            @NonNull @Override protected Model createModel() {
                return new Model() {
                };
            }

            @NonNull @Override protected BaseModelTable getModelTable() {
                return table;
            }

            @NonNull @Override protected ModelBody createBody() {
                return new ModelBody() {
                };
            }
        };
    }

    @Test public void from_returnsModelWithAllValuesSetFromCursor() throws Exception {
        final Cursor cursor = mock(Cursor.class);
        when(cursor.getColumnIndexOrThrow(table.id().selectName())).thenReturn(0);
        when(cursor.getColumnIndexOrThrow(table.modelState().selectName())).thenReturn(1);
        when(cursor.getString(0)).thenReturn("id");
        when(cursor.getString(1)).thenReturn("Normal");

        final Model model = modelConverter.from(cursor);

        assertThat(model).isNotNull();
        assertThat(model.getId()).isEqualTo("id");
        assertThat(model.getModelState()).isEqualTo(ModelState.Normal);
    }

    @Test public void toBody_returnsModelBodyWithAllValuesSet() throws Exception {
        final Model model = new Model() {
        };
        model.setId("id");
        model.setModelState(ModelState.Normal);

        final ModelBody body = modelConverter.toBody(model);

        assertThat(body).isNotNull();
        assertThat(body.getId()).isEqualTo("id");
        assertThat(body.getModelState()).isEqualTo(ModelState.Normal);
    }
}