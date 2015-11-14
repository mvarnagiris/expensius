package com.mvcoding.financius.extension

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.support.v7.view.ContextThemeWrapper

fun Context.toActivity(): Activity {
    when (this) {
        is Activity -> return this
        is ContextThemeWrapper -> return this.baseContext.toActivity()
        is android.view.ContextThemeWrapper -> return this.baseContext.toActivity()
        is ContextWrapper -> return this.baseContext.toActivity()
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