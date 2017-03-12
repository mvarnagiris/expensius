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
import com.mvcoding.expensius.data.ParameterDataSource
import com.mvcoding.expensius.extensions.putInList
import com.mvcoding.expensius.feature.ErrorView
import com.mvcoding.expensius.feature.Resolution.POSITIVE
import com.mvcoding.expensius.feature.ResolvableErrorView
import com.mvcoding.expensius.feature.RxState
import com.mvcoding.expensius.feature.login.LoginPresenter.Destination.APP
import com.mvcoding.expensius.feature.toError
import com.mvcoding.expensius.model.AuthProvider
import com.mvcoding.expensius.model.AuthProvider.ANONYMOUS
import com.mvcoding.expensius.model.AuthProvider.GOOGLE
import com.mvcoding.expensius.model.GoogleToken
import com.mvcoding.expensius.model.Login
import com.mvcoding.expensius.model.Login.AnonymousLogin
import com.mvcoding.expensius.model.Login.GoogleLogin
import com.mvcoding.mvp.Presenter
import rx.Observable
import rx.Subscription

class LoginPresenter(
        private val destination: Destination,
        private val loginSource: ParameterDataSource<Login, Unit>,
        private val schedulers: RxSchedulers) : Presenter<LoginPresenter.View>() {

    private val rxState = RxState<State>(State.WaitingForLoginSelection)

    override fun onViewAttached(view: View) {
        super.onViewAttached(view)

        rxState.registerState(State.WaitingForLoginSelection::class) { stateWaitingForLoginSelection(view) }
        rxState.registerState(State.AskingForGoogleToken::class) { stateAskingForGoogleToken(view) }
        rxState.registerState(State.LoggingIn::class) { stateLoggingIn(view, it.login) }
        rxState.registerState(State.ResolvableFailedLogin::class) { stateResolvableFailedLogin(view, it.login, it.throwable) }
        rxState.subscribe()
    }

    override fun onViewDetached(view: View) {
        super.onViewDetached(view)
        rxState.unsubscribe()
    }

    private fun stateWaitingForLoginSelection(view: View): List<Subscription> {
        if (destination == APP) view.showAllLoginOptions() else view.showAllLoginOptionsExceptSkip()
        return listOf(
                view.googleLogins().subscribeUntilDetached { requestGoogleToken() },
                view.skipLogins().subscribeUntilDetached { loginAnonymously() })
    }

    private fun stateAskingForGoogleToken(view: View): List<Subscription> {
        return view.showGoogleTokenRequest()
                .subscribeUntilDetached {
                    when (it) {
                        is GoogleTokenResult.SuccessfulGoogleTokenResult -> login(it.googleToken, forceLogin = false)
                        is GoogleTokenResult.FailedGoogleTokenResult -> showError(view, it.throwable)
                    }
                }
                .putInList()
    }

    private fun stateLoggingIn(view: View, login: Login): List<Subscription> {
        view.showLoggingIn(login.toAuthProvider())
        return loginSource.data(login)
                .subscribeOn(schedulers.io)
                .observeOn(schedulers.main)
                .subscribeUntilDetached({ view.displayDestination(destination) }, { showError(view, it, login) })
                .putInList()
    }

    private fun stateResolvableFailedLogin(view: View, login: Login, throwable: Throwable): List<Subscription> {
        return view.showResolvableError(throwable.toError())
                .subscribeUntilDetached { if (it == POSITIVE && login is GoogleLogin) login(login.googleToken, forceLogin = true) else waitForLoginSelection() }
                .putInList()
    }

    private fun waitForLoginSelection() = rxState.setState(State.WaitingForLoginSelection)
    private fun requestGoogleToken() = rxState.setState(State.AskingForGoogleToken)
    private fun login(googleToken: GoogleToken, forceLogin: Boolean) = rxState.setState(State.LoggingIn(GoogleLogin(googleToken, forceLogin)))
    private fun loginAnonymously() = rxState.setState(State.LoggingIn(AnonymousLogin))
    private fun failWithResolvableError(login: Login, throwable: Throwable) = rxState.setState(State.ResolvableFailedLogin(login, throwable))

    private fun showError(view: View, throwable: Throwable, login: Login? = null) {
        val error = throwable.toError()
        if (error.isResolvable() && login != null) failWithResolvableError(login, throwable)
        else {
            view.showError(error)
            waitForLoginSelection()
        }
    }

    private fun Login.toAuthProvider() = when (this) {
        is AnonymousLogin -> ANONYMOUS
        is GoogleLogin -> GOOGLE
    }

    interface View : Presenter.View, ErrorView, ResolvableErrorView {
        fun googleLogins(): Observable<Unit>
        fun skipLogins(): Observable<Unit>

        fun showAllLoginOptions()
        fun showAllLoginOptionsExceptSkip()
        fun showGoogleTokenRequest(): Observable<GoogleTokenResult>
        fun showLoggingIn(authProvider: AuthProvider)
        fun displayDestination(destination: Destination)
    }

    sealed class GoogleTokenResult {
        data class SuccessfulGoogleTokenResult(val googleToken: GoogleToken) : GoogleTokenResult()
        data class FailedGoogleTokenResult(val throwable: Throwable) : GoogleTokenResult()
    }

    enum class Destination {
        RETURN, APP, SUPPORT_DEVELOPER
    }

    private sealed class State {
        object WaitingForLoginSelection : State()
        object AskingForGoogleToken : State()
        data class LoggingIn(val login: Login) : State()
        data class ResolvableFailedLogin(val login: Login, val throwable: Throwable) : State()
    }
}