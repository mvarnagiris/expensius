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

package com.mvcoding.financius.feature.intro

import com.mvcoding.financius.feature.Session
import com.mvcoding.financius.feature.UserSettings
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.*
import rx.subjects.PublishSubject

class IntroPresenterTest {
    val skipLoginSubject = PublishSubject.create<Any>()
    val loginSubject = PublishSubject.create<Any>()
    val session = mock(Session::class.java)
    val userSettings = mock(UserSettings::class.java)
    val view = mock(IntroPresenter.View::class.java)
    val introPages = arrayListOf(IntroPage(), IntroPage(), IntroPage())
    val presenter = IntroPresenter(introPages, userSettings)

    @Before
    fun setUp() {
        given(view.onSkipLogin()).willReturn(skipLoginSubject)
        given(view.onLogin()).willReturn(loginSubject)
    }

    @Test
    fun showsIntroPages() {
        presenter.onAttachView(view)

        verify(view).showIntroPages(introPages)
    }

    @Test
    fun setsIntroductionAsSeenAndStartsOverviewWhenSkippingLogin() {
        presenter.onAttachView(view)

        skipLogin()

        verify(userSettings).setIsIntroductionSeen(true)
        verify(view).startOverview()
    }

    @Test
    fun setsIntroductionAsSeenAndStartsLoginWhenLoggingIn() {
        presenter.onAttachView(view)

        login()

        verify(userSettings).setIsIntroductionSeen(true)
        verify(view).startLogin()
    }

    private fun login() {
        loginSubject.onNext(Any())
    }

    private fun skipLogin() {
        skipLoginSubject.onNext(Any())
    }
}

