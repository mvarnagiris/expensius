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

package com.mvcoding.expensius.feature.tag

import com.mvcoding.expensius.ModelState
import java.io.Serializable
import java.util.*

data class Tag(
        val id: String = UUID.randomUUID().toString(),
        val modelState: ModelState = ModelState.NONE,
        val title: String,
        val color: Int) : Serializable {

    companion object {
        val noTag = Tag("", ModelState.NONE, "", 0)
    }

    fun withModelState(modelState: ModelState) = Tag(id, modelState, title, color)
}