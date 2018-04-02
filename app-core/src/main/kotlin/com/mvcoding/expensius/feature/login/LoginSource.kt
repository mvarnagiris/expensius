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

package com.mvcoding.expensius.feature.login

import com.mvcoding.expensius.data.DataSource
import com.mvcoding.expensius.data.DataWriter
import com.mvcoding.expensius.data.ParameterDataSource
import com.mvcoding.expensius.model.*
import io.reactivex.Observable
import io.reactivex.Observable.just

class LoginSource(
        private val login: (Login) -> Observable<LoggedInUserDetails>,
        private val logout: () -> Unit,
        private val getAppUser: () -> Observable<AppUser>,
        private val appUserWriter: DataWriter<AppUser>,
        private val tagsSource: DataSource<List<Tag>>,
        private val defaultTagsSource: DataSource<List<CreateTag>>,
        private val createTagsWriter: DataWriter<Set<CreateTag>>) : ParameterDataSource<Login, Unit> {

    override fun data(parameter: Login): Observable<Unit> = login(parameter)
            .switchMap { loggedInUserDetails -> getAppUser().map { it.withLoggedInUserDetails(loggedInUserDetails) } }
            .doOnNext { appUserWriter.write(it) }
            .switchMap { tagsSource.data().firstOrError().toObservable() }
            .switchMap { if (it.isEmpty()) defaultTagsSource.data() else just(emptyList()) }
            .doOnNext { if (it.isNotEmpty()) createTagsWriter.write(it.toSet()) }
            .doOnError { logout() }
            .map { Unit }
}