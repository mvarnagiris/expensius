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

package com.mvcoding.expensius.feature.calculator

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.support.design.widget.FloatingActionButton
import android.util.AttributeSet
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.jakewharton.rxbinding.view.clicks
import com.jakewharton.rxbinding.view.longClicks
import com.memoizrlabs.ShankModuleInitializer.initializeModules
import com.mvcoding.expensius.R
import com.mvcoding.expensius.extension.provideActivityScopedSingleton
import com.mvcoding.expensius.extension.toBaseActivity
import com.mvcoding.expensius.feature.calculator.CalculatorPresenter.State.CALCULATE
import com.mvcoding.expensius.feature.calculator.CalculatorPresenter.State.SAVE
import com.mvcoding.expensius.feature.transaction.Transaction
import com.mvcoding.expensius.feature.transaction.TransactionActivity
import rx.Observable.just
import rx.lang.kotlin.PublishSubject
import java.math.BigDecimal

class CalculatorView : LinearLayout, CalculatorPresenter.View {
    private val resultTextView by lazy { findViewById(R.id.resultTextView) as TextView }
    private val number0Button by lazy { findViewById(R.id.number0Button) as Button }
    private val number1Button by lazy { findViewById(R.id.number1Button) as Button }
    private val number2Button by lazy { findViewById(R.id.number2Button) as Button }
    private val number3Button by lazy { findViewById(R.id.number3Button) as Button }
    private val number4Button by lazy { findViewById(R.id.number4Button) as Button }
    private val number5Button by lazy { findViewById(R.id.number5Button) as Button }
    private val number6Button by lazy { findViewById(R.id.number6Button) as Button }
    private val number7Button by lazy { findViewById(R.id.number7Button) as Button }
    private val number8Button by lazy { findViewById(R.id.number8Button) as Button }
    private val number9Button by lazy { findViewById(R.id.number9Button) as Button }
    private val decimalButton by lazy { findViewById(R.id.decimalButton) as Button }
    private val deleteButton by lazy { findViewById(R.id.deleteButton) as Button }
    private val addButton by lazy { findViewById(R.id.addButton) as Button }
    private val subtractButton by lazy { findViewById(R.id.subtractButton) as Button }
    private val multiplyButton by lazy { findViewById(R.id.multiplyButton) as Button }
    private val divideButton by lazy { findViewById(R.id.divideButton) as Button }
    private val equalsFloatingActionButton by lazy { findViewById(R.id.equalsFloatingActionButton) as FloatingActionButton }

    private val floatingActionButtonClicks by lazy {
        val clicks = PublishSubject<Unit>()
        equalsFloatingActionButton.clicks()
                .doOnNext { isFloatingActionButtonClickConsumed = false }
                .subscribe { clicks.onNext(it) }
        clicks
    }
    private val presenter by lazy { provideActivityScopedSingleton(CalculatorPresenter::class) }
    private var isFloatingActionButtonClickConsumed = false
    private var state = SAVE

    companion object {
        const val RESULT_EXTRA_AMOUNT = "RESULT_EXTRA_AMOUNT"
    }

    constructor(context: Context?) : this(context, null)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun init(initialNumber: BigDecimal?, resultDestination: CalculatorPresenter.ResultDestination) {
        initializeModules(CalculatorModule(initialNumber, resultDestination))
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter?.onAttachView(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter?.onDetachView(this)
    }

    override fun onDigit0() = number0Button.clicks()

    override fun onDigit1() = number1Button.clicks()

    override fun onDigit2() = number2Button.clicks()

    override fun onDigit3() = number3Button.clicks()

    override fun onDigit4() = number4Button.clicks()

    override fun onDigit5() = number5Button.clicks()

    override fun onDigit6() = number6Button.clicks()

    override fun onDigit7() = number7Button.clicks()

    override fun onDigit8() = number8Button.clicks()

    override fun onDigit9() = number9Button.clicks()

    override fun onDecimal() = decimalButton.clicks()

    override fun onAdd() = addButton.clicks()

    override fun onSubtract() = subtractButton.clicks()

    override fun onMultiply() = multiplyButton.clicks()

    override fun onDivide() = divideButton.clicks()

    override fun onDelete() = deleteButton.clicks()

    override fun onClear() = deleteButton.longClicks()

    override fun onCalculate() = floatingActionButtonClicks.flatMap {
        just(state)
                .filter { it == CALCULATE && !isFloatingActionButtonClickConsumed }
                .doOnNext { isFloatingActionButtonClickConsumed = true }
    }.map { Unit }

    override fun onSave() = floatingActionButtonClicks.flatMap {
        just(state)
                .filter { it == SAVE && !isFloatingActionButtonClickConsumed }
                .doOnNext { isFloatingActionButtonClickConsumed = true }
    }.map { Unit }

    override fun showExpression(expression: String) {
        resultTextView.text = expression
    }

    override fun showState(state: CalculatorPresenter.State) {
        this.state = state
        equalsFloatingActionButton.isSelected = state == CALCULATE
    }

    override fun startResult(number: BigDecimal) {
        val activity = context.toBaseActivity()
        val data = Intent();
        data.putExtra(RESULT_EXTRA_AMOUNT, number)
        activity.setResult(RESULT_OK, data)
        context.toBaseActivity().finish()
    }

    override fun startTransaction(transaction: Transaction) {
        TransactionActivity.start(context, transaction)
        context.toBaseActivity().finish()
    }
}