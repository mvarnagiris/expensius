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

import com.mvcoding.expensius.extensions.splitIntoGroupIntervals
import com.mvcoding.expensius.extensions.toInterval
import com.mvcoding.expensius.model.ReportGroup
import com.mvcoding.expensius.model.Transaction
import org.joda.time.Interval
import java.math.BigDecimal
import java.math.BigDecimal.ZERO

class AmountGrouping {

    fun groupAmounts(
            transactions: List<Transaction>,
            reportGroup: ReportGroup,
            interval: Interval): Map<Interval, BigDecimal> {

        val groupedAmounts = calculateTotals(reportGroup, transactions)
        val defaultValue = { ZERO }
        return reportGroup.splitIntoGroupIntervals(interval).associateBy({ it }, { groupedAmounts.getOrElse(it, defaultValue) })
    }

    private fun calculateTotals(reportGroup: ReportGroup, transactions: List<Transaction>): Map<Interval, BigDecimal> = transactions
            .groupBy { reportGroup.toInterval(it.timestamp) }
            .mapValues { it.value.fold(ZERO) { totalAmount, transaction -> totalAmount + transaction.amount } }
}