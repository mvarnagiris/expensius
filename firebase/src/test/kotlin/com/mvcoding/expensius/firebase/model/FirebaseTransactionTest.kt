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

package com.mvcoding.expensius.firebase.model

import com.memoizr.assertk.expect
import com.mvcoding.expensius.firebase.extensions.*
import com.mvcoding.expensius.model.*
import com.mvcoding.expensius.model.NullModels.noBasicTransaction
import com.mvcoding.expensius.model.NullModels.noNote
import com.mvcoding.expensius.model.NullModels.noTimestamp
import com.mvcoding.expensius.model.TransactionState.PENDING
import com.mvcoding.expensius.model.TransactionType.EXPENSE
import com.mvcoding.expensius.model.extensions.aCreateTransaction
import com.mvcoding.expensius.model.extensions.aModelState
import com.mvcoding.expensius.model.extensions.aStringId
import com.mvcoding.expensius.model.extensions.aTransaction
import org.junit.Test
import java.math.BigDecimal
import java.math.BigDecimal.ZERO

class FirebaseTransactionTest {

    val modelState = aModelState()

    @Test
    fun `returns noTransaction when required fields ar null or empty`() {
        expect that aFirebaseTransaction().withId(null).toBasicTransaction(modelState) isEqualTo noBasicTransaction
        expect that aFirebaseTransaction().withId("").toBasicTransaction(modelState) isEqualTo noBasicTransaction
        expect that aFirebaseTransaction().withId(" ").toBasicTransaction(modelState) isEqualTo noBasicTransaction
    }

    @Test
    fun `returns Transaction with default values when not required fields ar null or empty`() {
        expect that aFirebaseTransaction().withTransactionType(null).toBasicTransaction(modelState) isNotEqualTo noBasicTransaction
        expect that aFirebaseTransaction().withTransactionType("unknown").toBasicTransaction(modelState) isNotEqualTo noBasicTransaction
        expect that aFirebaseTransaction().withTransactionType(null).toBasicTransaction(modelState).transactionType isEqualTo EXPENSE
        expect that aFirebaseTransaction().withTransactionType("unknown").toBasicTransaction(modelState).transactionType isEqualTo EXPENSE

        expect that aFirebaseTransaction().withTransactionState(null).toBasicTransaction(modelState) isNotEqualTo noBasicTransaction
        expect that aFirebaseTransaction().withTransactionState("unknown").toBasicTransaction(modelState) isNotEqualTo noBasicTransaction
        expect that aFirebaseTransaction().withTransactionState(null).toBasicTransaction(modelState).transactionState isEqualTo PENDING
        expect that aFirebaseTransaction().withTransactionState("unknown").toBasicTransaction(modelState).transactionState isEqualTo PENDING

        expect that aFirebaseTransaction().withTimestamp(null).toBasicTransaction(modelState) isNotEqualTo noBasicTransaction
        expect that aFirebaseTransaction().withTimestamp(null).toBasicTransaction(modelState).timestamp isEqualTo noTimestamp

        expect that aFirebaseTransaction().withTimestampInverse(null).toBasicTransaction(modelState) isNotEqualTo noBasicTransaction

        expect that aFirebaseTransaction().withAmount(null).toBasicTransaction(modelState) isNotEqualTo noBasicTransaction
        expect that aFirebaseTransaction().withAmount("").toBasicTransaction(modelState) isNotEqualTo noBasicTransaction
        expect that aFirebaseTransaction().withAmount(" ").toBasicTransaction(modelState) isNotEqualTo noBasicTransaction
        val transactionAmount1 = aFirebaseTransaction().withAmount(null)
        val transactionAmount2 = aFirebaseTransaction().withAmount("")
        val transactionAmount3 = aFirebaseTransaction().withAmount(" ")
        expect that transactionAmount1.toBasicTransaction(modelState).money isEqualTo Money(ZERO, Currency(transactionAmount1.currency!!))
        expect that transactionAmount2.toBasicTransaction(modelState).money isEqualTo Money(ZERO, Currency(transactionAmount2.currency!!))
        expect that transactionAmount3.toBasicTransaction(modelState).money isEqualTo Money(ZERO, Currency(transactionAmount3.currency!!))

        expect that aFirebaseTransaction().withCurrency(null).toBasicTransaction(modelState) isNotEqualTo noBasicTransaction
        expect that aFirebaseTransaction().withCurrency("").toBasicTransaction(modelState) isNotEqualTo noBasicTransaction
        expect that aFirebaseTransaction().withCurrency(" ").toBasicTransaction(modelState) isNotEqualTo noBasicTransaction
        val transactionCurrency1 = aFirebaseTransaction().withCurrency(null)
        val transactionCurrency2 = aFirebaseTransaction().withCurrency("")
        val transactionCurrency3 = aFirebaseTransaction().withCurrency(" ")
        expect that transactionCurrency1.toBasicTransaction(modelState).money isEqualTo Money(BigDecimal(transactionCurrency1.amount), defaultCurrency())
        expect that transactionCurrency2.toBasicTransaction(modelState).money isEqualTo Money(BigDecimal(transactionCurrency2.amount), defaultCurrency())
        expect that transactionCurrency3.toBasicTransaction(modelState).money isEqualTo Money(BigDecimal(transactionCurrency3.amount), defaultCurrency())

        expect that aFirebaseTransaction().withTagIds(null).toBasicTransaction(modelState) isNotEqualTo noBasicTransaction
        expect that aFirebaseTransaction().withTagIds(null).toBasicTransaction(modelState).tagIds.size isEqualTo 0

        expect that aFirebaseTransaction().withNote(null).toBasicTransaction(modelState) isNotEqualTo noBasicTransaction
        expect that aFirebaseTransaction().withNote("").toBasicTransaction(modelState) isNotEqualTo noBasicTransaction
        expect that aFirebaseTransaction().withNote(" ").toBasicTransaction(modelState) isNotEqualTo noBasicTransaction
        expect that aFirebaseTransaction().withNote(null).toBasicTransaction(modelState).note isEqualTo noNote
        expect that aFirebaseTransaction().withNote("").toBasicTransaction(modelState).note isEqualTo noNote
        expect that aFirebaseTransaction().withNote(" ").toBasicTransaction(modelState).note isEqualTo noNote
    }

    @Test
    fun `returns Transaction when all fields are set`() {
        val firebaseTransaction = aFirebaseTransaction()

        val transaction = firebaseTransaction.toBasicTransaction(modelState)

        expect that transaction.transactionId isEqualTo TransactionId(firebaseTransaction.id!!)
        expect that transaction.modelState isEqualTo modelState
        expect that transaction.transactionType isEqualTo TransactionType.valueOf(firebaseTransaction.transactionType!!)
        expect that transaction.transactionState isEqualTo TransactionState.valueOf(firebaseTransaction.transactionState!!)
        expect that transaction.timestamp isEqualTo Timestamp(firebaseTransaction.timestamp!!)
        expect that transaction.money isEqualTo Money(BigDecimal(firebaseTransaction.amount), Currency(firebaseTransaction.currency!!))
        expect that transaction.tagIds containsOnly firebaseTransaction.tags!!.map(::TagId)
        expect that transaction.note isEqualTo Note(firebaseTransaction.note!!)
    }

    @Test
    fun `converts CreateTransaction to FirebaseTransaction`() {
        val id = aStringId()
        val createTransaction = aCreateTransaction()

        val firebaseTransaction = createTransaction.toFirebaseTransaction(id)

        expect that firebaseTransaction.id isEqualTo id
        expect that firebaseTransaction.transactionType isEqualTo createTransaction.transactionType.name
        expect that firebaseTransaction.transactionState isEqualTo createTransaction.transactionState.name
        expect that firebaseTransaction.timestamp isEqualTo createTransaction.timestamp.millis
        expect that firebaseTransaction.timestampInverse isEqualTo -createTransaction.timestamp.millis
        expect that firebaseTransaction.amount isEqualTo createTransaction.money.amount.toPlainString()
        expect that firebaseTransaction.currency isEqualTo createTransaction.money.currency.code
        expect that firebaseTransaction.tags isEqualTo createTransaction.tags.map { it.tagId.id }
        expect that firebaseTransaction.note isEqualTo createTransaction.note.text
    }

    @Test
    fun `converts Transaction to firebase map`() {
        val transaction = aTransaction()

        val firebaseMap = transaction.toFirebaseMap()

        expect that firebaseMap.keys containsOnly setOf("id", "transactionType", "transactionState", "timestamp", "timestampInverse", "amount", "currency", "tags", "note")
        expect that firebaseMap["id"] isEqualTo transaction.transactionId.id
        expect that firebaseMap["transactionType"] isEqualTo transaction.transactionType.name
        expect that firebaseMap["transactionState"] isEqualTo transaction.transactionState.name
        expect that firebaseMap["timestamp"] isEqualTo transaction.timestamp.millis
        expect that firebaseMap["timestampInverse"] isEqualTo -transaction.timestamp.millis
        expect that firebaseMap["amount"] isEqualTo transaction.money.amount.toPlainString()
        expect that firebaseMap["currency"] isEqualTo transaction.money.currency.code
        expect that firebaseMap["tags"] isEqualTo transaction.tags.map { it.tagId.id }
        expect that firebaseMap["note"] isEqualTo transaction.note.text
    }
}