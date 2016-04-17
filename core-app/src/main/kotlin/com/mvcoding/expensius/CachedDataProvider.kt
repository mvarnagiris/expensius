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

package com.mvcoding.expensius

import com.mvcoding.expensius.feature.ErrorView
import com.mvcoding.expensius.feature.toError
import rx.Observable.empty

open class CachedDataProvider<DATA>(private val cachedDataService: CachedDataService<DATA>) {
    fun refresh() = cachedDataService.refresh()
    fun onlyValidData() = data().filter { it.isValid() }.flatMap { it.toObservable() }
    fun loadingStates() = cachedDataService.loadingStates.asObservable()
    fun emptyStates() = cachedDataService.emptyStates.asObservable()
    fun data() = cachedDataService.cachedData
    fun data(errorView: ErrorView) = data().flatMap {
        it.toObservable().doOnError { errorView.showError(it.toError()) }.onErrorResumeNext { empty() }
    }
}