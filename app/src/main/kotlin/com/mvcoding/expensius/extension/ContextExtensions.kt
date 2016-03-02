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

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.support.v7.view.ContextThemeWrapper
import com.mvcoding.expensius.feature.BaseActivity

fun Context.toBaseActivity(): BaseActivity {
    when (this) {
        is Activity -> return this as BaseActivity
        is ContextThemeWrapper -> return this.baseContext.toBaseActivity()
        is android.view.ContextThemeWrapper -> return this.baseContext.toBaseActivity()
        is ContextWrapper -> return this.baseContext.toBaseActivity()
        else -> throw IllegalArgumentException("Context " + this + " is not an Activity.")
    }
}

fun Context.isActivity(): Boolean {
    when (this) {
        is Activity -> return true
        is ContextThemeWrapper -> return this.baseContext.isActivity()
        is android.view.ContextThemeWrapper -> return this.baseContext.isActivity()
        is ContextWrapper -> return this.baseContext.isActivity()
        else -> return false
    }
}

fun Context.getColorFromTheme(attrId: Int) = getColorFromTheme(this, attrId)