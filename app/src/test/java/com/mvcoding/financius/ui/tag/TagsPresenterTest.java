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

import com.mvcoding.financius.api.ServiceApi;
import com.mvcoding.financius.data.DataLoadApi;
import com.mvcoding.financius.data.model.Tag;
import com.mvcoding.financius.data.paging.Page;
import com.mvcoding.financius.data.paging.PageResult;
import com.mvcoding.financius.ui.BasePresenterTest;
import com.mvcoding.financius.util.recyclerview.PagingEdge;
import com.mvcoding.financius.util.rx.RefreshEvent;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("Convert2Lambda") public class TagsPresenterTest extends BasePresenterTest<TagsPresenter, TagsPresenter.View> {
    private static final int PAGE_SIZE = 20;

    private static final PublishSubject<PagingEdge> edgeSubject = PublishSubject.create();
    private static final PublishSubject<RefreshEvent> refreshSubject = PublishSubject.create();
    private static final PublishSubject<Tag> tagSelectedSubject = PublishSubject.create();
    private static final PublishSubject<Object> saveSelectionSubject = PublishSubject.create();

    @Mock private DataLoadApi dataLoadApi;
    @Mock private ServiceApi serviceApi;

    private int totalDataSize;
    private boolean invalidateCache;
    private BehaviorSubject<PageResult<Tag>> pageResultSubject;

    @Override protected TagsPresenter createPresenter() {
        setTotalDataSize(PAGE_SIZE);
        setInvalidateCache(true);
        when(serviceApi.refresh()).thenReturn(Observable.just(new Object()));
        when(dataLoadApi.loadTags(any())).thenAnswer(new Answer<Observable<PageResult<Tag>>>() {
            @Override public Observable<PageResult<Tag>> answer(InvocationOnMock invocation) throws Throwable {
                //noinspection unchecked
                final Observable<Page> pageObservable = (Observable<Page>) invocation.getArguments()[0];
                pageObservable.subscribe(new Action1<Page>() {
                    @Override public void call(Page page) {
                        final int itemCount = Math.min(totalDataSize, page.getStart() + page.getSize());
                        final List<Tag> tags = new ArrayList<>(itemCount);
                        for (int i = 0; i < itemCount; i++) {
                            tags.add(mock(Tag.class));
                        }
                        final PageResult<Tag> pageResult = new PageResult<>(page, tags, invalidateCache);
                        pageResultSubject.onNext(pageResult);
                    }
                });
                return pageResultSubject;
            }
        });
        resetDataLoadApi();

        return createPresenter(TagsPresenter.DisplayType.View, null);
    }

    @Override protected TagsPresenter.View createView() {
        final TagsPresenter.View view = mock(TagsPresenter.View.class);
        when(view.onEdgeReached()).thenReturn(edgeSubject);
        when(view.onRefresh()).thenReturn(refreshSubject);
        when(view.onTagSelected()).thenReturn(tagSelectedSubject);
        when(view.onSaveSelection()).thenReturn(saveSelectionSubject);
        return view;
    }

    @Test public void onViewAttached_setDisplayType() throws Exception {
        presenterOnViewAttached();

        verify(view).setDisplayType(TagsPresenter.DisplayType.View);
    }

    @Test public void onViewAttached_setSelectedItems_whenThereAreSelectedItems() throws Exception {
        final Set<Tag> selectedItems = new HashSet<>();
        selectedItems.add(mock(Tag.class));
        createPresenter(TagsPresenter.DisplayType.MultiChoice, selectedItems);
        presenterOnViewAttached();

        verify(view).setSelectedItems(selectedItems);
    }

    @Test public void onViewAttached_doNotSetSelectedItems_whenThereAreNoSelectedItems() throws Exception {
        presenterOnViewAttached();

        verify(view, never()).setSelectedItems(any());
    }

    @Test public void onViewAttached_loadFirstPageAndShow_whenDoNotHaveCachedItems() throws Exception {
        presenterOnViewAttached();

        final ArgumentCaptor<List<Tag>> tagsArgument = ArgumentCaptor.forClass(List.class);
        verify(view).show(tagsArgument.capture());
        final List<Tag> tags = tagsArgument.getValue();
        assertThat(tags).isNotNull();
        assertThat(tags).hasSize(PAGE_SIZE);
    }

    @Test public void onViewAttached_showAndDoNotLoad_whenAlreadyHaveCachedItems() throws Exception {
        presenterOnViewAttached();
        presenterOnViewDetached();
        resetDataLoadApi();

        presenterOnViewAttached();

        final ArgumentCaptor<List<Tag>> tagsArgument = ArgumentCaptor.forClass(List.class);
        verify(view, times(2)).show(tagsArgument.capture());
        final List<Tag> tags = tagsArgument.getValue();
        assertThat(tags).isNotNull();
        assertThat(tags).hasSize(PAGE_SIZE);
    }

    @Test public void onEdgeReached_doNotLoadMoreItems_whenEdgeIsEndAndLastPageIsLoaded() throws Exception {
        setTotalDataSize(PAGE_SIZE - 1);
        presenterOnViewAttached();

        edgeSubject.onNext(PagingEdge.End);

        verify(view, times(1)).show(any());
        verify(view, never()).add(anyInt(), any());
    }

    @Test public void onEdgeReached_loadNextPage_whenEdgeIsEndAndLastPageIsNotLoaded() throws Exception {
        setTotalDataSize(PAGE_SIZE * 2);
        presenterOnViewAttached();
        setInvalidateCache(false);

        edgeSubject.onNext(PagingEdge.End);

        verify(view, times(1)).show(any());
        verify(view, times(1)).add(anyInt(), any());
    }

    @Test public void onEdgeReached_doNotLoadMoreItems_whenEdgeIsStartAndFirstPageIsLoaded() throws Exception {
        presenterOnViewAttached();

        edgeSubject.onNext(PagingEdge.Start);

        verify(view, times(1)).show(any());
        verify(view, never()).add(anyInt(), any());
    }

    @Test public void onEdgeReached_loadPreviousPage_whenEdgeIsStartAndFirstPageIsNotLoaded() throws Exception {
        setTotalDataSize(PAGE_SIZE * 2);
        presenterOnViewAttached();
        edgeSubject.onNext(PagingEdge.End);
        setInvalidateCache(false);

        edgeSubject.onNext(PagingEdge.Start);

        verify(view, times(2)).show(any());
        verify(view, times(1)).add(anyInt(), any());
    }

    @Test public void onRefresh_loadFirstPage() throws Exception {
        setTotalDataSize(PAGE_SIZE * 2);
        presenterOnViewAttached();
        setInvalidateCache(false);
        edgeSubject.onNext(PagingEdge.End);

        refreshSubject.onNext(new RefreshEvent());

        verify(view, times(1)).show(any());
        verify(view, times(1)).add(eq(PAGE_SIZE), any());
        verify(view, times(1)).update(eq(0), any());
    }

    @Test public void onTagSelected_startEdit_whenDisplayTypeIsView() throws Exception {
        final Tag tag = mock(Tag.class);
        presenterOnViewAttached();

        tagSelectedSubject.onNext(tag);

        verify(view).startEdit(tag);
    }

    @Test public void onTagSelected_startSelected_whenDisplayTypeIsSelect() throws Exception {
        createPresenter(TagsPresenter.DisplayType.Select, null);
        final Tag tag = mock(Tag.class);
        presenterOnViewAttached();

        tagSelectedSubject.onNext(tag);

        verify(view).startSelected(tag);
    }

    @Test public void onTagSelected_setSelected_whenDisplayTypeIsSingleChoice() throws Exception {
        createPresenter(TagsPresenter.DisplayType.SingleChoice, null);
        final Tag tag = mock(Tag.class);
        presenterOnViewAttached();

        tagSelectedSubject.onNext(tag);

        verify(view).setSelected(tag, true);
    }

    @Test public void onTagSelected_doNothing_whenDisplayTypeIsSingleChoiceThatItemIsAlreadySelected() throws Exception {
        createPresenter(TagsPresenter.DisplayType.SingleChoice, null);
        final Tag tag = mock(Tag.class);
        presenterOnViewAttached();

        tagSelectedSubject.onNext(tag);
        verify(view).setSelected(tag, true);

        tagSelectedSubject.onNext(tag);
        verify(view, never()).setSelected(tag, false);
    }

    @Test public void onTagSelected_selectNewSingleItem_whenDisplayTypeIsSingleChoiceAndItemIsAlreadySelected() throws Exception {
        final Tag tag1 = mock(Tag.class);
        final Tag tag2 = mock(Tag.class);
        createPresenter(TagsPresenter.DisplayType.SingleChoice, null);
        presenterOnViewAttached();

        tagSelectedSubject.onNext(tag1);
        verify(view).setSelected(tag1, true);

        tagSelectedSubject.onNext(tag2);
        verify(view).setSelected(tag1, false);
        verify(view).setSelected(tag2, true);
    }

    @Test public void onTagSelected_setSelected_whenDisplayTypeIsMultiChoice() throws Exception {
        createPresenter(TagsPresenter.DisplayType.MultiChoice, null);
        final Tag tag = mock(Tag.class);
        presenterOnViewAttached();

        tagSelectedSubject.onNext(tag);

        verify(view).setSelected(tag, true);
    }

    @Test public void onTagSelected_setSelectedFalse_whenDisplayTypeIsMultiChoiceThatItemIsAlreadySelected() throws Exception {
        createPresenter(TagsPresenter.DisplayType.MultiChoice, null);
        final Tag tag = mock(Tag.class);
        presenterOnViewAttached();

        tagSelectedSubject.onNext(tag);
        verify(view).setSelected(tag, true);

        tagSelectedSubject.onNext(tag);
        verify(view).setSelected(tag, false);
    }

    @Test public void onSaveSelection_startSelectedSingle_whenDisplayTypeIsSingleChoice() throws Exception {
        createPresenter(TagsPresenter.DisplayType.SingleChoice, null);
        final Tag tag = mock(Tag.class);
        presenterOnViewAttached();

        tagSelectedSubject.onNext(tag);
        saveSelectionSubject.onNext(new Object());

        verify(view).startSelected(tag);
    }

    @Test public void onSaveSelection_startSelectedSet_whenDisplayTypeIsMultiChoice() throws Exception {
        final Set<Tag> selectedItems = new HashSet<>();
        selectedItems.add(mock(Tag.class));
        createPresenter(TagsPresenter.DisplayType.MultiChoice, selectedItems);
        presenterOnViewAttached();

        saveSelectionSubject.onNext(new Object());

        verify(view).startSelected(selectedItems);
    }

    private void setTotalDataSize(int size) {
        totalDataSize = size;
    }

    private void setInvalidateCache(boolean invalidateCache) {
        this.invalidateCache = invalidateCache;
    }

    private void resetDataLoadApi() {
        pageResultSubject = BehaviorSubject.create();
    }

    private TagsPresenter createPresenter(TagsPresenter.DisplayType displayType, Set<Tag> selectedItems) {
        presenter = new TagsPresenter(displayType, selectedItems, dataLoadApi, serviceApi, PAGE_SIZE, Schedulers.immediate(), Schedulers.immediate());
        return presenter;
    }
}