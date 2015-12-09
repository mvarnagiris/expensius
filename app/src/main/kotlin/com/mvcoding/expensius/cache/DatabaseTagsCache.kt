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

package com.mvcoding.expensius.cache

import com.mvcoding.expensius.ModelState.NONE
import com.mvcoding.expensius.cache.database.Database
import com.mvcoding.expensius.cache.database.table.TagsTable
import com.mvcoding.expensius.extension.map
import com.mvcoding.expensius.extension.selectFrom
import com.mvcoding.expensius.extension.toContentValues
import com.mvcoding.expensius.extension.toTag
import com.mvcoding.expensius.feature.tag.Tag
import com.mvcoding.expensius.feature.tag.TagsCache
import rx.Observable

class DatabaseTagsCache(private val database: Database, private val tagsTable: TagsTable) : TagsCache {
    override fun save(tags: Set<Tag>) {
        database.save(tagsTable, tags.map { it.toContentValues(tagsTable) })
    }

    override fun tags(): Observable<List<Tag>> {
        return database.query(selectFrom(tagsTable).where("${tagsTable.modelState}=?", arrayOf(NONE.name))).map { it.map { it.toTag(tagsTable) } }
    }
}