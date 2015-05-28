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

package com.mvcoding.financius.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import static com.google.common.base.Preconditions.checkNotNull;

public class PreferencesUtils {
    /**
     * Add or remove object from {@link SharedPreferences}.
     *
     * @param context Any context.
     * @param key     Key of the object.
     * @param value   Object that needs to be stored, or if null, cleared from {@link SharedPreferences}.
     */
    public static void put(@NonNull Context context, @NonNull String key, @Nullable Object value) {
        checkNotNull(context, "Context cannot be null.");
        checkNotNull(key, "Key cannot be null.");

        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        if (value != null) {
            editor.putString(key, new Gson().toJson(value)).apply();
        } else {
            editor.remove(key).apply();
        }
    }

    /**
     * Get object from {@link SharedPreferences}.
     *
     * @param context Any context.
     * @param key     Key of the object.
     * @param cls     Class of expected object.
     * @param <T>     Object type.
     * @return Object from {@link SharedPreferences} or {@code null} if object was now found.
     */
    public static <T> T get(@NonNull Context context, @NonNull String key, @NonNull Class<T> cls) {
        checkNotNull(context, "Context cannot be null.");
        checkNotNull(key, "Key cannot be null.");
        checkNotNull(cls, "Class cannot be null.");

        final SharedPreferences preferences = getSharedPreferences(context);
        if (!preferences.contains(key)) {
            return null;
        }

        final String serialized = preferences.getString(key, null);
        if (serialized == null) {
            return null;
        }

        return new Gson().fromJson(serialized, cls);
    }

    public static SharedPreferences getSharedPreferences(@NonNull Context context) {
        checkNotNull(context, "Context cannot be null.");
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
