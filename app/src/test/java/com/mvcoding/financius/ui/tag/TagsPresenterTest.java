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

import com.mvcoding.financius.data.DataLoadApi;
import com.mvcoding.financius.data.model.Tag;
import com.mvcoding.financius.data.paging.Page;
import com.mvcoding.financius.data.paging.PageResult;
import com.mvcoding.financius.ui.BasePresenterTest;
import com.mvcoding.financius.util.rx.RefreshEvent;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TagsPresenterTest extends BasePresenterTest<TagsPresenter, TagsPresenter.View> {
    private static final int PAGE_SIZE = 20;

    private static final PublishSubject<TagsPresenter.Edge> edgeSubject = PublishSubject.create();
    private static final PublishSubject<RefreshEvent> refreshSubject = PublishSubject.create();
    private static final BehaviorSubject<PageResult<Tag>> pageResultSubject = BehaviorSubject.create();

    @Mock private DataLoadApi dataLoadApi;
    private int totalDataSize;
    private boolean invalidateCache;

    @Override protected TagsPresenter createPresenter() {
        setTotalDataSize(PAGE_SIZE);
        setInvalidateCache(true);
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
                        pageResultSubject.onNext(new PageResult<>(page, tags, invalidateCache));
                    }
                });
                return pageResultSubject;
            }
        });
        return new TagsPresenter(TagsPresenter.DisplayType.View, dataLoadApi, PAGE_SIZE, Schedulers.immediate(), Schedulers.immediate());
    }

    @Override protected TagsPresenter.View createView() {
        final TagsPresenter.View view = mock(TagsPresenter.View.class);
        when(view.onEdgeReached()).thenReturn(edgeSubject);
        when(view.onRefresh()).thenReturn(refreshSubject);
        return view;
    }

    @Test public void onViewAttached_setDisplayType() throws Exception {
        presenterOnViewAttached();

        verify(view).setDisplayType(TagsPresenter.DisplayType.View);
    }

    @Test public void onViewAttached_doNotShowAndLoadFirstPage_whenDoNotHaveCachedItems() throws Exception {
        presenterOnViewAttached();

        verify(view, never()).show(any());
        final ArgumentCaptor<List<Tag>> tagsArgument = ArgumentCaptor.forClass(List.class);
        verify(view).add(eq(0), tagsArgument.capture());
        final List<Tag> tags = tagsArgument.getValue();
        assertThat(tags).isNotNull();
        assertThat(tags).hasSize(PAGE_SIZE);
    }

    @Test public void onViewAttached_showAndDoNotLoad_whenAlreadyHaveCachedItems() throws Exception {
        presenterOnViewAttached();
        presenterOnViewDetached();

        presenterOnViewAttached();

        final ArgumentCaptor<List<Tag>> tagsArgument = ArgumentCaptor.forClass(List.class);
        verify(view).show(tagsArgument.capture());
        final List<Tag> tags = tagsArgument.getValue();
        assertThat(tags).isNotNull();
        assertThat(tags).hasSize(PAGE_SIZE);
        verify(view, times(1)).add(0, any());
    }

    @Test @Ignore public void onEdgeReached_doNotLoadMoreItems_whenEdgeIsStartAndFirstPageIsLoaded() throws Exception {
        presenterOnViewAttached();

        edgeSubject.onNext(TagsPresenter.Edge.Start);

        verify(view, never()).show(any());
        verify(view, times(1)).add(anyInt(), any());
    }

    @Test @Ignore public void onEdgeReached_loadPreviousPage_whenEdgeIsStartAndFirstPageIsNotLoaded() throws Exception {
        presenterOnViewAttached();

        edgeSubject.onNext(TagsPresenter.Edge.Start);

        verify(view, never()).show(any());
        verify(view, times(1)).add(anyInt(), any());
    }

    @Test @Ignore public void onEdgeReached_doNotLoadMoreItems_whenEdgeIsEndAndLastPageIsLoaded() throws Exception {
        fail("Not implemented.");
    }

    @Test @Ignore public void onEdgeReached_loadNextPage_whenEdgeIsEndAndLastPageIsNotLoaded() throws Exception {
        fail("Not implemented.");
    }

    @Test @Ignore public void onRefresh_loadFirstPage() throws Exception {
        fail("Not implemented.");
    }

    @Test @Ignore public void add_whenPageIsLoaded() throws Exception {
        fail("Not implemented.");
    }

    private void setTotalDataSize(int size) {
        totalDataSize = size;
    }

    public void setInvalidateCache(boolean invalidateCache) {
        this.invalidateCache = invalidateCache;
    }
}