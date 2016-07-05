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

package com.mvcoding.expensius.feature.splash

import com.mvcoding.expensius.model.AppUser.Companion.noAppUser
import com.mvcoding.expensius.model.anAppUser
import com.mvcoding.expensius.rxSchedulers
import com.mvcoding.expensius.service.AppUserService
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Test
import rx.Observable.just

class SplashPresenterTest {
    val appUserService: AppUserService = mock()
    val view: SplashPresenter.View = mock()
    val presenter = SplashPresenter(appUserService, rxSchedulers())

    @Test
    fun displaysLoginIfUserIsNotLoggedIn() {
        whenever(appUserService.appUsers()).thenReturn(just(noAppUser))

        presenter.attach(view)

        verify(view).displayLogin()
    }

    @Test
    fun displaysAppIfUserIsLoggedIn() {
        whenever(appUserService.appUsers()).thenReturn(just(anAppUser()))

        presenter.attach(view)

        verify(view).displayApp()
    }
}