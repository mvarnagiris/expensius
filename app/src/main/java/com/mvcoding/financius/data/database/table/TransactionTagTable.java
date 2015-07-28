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

import javax.inject.Singleton;

@Singleton public final class TransactionTagTable extends BaseRelationshipTable {
    public static final String SEPARATOR = "::";

    private static final TransactionTagTable INSTANCE = new TransactionTagTable();

    private final Column transactionId;
    private final Column tagId;

    private final DynamicColumn tagIds;
    private final DynamicColumn tagModelStates;
    private final DynamicColumn tagTitles;
    private final DynamicColumn tagColors;

    private TransactionTagTable() {
        this("transactionTag");
    }

    private TransactionTagTable(@NonNull String tableName) {
        super(tableName);
        transactionId = new Column(tableName, "transactionId", Column.Type.Text);
        tagId = new Column(tableName, "tagId", Column.Type.Text);

        final TagTable tagTable = TagTable.get();
        tagIds = new DynamicColumn("GROUP_CONCAT(" + tagTable.id().selectName() + ")", "tagIds");
        tagModelStates = new DynamicColumn("GROUP_CONCAT(" + tagTable.modelState().selectName() + ")", "tagModelStates");
        tagTitles = new DynamicColumn("GROUP_CONCAT(" + tagTable.title().selectName() + ")", "tagTitles");
        tagColors = new DynamicColumn("GROUP_CONCAT(" + tagTable.color().selectName() + ")", "tagColors");
    }

    public static TransactionTagTable get() {
        return INSTANCE;
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

    public DynamicColumn tagIds() {
        return tagIds;
    }

    public DynamicColumn tagModelStates() {
        return tagModelStates;
    }

    public DynamicColumn tagTitles() {
        return tagTitles;
    }

    public DynamicColumn tagColors() {
        return tagColors;
    }
}
