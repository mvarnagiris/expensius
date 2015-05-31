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
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;

import com.mvcoding.financius.R;
import com.mvcoding.financius.ui.ActivityStarter;
import com.mvcoding.financius.ui.BaseActivity;

import javax.inject.Inject;

import butterknife.InjectView;
import rx.Observable;
import rx.android.view.OnClickEvent;
import rx.android.view.ViewObservable;

public class OverviewActivity extends BaseActivity<OverviewPresenter.View> implements OverviewPresenter.View {
    @InjectView(R.id.newTransactionFloatingActionButton) FloatingActionButton newTransactionFloatingActionButton;

    @Inject OverviewPresenter presenter;

    public static void start(@NonNull Context context) {
        ActivityStarter.with(context, OverviewActivity.class).start();
    }

    @Override protected int getLayoutId() {
        return R.layout.activity_overview;
    }

    @NonNull @Override protected OverviewPresenter getPresenter() {
        return presenter;
    }

    @Nullable @Override protected OverviewPresenter.View getPresenterView() {
        return this;
    }

    @Nullable @Override protected Object[] getModules() {
        return new Object[]{new OverviewModule()};
    }

    @NonNull @Override public Observable<OnClickEvent> onNewTransactionClick() {
        return ViewObservable.clicks(newTransactionFloatingActionButton);
    }

    @Override public void startNewTransaction() {
        // TODO: Implement.
    }
}
