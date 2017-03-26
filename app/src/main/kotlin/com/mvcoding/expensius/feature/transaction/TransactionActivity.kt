/*
 * Copyright (C) 2017 Mantas Varnagiris.
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
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.ListPopupWindow
import android.view.Menu
import android.widget.ArrayAdapter
import com.jakewharton.rxbinding.support.v7.widget.itemClicks
import com.jakewharton.rxbinding.view.clicks
import com.jakewharton.rxbinding.widget.checkedChanges
import com.jakewharton.rxbinding.widget.textChanges
import com.mvcoding.expensius.*
import com.mvcoding.expensius.extension.displayName
import com.mvcoding.expensius.extension.getDimensionFromTheme
import com.mvcoding.expensius.extension.setTextIfChanged
import com.mvcoding.expensius.feature.ActivityStarter
import com.mvcoding.expensius.feature.BaseActivity
import com.mvcoding.expensius.feature.DateDialogFragment
import com.mvcoding.expensius.feature.calculator.CalculatorActivity
import com.mvcoding.expensius.model.*
import com.mvcoding.expensius.model.TransactionState.CONFIRMED
import com.mvcoding.expensius.model.TransactionState.PENDING
import com.mvcoding.expensius.model.TransactionType.EXPENSE
import com.mvcoding.expensius.model.TransactionType.INCOME
import kotlinx.android.synthetic.main.activity_transaction.*
import kotlinx.android.synthetic.main.toolbar.*
import org.joda.time.DateTime
import rx.Observable
import rx.lang.kotlin.PublishSubject
import rx.lang.kotlin.observable
import java.math.BigDecimal
import java.math.BigDecimal.ZERO

class TransactionActivity : BaseActivity(), TransactionPresenter.View {
    companion object {
        private const val EXTRA_TRANSACTION = "EXTRA_TRANSACTION"

        private const val REQUEST_AMOUNT = 1
        private const val REQUEST_DATE = 2
        private const val REQUEST_EXCHANGE_RATE = 3

        fun start(context: Context, transaction: Transaction) {
            ActivityStarter(context, TransactionActivity::class)
                    .extra(EXTRA_TRANSACTION, transaction)
                    .start()
        }
    }

    private val presenter by lazy { provideTransactionPresenter(intent.getSerializableExtra(EXTRA_TRANSACTION) as Transaction) }
    private val moneyFormatter by lazy { provideMoneyFormatter() }
    private val dateFormatter by lazy { provideDateFormatter() }
    private val amountSubject by lazy { PublishSubject<BigDecimal>() }
    private val exchangeRateSubject by lazy { PublishSubject<BigDecimal>() }
    private val rxBus by lazy { provideRxBus() }

    private var isArchiveToggleVisible = true
    private var archiveToggleTitle: String? = null
    private var timestamp = 0L
    private var amount = ZERO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)

        amountTextView.clicks().subscribe { CalculatorActivity.startWithInitialNumberForResult(this, REQUEST_AMOUNT, amount) }
        dateButton.clicks().subscribe { DateDialogFragment.show(supportFragmentManager, REQUEST_DATE, rxBus, timestamp) }

        presenter.attach(this)
    }

    override fun onDestroy() {
        presenter.detach(this)
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK) return

        when (requestCode) {
            REQUEST_AMOUNT -> amountSubject.onNext(CalculatorActivity.resultExtraAmount(data!!))
            REQUEST_EXCHANGE_RATE -> exchangeRateSubject.onNext(CalculatorActivity.resultExtraAmount(data!!))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.tag, menu)
        val menuItem = menu.findItem(R.id.action_archive)
        menuItem.isVisible = isArchiveToggleVisible
        menuItem.title = archiveToggleTitle
        return true
    }

    override fun transactionStateChanges(): Observable<TransactionState> = transactionStateCheckBox.checkedChanges()
            .map { if (it) CONFIRMED else PENDING }
            .distinctUntilChanged()

    override fun transactionTypeChanges(): Observable<TransactionType> = transactionTypeFloatingActionButton.clicks()
            .map { if (transactionTypeFloatingActionButton.isSelected) EXPENSE else INCOME }
            .distinctUntilChanged()

    override fun timestampChanges(): Observable<Long> = rxBus.observe<DateDialogFragment.DateDialogResult>()
            .map { DateTime(timestamp).withYear(it.year).withMonthOfYear(it.monthOfYear).withDayOfMonth(it.dayOfMonth).millis }

    override fun exchangeRateChanges(): Observable<BigDecimal> = exchangeRateSubject
    override fun amountChanges(): Observable<BigDecimal> = amountSubject
    override fun tagsChanges(): Observable<Set<Tag>> = quickTagsView.selectedTagsChanges()
    override fun noteChanges(): Observable<String> = noteEditText.textChanges().map { it.toString() }.distinctUntilChanged()
    override fun archiveToggles(): Observable<Unit> = toolbar.itemClicks().filter { it.itemId == R.id.action_archive }.map { Unit }
    override fun saveRequests(): Observable<Unit> = saveButton.clicks()
    override fun currencyChangeRequests(): Observable<Unit> = currencyButton.clicks()

    override fun currencyChanges(currencies: List<Currency>): Observable<Currency> = observable {
        val displayCurrencies = currencies.map { it.displayName() }
        val itemHeight = getDimensionFromTheme(this, R.attr.actionBarSize)
        val keyline = resources.getDimensionPixelSize(R.dimen.keyline)
        val keylineHalf = resources.getDimensionPixelOffset(R.dimen.keyline_half)
        val popupWindow = ListPopupWindow(this)
        popupWindow.anchorView = currencyButton
        popupWindow.setAdapter(ArrayAdapter<String>(this, R.layout.item_view_currency, R.id.currencyCodeTextView, displayCurrencies))
        popupWindow.setOnItemClickListener { _, _, position, _ -> it.onNext(currencies[position]); popupWindow.dismiss() }
        popupWindow.setOnDismissListener { it.onCompleted() }
        popupWindow.width = contentView.width - keyline
        popupWindow.height = Math.min(contentView.height - buttonBarView.height - currencyButton.height - keylineHalf, itemHeight * 7)
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

    override fun showTimestamp(timestamp: Timestamp) {
        this.timestamp = timestamp.millis
        dateButton.text = dateFormatter.formatDateRelativeToToday(timestamp.millis)
    }

    override fun showMoney(money: Money) {
        amount = money.amount
        amountTextView.text = moneyFormatter.format(money)
        currencyButton.text = money.currency.displayName()
    }

    override fun showTransactionState(transactionState: TransactionState): Unit = with(transactionStateCheckBox) { isChecked = transactionState == CONFIRMED }
    override fun showTransactionType(transactionType: TransactionType): Unit = with(transactionTypeFloatingActionButton) { isSelected = transactionType == INCOME }
    override fun showTags(tags: Set<Tag>): Unit = quickTagsView.showSelectedTags(tags)
    override fun showNote(note: Note): Unit = noteEditText.setTextIfChanged(note.text)
    override fun displayResult(): Unit = finish()
}