/*
 * Copyright (C) 2017 Mantas Varnagiris.
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

import com.mvcoding.expensius.data.ParameterDataSource
import com.mvcoding.expensius.feature.Resolution
import com.mvcoding.expensius.feature.Resolution.NEGATIVE
import com.mvcoding.expensius.feature.Resolution.POSITIVE
import com.mvcoding.expensius.feature.login.LoginPresenter.Destination
import com.mvcoding.expensius.feature.login.LoginPresenter.Destination.*
import com.mvcoding.expensius.feature.login.LoginPresenter.GoogleTokenResult.FailedGoogleTokenResult
import com.mvcoding.expensius.feature.login.LoginPresenter.GoogleTokenResult.SuccessfulGoogleTokenResult
import com.mvcoding.expensius.feature.toError
import com.mvcoding.expensius.model.AuthProvider.ANONYMOUS
import com.mvcoding.expensius.model.AuthProvider.GOOGLE
import com.mvcoding.expensius.model.GoogleToken
import com.mvcoding.expensius.model.Login
import com.mvcoding.expensius.model.UserAlreadyLinkedException
import com.mvcoding.expensius.model.aGoogleToken
import com.mvcoding.expensius.rxSchedulers
import com.nhaarman.mockito_kotlin.*
import org.junit.Before
import org.junit.Test
import rx.lang.kotlin.PublishSubject

class LoginPresenterTest {

    val loginWithGoogleSubject = PublishSubject<Unit>()
    val googleTokenSubject = PublishSubject<LoginPresenter.GoogleTokenResult>()
    var loginSubject = PublishSubject<Unit>()
    val skipLoginSubject = PublishSubject<Unit>()
    val errorResolutionSubject = PublishSubject<Resolution>()

    val googleToken = aGoogleToken()

    val loginSource = mock<ParameterDataSource<Login, Unit>>()
    val view = mock<LoginPresenter.View>()
    val inOrder = inOrder(view)

    @Before
    fun setUp() {
        whenever(view.googleLogins()).thenReturn(loginWithGoogleSubject)
        whenever(view.showGoogleTokenRequest()).thenReturn(googleTokenSubject)
        whenever(loginSource.data(any())).thenReturn(loginSubject)
        whenever(view.skipLogins()).thenReturn(skipLoginSubject)
        whenever(view.showResolvableError(any())).thenReturn(errorResolutionSubject)
    }

    @Test
    fun `shows all login options when destination is APP`() {
        val presenter = presenter(APP)
        presenter.attach(view)

        presenter.detach(view)
        presenter.attach(view)

        verify(view, times(2)).showAllLoginOptions()
    }

    @Test
    fun `shows all login options except skip when destination is RETURN`() {
        val presenter = presenter(RETURN)
        presenter.attach(view)

        presenter.detach(view)
        presenter.attach(view)

        verify(view, times(2)).showAllLoginOptionsExceptSkip()
    }

    @Test
    fun `shows all login options except skip when destination is SUPPORT_DEVELOPER`() {
        val presenter = presenter(SUPPORT_DEVELOPER)
        presenter.attach(view)

        presenter.detach(view)
        presenter.attach(view)

        verify(view, times(2)).showAllLoginOptionsExceptSkip()
    }

    @Test
    fun `can login with google`() {
        val presenter = presenter()
        presenter.attach(view)

        loginWithGoogle()
        inOrder.verify(view).showGoogleTokenRequest()

        presenter.detach(view)
        presenter.attach(view)
        inOrder.verify(view).showGoogleTokenRequest()

        receiveGoogleToken(googleToken)
        inOrder.verify(view).showLoggingIn(GOOGLE)

        presenter.detach(view)
        presenter.attach(view)
        inOrder.verify(view).showLoggingIn(GOOGLE)

        completeLogin()
        inOrder.verify(view).displayDestination(APP)
    }

    @Test
    fun `can skip login that will login anonymously`() {
        val presenter = presenter()
        presenter.attach(view)

        skipLogin()
        inOrder.verify(view).showLoggingIn(ANONYMOUS)

        presenter.detach(view)
        presenter.attach(view)
        inOrder.verify(view).showLoggingIn(ANONYMOUS)

        completeLogin()
        inOrder.verify(view).displayDestination(APP)
    }

    @Test
    fun `handles google token failures`() {
        val throwable = Throwable()
        presenter().attach(view)

        loginWithGoogle()
        failGoogleTokenRequest(throwable)

        inOrder.verify(view).showAllLoginOptions()
        inOrder.verify(view).showError(throwable.toError())
    }

    @Test
    fun `handles login errors when logging in with google`() {
        val throwable = Throwable()
        presenter().attach(view)

        loginWithGoogle()
        receiveGoogleToken(googleToken)
        failLogin(throwable)

        inOrder.verify(view).showAllLoginOptions()
        inOrder.verify(view).showError(throwable.toError())
    }

    @Test
    fun `handles login errors when logging in anonymously`() {
        val throwable = Throwable()
        presenter().attach(view)

        skipLogin()
        failLogin(throwable)

        inOrder.verify(view).showAllLoginOptions()
        inOrder.verify(view).showError(throwable.toError())
    }

    @Test
    fun `shows resolvable error and forces login if resolution is positive when login fails with with UserAlreadyLinkedException`() {
        val throwable = UserAlreadyLinkedException(Throwable())
        presenter().attach(view)

        loginWithGoogle()
        receiveGoogleToken(googleToken)
        failLogin(throwable)
        resolveError(POSITIVE)
        completeLogin()

        inOrder.verify(view).showResolvableError(throwable.toError())
        inOrder.verify(view).showLoggingIn(GOOGLE)
        inOrder.verify(view).displayDestination(APP)
    }

    @Test
    fun `waits for login selection again when login resolution is negative after UserAlreadyLinkedException`() {
        val throwable = UserAlreadyLinkedException(Throwable())
        presenter().attach(view)

        loginWithGoogle()
        receiveGoogleToken(googleToken)
        failLogin(throwable)
        resolveError(NEGATIVE)

        inOrder.verify(view).showAllLoginOptions()
        inOrder.verify(view).showResolvableError(throwable.toError())
    }

    fun presenter(destination: Destination = APP) = LoginPresenter(destination, loginSource, rxSchedulers())
    fun loginWithGoogle() = loginWithGoogleSubject.onNext(Unit)
    fun skipLogin() = skipLoginSubject.onNext(Unit)
    fun receiveGoogleToken(googleToken: GoogleToken) = googleTokenSubject.onNext(SuccessfulGoogleTokenResult(googleToken))
    fun failGoogleTokenRequest(throwable: Throwable) = googleTokenSubject.onNext(FailedGoogleTokenResult(throwable))
    fun completeLogin() = loginSubject.onNext(Unit)
    fun resolveError(resolution: Resolution) = errorResolutionSubject.onNext(resolution)
    fun failLogin(throwable: Throwable) {
        loginSubject.onError(throwable)
        loginSubject = PublishSubject()
        whenever(loginSource.data(any())).thenReturn(loginSubject)
    }
}