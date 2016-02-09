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

import com.mvcoding.expensius.model.ModelState

abstract class ModelTable(name: String) : Table(name) {
    val id = ValueColumn(this, "id", ValueColumn.Type.TEXT_PRIMARY_KEY);
    val modelState = ValueColumn(this, "modelState", ValueColumn.Type.TEXT, ModelState.NONE.name);

    override fun idColumns() = arrayOf(id)
    override fun columns() = arrayOf(id, modelState).plus(modelColumns())
    abstract fun modelColumns(): Array<ValueColumn>
}