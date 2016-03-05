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

package com.mvcoding.expensius.extension

import android.content.Context

fun getColorFromTheme(context: Context, attrId: Int): Int {
    val a = context.theme.obtainStyledAttributes(intArrayOf(attrId));
    val color = a.getColor(0, 0);
    a.recycle();
    return color;
}

fun getDimensionFromTheme(context: Context, attrId: Int): Int {
    val a = context.theme.obtainStyledAttributes(intArrayOf(attrId));
    val color = a.getDimensionPixelSize(0, 0);
    a.recycle();
    return color;
}