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

import com.mvcoding.expensius.model.AuthProvider.ANONYMOUS
import com.mvcoding.expensius.model.AuthProvider.GOOGLE
import com.mvcoding.expensius.model.anAppUser
import com.mvcoding.expensius.model.withAuthProvider
import com.mvcoding.expensius.rxSchedulers
import com.mvcoding.expensius.service.AppUserService
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Test
import rx.lang.kotlin.BehaviorSubject
import rx.lang.kotlin.PublishSubject

class LoginWithAccountPresenterTest {
    val loginFailuresSubject = PublishSubject<Unit>()
    val appUserSubject = BehaviorSubject(anAppUser().withAuthProvider(ANONYMOUS))

    val appUserService: AppUserService = mock()
    val view: LoginWithAccountPresenter.View = mock()
    val presenter = LoginWithAccountPresenter(appUserService, rxSchedulers())

    @Before
    fun setUp() {
        whenever(appUserService.appUser()).thenReturn(appUserSubject)
        whenever(view.loginFailures()).thenReturn(loginFailuresSubject)
    }

    @Test
    fun closesWhenLoginFails() {
        presenter.attach(view)

        loginFailure()

        verify(view).close()
    }

    @Test
    fun displaysSupportDeveloperWhenLoginSucceeds() {
        presenter.attach(view)

        loginWithAccount()

        verify(view).displaySupportDeveloper()
    }

    private fun loginWithAccount() = appUserSubject.onNext(anAppUser().withAuthProvider(GOOGLE))
    private fun loginFailure() = loginFailuresSubject.onNext(Unit)
}