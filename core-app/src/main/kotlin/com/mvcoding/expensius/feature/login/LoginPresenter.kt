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
import com.mvcoding.expensius.feature.handleError
import com.mvcoding.expensius.service.LoginService
import com.mvcoding.expensius.service.TagsWriteService
import com.mvcoding.mvp.Presenter
import rx.Observable
import rx.Observable.empty
import rx.Observable.just

class LoginPresenter(
        private val loginService: LoginService,
        private val tagsWriteService: TagsWriteService,
        private val defaultTags: DefaultTags,
        private val schedulers: RxSchedulers) : Presenter<LoginPresenter.View>() {

    private var loginRequest: Observable<Unit>? = null

    override fun onViewAttached(view: View) {
        super.onViewAttached(view)

        view.loginAnonymouslyRequests()
                .startWith(unitIfLoginInProgress())
                .doOnNext { view.showLoggingIn() }
                .observeOn(schedulers.io)
                .switchMap { loginAnonymously(view) }
                .observeOn(schedulers.io)
                .switchMap { createDefaultTags() }
                .observeOn(schedulers.main)
                .doOnNext { view.hideLoggingIn() }
                .subscribeUntilDetached { view.displayApp() }
    }

    private fun unitIfLoginInProgress() = loginRequest?.let { just(Unit) } ?: empty()

    private fun loginAnonymously(view: View): Observable<Unit> {
        val loginRequest = loginRequest ?: loginService.loginAnonymously().cache()
        this.loginRequest = loginRequest

        return loginRequest.observeOn(schedulers.main).onErrorResumeNext {
            view.handleError(it) {
                this.loginRequest = null
                view.hideLoggingIn()
            }
        }
    }

    private fun createDefaultTags() = tagsWriteService.createTags(defaultTags.getDefaultTags()).onErrorReturn { Unit }

    interface View : Presenter.View, ErrorView {
        fun loginAnonymouslyRequests(): Observable<Unit>

        fun showLoggingIn()
        fun hideLoggingIn()
        fun displayApp()
    }
}