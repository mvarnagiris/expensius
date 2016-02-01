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

package com.mvcoding.expensius.provider.database.table

import com.mvcoding.expensius.provider.database.table.ValueColumn.Type.INTEGER
import com.mvcoding.expensius.provider.database.table.ValueColumn.Type.TEXT

class TagsTable : ModelTable("tags") {
    companion object {
        const val COLUMN_SEPARATOR = ":!:"
        const val TAG_SEPARATOR = ";:;"
    }

    val title = ValueColumn(this, "title", TEXT);
    val color = ValueColumn(this, "color", INTEGER, "0");

    val transactionTags = CalculatedColumn(
            "group_concat(${columns().joinToString(separator = "+'$COLUMN_SEPARATOR'+", transform = { it.toString() })}, '$TAG_SEPARATOR')",
            "tags")

    override fun modelColumns() = arrayOf(title, color)
}