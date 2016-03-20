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

package com.mvcoding.expensius.model

import com.mvcoding.expensius.feature.transaction.TransactionState
import com.mvcoding.expensius.feature.transaction.TransactionState.CONFIRMED
import com.mvcoding.expensius.feature.transaction.TransactionType
import com.mvcoding.expensius.feature.transaction.TransactionType.EXPENSE
import com.mvcoding.expensius.model.ModelState.NONE
import java.lang.System.currentTimeMillis
import java.math.BigDecimal
import java.math.BigDecimal.ONE
import java.math.BigDecimal.ZERO

data class Transaction(
        override val id: String = "",
        override val modelState: ModelState = NONE,
        val transactionType: TransactionType = EXPENSE,
        val transactionState: TransactionState = CONFIRMED,
        val timestamp: Long = currentTimeMillis(),
        val currency: Currency,
        val exchangeRate: BigDecimal = ONE,
        val amount: BigDecimal = ZERO,
        val tags: Set<Tag> = emptySet(),
        val note: String = "") : Model {

    fun withModelState(modelState: ModelState) = copy(modelState = modelState)
    fun getAmountForCurrency(currency: Currency) = if (this.currency == currency) amount else amount.multiply(exchangeRate)
}