/*
 * Copyright (C) 2018 Mantas Varnagiris.
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

package com.mvcoding.expensius.model.extensions

import com.mvcoding.expensius.model.*
import java.math.BigDecimal

fun aTransactionId() = TransactionId(aStringId())
fun aTransactionType() = TransactionType.values().aRandomItem()
fun aTransactionState() = TransactionState.values().aRandomItem()
fun aTimestamp() = Timestamp(aLongTimestamp())
fun anAmount() = BigDecimal(anInt(100))
fun aMoney() = Money(anAmount(), aCurrency())
fun aNote() = Note(aString("note"))
fun aTransaction() = Transaction(
        aTransactionId(),
        aModelState(),
        aTransactionType(),
        aTransactionState(),
        aTimestamp(),
        aMoney(),
        someTags(),
        aNote())

fun someTransactions() = listOf(aTransaction(), aTransaction(), aTransaction())
fun someMoneys() = aRange().map { aMoney() }
fun Money.withCurrency(currency: Currency) = copy(currency = currency)

fun aBasicTransaction() = BasicTransaction(
        aTransactionId(),
        aModelState(),
        aTransactionType(),
        aTransactionState(),
        aTimestamp(),
        aMoney(),
        someTagsIds(),
        aNote())

fun aCreateTransaction() = CreateTransaction(aTransactionType(), aTransactionState(), aTimestamp(), aMoney(), someTags(), aNote())

fun Transaction.withAmount(amount: Int) = withAmount(amount.toDouble())
fun Transaction.withAmount(amount: Double) = copy(money = money.copy(amount = BigDecimal.valueOf(amount)))
fun Transaction.withAmount(amount: BigDecimal) = copy(money = money.copy(amount = amount))
fun Transaction.withTimestamp(millis: Long) = copy(timestamp = Timestamp(millis))
fun Transaction.withTimestamp(timestamp: Timestamp) = copy(timestamp = timestamp)
fun Transaction.withTransactionType(transactionType: TransactionType) = copy(transactionType = transactionType)
fun Transaction.withTransactionState(transactionState: TransactionState) = copy(transactionState = transactionState)
fun Transaction.withTags(tags: Set<Tag>) = copy(tags = tags)
fun Transaction.withNote(note: String) = copy(note = Note(note))
fun Transaction.withCurrency(currency: Currency) = copy(money = money.withCurrency(currency))

fun Note.withText(text: String) = copy(text = text)