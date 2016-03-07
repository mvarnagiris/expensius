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

import com.mvcoding.expensius.Settings
import com.mvcoding.expensius.feature.currency.CurrenciesProvider
import com.mvcoding.expensius.feature.tag.aTag
import com.mvcoding.expensius.feature.transaction.TransactionState.PENDING
import com.mvcoding.expensius.feature.transaction.TransactionType.INCOME
import com.mvcoding.expensius.model.Currency
import com.mvcoding.expensius.model.ModelState.ARCHIVED
import com.mvcoding.expensius.model.ModelState.NONE
import com.mvcoding.expensius.model.Tag
import com.nhaarman.mockito_kotlin.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.times
import rx.lang.kotlin.PublishSubject
import rx.observers.TestSubscriber.create
import java.math.BigDecimal
import java.math.BigDecimal.TEN

class TransactionPresenterTest {
    val transactionTypeSubject = PublishSubject<TransactionType>()
    val transactionStateSubject = PublishSubject<TransactionState>()
    val timestampSubject = PublishSubject<Long>()
    val requestCurrencyChangeSubject = PublishSubject<Unit>()
    val currencySubject = PublishSubject<Currency>()
    val exchangeRateSubject = PublishSubject<BigDecimal>()
    val amountSubject = PublishSubject<BigDecimal>()
    val tagsSubject = PublishSubject<Set<Tag>>()
    val noteSubject = PublishSubject<String>()
    val toggleArchiveSubject = PublishSubject<Unit>()
    val saveSubject = PublishSubject<Unit>()
    val view = mock<TransactionPresenter.View>()
    val transaction = aTransaction()
    val transactionsProvider = mock<TransactionsProvider>()
    val currenciesProvider = CurrenciesProvider()
    val settings = mock<Settings>()
    val presenter = TransactionPresenter(transaction, transactionsProvider, currenciesProvider, settings)

    @Before
    fun setUp() {
        whenever(view.onTransactionTypeChanged()).thenReturn(transactionTypeSubject)
        whenever(view.onTransactionStateChanged()).thenReturn(transactionStateSubject)
        whenever(view.onTimestampChanged()).thenReturn(timestampSubject)
        whenever(view.onCurrencyChangeRequested()).thenReturn(requestCurrencyChangeSubject)
        whenever(view.requestCurrency(any())).thenReturn(currencySubject)
        whenever(view.onExchangeRateChanged()).thenReturn(exchangeRateSubject)
        whenever(view.onAmountChanged()).thenReturn(amountSubject)
        whenever(view.onTagsChanged()).thenReturn(tagsSubject)
        whenever(view.onNoteChanged()).thenReturn(noteSubject)
        whenever(view.onToggleArchive()).thenReturn(toggleArchiveSubject)
        whenever(view.onSave()).thenReturn(saveSubject)
    }

    @Test
    fun showsInitialValues() {
        presenter.onViewAttached(view)

        verify(view).showTransactionType(transaction.transactionType)
        verify(view).showTransactionState(transaction.transactionState)
        verify(view).showTimestamp(transaction.timestamp)
        verify(view).showCurrency(transaction.currency)
        verify(view).showExchangeRate(transaction.exchangeRate)
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
        requestCurrencyChange()
        updateCurrency(currency)
        updateExchangeRate(TEN)
        updateAmount(TEN)
        updateTags(tags)
        updateNote("Updated note")

        verify(view).showTransactionType(INCOME)
        verify(view).showTransactionState(PENDING)
        verify(view).showTimestamp(1)
        verify(view).requestCurrency(allCurrencies())
        verify(view).showCurrency(currency)
        verify(view).showExchangeRate(TEN)
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
        requestCurrencyChange()
        updateCurrency(currency)
        updateExchangeRate(TEN)
        updateAmount(TEN)
        updateTags(tags)
        updateNote("Updated note")
        presenter.onViewDetached(view)
        presenter.onViewAttached(view)

        verify(view, times(2)).showTransactionType(INCOME)
        verify(view, times(2)).showTransactionState(PENDING)
        verify(view, times(2)).showTimestamp(1)
        verify(view, times(2)).showCurrency(currency)
        verify(view, times(2)).showExchangeRate(TEN)
        verify(view, times(2)).showAmount(TEN)
        verify(view, times(2)).showTags(tags)
        verify(view, times(2)).showNote("Updated note")
    }

    @Test
    fun savesTransactionAndDisplaysResult() {
        presenter.onViewAttached(view)

        save()

        verify(transactionsProvider).save(argThat { first() == transaction })
    }

    @Test
    fun archiveIsDisabledForNewTransaction() {
        val presenter = TransactionPresenter(aNewTransaction(), transactionsProvider, currenciesProvider, settings)

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

        verify(transactionsProvider).save(argThat { first() == archivedTransaction })
        verify(view).displayResult(archivedTransaction)
    }

    @Test
    fun restoresTagAndDisplaysResult() {
        val archivedTransaction = transaction.withModelState(ARCHIVED)
        val restoredTransaction = transaction.withModelState(NONE)
        val presenter = TransactionPresenter(archivedTransaction, transactionsProvider, currenciesProvider, settings)
        presenter.onViewAttached(view)

        toggleArchive()

        verify(transactionsProvider).save(argThat { first() == restoredTransaction })
        verify(view).displayResult(restoredTransaction)
    }

    @Test
    fun showsExchangeRateOnlyWhenCurrencyIsNotMain() {
        whenever(settings.mainCurrency).thenReturn(transaction.currency)
        presenter.onViewAttached(view)
        verify(view).showExchangeRateVisible(false)

        requestCurrencyChange()
        updateCurrency(Currency("OTHER"))
        verify(view).showExchangeRateVisible(true)
    }

    private fun toggleArchive() = toggleArchiveSubject.onNext(Unit)
    private fun updateTransactionType(transactionType: TransactionType) = transactionTypeSubject.onNext(transactionType)
    private fun updateTransactionState(transactionState: TransactionState) = transactionStateSubject.onNext(transactionState)
    private fun updateTimestamp(timestamp: Long) = timestampSubject.onNext(timestamp)
    private fun requestCurrencyChange() = requestCurrencyChangeSubject.onNext(Unit)
    private fun updateCurrency(currency: Currency) = currencySubject.onNext(currency)
    private fun updateExchangeRate(exchangeRate: BigDecimal) = exchangeRateSubject.onNext(exchangeRate)
    private fun updateAmount(amount: BigDecimal) = amountSubject.onNext(amount)
    private fun updateTags(tags: Set<Tag>) = tagsSubject.onNext(tags)
    private fun updateNote(note: String) = noteSubject.onNext(note)
    private fun save() = saveSubject.onNext(Unit)
    private fun allCurrencies() = create<List<Currency>>().apply { currenciesProvider.currencies().subscribe(this) }.onNextEvents.first()
}