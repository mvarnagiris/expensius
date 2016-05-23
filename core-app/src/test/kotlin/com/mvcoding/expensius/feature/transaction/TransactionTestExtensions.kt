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

package com.mvcoding.expensius.feature.transaction

import com.mvcoding.expensius.feature.tag.someTags
import com.mvcoding.expensius.feature.transaction.TransactionState.CONFIRMED
import com.mvcoding.expensius.feature.transaction.TransactionType.EXPENSE
import com.mvcoding.expensius.model.Currency
import com.mvcoding.expensius.model.ModelState.NONE
import com.mvcoding.expensius.model.Tag
import com.mvcoding.expensius.model.Transaction
import com.mvcoding.expensius.model.generateModelId
import java.lang.System.currentTimeMillis
import java.math.BigDecimal
import java.math.BigDecimal.ONE

fun aTransaction() = Transaction(
        generateModelId(),
        NONE,
        EXPENSE,
        CONFIRMED,
        currentTimeMillis(),
        aCurrency(),
        ONE,
        ONE,
        someTags(),
        "note")

fun aNewTransaction() = Transaction(currency = aCurrency())
fun Transaction.withTimestamp(timestamp: Long) = copy(timestamp = timestamp)
fun Transaction.withCurrency(currency: Currency) = copy(currency = currency)
fun Transaction.withCurrency(currency: String) = copy(currency = Currency(currency))
fun Transaction.withExchangeRate(exchangeRate: BigDecimal) = copy(exchangeRate = exchangeRate)
fun Transaction.withAmount(amount: BigDecimal) = copy(amount = amount)
fun Transaction.withTags(vararg tags: Tag) = copy(tags = setOf(*tags))
fun Transaction.withTransactionType(transactionType: TransactionType) = copy(transactionType = transactionType)