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

package com.mvcoding.financius.ui.introduction;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.mvcoding.financius.R;
import com.mvcoding.financius.ui.ActivityComponent;
import com.mvcoding.financius.ui.ActivityStarter;
import com.mvcoding.financius.ui.BaseActivity;
import com.mvcoding.financius.ui.Presenter;
import com.mvcoding.financius.ui.overview.OverviewActivity;
import com.mvcoding.financius.ui.user.LoginActivity;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import me.relex.circleindicator.CircleIndicator;
import rx.Observable;
import rx.android.view.OnClickEvent;
import rx.android.view.ViewObservable;

public class IntroductionActivity extends BaseActivity<IntroductionPresenter.View, IntroductionComponent> implements IntroductionPresenter.View, ViewPager.OnPageChangeListener {
    @Bind(R.id.viewPager) ViewPager viewPager;
    @Bind(R.id.circleIndicator) CircleIndicator circleIndicator;
    @Bind(R.id.nextImageButton) ImageButton nextImageButton;
    @Bind(R.id.loginButton) Button loginButton;
    @Bind(R.id.skipLoginButton) Button skipLoginButton;

    @Inject IntroductionPresenter presenter;

    public static void start(@NonNull Context context) {
        ActivityStarter.with(context, IntroductionActivity.class).start();
    }

    @Override protected int getLayoutId() {
        return R.layout.activity_introduction;
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewPager.setAdapter(new IntroductionAdapter());
        circleIndicator.setViewPager(viewPager);
        circleIndicator.setOnPageChangeListener(this);
    }

    @NonNull @Override protected IntroductionComponent createComponent(@NonNull ActivityComponent component) {
        return component.plus(new IntroductionModule());
    }

    @Override protected void inject(@NonNull IntroductionComponent component) {
        component.inject(this);
    }

    @NonNull @Override protected Presenter<IntroductionPresenter.View> getPresenter() {
        return presenter;
    }

    @NonNull @Override protected IntroductionPresenter.View getPresenterView() {
        return this;
    }

    @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override public void onPageSelected(int position) {
        final boolean isLastItem = position == viewPager.getAdapter().getCount() - 1;
        nextImageButton.setVisibility(isLastItem ? View.GONE : View.VISIBLE);
        skipLoginButton.setVisibility(isLastItem ? View.VISIBLE : View.GONE);
    }

    @Override public void onPageScrollStateChanged(int state) {
    }

    @OnClick(R.id.nextImageButton) void nextPage() {
        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
    }

    @NonNull @Override public Observable<OnClickEvent> onSkipLoginClick() {
        return ViewObservable.clicks(skipLoginButton);
    }

    @NonNull @Override public Observable<OnClickEvent> onLoginClick() {
        return ViewObservable.clicks(loginButton);
    }

    @Override public void startOverviewAndClose() {
        OverviewActivity.start(this);
        close();
    }

    @Override public void startLoginAndClose() {
        LoginActivity.start(this);
        close();
    }
}
