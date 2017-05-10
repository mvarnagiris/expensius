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

package com.mvcoding.expensius.model

import com.memoizr.assertk.expect
import org.junit.Test

class LocalFilterTest {

    @Test
    fun `filters transactions based on transaction type`() {
        val transactions = listOf(
                aTransaction().withTransactionType(TransactionType.EXPENSE),
                aTransaction().withTransactionType(TransactionType.INCOME))

        val filteredTransactions = aLocalFilter().withNoFilters().withTransactionType(TransactionType.INCOME).filter(transactions)

        expect that filteredTransactions containsOnly transactions.takeLast(1)
    }

    @Test
    fun `filters transactions based on transaction state`() {
        val transactions = listOf(
                aTransaction().withTransactionState(TransactionState.CONFIRMED),
                aTransaction().withTransactionState(TransactionState.PENDING))

        val filteredTransactions = aLocalFilter().withNoFilters().withTransactionState(TransactionState.PENDING).filter(transactions)

        expect that filteredTransactions containsOnly transactions.takeLast(1)
    }

    @Test
    fun `filters transactions based on tags`() {
        val filterTags = setOf(aTag().withTitle("a"), aTag().withTitle("b"))
        val transactions = listOf(
                aTransaction().withTags(emptySet()),
                aTransaction().withTags(setOf(aTag().withTitle("c"))),
                aTransaction().withTags(filterTags),
                aTransaction().withTags(filterTags.plus(aTag())))

        val filteredTransactions = aLocalFilter().withNoFilters().withTags(filterTags).filter(transactions)

        expect that filteredTransactions containsOnly transactions.takeLast(2)
    }

    @Test
    fun `filters transactions based on note`() {
        val transactions = listOf(
                aTransaction().withNote(""),
                aTransaction().withNote("c"),
                aTransaction().withNote("a"),
                aTransaction().withNote("A"),
                aTransaction().withNote("aa"),
                aTransaction().withNote("ab"),
                aTransaction().withNote("ba"),
                aTransaction().withNote("bab"),
                aTransaction().withNote("b a b"),
                aTransaction().withNote("b ab"),
                aTransaction().withNote("b ba"),
                aTransaction().withNote("b bab"))

        val filteredTransactions = aLocalFilter().withNoFilters().withNote(aNote().withText("a")).filter(transactions)

        expect that filteredTransactions containsOnly transactions.drop(2)
    }

    @Test
    fun `can combine filters`() {
        val filterTags = setOf(aTag().withTitle("a"))
        val transactions = listOf(
                aTransaction()
                        .withTransactionType(TransactionType.EXPENSE)
                        .withTransactionState(TransactionState.CONFIRMED)
                        .withTags(setOf(aTag().withTitle("b")))
                        .withNote(""),
                aTransaction()
                        .withTransactionType(TransactionType.INCOME)
                        .withTransactionState(TransactionState.PENDING)
                        .withTags(filterTags)
                        .withNote("a"))

        val filteredTransactions = aLocalFilter()
                .withTransactionType(TransactionType.INCOME)
                .withTransactionState(TransactionState.PENDING)
                .withTags(filterTags)
                .withNote(aNote().withText("a"))
                .filter(transactions)

        expect that filteredTransactions containsOnly transactions.takeLast(1)
    }
}