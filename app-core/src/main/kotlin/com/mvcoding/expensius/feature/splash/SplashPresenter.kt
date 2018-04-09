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

import com.mvcoding.expensius.feature.Error
import com.mvcoding.expensius.feature.login.LoginPresenter.Destination
import com.mvcoding.expensius.feature.login.LoginPresenter.Destination.APP
import com.mvcoding.expensius.feature.toError
import com.mvcoding.expensius.model.AppUser
import com.mvcoding.mvp.Presenter
import com.mvcoding.mvp.RxSchedulers
import com.mvcoding.mvp.behaviors.InitializationBehavior
import io.reactivex.Single

class SplashPresenter internal constructor(
        getAppUser: () -> Single<AppUser>,
        schedulers: RxSchedulers,
        isSuccess: (AppUser) -> Boolean,
        getSuccess: (AppUser) -> Unit,
        getFailure: (AppUser) -> Destination,
        mapError: (Throwable) -> Error) : Presenter<SplashPresenter.View>(
        InitializationBehavior(getAppUser, isSuccess, getSuccess, getFailure, mapError, schedulers)) {

    constructor(getAppUser: () -> Single<AppUser>, schedulers: RxSchedulers) : this(getAppUser, schedulers, { it.isLoggedIn() }, { Unit }, { APP }, { it.toError() })

    interface View : Presenter.View, InitializationBehavior.View<Unit, Destination, Error>
}