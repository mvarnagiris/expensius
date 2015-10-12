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

package com.mvcoding.financius.feature.splash;

import android.support.annotation.NonNull;

import com.mvcoding.financius.R;
import com.mvcoding.financius.feature.ActivityComponent;
import com.mvcoding.financius.feature.BaseActivity;
import com.mvcoding.financius.feature.Presenter;
import com.mvcoding.financius.feature.introduction.IntroductionActivity;
import com.mvcoding.financius.feature.overview.OverviewActivity;

import javax.inject.Inject;

public class SplashActivity extends BaseActivity<SplashPresenter.View, SplashComponent> implements SplashPresenter.View {
    @Inject SplashPresenter presenter;

    @Override protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @NonNull @Override protected SplashComponent createComponent(@NonNull ActivityComponent component) {
        return component.plus(new SplashModule());
    }

    @Override protected void inject(@NonNull SplashComponent component) {
        component.inject(this);
    }

    @NonNull @Override protected Presenter<SplashPresenter.View> getPresenter() {
        return presenter;
    }

    @NonNull @Override protected SplashPresenter.View getPresenterView() {
        return this;
    }

    @Override public void startOverviewAndClose() {
        OverviewActivity.start(this);
        close();
    }

    @Override public void startIntroductionAndClose() {
        IntroductionActivity.start(this);
        close();
    }
}
