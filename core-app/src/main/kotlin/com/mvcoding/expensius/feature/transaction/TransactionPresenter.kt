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
import com.mvcoding.expensius.feature.tag.Tag
import rx.Observable
import java.math.BigDecimal
import java.util.UUID.randomUUID

class TransactionPresenter(
        private var transaction: Transaction,
        private val transactionsProvider: TransactionsProvider) : Presenter<TransactionPresenter.View>() {

    override fun onAttachView(view: View) {
        super.onAttachView(view)

        val idObservable = Observable.just(transaction.id).filter { !it.isBlank() }.defaultIfEmpty(randomUUID().toString())
        val modelStateObservable = Observable.just(transaction.modelState)
        val transactionStateObservable = view.onTransactionStateChanged().startWith(transaction.transactionState).doOnNext { view.showTransactionState(it) }
        val transactionTypeObservable = view.onTransactionTypeChanged().startWith(transaction.transactionType).doOnNext { view.showTransactionType(it) }
        val timestampObservable = view.onTimestampChanged().startWith(transaction.timestamp).doOnNext { view.showTimestamp(it) }
        val currencyObservable = view.onCurrencyChanged().startWith(transaction.currency).doOnNext { view.showCurrency(it) }
        val amountObservable = view.onAmountChanged().startWith(transaction.amount).doOnNext { view.showAmount(it) }
        val tagsObservable = view.onTagsChanged().startWith(transaction.tags).doOnNext { view.showTags(it) }
        val noteObservable = view.onNoteChanged().startWith(transaction.note).doOnNext { view.showNote(it) }

        val transactionObservable = Observable.combineLatest(
                idObservable,
                modelStateObservable,
                transactionStateObservable,
                transactionTypeObservable,
                timestampObservable,
                currencyObservable,
                amountObservable,
                tagsObservable,
                noteObservable,
                { id,
                  modelState,
                  transactionState,
                  transactionType,
                  timestamp,
                  currency,
                  amount,
                  tags,
                  note ->
                    Transaction(
                            id,
                            modelState,
                            transactionType,
                            transactionState,
                            timestamp,
                            currency,
                            amount,
                            tags,
                            note)
                })
                .doOnNext { transaction = it }

        unsubscribeOnDetach(view.onSave()
                .withLatestFrom(transactionObservable, { action, transaction -> transaction })
                                    .doOnNext { transactionsProvider.save(setOf(it)) }
                .subscribe { view.startResult(it) })
    }

    interface View : Presenter.View {
        fun onTransactionStateChanged(): Observable<TransactionState>
        fun onTransactionTypeChanged(): Observable<TransactionType>
        fun onTimestampChanged(): Observable<Long>
        fun onCurrencyChanged(): Observable<Currency>
        fun onAmountChanged(): Observable<BigDecimal>
        fun onTagsChanged(): Observable<Set<Tag>>
        fun onNoteChanged(): Observable<String>
        fun onSave(): Observable<Unit>

        fun showTransactionState(transactionState: TransactionState)
        fun showTransactionType(transactionType: TransactionType)
        fun showTimestamp(timestamp: Long)
        fun showCurrency(currency: Currency)
        fun showAmount(amount: BigDecimal)
        fun showTags(tags: Set<Tag>)
        fun showNote(note: String)

        fun startResult(transaction: Transaction)
    }
}