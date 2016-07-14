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

import java.io.Serializable
import java.math.BigDecimal

data class TransactionId(val id: String) : Serializable
data class Note(val text: String) : Serializable

data class CreateTransaction(
        val transactionType: TransactionType,
        val transactionState: TransactionState,
        val timestamp: Long,
        val currency: Currency,
        val exchangeRate: BigDecimal,
        val amount: BigDecimal,
        val tags: Set<Tag>,
        val note: Note) : Serializable

data class Transaction(
        val transactionId: TransactionId,
        val modelState: ModelState,
        val transactionType: TransactionType,
        val transactionState: TransactionState,
        val timestamp: Long,
        val currency: Currency,
        val exchangeRate: BigDecimal,
        val amount: BigDecimal,
        val tags: Set<Tag>,
        val note: Note) : Serializable {

    fun withCurrency(currency: Currency) = copy(currency = currency)
    fun withAmount(amount: BigDecimal) = copy(amount = amount)
    fun withModelState(modelState: ModelState) = copy(modelState = modelState)
    fun getAmountForCurrency(currency: Currency): BigDecimal = if (this.currency == currency) amount else amount.multiply(exchangeRate)
}

enum class TransactionState {
    CONFIRMED, PENDING
}

enum class TransactionType {
    EXPENSE, INCOME
}