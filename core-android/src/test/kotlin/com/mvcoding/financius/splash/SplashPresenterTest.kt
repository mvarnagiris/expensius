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

package com.mvcoding.financius.splash

import com.mvcoding.financius.Session
import com.mvcoding.financius.UserSettings
import org.junit.Test
import org.mockito.Mockito.*

class SplashPresenterTest {
    val userSettings: UserSettings = mock(UserSettings::class.java)
    val session: Session = mock(Session::class.java)
    val view: SplashPresenter.View = mock(SplashPresenter.View::class.java)
    val presenter = SplashPresenter(userSettings, session)

    @Test
    fun startsOverviewWhenIntroductionWasSeen() {
        `when`(session.isLoggedIn()).thenReturn(false)
        `when`(userSettings.isIntroductionSeen()).thenReturn(true)

        presenter.onAttachView(view)

        verify(view).startOverview()
    }

    @Test
    fun startsOverviewWhenSessionIsLoggedIn() {
        `when`(session.isLoggedIn()).thenReturn(true)
        `when`(userSettings.isIntroductionSeen()).thenReturn(false)

        presenter.onAttachView(view)

        verify(view).startOverview()
    }

    @Test
    fun startsIntroductionWhenSessionIsNotLoggedIn() {
        `when`(session.isLoggedIn()).thenReturn(false)
        `when`(userSettings.isIntroductionSeen()).thenReturn(false)

        presenter.onAttachView(view)

        verify(view).startIntroduction()
    }
}

