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

package com.mvcoding.expensius.model.extensions

import com.mvcoding.expensius.model.*

fun aTagId() = TagId(aStringId())
fun aTitle() = Title(aString("title"))
fun aColor() = Color(anInt())
fun anOrder() = Order(anInt(100))
fun aCreateTag() = CreateTag(aTitle(), aColor(), anOrder())
fun someTags() = (0..anInt(5)).map { aTag() }.toSet()
fun someTagsIds() = (0..anInt(5)).map { aTagId() }.toSet()
fun aTag() = Tag(aTagId(), aModelState(), aTitle(), aColor(), anOrder())
fun Tag.withOrder(order: Int) = copy(order = Order(order))
fun Tag.withTitle(title: String) = copy(title = Title(title))
fun Tag.withModelState(modelState: ModelState) = copy(modelState = modelState)
