/*
 * Copyright (C) 2016 Mantas Varnagiris.
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

package com.mvcoding.expensius.feature.login

import com.mvcoding.expensius.feature.login.LoginPresenter.Destination
import com.mvcoding.expensius.feature.login.LoginPresenter.Destination.APP
import com.mvcoding.expensius.feature.login.LoginPresenter.Destination.SUPPORT_DEVELOPER
import com.mvcoding.expensius.feature.toError
import com.mvcoding.expensius.model.GoogleToken
import com.mvcoding.expensius.model.aCreateTag
import com.mvcoding.expensius.model.aGoogleToken
import com.mvcoding.expensius.model.aTag
import com.mvcoding.expensius.rxSchedulers
import com.mvcoding.expensius.service.LoginService
import com.mvcoding.expensius.service.TagsService
import com.mvcoding.expensius.service.TagsWriteService
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.inOrder
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Test
import org.mockito.InOrder
import rx.Observable.error
import rx.Observable.just
import rx.lang.kotlin.PublishSubject

class LoginPresenterTest {
    val someCreateTags = setOf(aCreateTag(), aCreateTag())

    val loginWithGoogleRequestsSubject = PublishSubject<Unit>()
    val skipLoginRequestsSubject = PublishSubject<Unit>()
    val googleTokenSubject = PublishSubject<GoogleToken>()

    val loginService: LoginService = mock()
    val tagsService: TagsService = mock()
    val tagsWriteService: TagsWriteService = mock()
    val defaultTags: DefaultTags = mock()

    val view: LoginPresenter.View = mock()
    val inOrder: InOrder = inOrder(view, tagsWriteService, loginService)
    val presenter = presenter()

    @Before
    fun setUp() {
        whenever(view.loginWithGoogleRequests()).thenReturn(loginWithGoogleRequestsSubject)
        whenever(view.skipLoginRequests()).thenReturn(skipLoginRequestsSubject)
        whenever(view.showLoginWithGoogle()).thenReturn(googleTokenSubject)

        whenever(loginService.loginWithGoogle(any())).thenReturn(just(Unit))
        whenever(loginService.loginAnonymously()).thenReturn(just(Unit))
        whenever(tagsService.items()).thenReturn(just(emptyList()))
        whenever(tagsWriteService.createTags(any())).thenReturn(just(Unit))
        whenever(defaultTags.getDefaultTags()).thenReturn(someCreateTags)
    }

    @Test
    fun canLoginWithGoogle() {
        val googleToken = aGoogleToken()
        presenter.attach(view)

        requestLoginWithGoogle()
        successfulLoginWithGoogle(googleToken)

        inOrder.verify(view).showLoading()
        inOrder.verify(view).showLoginWithGoogle()
        inOrder.verify(loginService).loginWithGoogle(googleToken)
        inOrder.verify(view).hideLoading()
        inOrder.verify(view).displayDestination(APP)
    }

    @Test
    fun canLoginAnonymously() {
        presenter.attach(view)

        requestSkipLogin()

        inOrder.verify(view).showLoading()
        inOrder.verify(loginService).loginAnonymously()
        inOrder.verify(view).hideLoading()
        inOrder.verify(view).displayDestination(APP)
    }

    @Test
    fun handlesAndRecoversFromLoginWithGoogleErrors() {
        val viewThrowable = Throwable()
        val loginThrowable = Throwable()
        val googleToken = aGoogleToken()
        val aSuccessfulLoginResult = just(Unit)
        whenever(view.showLoginWithGoogle()).thenReturn(error(viewThrowable), just(googleToken))
        whenever(loginService.loginWithGoogle(googleToken)).thenReturn(error(loginThrowable), aSuccessfulLoginResult)
        presenter.attach(view)

        requestLoginWithGoogle()
        inOrder.verify(view).showLoading()
        inOrder.verify(view).showLoginWithGoogle()
        inOrder.verify(view).hideLoading()
        inOrder.verify(view).showError(viewThrowable.toError())

        requestLoginWithGoogle()
        inOrder.verify(view).showLoading()
        inOrder.verify(view).showLoginWithGoogle()
        inOrder.verify(loginService).loginWithGoogle(googleToken)
        inOrder.verify(view).hideLoading()
        inOrder.verify(view).showError(loginThrowable.toError())

        requestLoginWithGoogle()
        inOrder.verify(view).showLoading()
        inOrder.verify(view).showLoginWithGoogle()
        inOrder.verify(loginService).loginWithGoogle(googleToken)
        inOrder.verify(view).hideLoading()
        inOrder.verify(view).displayDestination(APP)
    }

    @Test
    fun handlesAndRecoversFromLoginAnonymouslyErrors() {
        val loginThrowable = Throwable()
        val aSuccessfulLoginResult = just(Unit)
        whenever(loginService.loginAnonymously()).thenReturn(error(loginThrowable), aSuccessfulLoginResult)
        presenter.attach(view)

        requestSkipLogin()
        inOrder.verify(view).showLoading()
        inOrder.verify(loginService).loginAnonymously()
        inOrder.verify(view).hideLoading()
        inOrder.verify(view).showError(loginThrowable.toError())

        requestSkipLogin()
        inOrder.verify(view).showLoading()
        inOrder.verify(loginService).loginAnonymously()
        inOrder.verify(view).hideLoading()
        inOrder.verify(view).displayDestination(APP)
    }

    @Test
    fun continuesGoogleLoginAfterReattach() {
        val googleTokenSubject = PublishSubject<GoogleToken>()
        val googleLoginSubject = PublishSubject<Unit>()
        val googleToken = aGoogleToken()
        whenever(view.showLoginWithGoogle()).thenReturn(googleTokenSubject)
        whenever(loginService.loginWithGoogle(googleToken)).thenReturn(googleLoginSubject)
        presenter.attach(view)

        requestLoginWithGoogle()
        inOrder.verify(view).showLoading()
        inOrder.verify(view).showLoginWithGoogle()

        presenter.detach(view)
        presenter.attach(view)
        inOrder.verify(view).showLoading()
        inOrder.verify(view).showLoginWithGoogle()

        googleTokenSubject.onNext(googleToken)
        inOrder.verify(loginService).loginWithGoogle(googleToken)

        presenter.detach(view)
        presenter.attach(view)
        inOrder.verify(view).showLoading()

        googleLoginSubject.onNext(Unit)
        inOrder.verify(view).hideLoading()
        inOrder.verify(view).displayDestination(APP)

        verify(view, times(2)).showLoginWithGoogle()
        verify(loginService, times(1)).loginWithGoogle(any())
    }

    @Test
    fun continuesAnonymousLoginAfterReattach() {
        val anonymousLoginSubject = PublishSubject<Unit>()
        whenever(loginService.loginAnonymously()).thenReturn(anonymousLoginSubject)
        presenter.attach(view)

        requestSkipLogin()
        inOrder.verify(view).showLoading()

        presenter.detach(view)
        presenter.attach(view)
        inOrder.verify(view).showLoading()

        anonymousLoginSubject.onNext(Unit)
        inOrder.verify(view).hideLoading()
        inOrder.verify(view).displayDestination(APP)

        verify(loginService, times(1)).loginAnonymously()
    }

    @Test
    fun createsTagsForUserThatDoesNotHaveAnyTagsWhenLoggingInWithGoogle() {
        whenever(tagsService.items()).thenReturn(just(emptyList()))
        presenter.attach(view)

        requestLoginWithGoogle()
        successfulLoginWithGoogle()

        verify(tagsWriteService).createTags(someCreateTags)
        verify(tagsService).close()
    }

    @Test
    fun createsTagsForUserThatDoesNotHaveAnyTagsWhenLoggingInAnonymously() {
        whenever(tagsService.items()).thenReturn(just(emptyList()))
        presenter.attach(view)

        requestSkipLogin()

        verify(tagsWriteService).createTags(someCreateTags)
        verify(tagsService).close()
    }

    @Test
    fun doesNotCreatesTagsForUserThatDoesHaveTagsWhenLoggingInWithGoogle() {
        whenever(tagsService.items()).thenReturn(just(listOf(aTag())))
        presenter.attach(view)

        requestLoginWithGoogle()
        successfulLoginWithGoogle()

        verify(tagsWriteService, never()).createTags(any())
    }

    @Test
    fun doesNotCreatesTagsForUserThatDoesHaveTagsWhenLoggingInAnonymously() {
        whenever(tagsService.items()).thenReturn(just(listOf(aTag())))
        presenter.attach(view)

        requestSkipLogin()

        verify(tagsWriteService, never()).createTags(any())
    }

    @Test
    fun ignoresTagCreationErrors() {
        whenever(tagsWriteService.createTags(any())).thenReturn(error(Throwable()))
        presenter.attach(view)

        requestSkipLogin()

        verify(view).displayDestination(APP)
    }

    @Test
    fun showsSkipEnabledWhenDestinationIsApp() {
        presenter.attach(view)

        verify(view).showSkipEnabled()
    }

    @Test
    fun showsSkipDisabledWhenDestinationIsNotApp() {
        presenter(SUPPORT_DEVELOPER).attach(view)

        verify(view).showSkipDisabled()
    }

    private fun requestLoginWithGoogle() = loginWithGoogleRequestsSubject.onNext(Unit)
    private fun requestSkipLogin() = skipLoginRequestsSubject.onNext(Unit)
    private fun successfulLoginWithGoogle(googleToken: GoogleToken = aGoogleToken()) = googleTokenSubject.onNext(googleToken)
    private fun presenter(destination: Destination = APP) = LoginPresenter(
            destination,
            loginService,
            tagsService,
            tagsWriteService,
            defaultTags,
            rxSchedulers())
}
