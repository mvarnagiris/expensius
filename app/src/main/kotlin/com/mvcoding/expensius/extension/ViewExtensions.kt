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

package com.mvcoding.expensius.extension

import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider
import com.mvcoding.expensius.feature.SnackbarBuilder.Companion.snackbar
import kotlin.reflect.KClass

fun <T : Any> View.provideActivityScopedSingleton(cls: KClass<T>,
        arg1: Any? = null,
        arg2: Any? = null,
        arg3: Any? = null,
        arg4: Any? = null): T {
    return provideActivityScopedSingleton(cls, context, arg1, arg2, arg3, arg4)
}

fun View.makeOutlineProviderOval() {
    if (supportsLollipop()) {
        outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                outline.setOval(0, 0, view.width, view.height)
            }
        }
    }
}

fun View.snackbar(resId: Int, duration: Int) = snackbar(this, resId, duration)

fun View.doInEditMode(action: () -> Unit) {
    if (isInEditMode) action()
}

fun View.doNotInEditMode(action: () -> Unit) {
    if (!isInEditMode) action()
}

fun View.getColorFromTheme(attrId: Int) = context.getColorFromTheme(attrId)