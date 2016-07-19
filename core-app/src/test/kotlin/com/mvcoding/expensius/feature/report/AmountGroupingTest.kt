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

import com.mvcoding.expensius.extensions.toInterval
import com.mvcoding.expensius.extensions.toPeriod
import com.mvcoding.expensius.model.ReportGroup.DAY
import com.mvcoding.expensius.model.aTransaction
import com.mvcoding.expensius.model.withTimestamp
import org.hamcrest.CoreMatchers.equalTo
import org.joda.time.Interval
import org.junit.Assert.assertThat
import org.junit.Test
import java.lang.System.currentTimeMillis
import java.math.BigDecimal
import java.math.BigDecimal.ZERO

class AmountGroupingTest {
    @Test
    fun groupsAmountsIntoDaysThatFitWithinIntervalAndFillsRemainingSpacesWithZero() {
        val reportGroup = DAY
        val period = reportGroup.toPeriod()
        val interval = reportGroup.toInterval(currentTimeMillis()).withPeriodAfterStart(period.multipliedBy(4))

        val tooEarly = aTransaction().withAmount(BigDecimal(10)).withTimestamp(interval.startMillis - 1)
        val groupA1 = aTransaction().withAmount(BigDecimal(11)).withTimestamp(interval.startMillis)
        val groupA2 = aTransaction().withAmount(BigDecimal(12)).withTimestamp(interval.startMillis + 1)
        val groupB1 = aTransaction().withAmount(BigDecimal(13)).withTimestamp(interval.start.plus(period).millis)
        val groupC1 = aTransaction().withAmount(BigDecimal(14)).withTimestamp(interval.endMillis - 1)
        val groupC2 = aTransaction().withAmount(BigDecimal(15)).withTimestamp(interval.endMillis - 2)
        val groupC3 = aTransaction().withAmount(BigDecimal(16)).withTimestamp(interval.endMillis - 3)
        val tooLate = aTransaction().withAmount(BigDecimal(17)).withTimestamp(interval.endMillis)
        val transactions = listOf(tooEarly, groupA1, groupA2, groupB1, groupC1, groupC2, groupC3, tooLate)

        val expectedGroupedAmounts = mapOf(
                Interval(interval.start, period) to BigDecimal(23),
                Interval(interval.start.plus(period), period) to BigDecimal(13),
                Interval(interval.start.plus(period.multipliedBy(2)), period) to ZERO,
                Interval(interval.start.plus(period.multipliedBy(3)), period) to BigDecimal(45)
        )

        val groupedAmounts = AmountGrouping().groupAmounts(transactions, reportGroup, interval)

        assertThat(groupedAmounts, equalTo(expectedGroupedAmounts))
    }
}