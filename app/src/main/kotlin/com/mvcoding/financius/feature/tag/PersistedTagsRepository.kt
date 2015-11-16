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

import android.database.Cursor
import com.mvcoding.financius.database.Database
import com.mvcoding.financius.database.DatabaseQueryResult
import com.mvcoding.financius.database.sqlite.TagsTable
import com.mvcoding.financius.extension.select
import rx.Observable

class PersistedTagsRepository(private val database: Database, private val tagsTable: TagsTable) : TagsRepository {
    override fun save(tag: Tag) {
        //        database.save(tagsTable, convertToDatabaseRecord(tag))
    }

    override fun observeTags(): Observable<List<Tag>> {
        return database.load(select(tagsTable.columns()).from(tagsTable)).map { convertToTags(it) }
    }

    //    private fun convertToDatabaseRecord(tag: Tag): DatabaseRecord {
    //        return
    //    }

    private fun convertToTags(databaseQueryResult: DatabaseQueryResult): List<Tag> {
        val tags = arrayListOf<Tag>()
        //        if (cursor?.moveToFirst() ?: false) {
        //            do {
        //                tags.add(convertToTag(cursor!!))
        //            } while (cursor!!.moveToNext())
        //        }

        return tags
    }

    private fun convertToTag(cursor: Cursor): Tag {
        val id = cursor.getString(cursor.getColumnIndex(tagsTable.id.name))
        val title = cursor.getString(cursor.getColumnIndex(tagsTable.title.name))
        val color = cursor.getInt(cursor.getColumnIndex(tagsTable.color.name))
        return Tag(id, title, color)
    }
}