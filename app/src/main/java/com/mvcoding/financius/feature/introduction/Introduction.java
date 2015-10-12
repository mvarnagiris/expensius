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

package com.mvcoding.financius.feature.introduction;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

class Introduction {
    private final int imageResId;
    private final int titleResId;
    private final int messageResId;

    Introduction(@DrawableRes int imageResId, @StringRes int titleResId, @StringRes int messageResId) {
        this.imageResId = imageResId;
        this.titleResId = titleResId;
        this.messageResId = messageResId;
    }

    public int getImageResId() {
        return imageResId;
    }

    public int getTitleResId() {
        return titleResId;
    }

    public int getMessageResId() {
        return messageResId;
    }
}
