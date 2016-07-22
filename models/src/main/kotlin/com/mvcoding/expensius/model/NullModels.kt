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

import com.mvcoding.expensius.model.ModelState.NONE
import com.mvcoding.expensius.model.SubscriptionType.FREE
import com.mvcoding.expensius.model.TransactionState.CONFIRMED
import com.mvcoding.expensius.model.TransactionState.PENDING
import com.mvcoding.expensius.model.TransactionType.EXPENSE
import java.math.BigDecimal.ZERO

object NullModels {
    val noCurrency = Currency("")
    val noSettings = Settings(noCurrency, ReportPeriod.MONTH, ReportGroup.DAY, FREE)

    val noUserId = UserId("")
    val noAppUser = AppUser(noUserId, noSettings, emptySet())

    val noTagId = TagId("")
    val noTitle = Title("")
    val noColor = Color(0)
    val noOrder = Order(0)
    val noTag = Tag(noTagId, NONE, noTitle, noColor, noOrder)

    val noTransactionId = TransactionId("")
    val noTimestamp = Timestamp(0)
    val noMoney = Money(ZERO, noCurrency)
    val noNote = Note("")
    val noTransaction = Transaction(noTransactionId, NONE, EXPENSE, PENDING, noTimestamp, noMoney, emptySet(), noNote)

    fun newTransaction(timestamp: Timestamp, money: Money) = Transaction(
            noTransactionId,
            NONE,
            EXPENSE,
            CONFIRMED,
            timestamp,
            money,
            emptySet(),
            noNote)
}