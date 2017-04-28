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

package com.mvcoding.expensius.feature.report

import com.mvcoding.expensius.RxSchedulers
import com.mvcoding.expensius.feature.FilterDataOld
import com.mvcoding.expensius.feature.FilterOld
import com.mvcoding.expensius.feature.currency.ExchangeRatesProvider
import com.mvcoding.expensius.model.*
import com.mvcoding.mvp.Presenter
import java.math.BigDecimal.ZERO

class TrendReportPresenter(
        //        private val appUserService: AppUserService,
//        private val transactionsService: TransactionsService,
        private val exchangeRatesProvider: ExchangeRatesProvider,
        private val filter: FilterOld,
        private val schedulers: RxSchedulers) : Presenter<TrendReportPresenter.View>() {

    private val moneyGrouping = MoneyGrouping(exchangeRatesProvider)

    override fun onViewAttached(view: View) {
        super.onViewAttached(view)

//        combineLatest(appUserService.appUser(), transactionsService.items(), filter.filterData(), moneyGroupingData())
//                .subscribeOn(schedulers.io)
//                .observeOn(schedulers.computation)
//                .map { calculateTrends(it) }
//                .observeOn(schedulers.main)
//                .subscribeUntilDetached { view.showTrends(it.totalMoney, it.currentMoney, it.previousMoney) }
    }

    private fun moneyGroupingData() = { appUser: AppUser, transactions: List<Transaction>, filterData: FilterDataOld ->
        MoneyGroupingData(appUser, transactions, filterData)
    }

    private fun calculateTrends(moneyGroupingData: MoneyGroupingData): Trends {
        val reportPeriod = moneyGroupingData.appUser.settings.reportPeriod
        val reportGroup = moneyGroupingData.appUser.settings.reportGroup
        val currentIntervalFilterData = moneyGroupingData.filterData
        val previousIntervalFilterData = currentIntervalFilterData.withInterval(reportPeriod.previousInterval(currentIntervalFilterData.interval))
        val currency = moneyGroupingData.appUser.settings.mainCurrency
        val transactions = moneyGroupingData.transactions

        val currentIntervalGroupedMoney = groupMoney(transactions, currency, currentIntervalFilterData, reportGroup)
        val previousIntervalGroupedMoney = groupMoney(transactions, currency, previousIntervalFilterData, reportGroup)

        val normalizedCurrentIntervalGroupedMoney = normalize(currentIntervalGroupedMoney, previousIntervalGroupedMoney, currency)
        val normalizedPreviousIntervalGroupedMoney = normalize(previousIntervalGroupedMoney, currentIntervalGroupedMoney, currency)

        return Trends(
                normalizedCurrentIntervalGroupedMoney.sumMoney(currency, exchangeRatesProvider),
                normalizedCurrentIntervalGroupedMoney,
                normalizedPreviousIntervalGroupedMoney)
    }

    private fun groupMoney(transactions: List<Transaction>, currency: Currency, filterData: FilterDataOld, reportGroup: ReportGroup) =
            moneyGrouping.groupToIntervals(transactions, currency, filterData, reportGroup).map { it.money }

    private fun normalize(list: List<Money>, otherList: List<Money>, currency: Currency) =
            if (list.size < otherList.size) list.plus(addRange(list, otherList).map { list.lastOrNull() ?: Money(ZERO, currency) }) else list

    private fun addRange(list: List<Money>, otherList: List<Money>) = (0..(otherList.size - list.size - 1))

    interface View : Presenter.View {
        fun showTrends(totalMoney: Money, currentMoney: List<Money>, previousMoney: List<Money>)
    }

    private data class MoneyGroupingData(val appUser: AppUser, val transactions: List<Transaction>, val filterData: FilterDataOld)
    private data class Trends(val totalMoney: Money, val currentMoney: List<Money>, val previousMoney: List<Money>)
}