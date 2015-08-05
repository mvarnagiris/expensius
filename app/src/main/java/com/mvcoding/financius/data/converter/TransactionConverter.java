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
import android.database.MatrixCursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mvcoding.financius.core.endpoints.body.TransactionBody;
import com.mvcoding.financius.core.model.TransactionState;
import com.mvcoding.financius.core.model.TransactionType;
import com.mvcoding.financius.data.database.table.BaseModelTable;
import com.mvcoding.financius.data.database.table.PlaceTable;
import com.mvcoding.financius.data.database.table.TagTable;
import com.mvcoding.financius.data.database.table.TransactionTable;
import com.mvcoding.financius.data.database.table.TransactionTagTable;
import com.mvcoding.financius.data.model.Place;
import com.mvcoding.financius.data.model.Tag;
import com.mvcoding.financius.data.model.Transaction;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

public class TransactionConverter extends ModelConverter<TransactionBody, Transaction> {
    private final PlaceConverter placeConverter;
    private final TagConverter tagConverter;

    @Inject public TransactionConverter(@NonNull PlaceConverter placeConverter, @NonNull TagConverter tagConverter) {
        this.placeConverter = placeConverter;
        this.tagConverter = tagConverter;
    }

    @Override public Transaction from(@NonNull Cursor cursor) {
        final Transaction transaction = super.from(cursor);
        final TransactionTable table = TransactionTable.get();
        transaction.setTransactionType(TransactionType.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(table.transactionType()
                                                                                                                     .name()))));
        transaction.setTransactionState(TransactionState.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(table.transactionState()
                                                                                                                       .name()))));
        transaction.setDate(cursor.getLong(cursor.getColumnIndexOrThrow(table.date().name())));
        transaction.setAmount(BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndexOrThrow(table.amount().name()))));
        transaction.setCurrency(cursor.getString(cursor.getColumnIndexOrThrow(table.currency().name())));
        transaction.setNote(cursor.getString(cursor.getColumnIndexOrThrow(table.note().name())));

        if (cursor.getString(cursor.getColumnIndexOrThrow(PlaceTable.get().id().name())) != null) {
            transaction.setPlace(placeConverter.from(cursor));
        }

        final Cursor tagsCursor = getTagsCursor(cursor);
        if (tagsCursor != null && tagsCursor.moveToFirst()) {
            final Set<Tag> tags = new HashSet<>();
            do {
                tags.add(tagConverter.from(tagsCursor));
            } while (tagsCursor.moveToNext());
            transaction.setTags(tags);
        }

        return transaction;
    }

    @NonNull @Override public TransactionBody toBody(@NonNull Transaction model) {
        final TransactionBody body = super.toBody(model);
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

    MatrixCursor createEmptyTagsCursor() {
        return new MatrixCursor(TagTable.get().getQueryColumns());
    }

    @Nullable private Cursor getTagsCursor(@NonNull Cursor cursor) {
        final TransactionTagTable tagTable = TransactionTagTable.get();
        final String tagIdsText = cursor.getString(cursor.getColumnIndexOrThrow(tagTable.tagIds().selectName()));
        if (tagIdsText == null || tagIdsText.isEmpty()) {
            return null;
        }

        final String[] tagIds = tagIdsText.split(TransactionTagTable.SEPARATOR);
        final String[] tagModelStates = cursor.getString(cursor.getColumnIndexOrThrow(tagTable.tagModelStates().selectName()))
                .split(TransactionTagTable.SEPARATOR);
        final String[] tagTitles = cursor.getString(cursor.getColumnIndexOrThrow(tagTable.tagTitles().selectName()))
                .split(TransactionTagTable.SEPARATOR);
        final String[] tagColors = cursor.getString(cursor.getColumnIndexOrThrow(tagTable.tagColors().selectName()))
                .split(TransactionTagTable.SEPARATOR);

        final MatrixCursor tagsCursor = createEmptyTagsCursor();
        for (int i = 0, size = tagIds.length; i < size; i++) {
            tagsCursor.addRow(new Object[]{tagIds[i], tagModelStates[i], tagTitles[i], tagColors[i]});
        }
        return tagsCursor;
    }
}
