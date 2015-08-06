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

import com.mvcoding.financius.core.model.ModelState;
import com.mvcoding.financius.data.converter.TagConverter;
import com.mvcoding.financius.data.database.DatabaseQuery;
import com.mvcoding.financius.data.database.table.TagTable;
import com.mvcoding.financius.data.model.Tag;
import com.mvcoding.financius.data.paging.Page;
import com.mvcoding.financius.data.paging.PageLoader;
import com.mvcoding.financius.data.paging.PageResult;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

@Singleton public class DataLoadApi {
    private final PageLoader<Tag> tagPageLoader;
    private final TagConverter tagConverter;

    @Inject public DataLoadApi(@NonNull PageLoader<Tag> tagPageLoader, @NonNull TagConverter tagConverter) {
        this.tagPageLoader = tagPageLoader;
        this.tagConverter = tagConverter;
    }

    @NonNull public Observable<PageResult<Tag>> loadTags(@NonNull Observable<Page> pageObservable) {
        final TagTable table = TagTable.get();
        final DatabaseQuery databaseQuery = new DatabaseQuery().select(table.getQueryColumns())
                .from(table.getTableName())
                .where(table.modelState() + "=?", ModelState.Normal.name())
                .orderBy(table.title().name());

        return tagPageLoader.load(tagConverter, databaseQuery, pageObservable);
    }
}
