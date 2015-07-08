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

package com.mvcoding.financius.ui;

import android.support.annotation.NonNull;

import com.mvcoding.financius.BaseTest;
import com.mvcoding.financius.util.rx.Event;

import rx.subjects.Subject;

import static org.mockito.Mockito.mock;

public abstract class BasePresenterTest<P extends Presenter<V>, V extends PresenterView> extends BaseTest {
    protected P presenter;
    protected V view;

    @Override public void setUp() {
        super.setUp();
        presenter = createPresenter();
        view = createView();
    }

    protected abstract P createPresenter();

    protected abstract V createView();

    protected void presenterOnViewAttached() {
        presenter.onViewAttached(view);
    }

    protected void presenterOnViewDetached() {
        presenter.onViewDetached();
    }

    protected void performEvent(@NonNull Subject<Event, Event> subject) {
        final Event event = mock(Event.class);
        subject.onNext(event);
    }
}
