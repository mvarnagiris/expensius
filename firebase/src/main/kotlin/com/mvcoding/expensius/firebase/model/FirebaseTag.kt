/*
 * Copyright (C) 2016 Mantas Varnagiris.
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

package com.mvcoding.expensius.firebase.model

import com.mvcoding.expensius.model.*
import com.mvcoding.expensius.model.NullModels.noColor
import com.mvcoding.expensius.model.NullModels.noOrder
import com.mvcoding.expensius.model.NullModels.noTagId
import com.mvcoding.expensius.model.NullModels.noTitle

data class FirebaseTag(
        val id: String? = null,
        val title: String? = null,
        val color: Int? = null,
        val order: Int? = null) {

    // TODO Write tests
    fun toTag(modelState: ModelState) = Tag(
            id?.let(::TagId) ?: noTagId,
            modelState,
            title?.let(::Title) ?: noTitle,
            color?.let(::Color) ?: noColor,
            order?.let(::Order) ?: noOrder)
}