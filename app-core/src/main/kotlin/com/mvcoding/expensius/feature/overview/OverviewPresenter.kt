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

package com.mvcoding.expensius.feature.overview

import com.mvcoding.expensius.RxSchedulers
import com.mvcoding.expensius.data.DataSource
import com.mvcoding.expensius.model.RemoteFilter
import com.mvcoding.expensius.model.ReportPeriod
import com.mvcoding.expensius.model.ReportSettings
import com.mvcoding.mvp.Presenter
import org.joda.time.Interval
import rx.Observable
import rx.Observable.combineLatest

class OverviewPresenter(
        private val remoteFilterSource: DataSource<RemoteFilter>,
        private val reportSettingsSource: DataSource<ReportSettings>,
        private val schedulers: RxSchedulers) : Presenter<OverviewPresenter.View>() {

    override fun onViewAttached(view: View) {
        super.onViewAttached(view)

        view.createTransactionSelects().subscribeUntilDetached { view.displayCreateTransaction() }
        view.transactionsSelects().subscribeUntilDetached { view.displayTransactions() }
        view.tagsSelects().subscribeUntilDetached { view.displayTags() }
        view.trendsReportSelects().subscribeUntilDetached { view.displayTrendsReport() }
//        view.tagsReportSelects().subscribeUntilDetached { view.displayTagsReport() }
        view.settingsSelects().subscribeUntilDetached { view.displaySettings() }
        combineLatest(
                remoteFilterSource.data(),
                reportSettingsSource.data(),
                { filter, reportSettings -> FilterReportSettings(filter, reportSettings) })
                .subscribeOn(schedulers.io)
                .observeOn(schedulers.main)
                .subscribeUntilDetached { view.showInterval(it.remoteFilter.interval, it.reportSettings.reportPeriod) }
    }

    interface View : Presenter.View {
        fun createTransactionSelects(): Observable<Unit>
        fun transactionsSelects(): Observable<Unit>
        fun tagsSelects(): Observable<Unit>
        fun trendsReportSelects(): Observable<Unit>
        fun tagsReportSelects(): Observable<Unit>
        fun settingsSelects(): Observable<Unit>

        fun showInterval(interval: Interval, reportPeriod: ReportPeriod)

        fun displayCreateTransaction()
        fun displayTransactions()
        fun displayTags()
        fun displayTrendsReport()
        fun displayTagsReport()
        fun displaySettings()
    }

    private data class FilterReportSettings(val remoteFilter: RemoteFilter, val reportSettings: ReportSettings)
}