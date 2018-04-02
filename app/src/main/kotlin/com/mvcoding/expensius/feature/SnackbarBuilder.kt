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

import android.support.design.widget.Snackbar
import android.view.View
import com.jakewharton.rxbinding2.support.design.widget.dismisses

class SnackbarBuilder(private val view: View, private val resId: Int, private val duration: Int) {
    var actionResId: Int = 0
    var action: (() -> Unit)? = null
    var onDismiss: (() -> Unit)? = null

    companion object {
        fun snackbar(view: View, resId: Int, duration: Int) = SnackbarBuilder(view, resId, duration)
    }

    fun action(resId: Int, action: () -> Unit): SnackbarBuilder {
        this.actionResId = resId
        this.action = action
        return this
    }

    fun onDismiss(onDismiss: () -> Unit): SnackbarBuilder {
        this.onDismiss = onDismiss
        return this
    }

    fun show(): Snackbar {
        val snackbar = Snackbar.make(view, resId, duration)

        if (actionResId != 0) {
            snackbar.setAction(actionResId, { action!!.invoke() })
        }

        if (onDismiss != null) {
            snackbar.dismisses().subscribe { onDismiss!!.invoke() }
        }

        snackbar.show()
        return snackbar
    }
}