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

import com.mvcoding.expensius.Settings
import com.mvcoding.expensius.feature.Presenter
import com.mvcoding.expensius.feature.calculator.CalculatorPresenter.ResultDestination.TRANSACTION
import com.mvcoding.expensius.feature.calculator.CalculatorPresenter.State.CALCULATE
import com.mvcoding.expensius.feature.calculator.CalculatorPresenter.State.SAVE
import com.mvcoding.expensius.model.Transaction
import rx.Observable
import rx.Observable.merge
import java.math.BigDecimal

class CalculatorPresenter(
        private val calculator: Calculator,
        private val resultDestination: ResultDestination,
        private val settings: Settings,
        private val initialNumber: BigDecimal? = null) : Presenter<CalculatorPresenter.View>() {

    init {
        initialNumber?.let { calculator.setNumber(it) }
    }

    override fun onAttachView(view: View) {
        super.onAttachView(view)

        val expressionAlteringObservable = merge(arrayOf(
                view.onDigit0().doOnNext { calculator.digit0() },
                view.onDigit1().doOnNext { calculator.digit1() },
                view.onDigit2().doOnNext { calculator.digit2() },
                view.onDigit3().doOnNext { calculator.digit3() },
                view.onDigit4().doOnNext { calculator.digit4() },
                view.onDigit5().doOnNext { calculator.digit5() },
                view.onDigit6().doOnNext { calculator.digit6() },
                view.onDigit7().doOnNext { calculator.digit7() },
                view.onDigit8().doOnNext { calculator.digit8() },
                view.onDigit9().doOnNext { calculator.digit9() },
                view.onDecimal().doOnNext { calculator.decimal() },
                view.onAdd().doOnNext { calculator.add() },
                view.onSubtract().doOnNext { calculator.subtract() },
                view.onMultiply().doOnNext { calculator.multiply() },
                view.onDivide().doOnNext { calculator.divide() },
                view.onClear().doOnNext { calculator.clear() },
                view.onDelete().doOnNext { calculator.delete() },
                view.onCalculate().map { calculator.calculate() }.doOnNext { calculator.setNumber(it) }))
                .startWith(Unit)
                .map { calculator.getExpression() }
                .doOnNext { view.showState(if (calculator.isEmptyOrSingleNumber()) SAVE else CALCULATE) }
                .doOnNext { view.showExpression(it) }
                .map { calculator.calculate() }

        unsubscribeOnDetach(view.onSave()
                                    .withLatestFrom(expressionAlteringObservable, { unit, number -> number })
                                    .subscribe {
                                        if (resultDestination == TRANSACTION) {
                                            view.startTransaction(Transaction(currency = settings.getMainCurrency(), amount = it))
                                        } else {
                                            view.startResult(it)
                                        }
                                    })
    }

    enum class State { SAVE, CALCULATE }

    enum class ResultDestination { BACK, TRANSACTION }

    interface View : Presenter.View {
        fun onDigit0(): Observable<Unit>
        fun onDigit1(): Observable<Unit>
        fun onDigit2(): Observable<Unit>
        fun onDigit3(): Observable<Unit>
        fun onDigit4(): Observable<Unit>
        fun onDigit5(): Observable<Unit>
        fun onDigit6(): Observable<Unit>
        fun onDigit7(): Observable<Unit>
        fun onDigit8(): Observable<Unit>
        fun onDigit9(): Observable<Unit>
        fun onDecimal(): Observable<Unit>
        fun onAdd(): Observable<Unit>
        fun onSubtract(): Observable<Unit>
        fun onMultiply(): Observable<Unit>
        fun onDivide(): Observable<Unit>
        fun onDelete(): Observable<Unit>
        fun onClear(): Observable<Unit>
        fun onCalculate(): Observable<Unit>
        fun onSave(): Observable<Unit>
        fun showExpression(expression: String)
        fun showState(state: State)
        fun startResult(number: BigDecimal)
        fun startTransaction(transaction: Transaction)
    }
}