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

package com.mvcoding.expensius.feature.reports.tags

import com.mvcoding.expensius.data.DataSource
import com.mvcoding.expensius.data.ParameterDataSource
import com.mvcoding.expensius.model.*
import rx.Observable
import rx.Observable.combineLatest
import rx.Observable.from
import java.math.BigDecimal

class TagsReportSource(
        private val transactionsSource: DataSource<List<Transaction>>,
        private val otherTransactionsSource: DataSource<List<Transaction>>,
        private val localFilterSource: DataSource<LocalFilter>,
        private val reportSettingsSource: DataSource<ReportSettings>,
        private val moneyConversionSource: ParameterDataSource<MoneyConversion, Money>) : DataSource<TagsReport> {

    override fun data(): Observable<TagsReport> = combineLatest(
            dataForTotalMoneyWithGroupedMoneys(transactionsSource),
            dataForTotalMoneyWithGroupedMoneys(otherTransactionsSource),
            { (totalMoney, groupedMoney), (otherTotalMoney, otherGroupedMoney) -> TagsReport(groupedMoney, totalMoney, otherGroupedMoney, otherTotalMoney) })

    private fun dataForTotalMoneyWithGroupedMoneys(transactionsSource: DataSource<List<Transaction>>): Observable<Pair<Money, List<GroupedMoney<Tag>>>> {
        return combineLatest(
                transactionsSource.data(),
                localFilterSource.data(),
                reportSettingsSource.data(),
                ::ReportData)
                .switchMap {
                    val reportSettings = it.reportSettings
                    val filteredTransactions = it.localFilter.filter(it.transactions)
                    val currency = reportSettings.currency

                    from(filteredTransactions)
                            .flatMap { transaction -> moneyConversionSource.data(MoneyConversion(transaction.money, currency)).map { transaction.withMoney(it) } }
                            .toList()
                            .map {
                                val tagsAmountMap = hashMapOf<Tag, BigDecimal>()
                                var totalAmount = BigDecimal.ZERO
                                it.forEach { transaction ->
                                    val amount = transaction.money.amount
                                    totalAmount += amount
                                    transaction.tags.forEach {
                                        tagsAmountMap.compute(it) { _, currentAmount -> currentAmount?.let { it + amount } ?: amount }
                                    }
                                }

                                val totalMoney = Money(totalAmount, currency)
                                val groupedMoneys = tagsAmountMap
                                        .flatMap { listOf(GroupedMoney(it.key, Money(it.value, currency))) }
                                        .sortedWith(compareBy({ -it.money.amount }, { it.group.order }))
                                totalMoney to groupedMoneys
                            }
                }
    }

    private data class ReportData(
            val transactions: List<Transaction>,
            val localFilter: LocalFilter,
            val reportSettings: ReportSettings)
}