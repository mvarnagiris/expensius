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

import com.mvcoding.financius.core.endpoints.body.TransactionBody;
import com.mvcoding.financius.data.database.table.BaseModelTable;
import com.mvcoding.financius.data.database.table.TransactionTable;
import com.mvcoding.financius.data.model.Place;
import com.mvcoding.financius.data.model.Tag;
import com.mvcoding.financius.data.model.Transaction;

import java.util.HashSet;
import java.util.Set;

public class TransactionConverter extends ModelConverter<TransactionBody, Transaction> {
    @Override public Transaction from(@NonNull Cursor cursor) {
        final Transaction transaction = super.from(cursor);
        // TODO: Implement
        //        final TransactionTable table = TransactionTable.get();
        //        transactionType = TransactionType.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(table.transactionType().selectName())));
        //        transactionState = TransactionState.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(table.transactionState().selectName())));
        //        date = cursor.getLong(cursor.getColumnIndexOrThrow(table.date().selectName()));
        //        amount = BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndexOrThrow(table.amount().selectName())));
        //        date = cursor.getLong(cursor.getColumnIndexOrThrow(table.date().selectName()));
        //        date = cursor.getLong(cursor.getColumnIndexOrThrow(table.date().selectName()));
        //        date = cursor.getLong(cursor.getColumnIndexOrThrow(table.date().selectName()));
        return transaction;
    }

    @NonNull @Override public TransactionBody toBody(Transaction model) {
        TransactionBody body = super.toBody(model);
        body.setTransactionType(model.getTransactionType());
        body.setTransactionState(model.getTransactionState());
        body.setDate(model.getDate());
        body.setAmount(model.getAmount());
        body.setCurrency(model.getCurrency());
        body.setNote(model.getNote());

        final Place place = model.getPlace();
        if (place != null) {
            body.setPlaceId(place.getId());
        }

        final Set<Tag> tags = model.getTags();
        if (tags != null) {
            final Set<String> tagIds = new HashSet<>();
            for (Tag tag : tags) {
                tagIds.add(tag.getId());
            }
            body.setTagIds(tagIds);
        }
        return body;
    }

    @NonNull @Override protected Transaction createModel() {
        return new Transaction();
    }

    @NonNull @Override protected BaseModelTable getModelTable() {
        return TransactionTable.get();
    }

    @NonNull @Override protected TransactionBody createBody() {
        return new TransactionBody();
    }
}
