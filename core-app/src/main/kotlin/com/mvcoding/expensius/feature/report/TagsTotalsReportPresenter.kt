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
import com.mvcoding.expensius.model.Currency
import com.mvcoding.expensius.model.Tag
import com.mvcoding.expensius.model.Transaction
import com.mvcoding.expensius.service.AppUserService
import com.mvcoding.expensius.service.TransactionsService
import com.mvcoding.mvp.Presenter
import rx.Observable.combineLatest
import java.math.BigDecimal

class TagsTotalsReportPresenter(
        private val appUserService: AppUserService,
        private val transactionsService: TransactionsService,
        private val filter: Filter,
        private val schedulers: RxSchedulers) : Presenter<TagsTotalsReportPresenter.View>() {

    private val amountGrouping = AmountGrouping()

    override fun onViewAttached(view: View) {
        super.onViewAttached(view)

        val latestData = combineLatest(appUserService.appUser(), transactionsService.items(), filter.filterData()) { appUser, transactions, filterData ->
            TransactionsWithFilterData(appUser.settings.currency, transactions, filterData)
        }

        latestData.subscribeOn(schedulers.io)
                .observeOn(schedulers.computation)
                .map {
                    val filterData = it.filterData
                    val currency = it.currency
                    val transactions = filterData.filter(it.transactions)
                    amountGrouping.groupAmountsInTags(transactions).map { TagAmount(it.group, it.amount, currency) }
                }
                .observeOn(schedulers.main)
                .subscribeUntilDetached { view.showTagsTotals(it) }
    }

    interface View : Presenter.View {
        fun showTagsTotals(tagAmounts: List<TagAmount>)
    }

    data class TagAmount(val tag: Tag, val amount: BigDecimal, val currency: Currency)
    private data class TransactionsWithFilterData(val currency: Currency, val transactions: List<Transaction>, val filterData: FilterData)
}