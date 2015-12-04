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

package com.mvcoding.financius.feature

import android.support.design.widget.Snackbar
import android.view.View
import com.jakewharton.rxbinding.support.design.widget.dismisses

class SnackbarBuilder(private val view: View, private val resId: Int, private val duration: Int) {
    var actionResId: Int = 0
    var action: Runnable? = null
    var onDismiss: Runnable? = null

    companion object {
        fun snackbar(view: View, resId: Int, duration: Int) = SnackbarBuilder(view, resId, duration)
    }

    fun action(resId: Int, action: Runnable): SnackbarBuilder {
        this.actionResId = resId
        this.action = action
        return this
    }

    fun onDismiss(onDismiss: Runnable): SnackbarBuilder {
        this.onDismiss = onDismiss
        return this
    }

    fun show(): Snackbar {
        val snackbar = Snackbar.make(view, resId, duration)

        if (actionResId != 0) {
            snackbar.setAction(actionResId, { action!!.run() })
        }

        if (onDismiss != null) {
            val onDismiss = this.onDismiss
            snackbar.dismisses().subscribe { onDismiss.run() }
        }

        snackbar.show();
        return snackbar
    }
}