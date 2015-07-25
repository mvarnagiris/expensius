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

package com.mvcoding.financius.ui.tag;

import android.support.annotation.NonNull;

import com.mvcoding.financius.R;
import com.mvcoding.financius.ui.ActivityComponent;
import com.mvcoding.financius.ui.BaseActivity;
import com.mvcoding.financius.ui.Presenter;

import javax.inject.Inject;

public class TagActivity extends BaseActivity<TagPresenter.View, TagComponent> implements TagPresenter.View {
    @Inject TagPresenter presenter;

    @Override protected int getLayoutId() {
        return R.layout.activity_tag;
    }

    @NonNull @Override protected TagComponent createComponent(@NonNull ActivityComponent component) {
        return component.plus(new TagModule());
    }

    @Override protected void inject(@NonNull TagComponent component) {
        component.inject(this);
    }

    @NonNull @Override protected Presenter<TagPresenter.View> getPresenter() {
        return presenter;
    }

    @NonNull @Override protected TagPresenter.View getPresenterView() {
        return this;
    }
}
