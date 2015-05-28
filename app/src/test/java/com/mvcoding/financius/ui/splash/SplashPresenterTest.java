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

package com.mvcoding.financius.ui.splash;

import com.mvcoding.financius.api.Session;
import com.mvcoding.financius.ui.BasePresenterTest;

import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SplashPresenterTest extends BasePresenterTest<SplashPresenter, SplashPresenter.View> {
    @Mock private Session session;

    @Override protected SplashPresenter createPresenter() {
        return new SplashPresenter(session);
    }

    @Override protected SplashPresenter.View createView() {
        return mock(SplashPresenter.View.class);
    }

    @Test public void onViewAttached_startsTutorial_whenSessionIsNotLoggedIn() {
        when(session.isLoggedIn()).thenReturn(false);

        presenterJumpToOnViewAttached();

        verify(view).startTutorialAndClose();
    }

    @Test public void onViewAttached_startsOverview_whenSessionIsLoggedIn() {
        when(session.isLoggedIn()).thenReturn(true);

        presenterJumpToOnViewAttached();

        verify(view).startOverviewAndClose();
    }
}
