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

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import com.mvcoding.financius.core.endpoints.body.ModelBody;
import com.mvcoding.financius.core.model.ModelState;

import java.util.UUID;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor public abstract class Model<B extends ModelBody> implements Parcelable {
    private Long _id;
    private String id;
    private ModelState modelState;

    protected Model(@NonNull Parcel in) {
        _id = (Long) in.readValue(Long.class.getClassLoader());
        id = in.readString();
        modelState = (ModelState) in.readSerializable();
    }

    @Override @CallSuper public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(_id);
        dest.writeString(id);
        dest.writeSerializable(modelState);
    }

    @NonNull @CallSuper public B toBody() {
        final B body = createBody();
        body.setId(id);
        body.setModelState(modelState);
        return body;
    }

    @NonNull @CallSuper public Model<B> withDefaultValues() {
        id = UUID.randomUUID().toString();
        modelState = ModelState.Normal;
        return this;
    }

    public void validate() throws RuntimeException {
        toBody().validate();
    }

    @NonNull protected abstract B createBody();

    @Override public int describeContents() {
        return 0;
    }
}
