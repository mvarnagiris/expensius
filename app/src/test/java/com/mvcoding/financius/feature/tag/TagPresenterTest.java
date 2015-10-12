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

package com.mvcoding.financius.feature.tag;

import com.mvcoding.financius.core.endpoints.body.TagBody;
import com.mvcoding.financius.data.DataSaveApi;
import com.mvcoding.financius.data.converter.TagConverter;
import com.mvcoding.financius.data.model.Tag;
import com.mvcoding.financius.feature.BasePresenterTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import rx.Observable;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class) @PrepareForTest(TagConverter.class)
public class TagPresenterTest extends BasePresenterTest<TagPresenter, TagPresenter.View> {
    private PublishSubject<String> titleSubject = PublishSubject.create();
    private PublishSubject<Integer> colorSubject = PublishSubject.create();
    private PublishSubject<Object> saveSubject = PublishSubject.create();

    @Mock private DataSaveApi dataSaveApi;
    @Mock private TagConverter tagConverter;
    @Mock private TagBody tagBody;

    private Tag initialTag;

    @Override protected TagPresenter createPresenter() {
        initialTag = new Tag().withDefaultValues();
        initialTag.setTitle("title");
        initialTag.setColor(1);

        when(dataSaveApi.saveTag(initialTag)).thenReturn(Observable.just(initialTag));
        when(tagConverter.toBody(initialTag)).thenReturn(tagBody);

        return new TagPresenter(initialTag, dataSaveApi, tagConverter, Schedulers.immediate(), Schedulers.immediate());
    }

    @Override protected TagPresenter.View createView() {
        final TagPresenter.View view = mock(TagPresenter.View.class);
        when(view.onTitleChanged()).thenReturn(titleSubject);
        when(view.onColorChanged()).thenReturn(colorSubject);
        when(view.onSave()).thenReturn(saveSubject);
        return view;
    }

    @Test public void onViewAttached_showInitialTag() throws Exception {
        presenterOnViewAttached();

        verify(view).showTag(initialTag);
    }

    @Test public void showUpdatedTag_whenTagFieldsAreUpdated() throws Exception {
        presenterOnViewAttached();

        verify(view).showTag(initialTag);

        titleSubject.onNext("New title");
        verify(view, times(2)).showTag(initialTag);

        colorSubject.onNext(10);
        verify(view, times(3)).showTag(initialTag);
    }

    @Test public void onSave_startResult_whenTagIsSavedSuccessfully() throws Exception {
        presenterOnViewAttached();

        saveSubject.onNext(new Object());

        verify(dataSaveApi).saveTag(initialTag);
        verify(view).startResult(initialTag);
    }

    @Test public void onSave_doNotSave_whenValidationFails() throws Exception {
        final Throwable throwable = mock(RuntimeException.class);
        doThrow(throwable).when(tagBody).validate();
        presenterOnViewAttached();

        saveSubject.onNext(new Object());

        verify(dataSaveApi, never()).saveTag(initialTag);
        verify(view, never()).startResult(initialTag);
    }

    @Test public void onSave_showError_whenSavingFails() throws Exception {
        final Throwable throwable = mock(RuntimeException.class);
        doThrow(throwable).when(dataSaveApi).saveTag(initialTag);
        presenterOnViewAttached();

        saveSubject.onNext(new Object());

        verify(dataSaveApi).saveTag(initialTag);
        verify(view, never()).startResult(initialTag);
        verify(view).showError(throwable);
    }
}