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

import com.mvcoding.expensius.RxSchedulers
import com.mvcoding.expensius.feature.ErrorView
import com.mvcoding.expensius.feature.Resolution
import com.mvcoding.expensius.feature.ResolvableErrorView
import com.mvcoding.expensius.feature.login.LoginPresenter2.Destination.APP
import com.mvcoding.expensius.feature.login.LoginPresenter2.Login.ForcePreviousLoginAndLoseLocalDataIfUserAlreadyExists
import com.mvcoding.expensius.feature.login.LoginPresenter2.LoginState.FailedLogin
import com.mvcoding.expensius.feature.login.LoginPresenter2.LoginState.Idle
import com.mvcoding.expensius.feature.login.LoginPresenter2.LoginState.LoggingInAnonymously
import com.mvcoding.expensius.feature.login.LoginPresenter2.LoginState.LoggingInWithGoogle
import com.mvcoding.expensius.feature.login.LoginPresenter2.LoginState.SuccessfulLogin
import com.mvcoding.expensius.feature.login.LoginPresenter2.LoginState.WaitingGoogleToken
import com.mvcoding.expensius.feature.toError
import com.mvcoding.expensius.model.GoogleToken
import com.mvcoding.expensius.model.UserAlreadyLinkedException
import com.mvcoding.mvp.Presenter
import rx.Observable
import rx.Observable.merge

class LoginPresenter2(
        private val destination: Destination,
        private val loginStateSource: DataSource<LoginState>,
        private val loginWriter: DataWriter<Login>,
        private val schedulers: RxSchedulers) : Presenter<LoginPresenter2.View>() {

    override fun onViewAttached(view: View) {
        super.onViewAttached(view)

        loginStateSource.data()
                .subscribeOn(schedulers.io)
                .observeOn(schedulers.main)
                .subscribeUntilDetached { loginState ->
                    when (loginState) {
                        is Idle -> showIdle(view)
                        is WaitingGoogleToken -> requestGoogleTokenAndLogin(view)
                        is LoggingInAnonymously -> view.showLoggingInAnonymously()
                        is LoggingInWithGoogle -> view.showLoggingInWithGoogle()
                        is SuccessfulLogin -> view.displayDestination(destination)
                        is FailedLogin -> showError(view, loginState)
                    }
                }

        merge(
                view.loginWithGoogleRequests().map { Login.GetGoogleToken },
                view.skipLoginRequests().map { Login.AnonymousLogin })
                .observeOn(schedulers.io)
                .subscribeUntilDetached { loginWriter.write(it) }
    }

    private fun requestGoogleTokenAndLogin(view: View) {
        view.showGoogleTokenRequest()
                .observeOn(schedulers.io)
                .first()
                .subscribe { loginWriter.write(Login.GoogleLogin(it)) }
    }

    private fun showIdle(view: View) {
        if (destination == APP) view.showAllLoginOptions() else view.showAllLoginOptionsExceptSkip()
    }

    private fun showError(view: View, loginState: FailedLogin) {
        if (loginState.throwable is UserAlreadyLinkedException) {
            view.showResolvableError(loginState.throwable.toError())
                    .first()
                    .filter { it == Resolution.POSITIVE }
                    .observeOn(schedulers.io)
                    .subscribe { loginWriter.write(ForcePreviousLoginAndLoseLocalDataIfUserAlreadyExists) }
        } else {
            view.showError(loginState.throwable.toError())
        }
    }

    interface View : Presenter.View, ErrorView, ResolvableErrorView {
        fun loginWithGoogleRequests(): Observable<Unit>
        fun skipLoginRequests(): Observable<Unit>

        fun showGoogleTokenRequest(): Observable<GoogleToken>
        fun showAllLoginOptions()
        fun showAllLoginOptionsExceptSkip()
        fun showLoggingInWithGoogle()
        fun showLoggingInAnonymously()
        fun displayDestination(destination: Destination)
    }

    sealed class LoginState {
        object Idle : LoginState()
        object WaitingGoogleToken : LoginState()
        object LoggingInWithGoogle : LoginState()
        object LoggingInAnonymously : LoginState()
        object SuccessfulLogin : LoginState()
        data class FailedLogin(val throwable: Throwable) : LoginState()
    }

    sealed class Login {
        object AnonymousLogin : Login()
        object ForcePreviousLoginAndLoseLocalDataIfUserAlreadyExists : Login()
        object GetGoogleToken : Login()
        data class GoogleLogin(val googleToken: GoogleToken) : Login()
    }

    enum class Destination {
        RETURN, APP, SUPPORT_DEVELOPER
    }
}