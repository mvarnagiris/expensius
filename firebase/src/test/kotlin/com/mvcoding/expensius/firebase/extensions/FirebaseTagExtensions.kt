/*
 * Copyright (C) 2017 Mantas Varnagiris.
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

package com.mvcoding.expensius.firebase.extensions

import com.mvcoding.expensius.firebase.model.FirebaseTag
import com.mvcoding.expensius.model.extensions.aString
import com.mvcoding.expensius.model.extensions.aStringId
import com.mvcoding.expensius.model.extensions.anInt

fun aFirebaseTag() = FirebaseTag(
        id = aStringId(),
        title = aString("title"),
        color = anInt(),
        order = anInt())

fun FirebaseTag.withId(id: String?) = copy(id = id)
fun FirebaseTag.withTitle(title: String?) = copy(title = title)
fun FirebaseTag.withColor(color: Int?) = copy(color = color)
fun FirebaseTag.withOrder(order: Int?) = copy(order = order)