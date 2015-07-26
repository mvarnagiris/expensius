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

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.mvcoding.financius.core.endpoints.body.TagBody;
import com.mvcoding.financius.data.database.table.BaseModelTable;
import com.mvcoding.financius.data.database.table.TagTable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @EqualsAndHashCode(callSuper = true) @ToString(callSuper = true) @NoArgsConstructor public class Tag extends Model<TagBody> {
    public static final Parcelable.Creator<Tag> CREATOR = new Parcelable.Creator<Tag>() {
        public Tag createFromParcel(Parcel in) {
            return new Tag(in);
        }

        public Tag[] newArray(int size) {
            return new Tag[size];
        }
    };

    @SerializedName("title") private String title;
    @SerializedName("color") private int color;

    public Tag(@NonNull Cursor cursor) {
        with(cursor);
    }

    private Tag(@NonNull Parcel in) {
        super(in);
        title = in.readString();
        color = in.readInt();
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(title);
        dest.writeInt(color);
    }

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

    @NonNull @Override public Tag with(@NonNull Cursor cursor) {
        super.with(cursor);

        final TagTable table = TagTable.get();
        title = cursor.getString(cursor.getColumnIndexOrThrow(table.title().selectName()));
        color = cursor.getInt(cursor.getColumnIndexOrThrow(table.color().selectName()));

        return this;
    }

    @NonNull @Override protected TagBody createBody() {
        return new TagBody();
    }

    @NonNull @Override protected BaseModelTable getModelTable() {
        return TagTable.get();
    }
}
