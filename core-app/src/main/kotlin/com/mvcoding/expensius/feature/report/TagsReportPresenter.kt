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
import com.mvcoding.expensius.Settings
import com.mvcoding.expensius.extensions.splitIntoGroupIntervals
import com.mvcoding.expensius.extensions.toInterval
import com.mvcoding.expensius.feature.Filter
import com.mvcoding.expensius.feature.FilterData
import com.mvcoding.expensius.feature.transaction.TransactionsFilter
import com.mvcoding.expensius.feature.transaction.TransactionsProvider
import com.mvcoding.expensius.model.ModelState.NONE
import com.mvcoding.expensius.model.NullModels
import com.mvcoding.expensius.model.ReportGroup
import com.mvcoding.expensius.model.Tag
import com.mvcoding.expensius.model.Transaction
import com.mvcoding.expensius.model.TransactionState.CONFIRMED
import com.mvcoding.mvp.Presenter
import org.joda.time.Interval
import rx.Observable
import rx.Observable.combineLatest
import rx.Observable.just
import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.util.*

class TagsReportPresenter(
        private val filter: Filter,
        private val transactionsProvider: TransactionsProvider,
        private val settings: Settings,
        private val rxSchedulers: RxSchedulers) : Presenter<TagsReportPresenter.View>() {

    override fun onViewAttached(view: View) {
        super.onViewAttached(view)

        val filteredTransactions = filter.filterData()
                .doOnNext { updateIntervalState(view, it) }
                .filter { it.interval != null }
                .observeOn(rxSchedulers.io)
                .flatMap { queryTransactions(it) }

        combineFilteredTransactionsAndReportStep(filteredTransactions)
                .observeOn(rxSchedulers.computation)
                .map { convertToReportData(it.interval, it.transactions, it.reportGroup) }
                .subscribeOn(rxSchedulers.main)
                .observeOn(rxSchedulers.main)
                .subscribeUntilDetached { view.showTagsReportItems(it) }
    }

    private fun updateIntervalState(view: View, filterData: FilterData) {
        if (filterData.interval == null) view.showIntervalIsRequired() else view.hideIntervalIsRequired()
    }

    private fun queryTransactions(filterData: FilterData): Observable<IntervalAndTransactions> {
        val transactionsFilter = TransactionsFilter(
                NONE,
                filterData.interval,
                filterData.transactionType,
                CONFIRMED)
        return transactionsProvider.transactions(transactionsFilter)
                .map { IntervalAndTransactions(filterData.interval!!, it) }
    }

    private fun combineFilteredTransactionsAndReportStep(
            filteredTransactions: Observable<IntervalAndTransactions>) =
            combineLatest(filteredTransactions, just(ReportGroup.DAY), {
                intervalAndTransactions, reportGroup ->
                IntervalAndTransactionsAndReportStep(
                        intervalAndTransactions.interval,
                        intervalAndTransactions.transactions, reportGroup)
            })

    private fun convertToReportData(
            interval: Interval,
            transactions: List<Transaction>,
            reportGroup: ReportGroup): List<TagsReportItem> {
        val resultMap = reportGroup.splitIntoGroupIntervals(interval)
                .map { it to hashMapOf<Tag, BigDecimal>() }
                .toMap()

        transactions.forEach { transaction ->
            val stepInterval = reportGroup.toInterval(transaction.timestamp)
            val amountsMap = resultMap[stepInterval]
            transaction.tagsOrNoTag().forEach { tag ->
                val newAmount = amountsMap?.getOrElse(tag, { ZERO })
                                        ?.plus(transaction.getAmountForCurrency(settings.mainCurrency)) ?: ZERO
                amountsMap?.put(tag, newAmount)
            }
        }

        return prepareReportItems(resultMap)
    }

    private fun Transaction.tagsOrNoTag() = tags.let { tags -> if (tags.isEmpty()) setOf(NullModels.noTag) else tags }

    private fun prepareReportItems(
            resultMap: Map<Interval, HashMap<Tag, BigDecimal>>): List<TagsReportItem> {

        return resultMap.map {
            val interval = it.key
            val tagsWithAmount = it.value
                    .toSortedMap(Comparator { tagLeft, tagRight ->
                        tagLeft.order.value.compareTo(tagRight.order.value)
                    }).toList().map { TagWithAmount(it.first, it.second) }

            TagsReportItem(interval, tagsWithAmount)
        }
    }

    data class TagWithAmount(val tag: Tag, val amount: BigDecimal)
    data class TagsReportItem(val interval: Interval, val tagsWithAmount: List<TagWithAmount>)
    private data class IntervalAndTransactions(val interval: Interval, val transactions: List<Transaction>)
    private data class IntervalAndTransactionsAndReportStep(
            val interval: Interval,
            val transactions: List<Transaction>,
            val reportGroup: ReportGroup)

    interface View : Presenter.View {
        fun showIntervalIsRequired()
        fun hideIntervalIsRequired()
        fun showTagsReportItems(tagsReportItems: List<TagsReportItem>)
    }
}