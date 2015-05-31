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

package com.mvcoding.financius.ui.transaction.calculator;

import android.content.Context;
import android.database.Observable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mvcoding.financius.R;
import com.mvcoding.financius.ui.ActivityStarter;
import com.mvcoding.financius.ui.BaseActivity;

import javax.inject.Inject;

import rx.android.view.OnClickEvent;

public class CalculatorActivity extends BaseActivity<CalculatorPresenter.View> implements CalculatorPresenter.View {
    @Inject CalculatorPresenter presenter;

    public static void start(@NonNull Context context) {
        ActivityStarter.with(context, CalculatorActivity.class).start();
    }

    @Override protected int getLayoutId() {
        return R.layout.activity_calculator;
    }

    @NonNull @Override public CalculatorPresenter getPresenter() {
        return presenter;
    }

    @Nullable @Override protected CalculatorPresenter.View getPresenterView() {
        return this;
    }

    @Nullable @Override protected Object[] getModules() {
        return new Object[]{new CalculatorModule()};
    }

    @NonNull @Override public Observable<OnClickEvent> on0Click() {
        return null;
    }

    @NonNull @Override public Observable<OnClickEvent> on1Click() {
        return null;
    }

    @NonNull @Override public Observable<OnClickEvent> on2Click() {
        return null;
    }

    @NonNull @Override public Observable<OnClickEvent> on3Click() {
        return null;
    }

    @NonNull @Override public Observable<OnClickEvent> on4Click() {
        return null;
    }

    @NonNull @Override public Observable<OnClickEvent> on5Click() {
        return null;
    }

    @NonNull @Override public Observable<OnClickEvent> on6Click() {
        return null;
    }

    @NonNull @Override public Observable<OnClickEvent> on7Click() {
        return null;
    }

    @NonNull @Override public Observable<OnClickEvent> on8Click() {
        return null;
    }

    @NonNull @Override public Observable<OnClickEvent> on9Click() {
        return null;
    }

    @NonNull @Override public Observable<OnClickEvent> onDecimalClick() {
        return null;
    }

    @NonNull @Override public Observable<OnClickEvent> onEqualsClick() {
        return null;
    }

    @NonNull @Override public Observable<OnClickEvent> onDivideClick() {
        return null;
    }

    @NonNull @Override public Observable<OnClickEvent> onMultiplyClick() {
        return null;
    }

    @NonNull @Override public Observable<OnClickEvent> onSubtractClick() {
        return null;
    }

    @NonNull @Override public Observable<OnClickEvent> onAddClick() {
        return null;
    }

    @NonNull @Override public Observable<OnClickEvent> onDeleteClick() {
        return null;
    }
}
