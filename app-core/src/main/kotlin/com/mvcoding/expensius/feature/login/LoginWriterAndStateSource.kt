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

import com.mvcoding.expensius.feature.login.LoginPresenter2.Login
import com.mvcoding.expensius.feature.login.LoginPresenter2.Login.AnonymousLogin
import com.mvcoding.expensius.feature.login.LoginPresenter2.Login.ForcePreviousLoginAndLoseLocalDataIfUserAlreadyExists
import com.mvcoding.expensius.feature.login.LoginPresenter2.Login.GetGoogleToken
import com.mvcoding.expensius.feature.login.LoginPresenter2.Login.GoogleLogin
import com.mvcoding.expensius.feature.login.LoginPresenter2.LoginState
import com.mvcoding.expensius.feature.login.LoginPresenter2.LoginState.FailedLogin
import com.mvcoding.expensius.feature.login.LoginPresenter2.LoginState.Idle
import com.mvcoding.expensius.feature.login.LoginPresenter2.LoginState.LoggingInAnonymously
import com.mvcoding.expensius.feature.login.LoginPresenter2.LoginState.SuccessfulLogin
import com.mvcoding.expensius.feature.login.LoginPresenter2.LoginState.WaitingGoogleToken
import com.mvcoding.expensius.model.GoogleToken
import rx.Observable
import rx.lang.kotlin.BehaviorSubject
import rx.lang.kotlin.filterNotNull
import rx.subjects.SerializedSubject

class LoginWriterAndStateSource(
        private val loginAnonymously: () -> Observable<Unit>,
        private val loginWithGoogle: (GoogleToken, Boolean) -> Observable<Unit>) : DataWriter<Login>, DataSource<LoginState> {

    private val loginStateSubject = SerializedSubject(BehaviorSubject<LoginState>(Idle))
    private var googleToken: GoogleToken? = null

    override fun write(data: Login) {
        when (data) {
            is AnonymousLogin -> anonymousLogin()
            is ForcePreviousLoginAndLoseLocalDataIfUserAlreadyExists -> forcePreviousLogin()
            is GetGoogleToken -> loginStateSubject.onNext(WaitingGoogleToken)
            is GoogleLogin -> googleLogin(data.googleToken, false)
        }
    }

    private fun anonymousLogin() {
        loginStateSubject.onNext(LoggingInAnonymously)
        loginAnonymously()
                .map { SuccessfulLogin }
                .cast(LoginState::class.java)
                .onErrorReturn(::FailedLogin)
                .first()
                .subscribe { loginStateSubject.onNext(it) }
    }

    private fun googleLogin(googleToken: GoogleToken, forceLoginAndLoseLocalDataIfUserAlreadyExists: Boolean) {
        this.googleToken = googleToken
        loginStateSubject.onNext(LoginState.LoggingInWithGoogle)
        loginWithGoogle(googleToken, forceLoginAndLoseLocalDataIfUserAlreadyExists)
                .map { SuccessfulLogin }
                .doOnNext { this.googleToken = null }
                .cast(LoginState::class.java)
                .onErrorReturn(::FailedLogin)
                .first()
                .subscribe { loginStateSubject.onNext(it) }
    }

    private fun forcePreviousLogin() {
        data().first()
                .filter { it is FailedLogin }
                .map { googleToken }
                .filterNotNull()
                .subscribe { googleLogin(it, true) }
    }

    override fun data(): Observable<LoginState> = loginStateSubject
}