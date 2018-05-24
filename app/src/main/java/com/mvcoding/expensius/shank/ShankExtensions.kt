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

package com.mvcoding.expensius.shank

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import com.memoizrlabs.Scope
import com.memoizrlabs.ScopedCache
import com.memoizrlabs.Shank.with

fun Activity.withThisScope(): ScopedCache = with(getScope(this))
fun Fragment.withThisScope(): ScopedCache = with(getScope(this))
fun View.withActivityScope(): ScopedCache = with(activityScope())
fun View.withFragmentScope(): ScopedCache = with(fragmentScope())
fun View.withSmallestScope(): ScopedCache = with(smallestScope())

fun View.activityScope(): Scope = getScope(context) ?: throw IllegalArgumentException("Context $context is not Scoped")
fun View.fragmentScope(): Scope = getFragment()!!.let { getScope(it) ?: throw IllegalArgumentException("Fragment $it and its' parents are not Scoped") }
fun View.smallestScope(): Scope = getFragment()?.let { getScope(it) } ?: getScope(context) ?: throw IllegalArgumentException("Context $context is not Scoped")

private fun View.getFragment(): Fragment? {
    return (context as AppCompatActivity).supportFragmentManager.fragments.first { it.view?.findViewById<View>(id) != null }
}

private fun getScope(context: Context): Scope? = when (context) {
    is Scoped -> context.scope
    is ContextThemeWrapper -> getScope(context.baseContext)
    is android.view.ContextThemeWrapper -> getScope(context.baseContext)
    is ContextWrapper -> getScope(context.baseContext)
    else -> null
}

private fun getScope(fragment: Fragment): Scope? = when {
    fragment is Scoped -> fragment.scope
    fragment.parentFragment != null -> getScope(fragment.parentFragment!!)
    else -> null
}