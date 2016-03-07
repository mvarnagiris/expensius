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

package com.mvcoding.expensius.paging

import rx.Observable
import rx.Observable.combineLatest

abstract class PageLoader<T, Q, D, DI> {
    private var isNewDataLoad = false

    fun load(converter: (DI) -> T, query: Q, pageObservable: Observable<Page>): Observable<PageResult<T>> {
        return combineLatest(pageObservable, load(query).doOnNext { isNewDataLoad = true }, { page, data ->
            val wasNewDataLoad = isNewDataLoad
            isNewDataLoad = false

            val range = page.rangeTo(sizeOf(data))
            if (range.isEmpty()) {
                PageResult(page, emptyList<T>(), wasNewDataLoad)
            } else {
                range.map { converter.invoke(dataItemAtPosition(data, it)) }
                        .toList()
                        .let { PageResult(page, it, wasNewDataLoad) }
            }
        })
    }

    protected abstract fun load(query: Q): Observable<D>
    protected abstract fun sizeOf(data: D): Int
    protected abstract fun dataItemAtPosition(data: D, position: Int): DI
}