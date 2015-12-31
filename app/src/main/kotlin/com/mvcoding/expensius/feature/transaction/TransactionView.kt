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
import android.support.design.widget.FloatingActionButton
import android.util.AttributeSet
import android.widget.*
import com.memoizrlabs.ShankModuleInitializer.initializeModules
import com.mvcoding.expensius.R
import com.mvcoding.expensius.extension.provideActivityScopedSingleton
import com.mvcoding.expensius.extension.provideSingleton
import com.mvcoding.expensius.extension.toActivity
import com.mvcoding.expensius.feature.AmountFormatter
import com.mvcoding.expensius.feature.tag.Tag
import com.mvcoding.expensius.feature.transaction.Currency.Companion.noCurrency
import com.mvcoding.expensius.feature.transaction.TransactionState.CONFIRMED
import com.mvcoding.expensius.feature.transaction.TransactionType.INCOME
import net.danlew.android.joda.DateUtils.*
import org.joda.time.DateTime
import rx.Observable
import rx.Observable.empty
import java.math.BigDecimal
import java.math.BigDecimal.ZERO

class TransactionView : LinearLayout, TransactionPresenter.View {
    private val amountFormatter by lazy { provideSingleton(AmountFormatter::class) }
    private val presenter by lazy { provideActivityScopedSingleton(TransactionPresenter::class) }

    private val transactionTypeFloatingActionButton by lazy { findViewById(R.id.transactionTypeFloatingActionButton) as FloatingActionButton }
    private val amountTextView by lazy { findViewById(R.id.amountTextView) as TextView }
    private val dateButton by lazy { findViewById(R.id.dateButton) as Button }
    private val timeButton by lazy { findViewById(R.id.timeButton) as Button }
    private val tagsButton by lazy { findViewById(R.id.tagsButton) as Button }
    private val noteEditText by lazy { findViewById(R.id.noteEditText) as EditText }
    private val transactionStateCheckBox by lazy { findViewById(R.id.transactionStateCheckBox) as CheckBox }
    private val currencyButton by lazy { findViewById(R.id.currencyButton) as Button }

    private var currency: Currency = noCurrency
    private var amount = ZERO

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
        transactionStateCheckBox.isChecked = transactionState == CONFIRMED
    }

    override fun showTransactionType(transactionType: TransactionType) {
        transactionTypeFloatingActionButton.isSelected = transactionType == INCOME
    }

    override fun showTimestamp(timestamp: Long) {
        val dateTime = DateTime(timestamp)
        dateButton.text = formatDateTime(context, dateTime, FORMAT_SHOW_DATE)
        timeButton.text = formatDateTime(context, dateTime, FORMAT_SHOW_TIME)
    }

    override fun showCurrency(currency: Currency) {
        this.currency = currency;
        currencyButton.text = currency.code
        updateAmount()
    }

    override fun showAmount(amount: BigDecimal) {
        this.amount = amount
        updateAmount()
    }

    override fun showTags(tags: Set<Tag>) {
        tagsButton.text = tags.joinToString(separator = ", ", transform = { it.title })
    }

    override fun showNote(note: String) {
        noteEditText.setText(note)
    }

    override fun startResult(transaction: Transaction) {
        context.toActivity().finish()
    }

    private fun updateAmount() {
        amountTextView.text = amountFormatter.format(amount, currency)
    }
}