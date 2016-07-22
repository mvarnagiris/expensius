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
import com.mvcoding.expensius.feature.Filter
import com.mvcoding.expensius.feature.FilterData
import com.mvcoding.expensius.feature.currency.ExchangeRatesProvider
import com.mvcoding.expensius.model.AppUser
import com.mvcoding.expensius.model.Tag
import com.mvcoding.expensius.model.Transaction
import com.mvcoding.expensius.service.AppUserService
import com.mvcoding.expensius.service.TransactionsService
import com.mvcoding.mvp.Presenter
import rx.Observable.combineLatest

class TagsTotalsReportPresenter(
        private val appUserService: AppUserService,
        private val transactionsService: TransactionsService,
        exchangeRatesProvider: ExchangeRatesProvider,
        private val filter: Filter,
        private val schedulers: RxSchedulers) : Presenter<TagsTotalsReportPresenter.View>() {

    private val moneyGrouping = MoneyGrouping(exchangeRatesProvider)

    override fun onViewAttached(view: View) {
        super.onViewAttached(view)

        combineLatest(appUserService.appUser(), transactionsService.items(), filter.filterData(), moneyGroupingData())
                .subscribeOn(schedulers.io)
                .observeOn(schedulers.computation)
                .map { moneyGrouping.groupToTags(it.transactions, it.appUser.settings.mainCurrency, it.filterData) }
                .observeOn(schedulers.main)
                .subscribeUntilDetached { view.showTagsTotals(it) }
    }

    private fun moneyGroupingData() = { appUser: AppUser, transactions: List<Transaction>, filterData: FilterData ->
        MoneyGroupingData(appUser, transactions, filterData)
    }

    interface View : Presenter.View {
        fun showTagsTotals(tagAmounts: List<GroupedMoney<Tag>>)
    }

    private data class MoneyGroupingData(val appUser: AppUser, val transactions: List<Transaction>, val filterData: FilterData)
}