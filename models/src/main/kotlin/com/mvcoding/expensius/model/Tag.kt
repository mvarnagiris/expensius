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

package com.mvcoding.expensius.model

import java.io.Serializable

data class TagId(val id: String) : Serializable
data class Color(val rgb: Int) : Serializable
data class Title(val text: String) : Serializable
data class Order(val value: Int) : Serializable, Comparable<Order> {
    override fun compareTo(other: Order): Int = value.compareTo(other.value)
}

data class CreateTag(val title: Title, val color: Color, val order: Order) : Serializable
data class Tag(
        val tagId: TagId,
        val modelState: ModelState,
        val title: Title,
        val color: Color,
        val order: Order) : Serializable