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

import com.mvcoding.expensius.feature.tag.aTag
import com.mvcoding.expensius.feature.transaction.TransactionState.PENDING
import com.mvcoding.expensius.feature.transaction.TransactionType.INCOME
import com.mvcoding.expensius.model.ModelState.ARCHIVED
import com.mvcoding.expensius.model.ModelState.NONE
import com.mvcoding.expensius.model.Tag
import com.mvcoding.expensius.model.Transaction
import com.mvcoding.expensius.paging.Page
import com.mvcoding.expensius.paging.PageResult
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.*
import rx.Observable
import rx.lang.kotlin.PublishSubject
import java.math.BigDecimal
import java.math.BigDecimal.TEN

class TransactionPresenterTest {
    val transactionTypeSubject = PublishSubject<TransactionType>()
    val transactionStateSubject = PublishSubject<TransactionState>()
    val timestampSubject = PublishSubject<Long>()
    val currencySubject = PublishSubject<Currency>()
    val amountSubject = PublishSubject<BigDecimal>()
    val tagsSubject = PublishSubject<Set<Tag>>()
    val noteSubject = PublishSubject<String>()
    val toggleArchiveSubject = PublishSubject<Unit>()
    val saveSubject = PublishSubject<Unit>()
    val view = mock(TransactionPresenter.View::class.java)
    val transaction = aTransaction()
    val transactionsProvider = TransactionsProviderForTest()
    val presenter = TransactionPresenter(transaction, transactionsProvider)

    @Before
    fun setUp() {
        given(view.onTransactionTypeChanged()).willReturn(transactionTypeSubject)
        given(view.onTransactionStateChanged()).willReturn(transactionStateSubject)
        given(view.onTimestampChanged()).willReturn(timestampSubject)
        given(view.onCurrencyChanged()).willReturn(currencySubject)
        given(view.onAmountChanged()).willReturn(amountSubject)
        given(view.onTagsChanged()).willReturn(tagsSubject)
        given(view.onNoteChanged()).willReturn(noteSubject)
        given(view.onToggleArchive()).willReturn(toggleArchiveSubject)
        given(view.onSave()).willReturn(saveSubject)
    }

    @Test
    fun showsInitialValues() {
        presenter.onViewAttached(view)

        verify(view).showTransactionType(transaction.transactionType)
        verify(view).showTransactionState(transaction.transactionState)
        verify(view).showTimestamp(transaction.timestamp)
        verify(view).showCurrency(transaction.currency)
        verify(view).showAmount(transaction.amount)
        verify(view).showTags(transaction.tags)
        verify(view).showNote(transaction.note)
        verify(view).showModelState(transaction.modelState)
    }

    @Test
    fun showsUpdatedValues() {
        val tags = setOf(aTag(), aTag())
        val currency = Currency("EUR")
        presenter.onViewAttached(view)

        updateTransactionType(INCOME)
        updateTransactionState(PENDING)
        updateTimestamp(1)
        updateCurrency(currency)
        updateAmount(TEN)
        updateTags(tags)
        updateNote("Updated note")

        verify(view).showTransactionType(INCOME)
        verify(view).showTransactionState(PENDING)
        verify(view).showTimestamp(1)
        verify(view).showCurrency(currency)
        verify(view).showAmount(TEN)
        verify(view).showTags(tags)
        verify(view).showNote("Updated note")
    }

    @Test
    fun showsUpdatedValuesAfterReattach() {
        val tags = setOf(aTag(), aTag())
        val currency = Currency("EUR")
        presenter.onViewAttached(view)

        updateTransactionType(INCOME)
        updateTransactionState(PENDING)
        updateTimestamp(1)
        updateCurrency(currency)
        updateAmount(TEN)
        updateTags(tags)
        updateNote("Updated note")
        presenter.onViewDetached(view)
        presenter.onViewAttached(view)

        verify(view, times(2)).showTransactionType(INCOME)
        verify(view, times(2)).showTransactionState(PENDING)
        verify(view, times(2)).showTimestamp(1)
        verify(view, times(2)).showCurrency(currency)
        verify(view, times(2)).showAmount(TEN)
        verify(view, times(2)).showTags(tags)
        verify(view, times(2)).showNote("Updated note")
    }

    @Test
    fun savesTransactionAndDisplaysResult() {
        presenter.onViewAttached(view)

        save()

        assertThat(transactionsProvider.lastSavedTransactions?.first(), equalTo(transaction))
    }

    @Test
    fun archiveIsDisabledForNewTransaction() {
        val presenter = TransactionPresenter(aNewTransaction(), transactionsProvider)

        presenter.onViewAttached(view)

        verify(view).showArchiveEnabled(false)
    }

    @Test
    fun archiveIsEnabledForExistingTransaction() {
        presenter.onViewAttached(view)

        verify(view).showArchiveEnabled(true)
    }

    @Test
    fun archivesTransactionAndDisplaysResult() {
        val archivedTransaction = transaction.withModelState(ARCHIVED)
        presenter.onViewAttached(view)

        toggleArchive()

        assertThat(transactionsProvider.lastSavedTransactions?.first(), equalTo(archivedTransaction))
        verify(view).displayResult(archivedTransaction)
    }

    @Test
    fun restoresTagAndDisplaysResult() {
        val archivedTransaction = transaction.withModelState(ARCHIVED)
        val restoredTransaction = transaction.withModelState(NONE)
        val presenter = TransactionPresenter(archivedTransaction, transactionsProvider)
        presenter.onViewAttached(view)

        toggleArchive()

        assertThat(transactionsProvider.lastSavedTransactions?.first(), equalTo(restoredTransaction))
        verify(view).displayResult(restoredTransaction)
    }

    private fun toggleArchive() {
        toggleArchiveSubject.onNext(Unit)
    }

    fun updateTransactionType(transactionType: TransactionType) {
        transactionTypeSubject.onNext(transactionType)
    }

    fun updateTransactionState(transactionState: TransactionState) {
        transactionStateSubject.onNext(transactionState)
    }

    fun updateTimestamp(timestamp: Long) {
        timestampSubject.onNext(timestamp)
    }

    fun updateCurrency(currency: Currency) {
        currencySubject.onNext(currency)
    }

    fun updateAmount(amount: BigDecimal) {
        amountSubject.onNext(amount)
    }

    fun updateTags(tags: Set<Tag>) {
        tagsSubject.onNext(tags)
    }

    fun updateNote(note: String) {
        noteSubject.onNext(note)
    }

    fun save() {
        saveSubject.onNext(Unit)
    }

    class TransactionsProviderForTest : TransactionsProvider {
        var lastSavedTransactions: Set<Transaction>? = null

        override fun transactions(pages: Observable<Page>, transactionsFilter: TransactionsFilter): Observable<PageResult<Transaction>> {
            throw UnsupportedOperationException()
        }

        override fun transactions(transactionsFilter: TransactionsFilter): Observable<List<Transaction>> {
            throw UnsupportedOperationException()
        }

        override fun save(transactions: Set<Transaction>) {
            lastSavedTransactions = transactions
        }
    }
}