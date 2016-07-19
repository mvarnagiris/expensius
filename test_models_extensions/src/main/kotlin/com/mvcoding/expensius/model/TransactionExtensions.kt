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

fun aTransactionId() = TransactionId(aStringId())
fun aTransactionType() = TransactionType.values().aRandomItem()
fun aTransactionState() = TransactionState.values().aRandomItem()
fun aNote() = Note(aString("note"))
fun aTransaction() = Transaction(
        aTransactionId(),
        NONE,
        aTransactionType(),
        aTransactionState(),
        aLongTimestamp(),
        aCurrency(),
        anAmount(),
        anAmount(),
        (0..anInt(5)).map { aTag() }.toSet(),
        aNote())

fun Transaction.withTags(tags: Set<Tag>) = copy(tags = tags)
fun Transaction.withTimestamp(timestamp: Long) = copy(timestamp = timestamp)