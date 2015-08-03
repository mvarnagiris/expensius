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
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.mvcoding.financius.R;
import com.mvcoding.financius.data.model.Tag;
import com.mvcoding.financius.ui.ActivityComponent;
import com.mvcoding.financius.ui.ActivityStarter;
import com.mvcoding.financius.ui.BaseActivity;
import com.mvcoding.financius.ui.Presenter;
import com.mvcoding.financius.util.recyclerview.RecyclerUtils;
import com.mvcoding.financius.util.rx.Event;
import com.mvcoding.financius.util.rx.RefreshEvent;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import butterknife.Bind;
import rx.Observable;

public class TagsActivity extends BaseActivity<TagsPresenter.View, TagsComponent> implements TagsPresenter.View {
    private static final String EXTRA_DISPLAY_TYPE = "EXTRA_DISPLAY_TYPE";
    private static final String EXTRA_SELECTED_ITEMS = "EXTRA_SELECTED_ITEMS";

    @Bind(R.id.recyclerView) RecyclerView recyclerView;

    @Inject TagsPresenter presenter;

    private TagsAdapter adapter;

    public static void start(@NonNull Context context) {
        getActivityStarter(context).extra(EXTRA_DISPLAY_TYPE, TagsPresenter.DisplayType.View).start();
    }

    public static void startForResult(@NonNull Context context, int requestCode, @Nullable Set<Tag> selectedTags) {
        getActivityStarter(context).extra(EXTRA_DISPLAY_TYPE, TagsPresenter.DisplayType.MultiChoice)
                .extra(EXTRA_SELECTED_ITEMS, selectedTags)
                .startForResult(requestCode);
    }

    private static ActivityStarter getActivityStarter(@NonNull Context context) {
        return ActivityStarter.with(context, TagsActivity.class);
    }

    @Override protected int getLayoutId() {
        return R.layout.activity_tags;
    }

    @Override protected void onViewCreated(Bundle savedInstanceState) {
        super.onViewCreated(savedInstanceState);

        adapter = new TagsAdapter();
        RecyclerUtils.setupList(recyclerView);
        recyclerView.setAdapter(adapter);
    }

    @NonNull @Override protected TagsComponent createComponent(@NonNull ActivityComponent component) {
        final TagsPresenter.DisplayType displayType = (TagsPresenter.DisplayType) getIntent().getSerializableExtra(EXTRA_DISPLAY_TYPE);
        final Tag[] selectedItemsArray = (Tag[]) getIntent().getParcelableArrayExtra(EXTRA_SELECTED_ITEMS);
        Set<Tag> selectedItems = null;
        if (selectedItemsArray != null) {
            selectedItems = new HashSet<>();
            Collections.addAll(selectedItems, selectedItemsArray);
        }
        return component.plus(new TagsModule(displayType, selectedItems));
    }

    @Override protected void inject(@NonNull TagsComponent component) {
        component.inject(this);
    }

    @NonNull @Override protected Presenter<TagsPresenter.View> getPresenter() {
        return presenter;
    }

    @NonNull @Override protected TagsPresenter.View getPresenterView() {
        return this;
    }

    @NonNull @Override public Observable<TagsPresenter.Edge> onEdgeReached() {
        // TODO: Implement.
        return Observable.empty();
    }

    @NonNull @Override public Observable<RefreshEvent> onRefresh() {
        // TODO: Implement.
        return Observable.empty();
    }

    @NonNull @Override public Observable<Tag> onTagSelected() {
        return adapter.getItemClickObservable();
    }

    @NonNull @Override public Observable<Event> onSaveSelection() {
        // TODO: Implement.
        return Observable.empty();
    }

    @Override public void setSelectedItems(@NonNull Set<Tag> tags) {
        adapter.setSelectedItems(tags);
    }

    @Override public void setSelected(@NonNull Tag tag, boolean isSelected) {
        adapter.setSelected(tag, isSelected);
    }

    @Override public void setDisplayType(@NonNull TagsPresenter.DisplayType displayType) {
        adapter.setDisplayType(displayType);
    }

    @Override public void show(@NonNull List<Tag> tags) {
        adapter.setItems(tags);
    }

    @Override public void add(int position, @NonNull List<Tag> tags) {
        adapter.insertItems(position, tags);
    }

    @Override public void update(int position, @NonNull List<Tag> tags) {
        adapter.updateItems(position, tags);
    }

    @Override public void startEdit(@NonNull Tag tag) {
        // TODO: Implement.
    }

    @Override public void startSelected(@NonNull Tag tag) {
        // TODO: Implement.
    }

    @Override public void startSelected(@NonNull Set<Tag> tag) {
        // TODO: Implement.
    }
}
