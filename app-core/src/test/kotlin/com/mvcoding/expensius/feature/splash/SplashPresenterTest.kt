/*
 * Copyright (C) 2018 Mantas Varnagiris.
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

import com.mvcoding.expensius.feature.login.LoginPresenter.Destination.APP
import com.mvcoding.expensius.feature.toError
import com.mvcoding.expensius.model.AppUser
import com.mvcoding.mvp.behaviors.testInitializationBehavior
import com.mvcoding.mvp.trampolines
import org.junit.Test
import ro.kreator.aRandom

class SplashPresenterTest {

    private val appUser by aRandom<AppUser>()

    @Test
    fun `behaves like initialization behavior`() {
        testInitializationBehavior(appUser, Unit, APP, Throwable().toError()) { initialize, isSuccess, getSuccess, getFailure, mapError ->
            SplashPresenter(initialize, trampolines, isSuccess, getSuccess, getFailure, mapError)
        }
    }
}