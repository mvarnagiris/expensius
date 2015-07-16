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

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton public final class TransactionTagTable extends BaseRelationshipTable {
    private final Column transactionId;
    private final Column tagId;

    @Inject TransactionTagTable() {
        this("transactionTag");
    }

    private TransactionTagTable(@NonNull String tableName) {
        super(tableName);
        transactionId = new Column(tableName, "transactionId", Column.Type.Text);
        tagId = new Column(tableName, "tagId", Column.Type.Text);
    }

    @NonNull @Override protected Column[] getColumns() {
        return new Column[0];
    }

    @NonNull @Override protected Column[] getRelationshipColumns() {
        return new Column[]{transactionId, tagId};
    }

    @NonNull public Column transactionId() {
        return transactionId;
    }

    @NonNull public Column tagId() {
        return tagId;
    }
}
