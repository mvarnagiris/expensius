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
import com.memoizrlabs.Scope
import com.mvcoding.expensius.feature.SnackbarBuilder.Companion.snackbar

fun View.scope(): Scope = context.toBaseActivity().scope

fun View.makeOutlineProviderOval() {
    outlineProvider = object : ViewOutlineProvider() {
        override fun getOutline(view: View, outline: Outline) {
            outline.setOval(0, 0, view.width, view.height)
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
fun View.getString(resId: Int) = context.getString(resId)
fun View.activity() = context.toBaseActivity()