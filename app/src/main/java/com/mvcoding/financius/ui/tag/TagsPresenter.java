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

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mvcoding.financius.api.ServiceApi;
import com.mvcoding.financius.data.DataLoadApi;
import com.mvcoding.financius.data.model.Tag;
import com.mvcoding.financius.data.paging.Page;
import com.mvcoding.financius.data.paging.PageResult;
import com.mvcoding.financius.ui.ActivityScope;
import com.mvcoding.financius.ui.CloseablePresenterView;
import com.mvcoding.financius.ui.ErrorPresenterView;
import com.mvcoding.financius.ui.Presenter;
import com.mvcoding.financius.ui.PresenterView;
import com.mvcoding.financius.util.recyclerview.PagingEdge;
import com.mvcoding.financius.util.rx.Event;
import com.mvcoding.financius.util.rx.RefreshEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import rx.Observable;
import rx.Scheduler;

@ActivityScope class TagsPresenter extends Presenter<TagsPresenter.View> {
    private final DisplayType displayType;
    private final DataLoadApi dataLoadApi;
    private final ServiceApi serviceApi;
    private final int pageSize;
    private final Scheduler uiScheduler;
    private final Scheduler ioScheduler;
    private final Set<Tag> selectedItems;
    private final List<Tag> cache;

    private PageResult<Tag> startPageResult;
    private PageResult<Tag> endPageResult;
    private Observable<Page> pageObservable;

    TagsPresenter(@NonNull DisplayType displayType, @Nullable Set<Tag> selectedItems, @NonNull DataLoadApi dataLoadApi, @NonNull ServiceApi serviceApi, @IntRange(from = 1) int pageSize, @NonNull Scheduler uiScheduler, @NonNull Scheduler ioScheduler) {
        this.displayType = displayType;
        this.dataLoadApi = dataLoadApi;
        this.serviceApi = serviceApi;
        this.pageSize = pageSize;
        this.uiScheduler = uiScheduler;
        this.ioScheduler = ioScheduler;
        this.selectedItems = new HashSet<>();
        if (selectedItems != null) {
            this.selectedItems.addAll(selectedItems);
        }
        cache = new ArrayList<>();
    }

    @Override protected void onViewAttached(@NonNull View view) {
        super.onViewAttached(view);
        view.setDisplayType(displayType);

        if (!selectedItems.isEmpty()) {
            view.setSelectedItems(selectedItems);
        }

        if (!cache.isEmpty()) {
            view.show(cache);
        }

        unsubscribeOnDetach(dataLoadApi.loadTags(getPageObservable(view))
                                    .doOnNext(this::updateCache)
                                    .observeOn(uiScheduler)
                                    .subscribeOn(ioScheduler)
                                    .subscribe(pageResult -> showItems(view, pageResult), throwable -> showFatalError(throwable, view, view, uiScheduler)));

        unsubscribeOnDetach(view.onTagSelected()
                                    .subscribe(tag -> onItemSelected(view, tag), throwable -> showFatalError(throwable, view, view, uiScheduler)));

        unsubscribeOnDetach(view.onSaveSelection()
                                    .map(event -> selectedItems)
                                    .subscribe(tags -> onSaveSelection(view, tags), throwable -> showFatalError(throwable, view, view, uiScheduler)));
    }

    @NonNull private Observable<Page> getPageObservable(@NonNull View view) {
        final Observable<PagingEdge> edgeObservable = view.onEdgeReached();

        final Observable<Page> startEdgePageObservable = edgeObservable.filter(edge -> edge == PagingEdge.Start)
                .filter(edge -> startPageResult != null)
                .filter(edge -> startPageResult.hasPrevious())
                .map(edge -> startPageResult.getPage().getPreviousPage());

        final Observable<Page> endEdgePageObservable = edgeObservable.filter(edge -> edge == PagingEdge.End)
                .filter(edge -> endPageResult != null)
                .filter(edge -> endPageResult.hasNext())
                .map(edge -> endPageResult.getPage().getNextPage());

        final Observable<Page> refreshPageObservable = view.onRefresh()
                .flatMap(refreshEvent -> serviceApi.refresh())
                .map(event -> new Page(0, pageSize));

        pageObservable = pageObservable == null ? Observable.just(new Page(0, pageSize)) : Observable.empty();

        return Observable.merge(startEdgePageObservable, endEdgePageObservable, refreshPageObservable, pageObservable);
    }

    private void updateCache(@NonNull PageResult<Tag> pageResult) {
        final Page page = pageResult.getPage();
        final int insertPosition;
        if (pageResult.isDataInvalidated()) {
            cache.clear();
            startPageResult = null;
            endPageResult = null;
            insertPosition = 0;
        } else {
            insertPosition = page.getStart();
        }

        cache.addAll(insertPosition, pageResult.getItems());
    }

    private void showItems(@NonNull View view, @NonNull PageResult<Tag> pageResult) {
        final Page page = pageResult.getPage();
        final List<Tag> items = pageResult.getItems();

        if (pageResult.isDataInvalidated()) {
            view.show(items);
        } else if (isPageAlreadyLoaded(page)) {
            view.update(page.getStart(), items);
        } else {
            view.add(page.getStart(), items);
        }

        if (startPageResult == null || startPageResult.getPage().compareTo(page) >= 0) {
            startPageResult = pageResult;
        }

        if (endPageResult == null || endPageResult.getPage().compareTo(page) <= 0) {
            endPageResult = pageResult;
        }
    }

    private boolean isPageAlreadyLoaded(@NonNull Page page) {
        final int loadedStart = startPageResult == null ? 0 : startPageResult.getPage().getStart();
        final int loadedCount = endPageResult == null ? 0 : endPageResult.getPage().getStart() + endPageResult.getPage().getSize();

        return page.getStart() >= loadedStart && page.getStart() + page.getSize() <= loadedCount;
    }

    private void onItemSelected(@NonNull View view, @NonNull Tag tag) {
        switch (displayType) {
            case View: {
                view.startEdit(tag);
                break;
            }
            case Select: {
                view.startSelected(tag);
                break;
            }
            case SingleChoice: {
                final boolean isSelected = selectedItems.contains(tag);

                if (!isSelected) {
                    final Iterator<Tag> iterator = selectedItems.iterator();
                    if (iterator.hasNext()) {
                        final Tag oldTag = iterator.next();
                        view.setSelected(oldTag, false);
                    }
                    selectedItems.clear();
                    selectedItems.add(tag);
                    view.setSelected(tag, true);
                }
                break;
            }
            case MultiChoice: {
                final boolean isSelected = selectedItems.contains(tag);
                if (isSelected) {
                    selectedItems.remove(tag);
                } else {
                    selectedItems.add(tag);
                }
                view.setSelected(tag, !isSelected);
                break;
            }
            default:
                throw new IllegalStateException("DisplayType " + displayType + " is not supported.");
        }
    }

    private void onSaveSelection(@NonNull View view, @NonNull Set<Tag> selectedItems) {
        if (displayType == DisplayType.SingleChoice) {
            final Iterator<Tag> iterator = selectedItems.iterator();
            if (iterator.hasNext()) {
                final Tag selectedTag = iterator.next();
                view.startSelected(selectedTag);
            }
        } else {
            view.startSelected(selectedItems);
        }
    }

    public enum DisplayType {
        View, Select, SingleChoice, MultiChoice
    }

    public interface View extends PresenterView, ErrorPresenterView, CloseablePresenterView {
        @NonNull Observable<PagingEdge> onEdgeReached();
        @NonNull Observable<RefreshEvent> onRefresh();
        @NonNull Observable<Tag> onTagSelected();
        @NonNull Observable<Event> onSaveSelection();
        void setSelectedItems(@NonNull Set<Tag> tags);
        void setSelected(@NonNull Tag tag, boolean isSelected);
        void setDisplayType(@NonNull DisplayType displayType);
        void show(@NonNull List<Tag> tags);
        void add(int position, @NonNull List<Tag> tags);
        void update(int position, @NonNull List<Tag> tags);
        void startEdit(@NonNull Tag tag);
        void startSelected(@NonNull Tag tag);
        void startSelected(@NonNull Set<Tag> tags);
    }
}
