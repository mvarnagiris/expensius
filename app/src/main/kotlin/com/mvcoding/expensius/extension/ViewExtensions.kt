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

import android.graphics.Outline
import android.support.design.widget.Snackbar
import android.view.View
import android.view.ViewOutlineProvider
import com.memoizrlabs.Shank
import com.mvcoding.expensius.feature.SnackbarBuilder.Companion.snackbar
import kotlin.reflect.KClass

fun <T : Any> View.provideActivityScopedSingleton(cls: KClass<T>): T? {
    if (isInEditMode) {
        return null
    }

    val activity = context.toActivity()
    return Shank.withBoundScope(activity.javaClass, activity.observeFinish().map { Any() }).provide(cls.java)
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

fun View.createSnackbar(resId: Int, duration: Int) = Snackbar.make(this, resId, duration)

fun View.snackbar(resId: Int, duration: Int) = snackbar(this, resId, duration)