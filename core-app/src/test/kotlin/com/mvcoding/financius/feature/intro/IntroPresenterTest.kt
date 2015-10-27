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

import com.mvcoding.financius.Image
import com.mvcoding.financius.Settings
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.*
import rx.subjects.PublishSubject

class IntroPresenterTest {
    val skipLoginSubject = PublishSubject.create<Unit>()
    val loginSubject = PublishSubject.create<Unit>()
    val userSettings = mock(Settings::class.java)
    val view = mock(ViewForTest::class.java)
    val introPages = arrayListOf(IntroPage(ImageForTest(), "title1", "message1"), IntroPage(ImageForTest(), "title2", "message2"), IntroPage(ImageForTest(), "title3", "message3"))
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
        loginSubject.onNext(Unit)
    }

    private fun skipLogin() {
        skipLoginSubject.onNext(Unit)
    }

    class ImageForTest : Image<Any> {
        override val value = Any()
    }

    abstract class ViewForTest : IntroPresenter.View<Any>
}
