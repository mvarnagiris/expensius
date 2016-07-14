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

package com.mvcoding.expensius.feature.calculator

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.jakewharton.rxbinding.view.clicks
import com.jakewharton.rxbinding.view.longClicks
import com.mvcoding.expensius.R
import com.mvcoding.expensius.feature.ActivityStarter
import com.mvcoding.expensius.feature.BaseActivity
import com.mvcoding.expensius.feature.calculator.CalculatorPresenter.ResultDestination.BACK
import com.mvcoding.expensius.feature.calculator.CalculatorPresenter.ResultDestination.TRANSACTION
import kotlinx.android.synthetic.main.activity_calculator.*
import java.math.BigDecimal

class CalculatorActivity : BaseActivity(), CalculatorPresenter.View {
    companion object {
        private const val EXTRA_INITIAL_NUMBER = "EXTRA_INITIAL_NUMBER"
        private const val EXTRA_RESULT_DESTINATION = "EXTRA_RESULT_DESTINATION"
        private const val RESULT_EXTRA_AMOUNT = "RESULT_EXTRA_AMOUNT"

        fun start(context: Context) {
            ActivityStarter(context, CalculatorActivity::class)
                    .extra(EXTRA_RESULT_DESTINATION, TRANSACTION)
                    .start()
        }

        fun startWithInitialNumberForResult(context: Context, requestCode: Int, initialNumber: BigDecimal) {
            ActivityStarter(context, CalculatorActivity::class)
                    .extra(EXTRA_INITIAL_NUMBER, initialNumber)
                    .extra(EXTRA_RESULT_DESTINATION, BACK)
                    .startForResult(requestCode)
        }

        fun resultExtraAmount(data: Intent) = data.getSerializableExtra(RESULT_EXTRA_AMOUNT) as BigDecimal
    }

    private val presenter by lazy {
        provideCalculatorPresenter(
                intent.getSerializableExtra(EXTRA_INITIAL_NUMBER) as BigDecimal?,
                intent.getSerializableExtra(EXTRA_RESULT_DESTINATION) as CalculatorPresenter.ResultDestination)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)
    }

    override fun onStart() {
        super.onStart()
        presenter.attach(this)
    }

    override fun onStop() {
        super.onStop()
        presenter.detach(this)
    }

    override fun digit0() = number0Button.clicks()
    override fun digit1() = number1Button.clicks()
    override fun digit2() = number2Button.clicks()
    override fun digit3() = number3Button.clicks()
    override fun digit4() = number4Button.clicks()
    override fun digit5() = number5Button.clicks()
    override fun digit6() = number6Button.clicks()
    override fun digit7() = number7Button.clicks()
    override fun digit8() = number8Button.clicks()
    override fun digit9() = number9Button.clicks()
    override fun decimalRequests() = decimalButton.clicks()
    override fun addRequests() = addButton.clicks()
    override fun subtractRequests() = subtractButton.clicks()
    override fun multiplyRequests() = multiplyButton.clicks()
    override fun divideRequests() = divideButton.clicks()
    override fun deleteRequests() = deleteImageButton.clicks()
    override fun clearRequests() = deleteImageButton.longClicks()
    override fun saveRequests() = equalsFloatingActionButton.clicks()
    override fun showExpression(expression: String) = resultTextView.let { it.text = expression }
    override fun showState(state: CalculatorPresenter.State) = equalsFloatingActionButton.let { it.isSelected = state == CalculatorPresenter.State.CALCULATE }

    override fun displayResult(number: BigDecimal) {
        setResult(Activity.RESULT_OK, Intent().apply { putExtra(RESULT_EXTRA_AMOUNT, number) })
        finish()
    }

    override fun displayTransaction(number: BigDecimal) {
        //        TransactionActivity.start(this, transaction)
        //        finish()
    }
}