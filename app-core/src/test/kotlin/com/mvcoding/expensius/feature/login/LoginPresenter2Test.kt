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

import com.mvcoding.expensius.feature.Resolution
import com.mvcoding.expensius.feature.login.LoginPresenter2.Destination
import com.mvcoding.expensius.feature.login.LoginPresenter2.Destination.APP
import com.mvcoding.expensius.feature.login.LoginPresenter2.Destination.RETURN
import com.mvcoding.expensius.feature.login.LoginPresenter2.Destination.SUPPORT_DEVELOPER
import com.mvcoding.expensius.feature.login.LoginPresenter2.Login
import com.mvcoding.expensius.feature.login.LoginPresenter2.Login.AnonymousLogin
import com.mvcoding.expensius.feature.login.LoginPresenter2.Login.GoogleLogin
import com.mvcoding.expensius.feature.login.LoginPresenter2.LoginState
import com.mvcoding.expensius.feature.login.LoginPresenter2.LoginState.FailedLogin
import com.mvcoding.expensius.feature.login.LoginPresenter2.LoginState.Idle
import com.mvcoding.expensius.feature.login.LoginPresenter2.LoginState.LoggingInAnonymously
import com.mvcoding.expensius.feature.login.LoginPresenter2.LoginState.LoggingInWithGoogle
import com.mvcoding.expensius.feature.login.LoginPresenter2.LoginState.SuccessfulLogin
import com.mvcoding.expensius.feature.login.LoginPresenter2.LoginState.WaitingGoogleToken
import com.mvcoding.expensius.feature.toError
import com.mvcoding.expensius.model.GoogleToken
import com.mvcoding.expensius.model.UserAlreadyLinkedException
import com.mvcoding.expensius.model.aGoogleToken
import com.mvcoding.expensius.rxSchedulers
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Test
import rx.lang.kotlin.BehaviorSubject
import rx.lang.kotlin.PublishSubject

class LoginPresenter2Test {

    val loginStateSubject = BehaviorSubject<LoginState>()
    val loginWithGoogleRequestsSubject = PublishSubject<Unit>()
    val googleTokenSubject = PublishSubject<GoogleToken>()
    val skipLoginRequestsSubject = PublishSubject<Unit>()
    val errorResolutionSubject = PublishSubject<Resolution>()

    val loginStateSource = mock<DataSource<LoginState>>()
    val loginWriter = mock<DataWriter<Login>>()
    val view = mock<LoginPresenter2.View>()

    @Before
    fun setUp() {
        whenever(loginStateSource.data()).thenReturn(loginStateSubject)
        whenever(view.loginWithGoogleRequests()).thenReturn(loginWithGoogleRequestsSubject)
        whenever(view.skipLoginRequests()).thenReturn(skipLoginRequestsSubject)
        whenever(view.showGoogleTokenRequest()).thenReturn(googleTokenSubject)
        whenever(view.showResolvableError(any())).thenReturn(errorResolutionSubject)
    }

    @Test
    fun `shows all login options when destination is APP and login state is Idle`() {
        presenter(APP).attach(view)

        receiveLoginState(Idle)

        verify(view).showAllLoginOptions()
    }

    @Test
    fun `shows all login options except skip when destination is RETURN and login state is Idle`() {
        presenter(RETURN).attach(view)

        receiveLoginState(Idle)

        verify(view).showAllLoginOptionsExceptSkip()
    }

    @Test
    fun `shows all login options except skip when destination is SUPPORT_DEVELOPER and login state is Idle`() {
        presenter(SUPPORT_DEVELOPER).attach(view)

        receiveLoginState(Idle)

        verify(view).showAllLoginOptionsExceptSkip()
    }

    @Test
    fun `shows waiting google token request when login state is WaitingGoogleToken`() {
        presenter().attach(view)

        receiveLoginState(WaitingGoogleToken)

        verify(view).showGoogleTokenRequest()
    }

    @Test
    fun `shows logging in anonymously when login state is LoggingInAnonymously`() {
        presenter().attach(view)

        receiveLoginState(LoggingInAnonymously)

        verify(view).showLoggingInAnonymously()
    }

    @Test
    fun `shows logging in with google when login state is LoggingInWithGoogle`() {
        presenter().attach(view)

        receiveLoginState(LoggingInWithGoogle)

        verify(view).showLoggingInWithGoogle()
    }

    @Test
    fun `displays destination when login state is SuccessfulLogin`() {
        presenter(RETURN).attach(view)

        receiveLoginState(SuccessfulLogin)

        verify(view).displayDestination(RETURN)
    }

    @Test
    fun `shows error when login state is FailedLogin`() {
        val throwable = Throwable()
        presenter().attach(view)

        receiveLoginState(FailedLogin(throwable))

        verify(view).showError(throwable.toError())
    }

    @Test
    fun `shows resolvable error and forces login if resolution is positive when login state is FailedLogin with UserAlreadyLinkedException`() {
        val throwable = UserAlreadyLinkedException(Throwable())
        presenter().attach(view)

        receiveLoginState(FailedLogin(throwable))
        resolveError(Resolution.POSITIVE)

        verify(view).showResolvableError(throwable.toError())
        verify(loginWriter).write(Login.ForcePreviousLoginAndLoseLocalDataIfUserAlreadyExists)
    }

    @Test
    fun `logs in anonymously when user skips login`() {
        presenter().attach(view)

        requestSkipLogin()

        verify(loginWriter).write(AnonymousLogin)
    }

    @Test
    fun `requests google token when user requests google login`() {
        presenter().attach(view)

        requestLoginWithGoogle()

        verify(loginWriter).write(Login.GetGoogleToken)
    }

    @Test
    fun `logs in with google when google token is received`() {
        val googleToken = aGoogleToken()
        presenter().attach(view)

        receiveLoginState(WaitingGoogleToken)
        receiveGoogleToken(googleToken)

        verify(loginWriter).write(GoogleLogin(googleToken))
    }

    fun presenter(destination: Destination = APP) = LoginPresenter2(destination, loginStateSource, loginWriter, rxSchedulers())
    fun receiveLoginState(loginState: LoginState) = loginStateSubject.onNext(loginState)
    fun receiveGoogleToken(googleToken: GoogleToken) = googleTokenSubject.onNext(googleToken)
    fun requestLoginWithGoogle() = loginWithGoogleRequestsSubject.onNext(Unit)
    fun requestSkipLogin() = skipLoginRequestsSubject.onNext(Unit)
    fun resolveError(resolution: Resolution) = errorResolutionSubject.onNext(resolution)
}