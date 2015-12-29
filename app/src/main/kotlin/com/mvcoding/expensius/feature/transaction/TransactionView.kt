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
import android.widget.TextView
import com.memoizrlabs.ShankModuleInitializer.initializeModules
import com.mvcoding.expensius.R
import com.mvcoding.expensius.extension.provideActivityScopedSingleton
import com.mvcoding.expensius.feature.tag.Tag
import rx.Observable
import rx.Observable.empty
import java.math.BigDecimal

class TransactionView : LinearLayout, TransactionPresenter.View {
    private val amountTextView by lazy { findViewById(R.id.amountTextView) as TextView }
    private val presenter by lazy { provideActivityScopedSingleton(TransactionPresenter::class) }

    constructor(context: Context?) : this(context, null)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun init(transaction: Transaction) {
        initializeModules(TransactionModule(transaction))
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter?.onAttachView(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter?.onDetachView(this)
    }

    override fun onTransactionStateChanged(): Observable<TransactionState> {
        return empty()
    }

    override fun onTransactionTypeChanged(): Observable<TransactionType> {
        return empty()
    }

    override fun onTimestampChanged(): Observable<Long> {
        return empty()
    }

    override fun onCurrencyChanged(): Observable<Currency> {
        return empty()
    }

    override fun onAmountChanged(): Observable<BigDecimal> {
        return empty()
    }

    override fun onTagsChanged(): Observable<Set<Tag>> {
        return empty()
    }

    override fun onNoteChanged(): Observable<String> {
        return empty()
    }

    override fun onSave(): Observable<Unit> {
        return empty()
    }

    override fun showTransactionState(transactionState: TransactionState) {
    }

    override fun showTransactionType(transactionType: TransactionType) {
    }

    override fun showTimestamp(timestamp: Long) {
    }

    override fun showCurrency(currency: Currency) {
    }

    override fun showAmount(amount: BigDecimal) {
        amountTextView.text = amount.toPlainString()
    }

    override fun showTags(tags: Set<Tag>) {
    }

    override fun showNote(note: String) {
    }

    override fun startResult(transaction: Transaction) {
    }
}