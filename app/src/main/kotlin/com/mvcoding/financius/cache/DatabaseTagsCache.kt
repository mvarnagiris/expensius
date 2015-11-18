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

package com.mvcoding.financius.cache

import com.mvcoding.financius.cache.sqlite.TagsTable
import com.mvcoding.financius.extension.select
import com.mvcoding.financius.extension.toDatabaseRecord
import com.mvcoding.financius.feature.tag.Tag
import com.mvcoding.financius.feature.tag.TagsCache
import rx.Observable

class DatabaseTagsCache(private val database: Database<DatabaseRecord, Query>, private val tagsTable: TagsTable) : TagsCache {
    override fun save(tag: Tag) {
        database.save(tagsTable, tag.toDatabaseRecord(tagsTable))
    }

    override fun observeTags(): Observable<List<Tag>> {
        return database.load<List<Tag>>(select(tagsTable.columns()).from(tagsTable)).map { it.getResult() }
    }
}