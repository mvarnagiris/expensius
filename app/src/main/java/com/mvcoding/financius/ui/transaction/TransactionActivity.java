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

import com.mvcoding.financius.R;
import com.mvcoding.financius.ui.ActivityComponent;
import com.mvcoding.financius.ui.BaseActivity;

import javax.inject.Inject;

public class TransactionActivity extends BaseActivity<TransactionPresenter.View, TransactionComponent> implements TransactionPresenter.View {
    @Inject TransactionPresenter presenter;

    @Override protected int getLayoutId() {
        return R.layout.activity_transaction;
    }

    @NonNull @Override protected TransactionComponent createComponent(@NonNull ActivityComponent component) {
        return component.plus(new TransactionModule());
    }

    @Override protected void inject(@NonNull TransactionComponent component) {
        component.inject(this);
    }

    @NonNull @Override protected TransactionPresenter getPresenter() {
        return presenter;
    }

    @NonNull @Override protected TransactionPresenter.View getPresenterView() {
        return this;
    }
}
