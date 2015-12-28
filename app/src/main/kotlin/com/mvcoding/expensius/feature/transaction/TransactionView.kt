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

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.mvcoding.expensius.feature.tag.Tag
import rx.Observable
import java.math.BigDecimal

class TransactionView : LinearLayout, TransactionPresenter.View {
    constructor(context: Context?) : this(context, null)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onTransactionStateChanged(): Observable<TransactionState> {
        throw UnsupportedOperationException()
    }

    override fun onTransactionTypeChanged(): Observable<TransactionType> {
        throw UnsupportedOperationException()
    }

    override fun onTimestampChanged(): Observable<Long> {
        throw UnsupportedOperationException()
    }

    override fun onCurrencyChanged(): Observable<Currency> {
        throw UnsupportedOperationException()
    }

    override fun onAmountChanged(): Observable<BigDecimal> {
        throw UnsupportedOperationException()
    }

    override fun onTagsChanged(): Observable<Set<Tag>> {
        throw UnsupportedOperationException()
    }

    override fun onNoteChanged(): Observable<String> {
        throw UnsupportedOperationException()
    }

    override fun onSave(): Observable<Unit> {
        throw UnsupportedOperationException()
    }

    override fun showTransactionState(transactionState: TransactionState) {
        throw UnsupportedOperationException()
    }

    override fun showTransactionType(transactionType: TransactionType) {
        throw UnsupportedOperationException()
    }

    override fun showTimestamp(timestamp: Long) {
        throw UnsupportedOperationException()
    }

    override fun showCurrency(currency: Currency) {
        throw UnsupportedOperationException()
    }

    override fun showAmount(amount: BigDecimal) {
        throw UnsupportedOperationException()
    }

    override fun showTags(tags: Set<Tag>) {
        throw UnsupportedOperationException()
    }

    override fun showNote(note: String) {
        throw UnsupportedOperationException()
    }

    override fun startResult(transaction: Transaction) {
        throw UnsupportedOperationException()
    }
}