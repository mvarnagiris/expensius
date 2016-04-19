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

package com.mvcoding.expensius

import java.util.*

fun aString() = UUID.randomUUID().toString()
fun anInt() = anInt(Int.MAX_VALUE)
fun anInt(max: Int) = (Math.random() * max).toInt()
fun aBoolean() = Math.random() >= 0.5
fun <T> Array<T>.aRandomItem() = get(anInt(size - 1))