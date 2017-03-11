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

import com.mvcoding.expensius.data.DataSource
import com.mvcoding.expensius.data.DataWriter
import com.mvcoding.expensius.feature.login.LoginPresenter.Login
import com.mvcoding.expensius.feature.login.LoginPresenter.Login.*
import com.mvcoding.expensius.feature.login.LoginPresenter.LoginState
import com.mvcoding.expensius.feature.login.LoginPresenter.LoginState.*
import com.mvcoding.expensius.model.CreateTag
import com.mvcoding.expensius.model.GoogleToken
import com.mvcoding.expensius.model.Tag
import rx.Observable
import rx.Observable.just
import rx.lang.kotlin.BehaviorSubject
import rx.lang.kotlin.filterNotNull
import rx.subjects.SerializedSubject

class LoginWriterAndStateSource(
        private val loginAnonymously: () -> Observable<Unit>,
        private val loginWithGoogle: (GoogleToken, Boolean) -> Observable<Unit>,
        private val tagsSource: DataSource<List<Tag>>,
        private val defaultTagsSource: DataSource<List<CreateTag>>,
        private val createTagsWriter: DataWriter<List<CreateTag>>) : DataWriter<Login>, DataSource<LoginState> {

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
                .switchMap { createDefaultTagsIfNecessary() }
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
                .switchMap { createDefaultTagsIfNecessary() }
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

    private fun createDefaultTagsIfNecessary(): Observable<LoginState> = tagsSource.data()
            .first()
            .switchMap {
                if (it.isNotEmpty()) just(Unit)
                else defaultTagsSource.data().doOnNext { createTagsWriter.write(it) }.map { Unit }
            }
            .map { SuccessfulLogin }
            .onErrorReturn { SuccessfulLogin }
            .cast(LoginState::class.java)

    override fun data(): Observable<LoginState> = loginStateSubject
}