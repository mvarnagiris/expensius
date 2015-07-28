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

import com.mvcoding.financius.core.endpoints.body.TransactionBody;
import com.mvcoding.financius.core.model.ModelState;
import com.mvcoding.financius.core.model.TransactionState;
import com.mvcoding.financius.core.model.TransactionType;
import com.mvcoding.financius.data.database.table.PlaceTable;
import com.mvcoding.financius.data.database.table.TagTable;
import com.mvcoding.financius.data.database.table.TransactionTable;
import com.mvcoding.financius.data.database.table.TransactionTagTable;
import com.mvcoding.financius.data.model.Place;
import com.mvcoding.financius.data.model.Tag;
import com.mvcoding.financius.data.model.Transaction;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class TransactionConverterTest extends BaseModelConverterTest<TransactionConverter, Transaction, TransactionTable, TransactionBody> {
    @Override protected TransactionConverter createConverter() {
        final TransactionConverter converter = spy(new TransactionConverter(new PlaceConverter(), new TagConverter()));
        final MatrixCursor cursor = mock(MatrixCursor.class);

        final List<Tag> cursorTags = new ArrayList<>();
        final AtomicInteger index = new AtomicInteger(0);
        doAnswer(new Answer() {
            @Override public Object answer(InvocationOnMock invocation) throws Throwable {
                final Object[] tagValues = (Object[]) invocation.getArguments()[0];
                final Tag tag = new Tag();
                tag.setId((String) tagValues[0]);
                tag.setModelState(ModelState.valueOf((String) tagValues[1]));
                tag.setTitle((String) tagValues[2]);
                tag.setColor(Integer.valueOf((String) tagValues[3]));
                cursorTags.add(tag);
                return null;
            }
        }).when(cursor).addRow(any(Object[].class));
        when(cursor.moveToFirst()).thenAnswer(new Answer<Boolean>() {
            @Override public Boolean answer(InvocationOnMock invocation) throws Throwable {
                if (cursorTags.isEmpty()) {
                    return false;
                }
                prepareCursorRow(cursor, cursorTags, 0);
                return true;
            }
        });
        when(cursor.moveToNext()).thenAnswer(new Answer<Boolean>() {
            @Override public Boolean answer(InvocationOnMock invocation) throws Throwable {
                final int position = index.incrementAndGet();
                if (position >= cursorTags.size()) {
                    return false;
                }
                prepareCursorRow(cursor, cursorTags, position);
                return true;
            }
        });

        when(converter.createEmptyTagsCursor()).thenReturn(cursor);
        return converter;
    }

    private void prepareCursorRow(Cursor cursor, List<Tag> cursorTags, int position) {
        final TagTable table = TagTable.get();
        when(cursor.getColumnIndexOrThrow(table.id().selectName())).thenReturn(0);
        when(cursor.getColumnIndexOrThrow(table.modelState().selectName())).thenReturn(1);
        when(cursor.getColumnIndexOrThrow(table.title().selectName())).thenReturn(2);
        when(cursor.getColumnIndexOrThrow(table.color().selectName())).thenReturn(3);
        final Tag tag = cursorTags.get(position);
        when(cursor.getString(0)).thenReturn(tag.getId());
        when(cursor.getString(1)).thenReturn(tag.getModelState().name());
        when(cursor.getString(2)).thenReturn(tag.getTitle());
        when(cursor.getInt(3)).thenReturn(tag.getColor());
    }

    @Override protected Transaction createModel() {
        return new Transaction();
    }

    @Override protected TransactionTable getTable() {
        return TransactionTable.get();
    }

    @Override protected int prepareModelCursor(TransactionTable table, Cursor cursor) {
        final int startIndex = super.prepareModelCursor(table, cursor);
        when(cursor.getColumnIndexOrThrow(table.transactionType().selectName())).thenReturn(startIndex);
        when(cursor.getColumnIndexOrThrow(table.transactionState().selectName())).thenReturn(startIndex + 1);
        when(cursor.getColumnIndexOrThrow(table.date().selectName())).thenReturn(startIndex + 2);
        when(cursor.getColumnIndexOrThrow(table.amount().selectName())).thenReturn(startIndex + 3);
        when(cursor.getColumnIndexOrThrow(table.currency().selectName())).thenReturn(startIndex + 4);
        when(cursor.getColumnIndexOrThrow(table.placeId().selectName())).thenReturn(startIndex + 5);
        when(cursor.getColumnIndexOrThrow(table.note().selectName())).thenReturn(startIndex + 6);
        when(cursor.getString(startIndex)).thenReturn(TransactionType.Expense.name());
        when(cursor.getString(startIndex + 1)).thenReturn(TransactionState.Confirmed.name());
        when(cursor.getLong(startIndex + 2)).thenReturn(10L);
        when(cursor.getDouble(startIndex + 3)).thenReturn(1.2);
        when(cursor.getString(startIndex + 4)).thenReturn("USD");
        when(cursor.getString(startIndex + 5)).thenReturn("placeId");
        when(cursor.getString(startIndex + 6)).thenReturn("note");

        final PlaceTable placeTable = PlaceTable.get();
        when(cursor.getColumnIndexOrThrow(placeTable.id().selectName())).thenReturn(startIndex + 7);
        when(cursor.getColumnIndexOrThrow(placeTable.modelState().selectName())).thenReturn(startIndex + 8);
        when(cursor.getColumnIndexOrThrow(placeTable.placeId().selectName())).thenReturn(startIndex + 9);
        when(cursor.getColumnIndexOrThrow(placeTable.name().selectName())).thenReturn(startIndex + 10);
        when(cursor.getColumnIndexOrThrow(placeTable.address().selectName())).thenReturn(startIndex + 11);
        when(cursor.getColumnIndexOrThrow(placeTable.latitude().selectName())).thenReturn(startIndex + 12);
        when(cursor.getColumnIndexOrThrow(placeTable.longitude().selectName())).thenReturn(startIndex + 13);
        when(cursor.getString(startIndex + 7)).thenReturn("id");
        when(cursor.getString(startIndex + 8)).thenReturn(ModelState.Normal.name());
        when(cursor.getString(startIndex + 9)).thenReturn("placeId");
        when(cursor.getString(startIndex + 10)).thenReturn("name");
        when(cursor.getString(startIndex + 11)).thenReturn("address");
        when(cursor.getDouble(startIndex + 12)).thenReturn(1.2);
        when(cursor.getDouble(startIndex + 13)).thenReturn(3.4);

        final TransactionTagTable transactionTagTable = TransactionTagTable.get();
        when(cursor.getColumnIndexOrThrow(transactionTagTable.tagIds().selectName())).thenReturn(startIndex + 12);
        when(cursor.getColumnIndexOrThrow(transactionTagTable.tagModelStates().selectName())).thenReturn(startIndex + 13);
        when(cursor.getColumnIndexOrThrow(transactionTagTable.tagTitles().selectName())).thenReturn(startIndex + 14);
        when(cursor.getColumnIndexOrThrow(transactionTagTable.tagColors().selectName())).thenReturn(startIndex + 15);
        when(cursor.getString(startIndex + 12)).thenReturn("tag1Id" + TransactionTagTable.SEPARATOR + "tag2Id");
        when(cursor.getString(startIndex + 13)).thenReturn(ModelState.Normal.name() + TransactionTagTable.SEPARATOR + ModelState.Deleted.name());
        when(cursor.getString(startIndex + 14)).thenReturn("tag1Title" + TransactionTagTable.SEPARATOR + "tag2Title");
        when(cursor.getString(startIndex + 15)).thenReturn("1" + TransactionTagTable.SEPARATOR + "2");

        return startIndex + 16;
    }

    @Override protected void prepareModel(Transaction model) {
        super.prepareModel(model);
        model.setTransactionType(TransactionType.Expense);
        model.setTransactionState(TransactionState.Confirmed);
        model.setDate(10);
        model.setAmount(BigDecimal.valueOf(1.2));
        model.setCurrency("USD");
        final Place place = new Place();
        place.setId("id");
        model.setPlace(place);
        final Tag tag1 = new Tag();
        tag1.setId("id1");
        final Tag tag2 = new Tag();
        tag2.setId("id2");
        model.setTags(new HashSet<>(Arrays.asList(tag1, tag2)));
        model.setNote("note");
    }

    @Override protected void assertModel(Transaction model) {
        super.assertModel(model);
    }

    @Override protected void assertBody(TransactionBody body) {
        super.assertBody(body);
        assertThat(body.getTransactionType()).isEqualTo(TransactionType.Expense);
        assertThat(body.getTransactionState()).isEqualTo(TransactionState.Confirmed);
        assertThat(body.getDate()).isEqualTo(10);
        assertThat(body.getAmount()).isEqualTo(BigDecimal.valueOf(1.2));
        assertThat(body.getCurrency()).isEqualTo("USD");
        assertThat(body.getPlaceId()).isEqualTo("id");
        assertThat(body.getTagIds()).isEqualTo(new HashSet<>(Arrays.asList("id1", "id2")));
        assertThat(body.getNote()).isEqualTo("note");
    }
}