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

import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.when;

public class SplashPresenterTest extends BaseTest {
    @Mock private SplashPresenter.View view;
    @Mock private Session session;

    private SplashPresenter presenter;

    @Override public void setUp() {
        super.setUp();

        presenter = new SplashPresenter(session);
    }

    @Test public void onViewAttached_startsTutorial_whenSessionIsNotLoggedIn() {
        when(session.isLoggedIn()).thenReturn(false);
    }

    @Test public void onViewAttached_startsOverview_whenSessionIsLoggedIn() {
    }
}
