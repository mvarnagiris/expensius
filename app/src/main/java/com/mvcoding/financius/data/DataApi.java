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
import com.mvcoding.financius.data.model.Tag;
import com.mvcoding.financius.data.model.Transaction;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

@Singleton public class DataApi {
    @Inject public DataApi() {
    }

    public Observable<Transaction> saveTransaction(@NonNull Transaction transaction) throws ValidationException {
        transaction.validate();
        // TODO: Save transaction
        return Observable.just(transaction);
    }

    public Observable<Tag> saveTag(@NonNull Tag tag) throws ValidationException {
        tag.validate();
        // TODO: Save tag
        return Observable.just(tag);
    }
}
