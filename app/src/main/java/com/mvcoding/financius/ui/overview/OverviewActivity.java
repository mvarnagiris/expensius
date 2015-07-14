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
import android.support.design.widget.FloatingActionButton;

import com.mvcoding.financius.R;
import com.mvcoding.financius.ui.ActivityComponent;
import com.mvcoding.financius.ui.ActivityStarter;
import com.mvcoding.financius.ui.BaseActivity;
import com.mvcoding.financius.ui.calculator.CalculatorActivity;

import javax.inject.Inject;

import butterknife.Bind;
import rx.Observable;
import rx.android.view.OnClickEvent;
import rx.android.view.ViewObservable;

public class OverviewActivity extends BaseActivity<OverviewPresenter.View, OverviewComponent> implements OverviewPresenter.View {
    @Bind(R.id.newTransactionFloatingActionButton) FloatingActionButton newTransactionFloatingActionButton;

    @Inject OverviewPresenter presenter;

    public static void start(@NonNull Context context) {
        ActivityStarter.with(context, OverviewActivity.class).start();
    }

    @Override protected int getLayoutId() {
        return R.layout.activity_overview;
    }

    @NonNull @Override protected OverviewComponent createComponent(@NonNull ActivityComponent component) {
        return component.plus(new OverviewModule());
    }

    @Override protected void inject(@NonNull OverviewComponent component) {
        component.inject(this);
    }

    @NonNull @Override protected OverviewPresenter getPresenter() {
        return presenter;
    }

    @NonNull @Override protected OverviewPresenter.View getPresenterView() {
        return this;
    }

    @NonNull @Override public Observable<OnClickEvent> onNewTransactionClick() {
        return ViewObservable.clicks(newTransactionFloatingActionButton);
    }

    @Override public void startNewTransaction() {
        CalculatorActivity.start(this, null, newTransactionFloatingActionButton);
    }
}
