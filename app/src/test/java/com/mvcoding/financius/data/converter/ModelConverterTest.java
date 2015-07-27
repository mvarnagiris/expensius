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

import android.support.annotation.NonNull;

import com.mvcoding.financius.core.endpoints.body.ModelBody;
import com.mvcoding.financius.data.database.table.BaseModelTable;
import com.mvcoding.financius.data.database.table.Column;
import com.mvcoding.financius.data.model.Model;

import org.mockito.Mock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ModelConverterTest extends BaseModelConverterTest<ModelConverter<ModelBody, Model>, Model, BaseModelTable, ModelBody> {
    @Mock private BaseModelTable table;

    @Override protected ModelConverter<ModelBody, Model> createConverter() {
        final Column idColumn = mock(Column.class);
        final Column modelStateColumn = mock(Column.class);
        when(idColumn.selectName()).thenReturn("table.id");
        when(modelStateColumn.selectName()).thenReturn("table.modelState");
        when(table.id()).thenReturn(idColumn);
        when(table.modelState()).thenReturn(modelStateColumn);
        final String[] columns = new String[]{idColumn.selectName(), modelStateColumn.selectName()};
        when(table.getQueryColumns()).thenReturn(columns);

        return new ModelConverter<ModelBody, Model>() {
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

    @Override protected Model createModel() {
        return new Model() {
        };
    }

    @Override protected BaseModelTable getTable() {
        return table;
    }
}