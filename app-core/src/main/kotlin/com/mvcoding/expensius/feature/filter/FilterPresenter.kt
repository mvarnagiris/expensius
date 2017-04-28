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

package com.mvcoding.expensius.feature.filter

import com.mvcoding.expensius.RxSchedulers
import com.mvcoding.expensius.data.Cache
import com.mvcoding.expensius.model.Filter
import com.mvcoding.mvp.Presenter
import org.joda.time.Interval
import rx.Observable

class FilterPresenter(
        private val filterCache: Cache<Filter>,
        private val schedulers: RxSchedulers) : Presenter<FilterPresenter.View>() {

    override fun onViewAttached(view: View) {
        super.onViewAttached(view)

        filterCache.data()
                .subscribeOn(schedulers.io)
                .observeOn(schedulers.main)
                .subscribeUntilDetached { view.showInterval(it.interval) }

        view.previousIntervalRequests()
                .observeOn(schedulers.io)
                .switchMap { filterCache.data().first() }
                .subscribeUntilDetached { filterCache.write(it.withPreviousInterval()) }

        view.nextIntervalRequests()
                .observeOn(schedulers.io)
                .switchMap { filterCache.data().first() }
                .subscribeUntilDetached { filterCache.write(it.withNextInterval()) }
    }

    interface View : Presenter.View {
        fun previousIntervalRequests(): Observable<Unit>
        fun nextIntervalRequests(): Observable<Unit>
        fun showInterval(interval: Interval)
    }
}