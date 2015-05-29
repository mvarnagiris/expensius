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
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.mvcoding.financius.R;
import com.mvcoding.financius.ui.ActivityStarter;
import com.mvcoding.financius.ui.BaseActivity;
import com.mvcoding.financius.ui.Presenter;
import com.mvcoding.financius.ui.overview.OverviewActivity;
import com.mvcoding.financius.ui.user.LoginActivity;

import javax.inject.Inject;

import butterknife.InjectView;
import butterknife.OnClick;
import me.relex.circleindicator.CircleIndicator;
import rx.Observable;
import rx.android.view.OnClickEvent;
import rx.android.view.ViewObservable;

public class IntroductionActivity extends BaseActivity<IntroductionPresenter.View> implements IntroductionPresenter.View, ViewPager.OnPageChangeListener {
    @InjectView(R.id.viewPager) ViewPager viewPager;
    @InjectView(R.id.circleIndicator) CircleIndicator circleIndicator;
    @InjectView(R.id.nextImageButton) ImageButton nextImageButton;
    @InjectView(R.id.loginButton) Button loginButton;
    @InjectView(R.id.skipLoginButton) Button skipLoginButton;

    @Inject IntroductionPresenter presenter;

    public static void start(@NonNull Context context) {
        ActivityStarter.with(context, IntroductionActivity.class).start();
    }

    @Override protected int getLayoutId() {
        return R.layout.activity_introduction;
    }

    @Override protected void onViewCreated(@Nullable Bundle savedInstanceState) {
        super.onViewCreated(savedInstanceState);

        viewPager.setAdapter(new IntroductionAdapter());
        circleIndicator.setViewPager(viewPager);
        circleIndicator.setOnPageChangeListener(this);
    }

    @NonNull @Override protected Presenter<IntroductionPresenter.View> getPresenter() {
        return presenter;
    }

    @Nullable @Override protected IntroductionPresenter.View getPresenterView() {
        return this;
    }

    @Nullable @Override protected Object[] getModules() {
        return new Object[]{new IntroductionModule()};
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

    @Override public Observable<OnClickEvent> onSkipLoginClick() {
        return ViewObservable.clicks(skipLoginButton);
    }

    @Override public Observable<OnClickEvent> onLoginClick() {
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
