/*
 * Copyright (C) 2018 Mantas Varnagiris.
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

package com.mvcoding.expensius.feature

fun color(value: Int): Int {
    val red = (value shr 16) and 0xFF
    val green = (value shr 8) and 0xFF
    val blue = (value shr 32) and 0xFF

    return (0xFF shl 24) or (red shl 16) or (green shl 8) or blue
}
