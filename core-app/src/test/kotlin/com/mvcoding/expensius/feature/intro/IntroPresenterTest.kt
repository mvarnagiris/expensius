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

package com.mvcoding.expensius.feature.intro

import com.mvcoding.expensius.Image
import com.mvcoding.expensius.Settings
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.mock
import org.mockito.BDDMockito.verify
import rx.subjects.PublishSubject

class IntroPresenterTest {
    val skipLoginSubject = PublishSubject.create<Unit>()
    val loginSubject = PublishSubject.create<Unit>()
    val activeIntroPageSubject = PublishSubject.create<Int>()
    val settings = mock(Settings::class.java)
    val view = mock(ViewForTest::class.java)
    val introPages = arrayListOf(IntroPage(ImageForTest(), "title1", "message1"), IntroPage(ImageForTest(), "title2", "message2"), IntroPage(ImageForTest(), "title3", "message3"))
    val presenter = IntroPresenter(introPages, settings)

    @Before
    fun setUp() {
        given(view.onSkipLogin()).willReturn(skipLoginSubject)
        given(view.onLogin()).willReturn(loginSubject)
        given(view.onActiveIntroPagePositionChanged()).willReturn(activeIntroPageSubject)
    }

    @Test
    fun initiallyShowsIntroPagesWithActivePosition0() {
        presenter.attach(view)

        verify(view).showIntroPages(introPages, 0)
    }

    @Test
    fun showsIntroPagesWithPreservedPositionAfterReattach() {
        presenter.attach(view)
        setActiveIntroPagePosition(1)
        presenter.detach(view)

        presenter.attach(view)


        verify(view).showIntroPages(introPages, 1)
    }

    @Test
    fun setsIntroductionAsSeenAndStartsMainWhenSkippingLogin() {
        presenter.attach(view)

        skipLogin()

        verify(settings).isIntroductionSeen = true
        verify(view).startMain()
    }

    @Test
    fun setsIntroductionAsSeenAndStartsLoginWhenLoggingIn() {
        presenter.attach(view)

        login()

        verify(settings).isIntroductionSeen = true
        verify(view).startLogin()
    }

    private fun login() = loginSubject.onNext(Unit)

    private fun skipLogin() = skipLoginSubject.onNext(Unit)

    private fun setActiveIntroPagePosition(position: Int) = activeIntroPageSubject.onNext(position)

    class ImageForTest : Image<Any> {
        override val value = Any()
    }

    abstract class ViewForTest : IntroPresenter.View<Any>
}
