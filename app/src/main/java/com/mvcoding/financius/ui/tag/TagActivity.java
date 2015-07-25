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

import android.content.Context;
import android.support.annotation.NonNull;

import com.mvcoding.financius.R;
import com.mvcoding.financius.data.model.Tag;
import com.mvcoding.financius.ui.ActivityComponent;
import com.mvcoding.financius.ui.ActivityStarter;
import com.mvcoding.financius.ui.BaseActivity;
import com.mvcoding.financius.ui.Presenter;
import com.mvcoding.financius.util.rx.Event;

import javax.inject.Inject;

import rx.Observable;

public class TagActivity extends BaseActivity<TagPresenter.View, TagComponent> implements TagPresenter.View {
    private static final String EXTRA_TAG = "EXTRA_TAG";

    @Inject TagPresenter presenter;

    public static void startForResult(@NonNull Context context, int requestCode, @NonNull Tag tag) {
        ActivityStarter.with(context, TagActivity.class).extra(EXTRA_TAG, tag).startForResult(requestCode);
    }

    @Override protected int getLayoutId() {
        return R.layout.activity_tag;
    }

    @NonNull @Override protected TagComponent createComponent(@NonNull ActivityComponent component) {
        final Tag tag = getIntent().getParcelableExtra(EXTRA_TAG);
        return component.plus(new TagModule(tag));
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

    @NonNull @Override public Observable<String> onTitleChanged() {
        return null;
    }

    @NonNull @Override public Observable<Integer> onColorChanged() {
        return null;
    }

    @NonNull @Override public Observable<Event> onSave() {
        return null;
    }

    @Override public void showTag(@NonNull Tag tag) {

    }

    @Override public void startResult(@NonNull Tag tag) {

    }
}
