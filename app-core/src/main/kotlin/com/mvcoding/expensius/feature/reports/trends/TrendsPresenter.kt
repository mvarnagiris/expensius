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

package com.mvcoding.expensius.feature.reports.trends

import com.mvcoding.expensius.RxSchedulers
import com.mvcoding.expensius.data.Cache
import com.mvcoding.expensius.data.DataSource
import com.mvcoding.expensius.feature.ignoreError
import com.mvcoding.expensius.model.RemoteFilter
import com.mvcoding.expensius.model.ReportSettings
import com.mvcoding.expensius.model.Trends
import com.mvcoding.mvp.Presenter

class TrendsPresenter(
        private val trendsSource: DataSource<Trends>,
        reportSettingsSource: DataSource<ReportSettings>,
        private val secondaryRemoteFilterCache: Cache<RemoteFilter>,
        private val schedulers: RxSchedulers) : Presenter<TrendsPresenter.View>() {

    init {
        secondaryRemoteFilterCache.data()
                .first()
                .withLatestFrom(reportSettingsSource.data()) { remoteFilter, reportSettings -> remoteFilter to reportSettings }
                .subscribe { secondaryRemoteFilterCache.write(it.first.withPreviousInterval(it.second.reportPeriod)) }
    }

    override fun onViewAttached(view: View) {
        super.onViewAttached(view)

        trendsSource.data()
                .subscribeOn(schedulers.io)
                .observeOn(schedulers.main)
                .ignoreError()
                .subscribeUntilDetached { view.showTrends(it) }
    }

    interface View : Presenter.View {
        fun showTrends(trends: Trends)
    }
}