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
import com.mvcoding.expensius.model.Currency
import com.mvcoding.expensius.model.ModelState
import com.mvcoding.expensius.model.ModelState.ARCHIVED
import com.mvcoding.expensius.model.ModelState.NONE
import com.mvcoding.expensius.model.Tag
import com.mvcoding.expensius.model.Transaction
import com.mvcoding.expensius.model.generateModelId
import com.mvcoding.mvp.Presenter
import rx.Observable
import rx.Observable.combineLatest
import rx.Observable.just
import java.math.BigDecimal

class TransactionPresenter(
        private var transaction: Transaction,
        private val transactionsProvider: TransactionsProvider,
        private val currenciesProvider: CurrenciesProvider,
        private val settings: Settings) : Presenter<TransactionPresenter.View>() {

    override fun onViewAttached(view: View) {
        super.onViewAttached(view)

        view.showArchiveEnabled(transaction.isStored())
        view.showModelState(transaction.modelState)

        unsubscribeOnDetach(view.onSave()
                .withLatestFrom(transactionObservable(view), { action, transaction -> transaction })
                .doOnNext { transactionsProvider.save(setOf(it)) }
                .subscribe { view.displayResult(it) })

        unsubscribeOnDetach(view.onToggleArchive()
                .map { transactionWithToggledArchiveState() }
                .doOnNext { transactionsProvider.save(setOf(it)) }
                .subscribe { view.displayResult(it) })
    }

    private fun transactionObservable(view: View): Observable<Transaction>? {
        val ids = just(transaction.id).filter { !it.isBlank() }.defaultIfEmpty(generateModelId())
        val modelStates = just(transaction.modelState)
        val transactionTypes = view.onTransactionTypeChanged()
                .startWith(transaction.transactionType)
                .doOnNext { view.showTransactionType(it) }
        val transactionStates = view.onTransactionStateChanged()
                .startWith(transaction.transactionState)
                .doOnNext { view.showTransactionState(it) }
        val timestamps = view.onTimestampChanged().startWith(transaction.timestamp).doOnNext { view.showTimestamp(it) }
        val currencies = view.onCurrencyChangeRequested()
                .flatMap { currenciesProvider.currencies() }
                .flatMap { view.requestCurrency(it) }
                .startWith(transaction.currency)
                .doOnNext { view.showCurrency(it) }
                .doOnNext { view.showExchangeRateVisible(it != settings.mainCurrency) }
        val exchangeRates = view.onExchangeRateChanged().startWith(transaction.exchangeRate).doOnNext { view.showExchangeRate(it) }
        val amounts = view.onAmountChanged().startWith(transaction.amount).doOnNext { view.showAmount(it) }
        val tags = view.onTagsChanged().startWith(transaction.tags).doOnNext { view.showTags(it) }
        val notes = view.onNoteChanged().startWith(transaction.note).doOnNext { view.showNote(it) }

        val values = listOf(
                ids,
                modelStates,
                transactionTypes,
                transactionStates,
                timestamps,
                currencies,
                exchangeRates,
                amounts,
                tags,
                notes)

        return combineLatest(values) {
            @Suppress("UNCHECKED_CAST")
            Transaction(
                    it[0] as String,
                    it[1] as ModelState,
                    it[2] as TransactionType,
                    it[3] as TransactionState,
                    it[4] as Long,
                    it[5] as Currency,
                    it[6] as BigDecimal,
                    it[7] as BigDecimal,
                    it[8] as Set<Tag>,
                    it[9] as String
            )
        }.doOnNext { transaction = it }
    }

    private fun transactionWithToggledArchiveState() = transaction.withModelState(if (transaction.modelState == NONE) ARCHIVED else NONE)

    interface View : Presenter.View {
        fun onTransactionStateChanged(): Observable<TransactionState>
        fun onTransactionTypeChanged(): Observable<TransactionType>
        fun onTimestampChanged(): Observable<Long>
        fun onCurrencyChangeRequested(): Observable<Unit>
        fun onExchangeRateChanged(): Observable<BigDecimal>
        fun onAmountChanged(): Observable<BigDecimal>
        fun onTagsChanged(): Observable<Set<Tag>>
        fun onNoteChanged(): Observable<String>
        fun onToggleArchive(): Observable<Unit>
        fun onSave(): Observable<Unit>
        fun requestCurrency(currencies: List<Currency>): Observable<Currency>

        fun showArchiveEnabled(archiveEnabled: Boolean)
        fun showModelState(modelState: ModelState)
        fun showTransactionState(transactionState: TransactionState)
        fun showTransactionType(transactionType: TransactionType)
        fun showTimestamp(timestamp: Long)
        fun showCurrency(currency: Currency)
        fun showExchangeRate(exchangeRate: BigDecimal)
        fun showExchangeRateVisible(visible: Boolean)
        fun showAmount(amount: BigDecimal)
        fun showTags(tags: Set<Tag>)
        fun showNote(note: String)

        fun displayResult(transaction: Transaction)
    }
}