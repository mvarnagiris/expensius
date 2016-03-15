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

package com.mvcoding.expensius.provider

import com.mvcoding.expensius.extension.map
import com.mvcoding.expensius.extension.toContentValues
import com.mvcoding.expensius.extension.toTag
import com.mvcoding.expensius.feature.tag.TagsProvider
import com.mvcoding.expensius.model.ModelState
import com.mvcoding.expensius.model.ModelState.ARCHIVED
import com.mvcoding.expensius.model.ModelState.NONE
import com.mvcoding.expensius.model.Tag
import com.mvcoding.expensius.provider.database.Database
import com.mvcoding.expensius.provider.database.Order
import com.mvcoding.expensius.provider.database.OrderDirection.ASC
import com.mvcoding.expensius.provider.database.SaveDatabaseAction
import com.mvcoding.expensius.provider.database.select
import com.mvcoding.expensius.provider.database.table.TagsTable
import rx.Observable

class DatabaseTagsProvider(private val database: Database, private val tagsTable: TagsTable) : TagsProvider {
    override fun save(tags: Set<Tag>) {
        database.save(tags.map { SaveDatabaseAction(tagsTable, it.toContentValues(tagsTable)) })
    }

    override fun tags(): Observable<List<Tag>> {
        return queryTags(NONE)
    }

    override fun archivedTags(): Observable<List<Tag>> {
        return queryTags(ARCHIVED)
    }

    private fun queryTags(modelState: ModelState) =
            database.query(select(tagsTable)
                    .from(tagsTable)
                    .where("${tagsTable.modelState}=?", modelState.name)
                    .orderBy(Order(tagsTable.order, ASC)))
                    .map { it.map { it.toTag(tagsTable) } }
}