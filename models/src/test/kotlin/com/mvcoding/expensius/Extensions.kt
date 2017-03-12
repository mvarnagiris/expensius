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
package com.mvcoding.expensius

import java.math.BigDecimal
import java.util.*

fun aString() = UUID.randomUUID().toString()
fun aString(string: String) = "$string${anInt(1000)}"
fun aStringId() = aString("id_")
fun aLong() = Random().nextLong()
fun aLong(limit: Int) = anInt(limit).toLong()
fun anInt() = Random().nextInt()
fun anInt(limit: Int) = Random().nextInt(limit)
fun aLongTimestamp() = System.currentTimeMillis() - anInt(1000 * 60 * 60 * 24)
fun anAmount() = BigDecimal(anInt(100))
fun aBoolean() = Math.random() >= 0.5
fun <T> Array<T>.aRandomItem() = get(anInt(size - 1))