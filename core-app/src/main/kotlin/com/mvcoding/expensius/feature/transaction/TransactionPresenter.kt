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

import com.mvcoding.expensius.feature.Presenter
import com.mvcoding.expensius.model.ModelState
import com.mvcoding.expensius.model.ModelState.ARCHIVED
import com.mvcoding.expensius.model.ModelState.NONE
import com.mvcoding.expensius.model.Tag
import com.mvcoding.expensius.model.Transaction
import rx.Observable
import rx.Observable.just
import java.math.BigDecimal
import java.util.UUID.randomUUID

class TransactionPresenter(
        private var transaction: Transaction,
        private val transactionsProvider: TransactionsProvider) : Presenter<TransactionPresenter.View>() {

    override fun onAttachView(view: View) {
        super.onAttachView(view)

        view.showArchiveEnabled(transaction.isStored())
        view.showModelState(transaction.modelState)

        val ids = just(transaction.id).filter { !it.isBlank() }.defaultIfEmpty(randomUUID().toString())
        val modelStates = just(transaction.modelState)
        val transactionStates = view.onTransactionStateChanged().startWith(transaction.transactionState).doOnNext {
            view.showTransactionState(it)
        }
        val transactionTypes = view.onTransactionTypeChanged().startWith(transaction.transactionType).doOnNext {
            view.showTransactionType(it)
        }
        val timestamps = view.onTimestampChanged().startWith(transaction.timestamp).doOnNext { view.showTimestamp(it) }
        val currencies = view.onCurrencyChanged().startWith(transaction.currency).doOnNext { view.showCurrency(it) }
        val amounts = view.onAmountChanged().startWith(transaction.amount).doOnNext { view.showAmount(it) }
        val tags = view.onTagsChanged().startWith(transaction.tags).doOnNext { view.showTags(it) }
        val notes = view.onNoteChanged().startWith(transaction.note).doOnNext { view.showNote(it) }

        val transactionObservable = Observable.combineLatest(
                ids,
                modelStates,
                transactionStates,
                transactionTypes,
                timestamps,
                currencies,
                amounts,
                tags,
                notes,
                { id, modelState, transactionState, transactionType, timestamp, currency, amount, tags, note ->
                    Transaction(id, modelState, transactionType, transactionState, timestamp, currency, amount, tags, note)
                })
                .doOnNext { transaction = it }

        unsubscribeOnDetach(view.onSave()
                                    .withLatestFrom(transactionObservable, { action, transaction -> transaction })
                                    .doOnNext { transactionsProvider.save(setOf(it)) }
                                    .subscribe { view.displayResult(it) })

        unsubscribeOnDetach(view.onToggleArchive()
                                    .map { transactionWithToggledArchiveState() }
                                    .doOnNext { transactionsProvider.save(setOf(it)) }
                                    .subscribe { view.displayResult(it) })
    }

    private fun transactionWithToggledArchiveState() = transaction.withModelState(if (transaction.modelState == NONE) ARCHIVED else NONE)

    interface View : Presenter.View {
        fun onTransactionStateChanged(): Observable<TransactionState>
        fun onTransactionTypeChanged(): Observable<TransactionType>
        fun onTimestampChanged(): Observable<Long>
        fun onCurrencyChanged(): Observable<Currency>
        fun onAmountChanged(): Observable<BigDecimal>
        fun onTagsChanged(): Observable<Set<Tag>>
        fun onNoteChanged(): Observable<String>
        fun onToggleArchive(): Observable<Unit>
        fun onSave(): Observable<Unit>

        fun showArchiveEnabled(archiveEnabled: Boolean)
        fun showModelState(modelState: ModelState)
        fun showTransactionState(transactionState: TransactionState)
        fun showTransactionType(transactionType: TransactionType)
        fun showTimestamp(timestamp: Long)
        fun showCurrency(currency: Currency)
        fun showAmount(amount: BigDecimal)
        fun showTags(tags: Set<Tag>)
        fun showNote(note: String)

        fun displayResult(transaction: Transaction)
    }
}