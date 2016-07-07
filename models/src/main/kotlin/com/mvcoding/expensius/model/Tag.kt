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

import com.mvcoding.expensius.model.Color.Companion.noColor
import com.mvcoding.expensius.model.ModelState.NONE
import com.mvcoding.expensius.model.Order.Companion.noOrder
import com.mvcoding.expensius.model.TagId.Companion.noTagId
import com.mvcoding.expensius.model.Title.Companion.noTitle
import java.io.Serializable

data class TagId(val id: String) : Serializable {
    companion object {
        val noTagId = TagId("")
    }
}

data class Title(val text: String) : Serializable {
    companion object {
        val noTitle = Title("")
    }

    fun trimmed() = copy(text = text.trim())
}

data class Color(val rgb: Int) : Serializable {
    companion object {
        val noColor = Color(0)
    }
}

data class Order(val value: Int) : Serializable, Comparable<Order> {
    companion object {
        val noOrder = Order(0)
    }

    override fun compareTo(other: Order): Int = value.compareTo(other.value)
}

data class CreateTag(val title: Title, val color: Color, val order: Order) : Serializable
data class Tag(
        val tagId: TagId,
        val modelState: ModelState,
        val title: Title,
        val color: Color,
        val order: Order) : Serializable {

    companion object {
        val noTag = Tag(noTagId, NONE, noTitle, noColor, noOrder)
    }

    fun withModelState(modelState: ModelState) = copy(modelState = modelState)
    fun withOrder(order: Int) = withOrder(Order(order))
    fun withOrder(order: Order) = copy(order = order)
}