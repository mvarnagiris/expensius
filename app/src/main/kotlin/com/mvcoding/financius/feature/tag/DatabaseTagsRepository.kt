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

package com.mvcoding.financius.feature.tag

import com.mvcoding.financius.database.Database
import com.mvcoding.financius.database.sqlite.TagsTable
import com.mvcoding.financius.extension.select
import com.mvcoding.financius.extension.toDatabaseRecord
import rx.Observable

class DatabaseTagsRepository(private val database: Database, private val tagsTable: TagsTable) : TagsRepository {
    override fun save(tag: Tag) {
        database.save(tagsTable, tag.toDatabaseRecord(tagsTable))
    }

    override fun observeTags(): Observable<List<Tag>> {
        return database.load<List<Tag>>(select(tagsTable.columns()).from(tagsTable)).map { it.getResult() }
    }
}