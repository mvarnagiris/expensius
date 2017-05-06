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

package com.mvcoding.expensius.feature.reports.trends

class TrendsSourceTest {

//    @Test
//    fun `groups amounts into report group intervals`() {
//        val reportGroup = ReportGroup.DAY
//        val period = reportGroup.toPeriod()
//        val interval = reportGroup.toInterval(System.currentTimeMillis()).withPeriodAfterStart(period.multipliedBy(4))
//        val filterData = FilterDataOld(interval, EXPENSE, CONFIRMED)
//        val currency = aCurrency()
//
//        val tooEarly = aTransaction().withAmount(10).withTransactionType(EXPENSE).withTransactionState(CONFIRMED).withTimestamp(interval.startMillis - 1)
//        val notExpense = aTransaction().withAmount(11).withTransactionType(INCOME).withTransactionState(CONFIRMED).withTimestamp(interval.startMillis)
//        val notConfirmed = aTransaction().withAmount(12).withTransactionType(EXPENSE).withTransactionState(PENDING).withTimestamp(interval.startMillis)
//        val groupA1 = aTransaction().withAmount(13).withTransactionType(EXPENSE).withTransactionState(CONFIRMED).withTimestamp(interval.startMillis)
//        val groupA2 = aTransaction().withAmount(14).withTransactionType(EXPENSE).withTransactionState(CONFIRMED).withTimestamp(interval.startMillis + 1)
//        val groupB1 = aTransaction().withAmount(15).withTransactionType(EXPENSE).withTransactionState(CONFIRMED).withTimestamp(interval.start.plus(period).millis)
//        val groupC1 = aTransaction().withAmount(16).withTransactionType(EXPENSE).withTransactionState(CONFIRMED).withTimestamp(interval.endMillis - 1)
//        val groupC2 = aTransaction().withAmount(17).withTransactionType(EXPENSE).withTransactionState(CONFIRMED).withTimestamp(interval.endMillis - 2)
//        val groupC3 = aTransaction().withAmount(18).withTransactionType(EXPENSE).withTransactionState(CONFIRMED).withTimestamp(interval.endMillis - 3)
//        val tooLate = aTransaction().withAmount(19).withTransactionType(EXPENSE).withTransactionState(CONFIRMED).withTimestamp(interval.endMillis)
//        val transactions = listOf(tooEarly, notExpense, notConfirmed, groupA1, groupA2, groupB1, groupC1, groupC2, groupC3, tooLate)
//
//        val expectedGroupedAmounts = listOf(
//                GroupedMoney(Interval(interval.start, period), Money(BigDecimal.valueOf(27.toDouble()), currency)),
//                GroupedMoney(Interval(interval.start.plus(period), period), Money(BigDecimal.valueOf(15.toDouble()), currency)),
//                GroupedMoney(Interval(interval.start.plus(period.multipliedBy(2)), period), Money(ZERO, currency)),
//                GroupedMoney(Interval(interval.start.plus(period.multipliedBy(3)), period), Money(BigDecimal.valueOf(51.toDouble()), currency))
//        )
//
//        val groupedMoney = moneyGrouping.groupToIntervals(transactions, currency, filterData, reportGroup)
//
//        assertThat(groupedMoney, equalTo(expectedGroupedAmounts))
//    }
}