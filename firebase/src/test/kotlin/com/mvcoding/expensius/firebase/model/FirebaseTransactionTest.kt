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
    val tagsCache = mapOf(aTag().let { it.tagId.id to it }, aTag().let { it.tagId.id to it }, aTag().let { it.tagId.id to it })
    val tagsWithinCache = tagsCache.values
    val someTagsOutsideCache = tagsWithinCache.plus(aTag())
    val tagsOutsideCache = listOf(aTag())

    @Test
    fun `returns noTransaction when required fields ar null or empty`() {
        expect that aFirebaseTransaction().withId(null).toTransaction(modelState, tagsCache) isEqualTo noTransaction
        expect that aFirebaseTransaction().withId("").toTransaction(modelState, tagsCache) isEqualTo noTransaction
        expect that aFirebaseTransaction().withId(" ").toTransaction(modelState, tagsCache) isEqualTo noTransaction
    }

    @Test
    fun `returns Transaction with default values when not required fields ar null or empty`() {
        expect that aFirebaseTransaction().withTransactionType(null).toTransaction(modelState, tagsCache) isNotEqualTo noTransaction
        expect that aFirebaseTransaction().withTransactionType("unknown").toTransaction(modelState, tagsCache) isNotEqualTo noTransaction
        expect that aFirebaseTransaction().withTransactionType(null).toTransaction(modelState, tagsCache).transactionType isEqualTo EXPENSE
        expect that aFirebaseTransaction().withTransactionType("unknown").toTransaction(modelState, tagsCache).transactionType isEqualTo EXPENSE

        expect that aFirebaseTransaction().withTransactionState(null).toTransaction(modelState, tagsCache) isNotEqualTo noTransaction
        expect that aFirebaseTransaction().withTransactionState("unknown").toTransaction(modelState, tagsCache) isNotEqualTo noTransaction
        expect that aFirebaseTransaction().withTransactionState(null).toTransaction(modelState, tagsCache).transactionState isEqualTo PENDING
        expect that aFirebaseTransaction().withTransactionState("unknown").toTransaction(modelState, tagsCache).transactionState isEqualTo PENDING

        expect that aFirebaseTransaction().withTimestamp(null).toTransaction(modelState, tagsCache) isNotEqualTo noTransaction
        expect that aFirebaseTransaction().withTimestamp(null).toTransaction(modelState, tagsCache).timestamp isEqualTo noTimestamp

        expect that aFirebaseTransaction().withTimestampInverse(null).toTransaction(modelState, tagsCache) isNotEqualTo noTransaction

        expect that aFirebaseTransaction().withAmount(null).toTransaction(modelState, tagsCache) isNotEqualTo noTransaction
        expect that aFirebaseTransaction().withAmount("").toTransaction(modelState, tagsCache) isNotEqualTo noTransaction
        expect that aFirebaseTransaction().withAmount(" ").toTransaction(modelState, tagsCache) isNotEqualTo noTransaction
        val transactionAmount1 = aFirebaseTransaction().withAmount(null)
        val transactionAmount2 = aFirebaseTransaction().withAmount("")
        val transactionAmount3 = aFirebaseTransaction().withAmount(" ")
        expect that transactionAmount1.toTransaction(modelState, tagsCache).money isEqualTo Money(ZERO, Currency(transactionAmount1.currency!!))
        expect that transactionAmount2.toTransaction(modelState, tagsCache).money isEqualTo Money(ZERO, Currency(transactionAmount2.currency!!))
        expect that transactionAmount3.toTransaction(modelState, tagsCache).money isEqualTo Money(ZERO, Currency(transactionAmount3.currency!!))

        expect that aFirebaseTransaction().withCurrency(null).toTransaction(modelState, tagsCache) isNotEqualTo noTransaction
        expect that aFirebaseTransaction().withCurrency("").toTransaction(modelState, tagsCache) isNotEqualTo noTransaction
        expect that aFirebaseTransaction().withCurrency(" ").toTransaction(modelState, tagsCache) isNotEqualTo noTransaction
        val transactionCurrency1 = aFirebaseTransaction().withCurrency(null)
        val transactionCurrency2 = aFirebaseTransaction().withCurrency("")
        val transactionCurrency3 = aFirebaseTransaction().withCurrency(" ")
        expect that transactionCurrency1.toTransaction(modelState, tagsCache).money isEqualTo Money(BigDecimal(transactionCurrency1.amount), defaultCurrency())
        expect that transactionCurrency2.toTransaction(modelState, tagsCache).money isEqualTo Money(BigDecimal(transactionCurrency2.amount), defaultCurrency())
        expect that transactionCurrency3.toTransaction(modelState, tagsCache).money isEqualTo Money(BigDecimal(transactionCurrency3.amount), defaultCurrency())

        expect that aFirebaseTransaction().withTags(null).toTransaction(modelState, tagsCache) isNotEqualTo noTransaction
        expect that aFirebaseTransaction().withTags(someTagsOutsideCache).toTransaction(modelState, tagsCache) isNotEqualTo noTransaction
        expect that aFirebaseTransaction().withTags(tagsOutsideCache).toTransaction(modelState, tagsCache) isNotEqualTo noTransaction
        expect that aFirebaseTransaction().withTags(null).toTransaction(modelState, tagsCache).tags.size isEqualTo 0
        expect that aFirebaseTransaction().withTags(someTagsOutsideCache).toTransaction(modelState, tagsCache).tags containsOnly someTagsOutsideCache.dropLast(1)
        expect that aFirebaseTransaction().withTags(tagsOutsideCache).toTransaction(modelState, tagsCache).tags.size isEqualTo 0

        expect that aFirebaseTransaction().withNote(null).toTransaction(modelState, tagsCache) isNotEqualTo noTransaction
        expect that aFirebaseTransaction().withNote("").toTransaction(modelState, tagsCache) isNotEqualTo noTransaction
        expect that aFirebaseTransaction().withNote(" ").toTransaction(modelState, tagsCache) isNotEqualTo noTransaction
        expect that aFirebaseTransaction().withNote(null).toTransaction(modelState, tagsCache).note isEqualTo noNote
        expect that aFirebaseTransaction().withNote("").toTransaction(modelState, tagsCache).note isEqualTo noNote
        expect that aFirebaseTransaction().withNote(" ").toTransaction(modelState, tagsCache).note isEqualTo noNote
    }

    @Test
    fun `returns Transaction when all fields are set`() {
        val firebaseTransaction = aFirebaseTransaction().withTags(tagsWithinCache)

        val transaction = firebaseTransaction.toTransaction(modelState, tagsCache)

        expect that transaction.transactionId isEqualTo TransactionId(firebaseTransaction.id!!)
        expect that transaction.modelState isEqualTo modelState
        expect that transaction.transactionType isEqualTo TransactionType.valueOf(firebaseTransaction.transactionType!!)
        expect that transaction.transactionState isEqualTo TransactionState.valueOf(firebaseTransaction.transactionState!!)
        expect that transaction.timestamp isEqualTo Timestamp(firebaseTransaction.timestamp!!)
        expect that transaction.money isEqualTo Money(BigDecimal(firebaseTransaction.amount), Currency(firebaseTransaction.currency!!))
        expect that transaction.tags containsOnly tagsWithinCache
        expect that transaction.note isEqualTo Note(firebaseTransaction.note!!)
    }
}