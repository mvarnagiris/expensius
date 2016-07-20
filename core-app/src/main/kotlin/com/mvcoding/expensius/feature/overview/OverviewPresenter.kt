/*
 * Copyright (C) 2015 Mantas Varnagiris.
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

import com.mvcoding.expensius.feature.Filter
import com.mvcoding.expensius.model.ReportPeriod
import com.mvcoding.expensius.model.ReportPeriod.MONTH
import com.mvcoding.mvp.Presenter
import org.joda.time.Interval
import rx.Observable
import rx.lang.kotlin.filterNotNull

class OverviewPresenter(private val filter: Filter) : Presenter<OverviewPresenter.View>() {

    override fun onViewAttached(view: View) {
        super.onViewAttached(view)

        filter.filterData().map { it.interval }.filterNotNull().distinctUntilChanged().subscribeUntilDetached { view.showInterval(MONTH, it) }
        view.createTransactionSelects().subscribeUntilDetached { view.displayCreateTransaction() }
        view.transactionsSelects().subscribeUntilDetached { view.displayTransactions() }
        view.tagsSelects().subscribeUntilDetached { view.displayTags() }
        view.settingsSelects().subscribeUntilDetached { view.displaySettings() }
    }

    interface View : Presenter.View {
        fun createTransactionSelects(): Observable<Unit>
        fun transactionsSelects(): Observable<Unit>
        fun tagsSelects(): Observable<Unit>
        fun settingsSelects(): Observable<Unit>

        fun showInterval(reportPeriod: ReportPeriod, interval: Interval)

        fun displayCreateTransaction()
        fun displayTransactions()
        fun displayTags()
        fun displaySettings()
    }
}