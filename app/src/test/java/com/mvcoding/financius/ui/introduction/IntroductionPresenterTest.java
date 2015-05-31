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

package com.mvcoding.financius.ui.introduction;

import com.mvcoding.financius.api.Session;
import com.mvcoding.financius.ui.BasePresenterTest;
import com.mvcoding.financius.ui.UserSettings;

import org.junit.Test;
import org.mockito.Mock;

import rx.android.view.OnClickEvent;
import rx.subjects.PublishSubject;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class IntroductionPresenterTest extends BasePresenterTest<IntroductionPresenter, IntroductionPresenter.View> {
    private final PublishSubject<OnClickEvent> skipLoginClick = PublishSubject.create();
    private final PublishSubject<OnClickEvent> loginClick = PublishSubject.create();

    @Mock private Session session;
    @Mock private UserSettings userSettings;

    @Override protected IntroductionPresenter createPresenter() {
        return new IntroductionPresenter(userSettings);
    }

    @Override protected IntroductionPresenter.View createView() {
        final IntroductionPresenter.View view = mock(IntroductionPresenter.View.class);
        when(view.onSkipLoginClick()).thenReturn(skipLoginClick);
        when(view.onLoginClick()).thenReturn(loginClick);
        return view;
    }

    @Test public void onSkipLoginClick_setsIntroductionAsSeenAndStartsOverviewAndCloses() {
        presenterJumpToOnViewAttached();

        performClick(skipLoginClick);

        verify(userSettings).setIsIntroductionSeen(true);
        verify(view).startOverviewAndClose();
    }

    @Test public void onLoginClick_setsIntroductionAsSeenAndStartsLoginAndCloses() {
        presenterJumpToOnViewAttached();

        performClick(loginClick);

        verify(userSettings).setIsIntroductionSeen(true);
        verify(view).startLoginAndClose();
    }
}
