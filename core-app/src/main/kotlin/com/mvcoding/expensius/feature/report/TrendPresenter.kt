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

package com.mvcoding.expensius.feature.report

import com.mvcoding.expensius.RxSchedulers
import com.mvcoding.expensius.extensions.interval
import com.mvcoding.expensius.extensions.previousInterval
import com.mvcoding.expensius.feature.Filter
import com.mvcoding.expensius.feature.FilterData
import com.mvcoding.expensius.model.Currency
import com.mvcoding.expensius.model.ReportGroup
import com.mvcoding.expensius.model.ReportGroup.DAY
import com.mvcoding.expensius.model.ReportPeriod
import com.mvcoding.expensius.model.ReportPeriod.MONTH
import com.mvcoding.expensius.model.TimestampProvider
import com.mvcoding.expensius.model.Transaction
import com.mvcoding.expensius.service.AppUserService
import com.mvcoding.expensius.service.TransactionsService
import com.mvcoding.mvp.Presenter
import org.joda.time.Interval
import rx.Observable.combineLatest
import java.math.BigDecimal

class TrendPresenter(
        private val appUserService: AppUserService,
        private val transactionsService: TransactionsService,
        private val filter: Filter,
        private val timestampProvider: TimestampProvider,
        private val schedulers: RxSchedulers) : Presenter<TrendPresenter.View>() {

    private val amountGrouping = AmountGrouping()

    override fun onViewAttached(view: View) {
        super.onViewAttached(view)

        val latestData = combineLatest(appUserService.appUser(), transactionsService.items(), filter.filterData()) { appUser, transactions, filterData ->
            TransactionsWithFilterData(appUser.settings.currency, transactions, filterData)
        }

        latestData.subscribeOn(schedulers.io)
                .observeOn(schedulers.computation)
                .map { calculateTrends(it) }
                .observeOn(schedulers.main)
                .subscribeUntilDetached { view.showTrends(it.currency, it.totalAmount, it.currentAmounts, it.previousAmounts) }
    }

    private fun calculateTrends(transactionsWithFilterData: TransactionsWithFilterData): Trends {
        val reportPeriod = MONTH
        val reportGroup = DAY
        val interval = currentInterval(reportPeriod, transactionsWithFilterData)
        val previousInterval = reportPeriod.previousInterval(interval)

        val totalAmounts = groupAmounts(interval, reportGroup, transactionsWithFilterData)
        val previousTotalAmounts = groupAmounts(previousInterval, reportGroup, transactionsWithFilterData)

        val normalizedTotalAmounts = normalize(totalAmounts, previousTotalAmounts)
        val normalizedPreviousTotalAmounts = normalize(previousTotalAmounts, totalAmounts)

        return Trends(
                transactionsWithFilterData.currency,
                normalizedTotalAmounts.fold(BigDecimal.ZERO) { totalAmount, amount -> totalAmount + amount },
                normalizedTotalAmounts,
                normalizedPreviousTotalAmounts)
    }

    private fun normalize(list: List<BigDecimal>, otherList: List<BigDecimal>) =
            if (list.size < otherList.size) list.plus((0..(otherList.size - list.size - 1)).map { list.lastOrNull() ?: BigDecimal.ZERO }) else list

    private fun currentInterval(reportPeriod: ReportPeriod, transactionsWithFilterData: TransactionsWithFilterData) =
            transactionsWithFilterData.filterData.interval ?: reportPeriod.interval(timestampProvider.currentTimestamp())

    private fun groupAmounts(interval: Interval, reportGroup: ReportGroup, transactionsWithFilterData: TransactionsWithFilterData) =
            amountGrouping.groupAmounts(transactionsWithFilterData.transactions, reportGroup, interval).values.toList()

    interface View : Presenter.View {
        fun showTrends(currency: Currency, totalAmount: BigDecimal, currentAmounts: List<BigDecimal>, previousAmounts: List<BigDecimal>)
    }

    private data class TransactionsWithFilterData(val currency: Currency, val transactions: List<Transaction>, val filterData: FilterData)
    private data class Trends(val currency: Currency, val totalAmount: BigDecimal, val currentAmounts: List<BigDecimal>, val previousAmounts: List<BigDecimal>)
}