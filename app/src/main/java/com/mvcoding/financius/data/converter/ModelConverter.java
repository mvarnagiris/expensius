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

import com.mvcoding.financius.core.endpoints.body.ModelBody;
import com.mvcoding.financius.core.model.ModelState;
import com.mvcoding.financius.data.DataConverter;
import com.mvcoding.financius.data.database.table.BaseModelTable;
import com.mvcoding.financius.data.model.Model;

abstract class ModelConverter<B extends ModelBody, M extends Model> implements DataConverter<M> {
    @Override public M from(@NonNull Cursor cursor) {
        final BaseModelTable table = getModelTable();
        final M model = createModel();
        model.setId(cursor.getString(cursor.getColumnIndexOrThrow(table.id().selectName())));
        model.setModelState(ModelState.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(table.modelState().selectName()))));
        return model;
    }

    @NonNull public B toBody(M model) {
        final B body = createBody();
        body.setId(model.getId());
        body.setModelState(model.getModelState());
        return body;
    }

    @NonNull protected abstract M createModel();

    @NonNull protected abstract BaseModelTable getModelTable();

    @NonNull protected abstract B createBody();
}
