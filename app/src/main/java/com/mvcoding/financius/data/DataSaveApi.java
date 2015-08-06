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
import com.mvcoding.financius.data.converter.TagConverter;
import com.mvcoding.financius.data.database.Database;
import com.mvcoding.financius.data.database.table.TagTable;
import com.mvcoding.financius.data.model.Tag;
import com.mvcoding.financius.data.model.Transaction;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

@Singleton public class DataSaveApi {
    private final Database database;
    private final TagConverter tagConverter;

    @Inject public DataSaveApi(@NonNull Database database, @NonNull TagConverter tagConverter) {
        this.database = database;
        this.tagConverter = tagConverter;
    }

    @NonNull public Observable<Transaction> saveTransaction(@NonNull Transaction transaction) throws ValidationException {
        //        transaction.validate();
        // TODO: Save transaction
        return Observable.just(transaction);
    }

    @NonNull public Observable<Tag> saveTag(@NonNull Tag model) throws ValidationException {
        return Observable.just(model)
                .doOnNext(tag -> tagConverter.toBody(tag).validate())
                .doOnNext(tag -> database.save(TagTable.get(), tagConverter.toContentValues(tag)));
    }
}
