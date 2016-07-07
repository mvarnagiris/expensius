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

import com.mvcoding.expensius.model.Color
import com.mvcoding.expensius.model.Color.Companion.noColor
import com.mvcoding.expensius.model.ModelState.NONE
import com.mvcoding.expensius.model.ModelState.valueOf
import com.mvcoding.expensius.model.Order
import com.mvcoding.expensius.model.Order.Companion.noOrder
import com.mvcoding.expensius.model.Tag
import com.mvcoding.expensius.model.TagId
import com.mvcoding.expensius.model.TagId.Companion.noTagId
import com.mvcoding.expensius.model.Title
import com.mvcoding.expensius.model.Title.Companion.noTitle

data class FirebaseTag(
        val id: String? = null,
        val modelState: String? = null,
        val title: String? = null,
        val color: Int? = null,
        val order: Int? = null) {

    fun toTag() = Tag(
            id?.let { TagId(it) } ?: noTagId,
            modelState?.let { valueOf(it) } ?: NONE,
            title?.let { Title(it) } ?: noTitle,
            color?.let { Color(it) } ?: noColor,
            order?.let { Order(it) } ?: noOrder
    )
}