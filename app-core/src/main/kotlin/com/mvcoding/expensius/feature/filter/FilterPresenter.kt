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

package com.mvcoding.expensius.feature.filter

import com.mvcoding.expensius.RxSchedulers
import com.mvcoding.expensius.data.Cache
import com.mvcoding.expensius.data.DataSource
import com.mvcoding.expensius.model.RemoteFilter
import com.mvcoding.expensius.model.ReportPeriod
import com.mvcoding.expensius.model.ReportSettings
import com.mvcoding.mvp.Presenter
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables.combineLatest
import io.reactivex.rxkotlin.withLatestFrom
import org.joda.time.Interval

class FilterPresenter(
        private val remoteFilterCache: Cache<RemoteFilter>,
        private val reportSettingsSource: DataSource<ReportSettings>,
        private val schedulers: RxSchedulers) : Presenter<FilterPresenter.View>() {

    override fun onViewAttached(view: View) {
        super.onViewAttached(view)


        val filterReportSettings = combineLatest(
                remoteFilterCache.data(),
                reportSettingsSource.data(),
                { filter, reportSettings -> FilterReportSettings(filter, reportSettings) })
                .share()

        filterReportSettings
                .subscribeOn(schedulers.io)
                .observeOn(schedulers.main)
                .subscribeUntilDetached { view.showInterval(it.remoteFilter.interval, it.reportSettings.reportPeriod) }

        view.previousIntervalRequests()
                .observeOn(schedulers.io)
                .withLatestFrom(filterReportSettings) { _, filterReportSettings -> filterReportSettings }
                .subscribeUntilDetached { remoteFilterCache.write(it.remoteFilter.withPreviousInterval(it.reportSettings.reportPeriod)) }

        view.nextIntervalRequests()
                .observeOn(schedulers.io)
                .withLatestFrom(filterReportSettings) { _, filterReportSettings -> filterReportSettings }
                .subscribeUntilDetached { remoteFilterCache.write(it.remoteFilter.withNextInterval(it.reportSettings.reportPeriod)) }
    }

    interface View : Presenter.View {
        fun previousIntervalRequests(): Observable<Unit>
        fun nextIntervalRequests(): Observable<Unit>
        fun showInterval(interval: Interval, reportPeriod: ReportPeriod)
    }

    private data class FilterReportSettings(val remoteFilter: RemoteFilter, val reportSettings: ReportSettings)
}