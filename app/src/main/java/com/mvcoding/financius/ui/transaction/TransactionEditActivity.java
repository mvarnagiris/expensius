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

package com.mvcoding.financius.ui.transaction;

import android.support.annotation.NonNull;

import com.mvcoding.financius.BaseComponent;
import com.mvcoding.financius.ui.ActivityComponent;
import com.mvcoding.financius.ui.BaseActivity;
import com.mvcoding.financius.ui.Presenter;
import com.mvcoding.financius.ui.PresenterView;

public class TransactionEditActivity extends BaseActivity {
    @Override protected int getLayoutId() {
        return 0;
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
