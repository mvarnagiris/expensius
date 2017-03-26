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
import com.mvcoding.expensius.aStringId
import com.mvcoding.expensius.firebase.extensions.*
import com.mvcoding.expensius.model.*
import com.mvcoding.expensius.model.NullModels.noNote
import com.mvcoding.expensius.model.NullModels.noTimestamp
import com.mvcoding.expensius.model.NullModels.noTransaction
import com.mvcoding.expensius.model.TransactionState.PENDING
import com.mvcoding.expensius.model.TransactionType.EXPENSE
import org.junit.Test
import java.math.BigDecimal
import java.math.BigDecimal.ZERO

class FirebaseTransactionTest {

    val modelState = aModelState()

    @Test
    fun `returns noTransaction when required fields ar null or empty`() {
        expect that aFirebaseTransaction().withId(null).toTransaction(modelState) isEqualTo noTransaction
        expect that aFirebaseTransaction().withId("").toTransaction(modelState) isEqualTo noTransaction
        expect that aFirebaseTransaction().withId(" ").toTransaction(modelState) isEqualTo noTransaction
    }

    @Test
    fun `returns Transaction with default values when not required fields ar null or empty`() {
        expect that aFirebaseTransaction().withTransactionType(null).toTransaction(modelState) isNotEqualTo noTransaction
        expect that aFirebaseTransaction().withTransactionType("unknown").toTransaction(modelState) isNotEqualTo noTransaction
        expect that aFirebaseTransaction().withTransactionType(null).toTransaction(modelState).transactionType isEqualTo EXPENSE
        expect that aFirebaseTransaction().withTransactionType("unknown").toTransaction(modelState).transactionType isEqualTo EXPENSE

        expect that aFirebaseTransaction().withTransactionState(null).toTransaction(modelState) isNotEqualTo noTransaction
        expect that aFirebaseTransaction().withTransactionState("unknown").toTransaction(modelState) isNotEqualTo noTransaction
        expect that aFirebaseTransaction().withTransactionState(null).toTransaction(modelState).transactionState isEqualTo PENDING
        expect that aFirebaseTransaction().withTransactionState("unknown").toTransaction(modelState).transactionState isEqualTo PENDING

        expect that aFirebaseTransaction().withTimestamp(null).toTransaction(modelState) isNotEqualTo noTransaction
        expect that aFirebaseTransaction().withTimestamp(null).toTransaction(modelState).timestamp isEqualTo noTimestamp

        expect that aFirebaseTransaction().withTimestampInverse(null).toTransaction(modelState) isNotEqualTo noTransaction

        expect that aFirebaseTransaction().withAmount(null).toTransaction(modelState) isNotEqualTo noTransaction
        expect that aFirebaseTransaction().withAmount("").toTransaction(modelState) isNotEqualTo noTransaction
        expect that aFirebaseTransaction().withAmount(" ").toTransaction(modelState) isNotEqualTo noTransaction
        val transactionAmount1 = aFirebaseTransaction().withAmount(null)
        val transactionAmount2 = aFirebaseTransaction().withAmount("")
        val transactionAmount3 = aFirebaseTransaction().withAmount(" ")
        expect that transactionAmount1.toTransaction(modelState).money isEqualTo Money(ZERO, Currency(transactionAmount1.currency!!))
        expect that transactionAmount2.toTransaction(modelState).money isEqualTo Money(ZERO, Currency(transactionAmount2.currency!!))
        expect that transactionAmount3.toTransaction(modelState).money isEqualTo Money(ZERO, Currency(transactionAmount3.currency!!))

        expect that aFirebaseTransaction().withCurrency(null).toTransaction(modelState) isNotEqualTo noTransaction
        expect that aFirebaseTransaction().withCurrency("").toTransaction(modelState) isNotEqualTo noTransaction
        expect that aFirebaseTransaction().withCurrency(" ").toTransaction(modelState) isNotEqualTo noTransaction
        val transactionCurrency1 = aFirebaseTransaction().withCurrency(null)
        val transactionCurrency2 = aFirebaseTransaction().withCurrency("")
        val transactionCurrency3 = aFirebaseTransaction().withCurrency(" ")
        expect that transactionCurrency1.toTransaction(modelState).money isEqualTo Money(BigDecimal(transactionCurrency1.amount), defaultCurrency())
        expect that transactionCurrency2.toTransaction(modelState).money isEqualTo Money(BigDecimal(transactionCurrency2.amount), defaultCurrency())
        expect that transactionCurrency3.toTransaction(modelState).money isEqualTo Money(BigDecimal(transactionCurrency3.amount), defaultCurrency())

        expect that aFirebaseTransaction().withTagIds(null).toTransaction(modelState) isNotEqualTo noTransaction
        expect that aFirebaseTransaction().withTagIds(null).toTransaction(modelState).tagIds.size isEqualTo 0

        expect that aFirebaseTransaction().withNote(null).toTransaction(modelState) isNotEqualTo noTransaction
        expect that aFirebaseTransaction().withNote("").toTransaction(modelState) isNotEqualTo noTransaction
        expect that aFirebaseTransaction().withNote(" ").toTransaction(modelState) isNotEqualTo noTransaction
        expect that aFirebaseTransaction().withNote(null).toTransaction(modelState).note isEqualTo noNote
        expect that aFirebaseTransaction().withNote("").toTransaction(modelState).note isEqualTo noNote
        expect that aFirebaseTransaction().withNote(" ").toTransaction(modelState).note isEqualTo noNote
    }

    @Test
    fun `returns Transaction when all fields are set`() {
        val firebaseTransaction = aFirebaseTransaction()

        val transaction = firebaseTransaction.toTransaction(modelState)

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
        expect that firebaseTransaction.tags isEqualTo createTransaction.tagIds.map { it.id }
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
        expect that firebaseMap["tags"] isEqualTo transaction.tagIds.map { it.id }
        expect that firebaseMap["note"] isEqualTo transaction.note.text
    }
}