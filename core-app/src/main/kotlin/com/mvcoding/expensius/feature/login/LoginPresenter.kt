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

import com.mvcoding.expensius.RxSchedulers
import com.mvcoding.expensius.feature.ErrorView
import com.mvcoding.expensius.feature.LoadingView
import com.mvcoding.expensius.feature.toError
import com.mvcoding.expensius.model.GoogleToken
import com.mvcoding.expensius.service.LoginService
import com.mvcoding.expensius.service.TagsService
import com.mvcoding.expensius.service.TagsWriteService
import com.mvcoding.mvp.Presenter
import rx.Observable
import rx.Observable.empty
import rx.Observable.just
import rx.lang.kotlin.toSingletonObservable

class LoginPresenter(
        private val destination: Destination,
        private val loginService: LoginService,
        private val tagsService: TagsService,
        private val tagsWriteService: TagsWriteService,
        private val defaultTags: DefaultTags,
        private val schedulers: RxSchedulers) : Presenter<LoginPresenter.View>() {

    private var isGoogleLoginInProgress = false
    private var isAnonymousLoginInProgress = false
    private var googleToken: GoogleToken? = null
    private var loginRequest: Observable<Unit>? = null

    override fun onViewAttached(view: View) {
        super.onViewAttached(view)

        if (destination == Destination.APP) view.showSkipEnabled() else view.showSkipDisabled()

        view.loginWithGoogleRequests()
                .startWith(isGoogleLoginInProgress.toSingletonObservable().filter { it }.map { Unit })
                .doOnNext { isGoogleLoginInProgress = true }
                .doOnNext { view.showLoading() }
                .switchMap { googleToken?.toSingletonObservable() ?: view.showLoginWithGoogle().handleError(view).doOnNext { googleToken = it } }
                .observeOn(schedulers.io)
                .switchMap { loginWithGoogle(it).observeOn(schedulers.main).handleError(view) }
                .observeOn(schedulers.io)
                .switchMap { createDefaultTagsIfNecessary() }
                .observeOn(schedulers.main)
                .doOnNext { view.hideLoading() }
                .doOnNext { isGoogleLoginInProgress = false }
                .subscribeUntilDetached { view.displayDestination(destination) }

        view.skipLoginRequests()
                .startWith(isAnonymousLoginInProgress.toSingletonObservable().filter { it }.map { Unit })
                .doOnNext { isAnonymousLoginInProgress = true }
                .doOnNext { view.showLoading() }
                .observeOn(schedulers.io)
                .switchMap { loginAnonymously().observeOn(schedulers.main).handleError(view) }
                .observeOn(schedulers.io)
                .switchMap { createDefaultTagsIfNecessary() }
                .observeOn(schedulers.main)
                .doOnNext { view.hideLoading() }
                .doOnNext { isAnonymousLoginInProgress = false }
                .subscribeUntilDetached { view.displayDestination(destination) }
    }

    private fun loginWithGoogle(googleToken: GoogleToken): Observable<Unit> {
        val loginRequest = loginRequest ?: loginService.loginWithGoogle(googleToken).cache()
        this.loginRequest = loginRequest
        return loginRequest
    }

    private fun loginAnonymously(): Observable<Unit> {
        val loginRequest = loginRequest ?: loginService.loginAnonymously().cache()
        this.loginRequest = loginRequest
        return loginRequest
    }

    private fun createDefaultTagsIfNecessary(): Observable<Unit> = tagsService.items()
            .first()
            .doOnNext { tagsService.close() }
            .switchMap { if (it.isNotEmpty()) just(Unit) else tagsWriteService.createTags(defaultTags.getDefaultTags()).onErrorReturn { Unit } }

    private fun <T> Observable<T>.handleError(view: View): Observable<T> = onErrorResumeNext {
        view.hideLoading()
        view.showError(it.toError())
        isGoogleLoginInProgress = false
        googleToken = null
        loginRequest = null
        empty()
    }

    interface View : Presenter.View, ErrorView, LoadingView {
        fun loginWithGoogleRequests(): Observable<Unit>
        fun skipLoginRequests(): Observable<Unit>

        fun showLoginWithGoogle(): Observable<GoogleToken>
        fun showSkipEnabled()
        fun showSkipDisabled()

        fun displayDestination(destination: Destination)
    }

    enum class Destination {
        APP, SUPPORT_DEVELOPER
    }
}
