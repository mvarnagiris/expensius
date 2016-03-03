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

import android.content.SharedPreferences
import com.google.gson.Gson
import kotlin.reflect.KClass

fun <T : Any> SharedPreferences.getObject(key: String, kClass: KClass<T>, defaultValue: () -> T): T {
    return getString(key, null)?.let { Gson().fromJson(it, kClass.java) } ?: defaultValue()
}

fun <T : Any> SharedPreferences.putObject(key: String, obj: T) {
    edit().putString(key, Gson().toJson(obj)).apply()
}

fun SharedPreferences.putBoolean(key: String, value: Boolean) {
    edit().putBoolean(key, value).apply()
}