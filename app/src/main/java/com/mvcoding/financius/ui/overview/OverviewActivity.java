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

package com.mvcoding.financius.ui.overview;

import android.content.Context;
import android.support.annotation.NonNull;

import com.mvcoding.financius.ui.ActivityStarter;
import com.mvcoding.financius.ui.BaseActivity;

public class OverviewActivity extends BaseActivity {
    public static void start(@NonNull Context context) {
        ActivityStarter.with(context, OverviewActivity.class).start();
    }

    @Override protected int getLayoutId() {
        return 0;
    }
}
