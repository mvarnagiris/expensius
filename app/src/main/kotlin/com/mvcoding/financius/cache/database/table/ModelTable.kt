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

package com.mvcoding.financius.cache.database.table

import com.mvcoding.financius.ModelState

abstract class ModelTable(name: String) : Table(name) {
    val id = Column(this, "id", Column.Type.TextPrimaryKey);
    val modelState = Column(this, "modelState", Column.Type.Text, ModelState.None.name);

    override fun columns(): List<Column> {
        return listOf(id, modelState).plus(modelColumns())
    }

    abstract fun modelColumns(): List<Column>
}