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
import android.util.AttributeSet
import android.widget.LinearLayout
import com.jakewharton.rxbinding.view.clicks
import com.jakewharton.rxbinding.view.longClicks
import com.mvcoding.expensius.extension.doNotInEditMode
import com.mvcoding.expensius.extension.provideActivityScopedSingleton
import com.mvcoding.expensius.extension.toBaseActivity
import com.mvcoding.expensius.feature.calculator.CalculatorPresenter.State.CALCULATE
import com.mvcoding.expensius.feature.transaction.TransactionActivity
import com.mvcoding.expensius.model.Transaction
import kotlinx.android.synthetic.main.view_calculator.view.*
import java.math.BigDecimal

class CalculatorView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        LinearLayout(context, attrs, defStyleAttr), CalculatorPresenter.View {

    private lateinit var presenter: CalculatorPresenter

    companion object {
        const val RESULT_EXTRA_AMOUNT = "RESULT_EXTRA_AMOUNT"
    }

    fun init(initialNumber: BigDecimal?, resultDestination: CalculatorPresenter.ResultDestination) {
        presenter = provideActivityScopedSingleton(CalculatorPresenter::class, initialNumber, resultDestination)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        doNotInEditMode { presenter.attach(this) }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter.detach(this)
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
    override fun onDelete() = deleteImageButton.clicks()
    override fun onClear() = deleteImageButton.longClicks()
    override fun onSave() = equalsFloatingActionButton.clicks()
    override fun showExpression(expression: String) = resultTextView.let { it.text = expression }
    override fun showState(state: CalculatorPresenter.State) = equalsFloatingActionButton.let { it.isSelected = state == CALCULATE }

    override fun startResult(number: BigDecimal) = context.toBaseActivity().let {
        it.setResult(RESULT_OK, Intent().apply { putExtra(RESULT_EXTRA_AMOUNT, number) })
        it.finish()
    }

    override fun startTransaction(transaction: Transaction) {
        TransactionActivity.start(context, transaction)
        context.toBaseActivity().finish()
    }
}