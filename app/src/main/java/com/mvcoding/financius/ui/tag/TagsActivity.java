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
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.jakewharton.rxbinding.view.RxView;
import com.mvcoding.financius.R;
import com.mvcoding.financius.data.model.Tag;
import com.mvcoding.financius.ui.ActivityComponent;
import com.mvcoding.financius.ui.ActivityStarter;
import com.mvcoding.financius.ui.BaseActivity;
import com.mvcoding.financius.ui.Presenter;
import com.mvcoding.financius.util.recyclerview.PagingEdge;
import com.mvcoding.financius.util.recyclerview.RecyclerUtils;
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

    private static final String RESULT_EXTRA_ITEM = "RESULT_EXTRA_ITEM";
    private static final String RESULT_EXTRA_ITEMS = "RESULT_EXTRA_ITEMS";

    private static final int REQUEST_TAG = 1;

    @Bind(R.id.recyclerView) RecyclerView recyclerView;
    @Bind(R.id.buttonBarView) View buttonBarView;
    @Bind(R.id.saveButton) Button saveButton;

    @Inject TagsPresenter presenter;

    private TagsAdapter adapter;
    private View lastClickedView;

    public static void start(@NonNull Context context) {
        getActivityStarter(context).extra(EXTRA_DISPLAY_TYPE, TagsPresenter.DisplayType.View).start();
    }

    public static void startForResult(@NonNull Context context, int requestCode, @Nullable Set<Tag> selectedTags) {
        getActivityStarter(context).extra(EXTRA_DISPLAY_TYPE, TagsPresenter.DisplayType.MultiChoice)
                .extra(EXTRA_SELECTED_ITEMS, selectedTags)
                .startForResult(requestCode);
    }

    @Nullable public static Tag getResultTag(@NonNull Intent data) {
        return data.getParcelableExtra(RESULT_EXTRA_ITEM);
    }

    @Nullable public static Set<Tag> getResultTags(@NonNull Intent data) {
        return getTags(data, RESULT_EXTRA_ITEMS);
    }

    @Nullable private static Set<Tag> getTags(@NonNull Intent intent, @NonNull String extraName) {
        final Parcelable[] parcelables = intent.getParcelableArrayExtra(extraName);
        final Tag[] itemArray = new Tag[parcelables == null ? 0 : parcelables.length];
        for (int i = 0, size = parcelables == null ? 0 : parcelables.length; i < size; i++) {
            itemArray[i] = (Tag) parcelables[i];
        }

        Set<Tag> items = null;
        if (itemArray.length > 0) {
            items = new HashSet<>();
            Collections.addAll(items, itemArray);
        }

        return items;
    }

    @NonNull private static ActivityStarter getActivityStarter(@NonNull Context context) {
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

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.models, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_create:
                // TODO: Go through presenter.
                TagActivity.startForResult(this, REQUEST_TAG, new Tag().withDefaultValues());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull @Override protected TagsComponent createComponent(@NonNull ActivityComponent component) {
        final TagsPresenter.DisplayType displayType = (TagsPresenter.DisplayType) getIntent().getSerializableExtra(EXTRA_DISPLAY_TYPE);
        final Set<Tag> selectedItems = getTags(getIntent(), EXTRA_SELECTED_ITEMS);

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

    @NonNull @Override public Observable<PagingEdge> onEdgeReached() {
        // TODO: Implement.
        return Observable.empty();
    }

    @NonNull @Override public Observable<RefreshEvent> onRefresh() {
        // TODO: Implement.
        return Observable.empty();
    }

    @NonNull @Override public Observable<Tag> onTagSelected() {
        return adapter.getItemClickObservable()
                .doOnNext(tagClickEvent -> lastClickedView = tagClickEvent.getView().findViewById(R.id.titleTextView))
                .map(TagsAdapter.TagClickEvent::getTag);
    }

    @NonNull @Override public Observable<Object> onSaveSelection() {
        return RxView.clicks(saveButton);
    }

    @Override public void setSelectedItems(@NonNull Set<Tag> tags) {
        adapter.setSelectedItems(tags);
    }

    @Override public void setSelected(@NonNull Tag tag, boolean isSelected) {
        adapter.setSelected(tag, isSelected);
    }

    @Override public void setDisplayType(@NonNull TagsPresenter.DisplayType displayType) {
        adapter.setDisplayType(displayType);
        buttonBarView.setVisibility(displayType == TagsPresenter.DisplayType.View ? View.GONE : View.VISIBLE);
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
        TagActivity.startForResult(this, REQUEST_TAG, tag, lastClickedView);
    }

    @Override public void startSelected(@NonNull Tag tag) {
        final Intent data = new Intent();
        data.putExtra(RESULT_EXTRA_ITEM, tag);
        setResult(RESULT_OK, data);
        close();
    }

    @Override public void startSelected(@NonNull Set<Tag> tags) {
        final Intent data = new Intent();
        data.putExtra(RESULT_EXTRA_ITEMS, tags.toArray(new Tag[tags.size()]));
        setResult(RESULT_OK, data);
        close();
    }
}
