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
import android.support.v7.widget.ListPopupWindow
import android.util.AttributeSet
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import com.jakewharton.rxbinding.support.v7.widget.itemClicks
import com.jakewharton.rxbinding.view.clicks
import com.jakewharton.rxbinding.widget.checkedChanges
import com.jakewharton.rxbinding.widget.textChanges
import com.mvcoding.expensius.R
import com.mvcoding.expensius.extension.doNotInEditMode
import com.mvcoding.expensius.extension.getDimensionFromTheme
import com.mvcoding.expensius.extension.setTextIfChanged
import com.mvcoding.expensius.extension.toBaseActivity
import com.mvcoding.expensius.feature.DateDialogFragment
import com.mvcoding.expensius.feature.DateDialogFragment.DateDialogResult
import com.mvcoding.expensius.feature.calculator.CalculatorActivity
import com.mvcoding.expensius.feature.transaction.TransactionState.CONFIRMED
import com.mvcoding.expensius.feature.transaction.TransactionState.PENDING
import com.mvcoding.expensius.feature.transaction.TransactionType.EXPENSE
import com.mvcoding.expensius.feature.transaction.TransactionType.INCOME
import com.mvcoding.expensius.model.Currency
import com.mvcoding.expensius.model.ModelState
import com.mvcoding.expensius.model.Tag
import com.mvcoding.expensius.model.Transaction
import com.mvcoding.expensius.provideAmountFormatter
import com.mvcoding.expensius.provideDateFormatter
import com.mvcoding.expensius.provideRxBus
import kotlinx.android.synthetic.main.toolbar.view.*
import kotlinx.android.synthetic.main.view_transaction.view.*
import org.joda.time.DateTime
import rx.Observable
import rx.lang.kotlin.PublishSubject
import java.math.BigDecimal
import java.math.BigDecimal.ONE
import java.math.BigDecimal.ZERO

class TransactionView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        LinearLayout(context, attrs, defStyleAttr), TransactionPresenter.View {

    companion object {
        private const val REQUEST_AMOUNT = 1
        private const val REQUEST_DATE = 2
        private const val REQUEST_EXCHANGE_RATE = 3
    }

    var isArchiveToggleVisible = true
    var archiveToggleTitle = resources.getString(R.string.archive)

    private val amountFormatter by lazy { provideAmountFormatter() }
    private val dateFormatter by lazy { provideDateFormatter() }
    private val amountSubject by lazy { PublishSubject<BigDecimal>() }
    private val exchangeRateSubject by lazy { PublishSubject<BigDecimal>() }
    private val rxBus by lazy { provideRxBus() }

    private lateinit var presenter: TransactionPresenter
    private var transactionState = CONFIRMED
    private var transactionType = EXPENSE
    private var amount = ZERO
    private var currency = Currency()
    private var exchangeRate = ONE
    private var timestamp = 0L
    private var allowTransactionStateChanges = false

    override fun onFinishInflate() {
        super.onFinishInflate()
        doNotInEditMode {
            amountTextView.clicks().subscribe { CalculatorActivity.startWithInitialNumberForResult(context, REQUEST_AMOUNT, amount) }
            exchangeRateButton.clicks()
                    .subscribe { CalculatorActivity.startWithInitialNumberForResult(context, REQUEST_EXCHANGE_RATE, exchangeRate) }
            dateButton.clicks()
                    .subscribe { DateDialogFragment.show(context.toBaseActivity().supportFragmentManager, REQUEST_DATE, rxBus, timestamp) }
        }
    }

    fun init(transaction: Transaction) {
        presenter = provideTransactionPresenter(transaction)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != RESULT_OK) return

        when (requestCode) {
            REQUEST_AMOUNT -> amountSubject.onNext(CalculatorActivity.resultExtraAmount(data!!))
            REQUEST_EXCHANGE_RATE -> exchangeRateSubject.onNext(CalculatorActivity.resultExtraAmount(data!!))
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        doNotInEditMode { presenter.attach(this) }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter.detach(this)
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

    override fun onTransactionTypeChanged() = transactionTypeFloatingActionButton.clicks()
            .map { if (transactionType == EXPENSE) INCOME else EXPENSE }

    override fun onTimestampChanged() = rxBus.observe(DateDialogResult::class)
            .map { DateTime(timestamp).withYear(it.year).withMonthOfYear(it.monthOfYear).withDayOfMonth(it.dayOfMonth).millis }

    override fun onCurrencyChangeRequested() = currencyButton.clicks()
    override fun onExchangeRateChanged() = exchangeRateSubject.asObservable()
    override fun onAmountChanged() = amountSubject.asObservable()
    override fun onTagsChanged() = quickTagsView.selectedTagsChanges()
    override fun onNoteChanged() = noteEditText.textChanges().map { it.toString() }.distinctUntilChanged()
    override fun onToggleArchive() = toolbar.itemClicks().filter { it.itemId == R.id.action_archive }.map { Unit }
    override fun onSave() = saveButton.clicks()

    override fun requestCurrency(currencies: List<Currency>): Observable<Currency> = Observable.create {
        val displayCurrencies = currencies.map { it.displayName() }
        val itemHeight = getDimensionFromTheme(context, R.attr.actionBarSize)
        val keyline = resources.getDimensionPixelSize(R.dimen.keyline)
        val keylineHalf = resources.getDimensionPixelOffset(R.dimen.keyline_half)
        val popupWindow = ListPopupWindow(context)
        popupWindow.anchorView = currencyButton
        popupWindow.setAdapter(ArrayAdapter<String>(context, R.layout.item_view_currency, R.id.currencyCodeTextView, displayCurrencies))
        popupWindow.setOnItemClickListener { adapterView, view, position, id -> it.onNext(currencies[position]); popupWindow.dismiss() }
        popupWindow.setOnDismissListener { it.onCompleted() }
        popupWindow.width = width - keyline
        popupWindow.height = Math.min(height - buttonBarView.height - currencyButton.height - keylineHalf, itemHeight * 7)
        popupWindow.isModal = true
        popupWindow.horizontalOffset = keylineHalf
        popupWindow.show()
    }

    override fun showArchiveEnabled(archiveEnabled: Boolean) {
        isArchiveToggleVisible = archiveEnabled
        toolbar.menu.findItem(R.id.action_archive)?.isVisible = archiveEnabled
    }

    override fun showModelState(modelState: ModelState) {
        archiveToggleTitle = resources.getString(if (modelState == ModelState.NONE) R.string.archive else R.string.restore)
        toolbar.menu.findItem(R.id.action_archive)?.title = archiveToggleTitle
    }

    override fun showTransactionState(transactionState: TransactionState) {
        this.transactionState = transactionState
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
        this.currency = currency
        currencyButton.text = currency.displayName()
        updateAmount()
    }

    override fun showExchangeRate(exchangeRate: BigDecimal) {
        this.exchangeRate = exchangeRate
        exchangeRateButton.text = exchangeRate.toPlainString()
    }

    override fun showExchangeRateVisible(visible: Boolean) {
        exchangeRateButton.visibility = if (visible) VISIBLE else GONE
    }

    override fun showAmount(amount: BigDecimal) {
        this.amount = amount
        updateAmount()
    }

    override fun showTags(tags: Set<Tag>) = quickTagsView.showSelectedTags(tags)
    override fun showNote(note: String) = noteEditText.setTextIfChanged(note)
    override fun displayResult(transaction: Transaction) = context.toBaseActivity().finish()

    private fun updateAmount() {
        amountTextView.text = amountFormatter.format(amount, currency)
    }
}