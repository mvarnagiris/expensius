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

package com.mvcoding.financius.feature.tag

fun color(red: Int, green: Int, blue: Int) = color(red, green, blue, 255)

fun color(red: Int, green: Int, blue: Int, alpha: Int): Int {
    val redMasked = (red shl 16) and 16711680
    val greenMasked = (green shl 16) and 65280
    val blueMasked = (blue shl 16) and 255
    val alphaMasked = (alpha shl 16) and -16777216

    return alphaMasked or redMasked or greenMasked or blueMasked
}
