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

package com.mvcoding.financius.data;

import android.support.annotation.NonNull;

import com.mvcoding.financius.core.endpoints.body.ValidationException;
import com.mvcoding.financius.core.model.ModelState;
import com.mvcoding.financius.data.database.Database;
import com.mvcoding.financius.data.database.DatabaseQuery;
import com.mvcoding.financius.data.database.table.TagTable;
import com.mvcoding.financius.data.model.Tag;
import com.mvcoding.financius.data.model.Transaction;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import lombok.RequiredArgsConstructor;
import rx.Observable;

@Singleton public class DataApi {
    private final Database database;

    @Inject public DataApi(@NonNull Database database) {
        this.database = database;
    }

    @NonNull public Observable<Transaction> saveTransaction(@NonNull Transaction transaction) throws ValidationException {
        transaction.validate();
        // TODO: Save transaction
        return Observable.just(transaction);
    }

    @NonNull public Observable<Tag> saveTag(@NonNull Tag tag) throws ValidationException {
        tag.validate();
        // TODO: Save tag
        return Observable.just(tag);
    }

    @NonNull public Observable<List<Tag>> loadTags(@NonNull Observable<Page> pageObservable) {
        final TagTable table = TagTable.get();
        final DatabaseQuery databaseQuery = new DatabaseQuery().select(table.getQueryColumns())
                .from(table.getTableName())
                .where(table.modelState() + "=?", ModelState.Normal.name());

        return pageObservable.withLatestFrom(database.load(databaseQuery), (page, cursor) -> {
            final List<Tag> tags = new ArrayList<>();
            for (int i = page.start; i < page.count; i++) {
                cursor.moveToPosition(i);
                tags.add(new Tag(cursor));
            }
            return tags;
        });
    }

    @RequiredArgsConstructor public static class Page {
        private final int start;
        private final int count;
    }
}
