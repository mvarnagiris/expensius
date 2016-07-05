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

import com.mvcoding.expensius.feature.toError
import com.mvcoding.expensius.model.AppUser
import com.mvcoding.expensius.model.anAppUser
import com.mvcoding.expensius.rxSchedulers
import com.mvcoding.expensius.service.LoginService
import com.nhaarman.mockito_kotlin.inOrder
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Test
import org.mockito.InOrder
import rx.Observable.error
import rx.Observable.just
import rx.lang.kotlin.PublishSubject

class LoginPresenterTest {
    val loginAnonymouslyRequestsSubject = PublishSubject<Unit>()

    val loginService: LoginService = mock()
    val view: LoginPresenter.View = mock()
    val inOrder: InOrder = inOrder(view)
    val presenter = LoginPresenter(loginService, rxSchedulers())

    @Before
    fun setUp() {
        whenever(view.loginAnonymouslyRequests()).thenReturn(loginAnonymouslyRequestsSubject)
    }

    @Test
    fun canLoginAnonymously() {
        whenever(loginService.loginAnonymously()).thenReturn(just(anAppUser()))
        presenter.attach(view)

        loginAnonymously()

        inOrder.verify(view).showLoggingIn()
        inOrder.verify(view).hideLoggingIn()
        inOrder.verify(view).displayApp()
    }

    @Test
    fun handlesAnonymousLoginErrors() {
        val throwable = Throwable()
        whenever(loginService.loginAnonymously()).thenReturn(error(throwable), just(anAppUser()))
        presenter.attach(view)

        loginAnonymously()
        inOrder.verify(view).showLoggingIn()
        inOrder.verify(view).hideLoggingIn()
        inOrder.verify(view).showError(throwable.toError())

        loginAnonymously()
        inOrder.verify(view).showLoggingIn()
        inOrder.verify(view).hideLoggingIn()
        inOrder.verify(view).displayApp()
    }

    @Test
    fun resumesLoginAfterReattach() {
        val loginSubject = PublishSubject<AppUser>()
        whenever(loginService.loginAnonymously()).thenReturn(loginSubject)
        presenter.attach(view)

        loginAnonymously()
        inOrder.verify(view).showLoggingIn()

        presenter.detach(view)
        presenter.attach(view)
        inOrder.verify(view).showLoggingIn()

        loginSubject.onNext(anAppUser())
        inOrder.verify(view).hideLoggingIn()
        inOrder.verify(view).displayApp()
    }

    private fun loginAnonymously(): Unit = loginAnonymouslyRequestsSubject.onNext(Unit)
}