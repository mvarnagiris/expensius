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

package com.mvcoding.financius.feature.overview;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.widget.Button;

import com.jakewharton.rxbinding.view.RxView;
import com.mvcoding.financius.R;
import com.mvcoding.financius.feature.ActivityComponent;
import com.mvcoding.financius.feature.ActivityStarter;
import com.mvcoding.financius.feature.BaseActivity;
import com.mvcoding.financius.feature.calculator.CalculatorActivity;
import com.mvcoding.financius.feature.tag.TagsActivity;

import javax.inject.Inject;

import butterknife.Bind;
import rx.Observable;

public class OverviewActivity extends BaseActivity<OverviewPresenter.View, OverviewComponent> implements OverviewPresenter.View {
    @Bind(R.id.tagsButton) Button tagsButton;
    @Bind(R.id.newTransactionFloatingActionButton) FloatingActionButton newTransactionFloatingActionButton;

    @Inject OverviewPresenter presenter;

    public static void start(@NonNull Context context) {
        ActivityStarter.with(context, OverviewActivity.class).start();
    }

    @Override protected int getLayoutId() {
        return R.layout.activity_overview;
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tagsButton.setOnClickListener(v -> TagsActivity.start(this));
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

    @NonNull @Override public Observable<Object> onNewTransactionClick() {
        return RxView.clicks(newTransactionFloatingActionButton);
    }

    @Override public void startNewTransaction() {
        CalculatorActivity.start(this, null, newTransactionFloatingActionButton);
    }
}
