/*
 * Copyright (C) 2015 Mantas Varnagiris.
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

package com.mvcoding.expensius.feature.transaction

import com.mvcoding.expensius.ModelState
import com.mvcoding.expensius.ModelState.NONE
import com.mvcoding.expensius.feature.tag.Tag
import com.mvcoding.expensius.feature.transaction.TransactionState.CONFIRMED
import com.mvcoding.expensius.feature.transaction.TransactionType.EXPENSE
import java.io.Serializable
import java.lang.System.currentTimeMillis
import java.math.BigDecimal
import java.util.UUID.randomUUID

data class Transaction(
        val id: String = randomUUID().toString(),
        val modelState: ModelState = NONE,
        val transactionType: TransactionType,
        val transactionState: TransactionState,
        val timestamp: Long,
        val currency: Currency,
        val amount: BigDecimal,
        val tags: Set<Tag>,
        val note: String) : Serializable {

    companion object {
        fun transaction(currency: Currency, amount: BigDecimal) = Transaction(
                transactionType = EXPENSE,
                transactionState = CONFIRMED,
                timestamp = currentTimeMillis(),
                currency = currency,
                amount = amount,
                tags = emptySet(),
                note = "")
    }
}