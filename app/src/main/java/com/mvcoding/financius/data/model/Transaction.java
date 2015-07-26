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
import com.mvcoding.financius.UserSettings;
import com.mvcoding.financius.core.endpoints.body.TransactionBody;
import com.mvcoding.financius.core.model.TransactionState;
import com.mvcoding.financius.core.model.TransactionType;
import com.mvcoding.financius.data.database.table.BaseModelTable;
import com.mvcoding.financius.data.database.table.TransactionTable;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @EqualsAndHashCode(callSuper = true) @ToString(callSuper = true) @NoArgsConstructor
public class Transaction extends Model<TransactionBody> {
    public static final Parcelable.Creator<Transaction> CREATOR = new Parcelable.Creator<Transaction>() {
        public Transaction createFromParcel(Parcel in) {
            return new Transaction(in);
        }

        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };

    @SerializedName("transactionType") private TransactionType transactionType;
    @SerializedName("transactionState") private TransactionState transactionState;
    @SerializedName("date") private long date;
    @SerializedName("amount") private BigDecimal amount;
    @SerializedName("currency") private String currency;
    @SerializedName("place") private Place place;
    @SerializedName("tags") private Set<Tag> tags;
    @SerializedName("note") private String note;

    public Transaction(@NonNull Transaction transaction) {
        setId(transaction.getId());
        setModelState(transaction.getModelState());
        transactionType = transaction.getTransactionType();
        transactionState = transaction.getTransactionState();
        date = transaction.getDate();
        amount = transaction.getAmount();
        currency = transaction.getCurrency();
        place = transaction.getPlace();
        tags = transaction.getTags();
        note = transaction.getNote();
    }

    private Transaction(@NonNull Parcel in) {
        super(in);
        transactionType = (TransactionType) in.readSerializable();
        transactionState = (TransactionState) in.readSerializable();
        date = in.readLong();
        amount = (BigDecimal) in.readSerializable();
        currency = in.readString();
        place = in.readParcelable(Place.class.getClassLoader());
        final Tag[] tagArray = (Tag[]) in.readParcelableArray(Tag.class.getClassLoader());
        tags = tagArray == null ? null : new HashSet<>(Arrays.asList(tagArray));
        note = in.readString();
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeSerializable(transactionType);
        dest.writeSerializable(transactionState);
        dest.writeLong(date);
        dest.writeSerializable(amount);
        dest.writeString(currency);
        dest.writeParcelable(place, flags);
        dest.writeParcelableArray(tags == null ? null : tags.toArray(new Tag[tags.size()]), flags);
        dest.writeString(note);
    }

    @NonNull @Override public TransactionBody toBody() {
        final TransactionBody body = super.toBody();
        body.setTransactionType(transactionType);
        body.setTransactionState(transactionState);
        body.setDate(date);
        body.setAmount(amount);
        body.setCurrency(currency);
        body.setNote(note);

        if (place != null) {
            body.setPlaceId(place.getId());
        }

        if (tags != null) {
            final Set<String> tagIds = new HashSet<>();
            for (Tag tag : tags) {
                tagIds.add(tag.getId());
            }
            body.setTagIds(tagIds);
        }

        return body;
    }

    @NonNull @Override public Transaction withDefaultValues() {
        super.withDefaultValues();
        transactionType = TransactionType.Expense;
        transactionState = TransactionState.Confirmed;
        amount = BigDecimal.ZERO;
        date = System.currentTimeMillis();
        return this;
    }

    @NonNull public Transaction withDefaultValues(@NonNull UserSettings userSettings) {
        withDefaultValues();
        currency = userSettings.getCurrency();
        return this;
    }

    @NonNull @Override public Transaction with(@NonNull Cursor cursor) {
        super.with(cursor);

        // TODO Implement.
        //        final TransactionTable table = TransactionTable.get();
        //        transactionType = TransactionType.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(table.transactionType().selectName())));
        //        transactionState = TransactionState.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(table.transactionState().selectName())));
        //        date = cursor.getLong(cursor.getColumnIndexOrThrow(table.date().selectName()));
        //        amount = BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndexOrThrow(table.amount().selectName())));
        //        date = cursor.getLong(cursor.getColumnIndexOrThrow(table.date().selectName()));
        //        date = cursor.getLong(cursor.getColumnIndexOrThrow(table.date().selectName()));
        //        date = cursor.getLong(cursor.getColumnIndexOrThrow(table.date().selectName()));

        return this;
    }

    @NonNull @Override protected TransactionBody createBody() {
        return new TransactionBody();
    }

    @NonNull @Override protected BaseModelTable getModelTable() {
        return TransactionTable.get();
    }
}
