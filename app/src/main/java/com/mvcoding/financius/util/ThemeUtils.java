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
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;

public class ThemeUtils {
    public static int getColor(@NonNull Context context, @AttrRes int resId) {
        final TypedArray a = context.getTheme().obtainStyledAttributes(new int[]{resId});
        final int color = a.getColor(0, 0);
        a.recycle();
        return color;
    }

    public static Drawable getDrawable(@NonNull Context context, @AttrRes int resId) {
        final TypedArray a = context.getTheme().obtainStyledAttributes(new int[]{resId});
        final Drawable drawable = a.getDrawable(0);
        a.recycle();
        return drawable;
    }
}
