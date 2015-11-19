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

package com.mvcoding.financius.extension

import android.content.ContentValues
import android.database.Cursor
import com.mvcoding.financius.ModelState
import com.mvcoding.financius.cache.database.QueryRequest
import com.mvcoding.financius.cache.database.table.Column
import com.mvcoding.financius.cache.database.table.Table
import com.mvcoding.financius.cache.database.table.TagsTable
import com.mvcoding.financius.feature.tag.Tag

fun select(columns: List<Column>): QueryRequest.Select = QueryRequest.Select(columns);
fun selectFrom(table: Table): QueryRequest.From = select(table.columns()).from(table);

fun Tag.toContentValues(tagsTable: TagsTable): ContentValues {
    val contentValues = ContentValues()
    contentValues.put(tagsTable.id.name, id)
    contentValues.put(tagsTable.modelState.name, modelState.name)
    contentValues.put(tagsTable.title.name, title)
    contentValues.put(tagsTable.color.name, color)
    return contentValues
}

fun Cursor.toTag(tagsTable: TagsTable): Tag {
    val id = getString(this.getColumnIndex(tagsTable.id.name))
    val modelState = ModelState.valueOf(getString(this.getColumnIndex(tagsTable.modelState.name)))
    val title = getString(this.getColumnIndex(tagsTable.title.name))
    val color = getInt(this.getColumnIndex(tagsTable.color.name))
    return Tag(id, modelState, title, color)
}

fun <T> Cursor.map(mapper: ((Cursor) -> T)): List<T> {
    if (!moveToFirst()) {
        return emptyList()
    }

    val items = arrayListOf<T>()
    do {
        items.add(mapper.invoke(this))
    } while (moveToNext())
    return items
}
