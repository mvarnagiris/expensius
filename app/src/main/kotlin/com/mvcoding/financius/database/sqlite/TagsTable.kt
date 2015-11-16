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

package com.mvcoding.financius.database.sqlite

import com.mvcoding.financius.extension.column

class TagsTable : SqliteModelTable("tags") {
    val title = column(this, "title", SqliteColumn.Type.Text);
    val color = column(this, "color", SqliteColumn.Type.Integer, "0");

    override fun modelColumns(): Array<SqliteColumn> {
        return arrayOf(title, color)
    }
}