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

import com.mvcoding.expensius.CachedData.Companion.cachedData
import com.mvcoding.expensius.EmptyState.EMPTY
import com.mvcoding.expensius.EmptyState.NOT_EMPTY
import com.mvcoding.expensius.LoadingState.LOADED
import com.mvcoding.expensius.LoadingState.LOADING
import rx.Observable
import rx.Observable.error
import rx.Observable.just
import rx.lang.kotlin.BehaviorSubject

class CachedDataService<DATA>(private val getData: () -> Observable<DATA>) {
    private var cache: Observable<CachedData<DATA>>? = null
    private var lastSuccessfulCache: DATA? = null
    private val refreshes = BehaviorSubject(Unit)
    private val isEmpty: (DATA) -> Boolean = {
        when (it) {
            is Collection<*> -> it.isEmpty()
            is Array<*> -> it.isEmpty()
            else -> false
        }
    }

    val cachedData = refreshes.flatMap { cache ?: getDataAndHandleErrors() }
    val loadingStates = BehaviorSubject<LoadingState>()
    val emptyStates = BehaviorSubject<EmptyState>()

    fun refresh() {
        cache = null
        refreshes.onNext(Unit)
    }

    fun updateCacheButDoNotEmit(newCache: DATA) {
        cache = just(cachedData(newCache))
        lastSuccessfulCache = newCache
        emitNewEmptyStateIfChanged(newCache)
    }

    private fun emitNewEmptyStateIfChanged(newCache: DATA) {
        val currentEmptyState = emptyStates.value ?: EMPTY
        val newEmptyState = newCache.toEmptyState()
        if (currentEmptyState != newEmptyState) {
            emptyStates.onNext(newEmptyState)
        }
    }

    private fun getDataAndHandleErrors(): Observable<CachedData<DATA>>? {
        cache = invokeGetData(getData)
                .doOnNext { loadingStates.onNext(LOADED) }
                .doOnError { loadingStates.onNext(LOADED) }
                .doOnNext { emptyStates.onNext(it.toEmptyState()) }
                .cache()
                .map { cachedData(it) }
                .onErrorResumeNext { just(cachedData(it)) }
        return cache
    }

    private fun DATA.toEmptyState() = if (isEmpty(this)) EMPTY else NOT_EMPTY

    private fun invokeGetData(getData: () -> Observable<DATA>): Observable<DATA> {
        loadingStates.onNext(LOADING)
        return getData()
                .doOnNext { lastSuccessfulCache = it }
                .doOnError { lastSuccessfulCache?.let { cache = just(cachedData(it)) } }
    }
}

interface CachedData<DATA> {
    companion object {
        fun <DATA> cachedData(data: DATA): CachedData<DATA> = ValidCachedData(data)
        fun <DATA> cachedData(throwable: Throwable): CachedData<DATA> = FailedCachedData(throwable)
    }

    fun toObservable(): Observable<DATA>
    fun isValid(): Boolean

    private data class ValidCachedData<DATA>(private val data: DATA) : CachedData<DATA> {
        override fun toObservable() = just(data)
        override fun isValid() = true
    }

    private data class FailedCachedData<DATA>(val throwable: Throwable) : CachedData<DATA> {
        override fun toObservable() = error<DATA>(throwable)
        override fun isValid() = false
    }
}

enum class LoadingState {
    LOADING, LOADED
}

enum class EmptyState {
    EMPTY, NOT_EMPTY
}