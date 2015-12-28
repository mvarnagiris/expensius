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

import com.mvcoding.expensius.feature.tag.Tag
import com.mvcoding.expensius.feature.tag.aTag
import com.mvcoding.expensius.feature.transaction.TransactionState.PENDING
import com.mvcoding.expensius.feature.transaction.TransactionType.INCOME
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.*
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
    val saveSubject = PublishSubject<Unit>()
    val view = mock(TransactionPresenter.View::class.java)
    val transaction = aTransaction()
    val transactionsCache = mock(TransactionsCache::class.java)
    val presenter = TransactionPresenter(transaction, transactionsCache)

    @Before
    fun setUp() {
        given(view.onTransactionTypeChanged()).willReturn(transactionTypeSubject)
        given(view.onTransactionStateChanged()).willReturn(transactionStateSubject)
        given(view.onTimestampChanged()).willReturn(timestampSubject)
        given(view.onCurrencyChanged()).willReturn(currencySubject)
        given(view.onAmountChanged()).willReturn(amountSubject)
        given(view.onTagsChanged()).willReturn(tagsSubject)
        given(view.onNoteChanged()).willReturn(noteSubject)
        given(view.onSave()).willReturn(saveSubject)
    }

    @Test
    fun showsInitialValues() {
        presenter.onAttachView(view)

        verify(view).showTransactionType(transaction.transactionType)
        verify(view).showTransactionState(transaction.transactionState)
        verify(view).showTimestamp(transaction.timestamp)
        verify(view).showCurrency(transaction.currency)
        verify(view).showAmount(transaction.amount)
        verify(view).showTags(transaction.tags)
        verify(view).showNote(transaction.note)
    }

    @Test
    fun showsUpdatedValues() {
        val tags = setOf(aTag(), aTag())
        val currency = Currency("EUR")
        presenter.onAttachView(view)

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
        presenter.onAttachView(view)

        updateTransactionType(INCOME)
        updateTransactionState(PENDING)
        updateTimestamp(1)
        updateCurrency(currency)
        updateAmount(TEN)
        updateTags(tags)
        updateNote("Updated note")
        presenter.onDetachView(view)
        presenter.onAttachView(view)

        verify(view, times(2)).showTransactionType(INCOME)
        verify(view, times(2)).showTransactionState(PENDING)
        verify(view, times(2)).showTimestamp(1)
        verify(view, times(2)).showCurrency(currency)
        verify(view, times(2)).showAmount(TEN)
        verify(view, times(2)).showTags(tags)
        verify(view, times(2)).showNote("Updated note")
    }

    @Test
    fun savesTransactionAndStartsResult() {
        presenter.onAttachView(view)

        save()

        verify(transactionsCache).save(setOf(transaction))
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
}