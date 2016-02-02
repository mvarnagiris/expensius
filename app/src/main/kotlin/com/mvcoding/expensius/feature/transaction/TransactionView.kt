/*
 * Copyright (C) 2016 Mantas Varnagiris.
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

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.support.design.widget.FloatingActionButton
import android.util.AttributeSet
import android.widget.*
import com.jakewharton.rxbinding.view.clicks
import com.jakewharton.rxbinding.widget.checkedChanges
import com.jakewharton.rxbinding.widget.textChanges
import com.mvcoding.expensius.R
import com.mvcoding.expensius.RxBus
import com.mvcoding.expensius.extension.provideActivityScopedSingleton
import com.mvcoding.expensius.extension.provideSingleton
import com.mvcoding.expensius.extension.toBaseActivity
import com.mvcoding.expensius.feature.AmountFormatter
import com.mvcoding.expensius.feature.DateDialogFragment
import com.mvcoding.expensius.feature.DateDialogFragment.DateDialogResult
import com.mvcoding.expensius.feature.DateFormatter
import com.mvcoding.expensius.feature.calculator.CalculatorActivity
import com.mvcoding.expensius.feature.tag.QuickTagsView
import com.mvcoding.expensius.feature.tag.Tag
import com.mvcoding.expensius.feature.transaction.TransactionState.CONFIRMED
import com.mvcoding.expensius.feature.transaction.TransactionState.PENDING
import com.mvcoding.expensius.feature.transaction.TransactionType.EXPENSE
import com.mvcoding.expensius.feature.transaction.TransactionType.INCOME
import org.joda.time.DateTime
import rx.Observable
import rx.lang.kotlin.PublishSubject
import java.math.BigDecimal
import java.math.BigDecimal.ZERO

class TransactionView : LinearLayout, TransactionPresenter.View {
    companion object {
        private const val REQUEST_AMOUNT = 1
        private const val REQUEST_DATE = 2
    }

    private val amountFormatter by lazy { provideSingleton(AmountFormatter::class) }
    private val dateFormatter by lazy { provideSingleton(DateFormatter::class) }
    private val currencySubject by lazy { PublishSubject<Currency>() }
    private val amountSubject by lazy { PublishSubject<BigDecimal>() }
    private val rxBus by lazy { provideSingleton(RxBus::class) }

    private val transactionTypeFloatingActionButton by lazy { findViewById(R.id.transactionTypeFloatingActionButton) as FloatingActionButton }
    private val amountTextView by lazy { findViewById(R.id.amountTextView) as TextView }
    private val quickTagsView by lazy { findViewById(R.id.quickTagsView) as QuickTagsView }
    private val dateButton by lazy { findViewById(R.id.dateButton) as Button }
    private val noteEditText by lazy { findViewById(R.id.noteEditText) as EditText }
    private val transactionStateCheckBox by lazy { findViewById(R.id.transactionStateCheckBox) as CheckBox }
    private val currencyButton by lazy { findViewById(R.id.currencyButton) as Button }
    private val saveButton by lazy { findViewById(R.id.saveButton) as Button }

    private lateinit var presenter: TransactionPresenter
    private var transactionState = CONFIRMED
    private var transactionType = EXPENSE
    private var amount = ZERO
    private var currency = Currency()
    private var timestamp = 0L
    private var allowTransactionStateChanges = false
    private var allowNoteChanges = true

    constructor(context: Context?) : this(context, null)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (!isInEditMode) {
            amountTextView.clicks().subscribe { CalculatorActivity.startWithInitialNumberForResult(context, REQUEST_AMOUNT, amount) }
            dateButton.clicks().subscribe {
                DateDialogFragment.show(context.toBaseActivity().supportFragmentManager,
                                        REQUEST_DATE,
                                        rxBus,
                                        timestamp)
            }
        }
    }

    fun init(transaction: Transaction) {
        presenter = provideActivityScopedSingleton(TransactionPresenter::class, transaction)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode != REQUEST_AMOUNT || resultCode != RESULT_OK) {
            return
        }

        amountSubject.onNext(CalculatorActivity.resultExtraAmount(data!!))
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter.onAttachView(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter.onDetachView(this)
    }

    override fun onTransactionStateChanged(): Observable<TransactionState> {
        return transactionStateCheckBox
                .checkedChanges()
                .filter {
                    if (allowTransactionStateChanges) {
                        true
                    } else {
                        allowTransactionStateChanges = true
                        false
                    }
                }
                .map { if (transactionState == CONFIRMED) PENDING else CONFIRMED }
    }

    override fun onTransactionTypeChanged() = transactionTypeFloatingActionButton.clicks().map { if (transactionType == EXPENSE) INCOME else EXPENSE }

    override fun onTimestampChanged() = rxBus.observe(DateDialogResult::class.java).map {
        DateTime(timestamp).withYear(it.year).withMonthOfYear(it.monthOfYear).withDayOfMonth(it.dayOfMonth).millis
    }

    override fun onCurrencyChanged() = currencySubject.asObservable()

    override fun onAmountChanged() = amountSubject.asObservable()

    override fun onTagsChanged() = quickTagsView.selectedTagsChanges()

    override fun onNoteChanged(): Observable<String> {
        return noteEditText.textChanges().doOnNext { allowNoteChanges = false }.map { it.toString() }
    }

    override fun onSave() = saveButton.clicks()

    override fun showTransactionState(transactionState: TransactionState) {
        this.transactionState = transactionState;
        allowTransactionStateChanges = false
        transactionStateCheckBox.isChecked = transactionState == CONFIRMED
    }

    override fun showTransactionType(transactionType: TransactionType) {
        this.transactionType = transactionType
        transactionTypeFloatingActionButton.isSelected = transactionType == INCOME
    }

    override fun showTimestamp(timestamp: Long) {
        this.timestamp = timestamp
        dateButton.text = dateFormatter.formatDateRelativeToToday(timestamp)
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
        quickTagsView.showSelectedTags(tags)
    }

    override fun showNote(note: String) {
        if (allowNoteChanges) {
            noteEditText.setText(note)
        }
        allowNoteChanges = true
    }

    override fun startResult(transaction: Transaction) {
        context.toBaseActivity().finish()
    }

    private fun updateAmount() {
        amountTextView.text = amountFormatter.format(amount, currency)
    }
}