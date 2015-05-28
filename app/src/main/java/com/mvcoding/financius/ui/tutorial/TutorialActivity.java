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

package com.mvcoding.financius.ui.tutorial;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mvcoding.financius.R;
import com.mvcoding.financius.ui.BaseActivity;
import com.mvcoding.financius.ui.Presenter;

import javax.inject.Inject;

public class TutorialActivity extends BaseActivity<TutorialPresenter.View> implements TutorialPresenter.View {
    @Inject TutorialPresenter presenter;

    @Override protected int getLayoutId() {
        return R.layout.activity_tutorial;
    }

    @NonNull @Override protected Presenter<TutorialPresenter.View> getPresenter() {
        return presenter;
    }

    @Nullable @Override protected TutorialPresenter.View getPresenterView() {
        return this;
    }

    @Nullable @Override protected Object[] getModules() {
        return new Object[]{new TutorialModule()};
    }
}
