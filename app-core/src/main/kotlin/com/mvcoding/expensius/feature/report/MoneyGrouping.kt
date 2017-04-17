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

import com.mvcoding.expensius.extensions.splitIntoGroupIntervals
import com.mvcoding.expensius.extensions.toInterval
import com.mvcoding.expensius.feature.FilterDataOld
import com.mvcoding.expensius.feature.currency.ExchangeRatesProvider
import com.mvcoding.expensius.model.*
import com.mvcoding.expensius.model.Currency
import com.mvcoding.expensius.model.NullModels.noTag
import org.joda.time.Interval
import java.math.BigDecimal.ZERO
import java.util.*

class MoneyGrouping(private val exchangeRatesProvider: ExchangeRatesProvider) {

    fun groupToIntervals(transactions: List<Transaction>, currency: Currency, filterData: FilterDataOld, reportGroup: ReportGroup): List<GroupedMoney<Interval>> {
        val defaultValue = { Money(ZERO, currency) }
        val groupedMoney = filterData.filter(transactions).groupBy({ reportGroup.toInterval(it.timestamp) }, { it.money }).sumMoney(currency)
        return reportGroup.splitIntoGroupIntervals(filterData.interval)
                .associateBy({ it }, { groupedMoney.getOrElse(it, defaultValue) })
                .map { GroupedMoney(it.key, it.value) }
    }

    fun groupToTags(transactions: List<Transaction>, currency: Currency, filterData: FilterDataOld): List<GroupedMoney<Tag>> = filterData.filter(transactions)
            .fold(hashMapOf<Tag, ArrayList<Money>>()) { map, transaction -> transaction.appendMoneyToTags(map) }
            .sumMoney(currency)
            .map { GroupedMoney(it.key, it.value) }
            .sortedWith(compareBy({ -it.money.amount }, { it.group.order }))

    private fun Transaction.appendMoneyToTags(map: HashMap<Tag, ArrayList<Money>>) = tagsOrNoTag()/*.forEach { map.appendAmountToTag(it, money) }*/.let { map }
    private fun Transaction.tagsOrNoTag() = tags.let { if (it.isEmpty()) setOf(noTag) else it }
    private fun HashMap<Tag, ArrayList<Money>>.appendAmountToTag(tag: Tag, money: Money) = getOrPut(tag, { arrayListOf() }).add(money)
    private fun <KEY> Map<KEY, List<Money>>.sumMoney(currency: Currency) = mapValues { it.value.sumMoney(currency, exchangeRatesProvider) }
}

data class GroupedMoney<out GROUP>(val group: GROUP, val money: Money)