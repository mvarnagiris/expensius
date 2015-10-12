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

package com.mvcoding.financius.feature.user;

import android.content.Context;
import android.support.annotation.NonNull;

import com.mvcoding.financius.BaseComponent;
import com.mvcoding.financius.R;
import com.mvcoding.financius.feature.ActivityComponent;
import com.mvcoding.financius.feature.ActivityStarter;
import com.mvcoding.financius.feature.BaseActivity;
import com.mvcoding.financius.feature.Presenter;
import com.mvcoding.financius.feature.PresenterView;

public class LoginActivity extends BaseActivity {
    public static void start(@NonNull Context context) {
        ActivityStarter.with(context, LoginActivity.class).start();
    }

    @Override protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @NonNull @Override protected BaseComponent createComponent(@NonNull ActivityComponent component) {
        return null;
    }

    @Override protected void inject(@NonNull BaseComponent component) {

    }

    @NonNull @Override protected Presenter getPresenter() {
        return null;
    }

    @NonNull @Override protected PresenterView getPresenterView() {
        return null;
    }
}
