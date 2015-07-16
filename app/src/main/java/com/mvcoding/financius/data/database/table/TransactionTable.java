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

package com.mvcoding.financius.data.database.table;

import android.support.annotation.NonNull;

import com.mvcoding.financius.core.model.TransactionState;
import com.mvcoding.financius.core.model.TransactionType;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton public final class TransactionTable extends BaseModelTable {
    private final Column transactionType;
    private final Column transactionState;
    private final Column date;
    private final Column amount;
    private final Column currency;
    private final Column placeId;
    private final Column note;

    @Inject TransactionTable() {
        this("transaction");
    }

    private TransactionTable(@NonNull String tableName) {
        super(tableName);
        transactionType = new Column(tableName, "transactionType", Column.Type.Text, TransactionType.Expense.name());
        transactionState = new Column(tableName, "transactionState", Column.Type.Text, TransactionState.Pending.name());
        date = new Column(tableName, "date", Column.Type.DateTime, "0");
        amount = new Column(tableName, "amount", Column.Type.Real, "0");
        currency = new Column(tableName, "currency", Column.Type.Text);
        placeId = new Column(tableName, "placeId", Column.Type.Text);
        note = new Column(tableName, "note", Column.Type.Text);
    }

    @NonNull @Override protected Column[] getModelColumns() {
        return new Column[]{transactionType, transactionState, date, amount, currency, note};
    }

    @NonNull public Column transactionType() {
        return transactionType;
    }

    @NonNull public Column transactionState() {
        return transactionState;
    }

    @NonNull public Column date() {
        return date;
    }

    @NonNull public Column amount() {
        return amount;
    }

    @NonNull public Column currency() {
        return currency;
    }

    @NonNull public Column note() {
        return note;
    }
}
