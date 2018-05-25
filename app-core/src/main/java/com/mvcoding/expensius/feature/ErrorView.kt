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

package com.mvcoding.expensius.feature

import com.mvcoding.expensius.RxSchedulers
import com.mvcoding.expensius.model.UserAlreadyLinkedException
import com.mvcoding.mvp.Presenter
import io.reactivex.Observable
import io.reactivex.Observable.empty
import io.reactivex.Observable.just

interface ErrorView : Presenter.View {
    fun showError(error: Error)
}

interface ResolvableErrorView : Presenter.View {
    fun showResolvableError(error: Error): Observable<Resolution>
}

enum class Resolution {
    POSITIVE, NEGATIVE
}

data class Error(val throwable: Throwable) {
    fun isResolvable() = throwable is UserAlreadyLinkedException
}

fun Throwable.toError() = Error(this)

fun <T> Observable<T>.ignoreError(): Observable<T> = onErrorResumeNext { error: Throwable -> empty() }
fun <T> Observable<T>.handleError(
        errorView: ErrorView,
        schedulers: RxSchedulers,
        doBeforeShowError: (Throwable) -> Unit = {}): Observable<T> = onErrorResumeNext { error: Throwable ->
    just(error).observeOn(schedulers.main).doOnNext { doBeforeShowError(it) }.subscribe { errorView.showError(it.toError()) }
    empty()
}