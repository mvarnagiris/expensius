package com.mvcoding.expensius.extension

import android.content.ContentValues
import android.database.Cursor
import com.mvcoding.expensius.model.ModelState
import com.mvcoding.expensius.model.Tag
import com.mvcoding.expensius.provider.database.table.TagsTable

fun Tag.toContentValues(tagsTable: TagsTable): ContentValues {
    val contentValues = ContentValues()
    contentValues.put(tagsTable.id.name, id)
    contentValues.put(tagsTable.modelState.name, modelState.name)
    contentValues.put(tagsTable.title.name, title)
    contentValues.put(tagsTable.color.name, color)
    contentValues.put(tagsTable.order.name, order)
    return contentValues
}

fun Cursor.toTag(tagsTable: TagsTable): Tag {
    val id = getString(this.getColumnIndex(tagsTable.id.name))
    val modelState = ModelState.valueOf(getString(this.getColumnIndex(tagsTable.modelState.name)))
    val title = getString(this.getColumnIndex(tagsTable.title.name))
    val color = getInt(this.getColumnIndex(tagsTable.color.name))
    val order = getInt(this.getColumnIndex(tagsTable.order.name))
    return Tag(id, modelState, title, color, order)
}