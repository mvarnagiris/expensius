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

data class FirebaseTag(
        val id: String? = null,
        val title: String? = null,
        val color: Int? = null,
        val order: Int? = null) {

//    fun toTag(modelState: ModelState) = Tag(
//            id?.let { TagId(it) } ?: noTagId,
//            modelState,
//            title?.let { Title(it) } ?: noTitle,
//            color?.let { Color(it) } ?: noColor,
//            order?.let { Order(it) } ?: noOrder
//    )
}