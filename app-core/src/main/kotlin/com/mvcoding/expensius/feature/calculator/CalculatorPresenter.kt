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

package com.mvcoding.expensius.feature.calculator

import com.mvcoding.expensius.data.DataSource
import com.mvcoding.expensius.feature.calculator.CalculatorPresenter.ResultDestination.TRANSACTION
import com.mvcoding.expensius.feature.calculator.CalculatorPresenter.State.CALCULATE
import com.mvcoding.expensius.feature.calculator.CalculatorPresenter.State.SAVE
import com.mvcoding.expensius.model.AppUser
import com.mvcoding.expensius.model.Money
import com.mvcoding.expensius.model.NullModels.newTransaction
import com.mvcoding.expensius.model.TimestampProvider
import com.mvcoding.expensius.model.Transaction
import com.mvcoding.mvp.Presenter
import rx.Observable
import rx.Observable.merge
import rx.lang.kotlin.PublishSubject
import java.math.BigDecimal

class CalculatorPresenter(
        private val calculator: Calculator,
        private val resultDestination: ResultDestination,
        private val appUserSource: DataSource<AppUser>,
        private val timestampProvider: TimestampProvider,
        initialNumber: BigDecimal? = null) : Presenter<CalculatorPresenter.View>() {

    private var canSave = true

    init {
        initialNumber?.run { calculator.setNumber(this) }
    }

    override fun onViewAttached(view: View) {
        super.onViewAttached(view)

        val saves = PublishSubject<Unit>()
        unsubscribeOnDetach(view.saveRequests().subscribe(saves))
        val expressionChanges = merge(arrayOf(
                view.digit0().doOnNext { calculator.digit0() },
                view.digit1().doOnNext { calculator.digit1() },
                view.digit2().doOnNext { calculator.digit2() },
                view.digit3().doOnNext { calculator.digit3() },
                view.digit4().doOnNext { calculator.digit4() },
                view.digit5().doOnNext { calculator.digit5() },
                view.digit6().doOnNext { calculator.digit6() },
                view.digit7().doOnNext { calculator.digit7() },
                view.digit8().doOnNext { calculator.digit8() },
                view.digit9().doOnNext { calculator.digit9() },
                view.decimalRequests().doOnNext { calculator.decimal() },
                view.addRequests().doOnNext { calculator.add() },
                view.subtractRequests().doOnNext { calculator.subtract() },
                view.multiplyRequests().doOnNext { calculator.multiply() },
                view.divideRequests().doOnNext { calculator.divide() },
                view.clearRequests().doOnNext { calculator.clear() },
                view.deleteRequests().doOnNext { calculator.delete() },
                saves
                        .filter { getCurrentState() == CALCULATE }
                        .map { calculator.calculate() }
                        .doOnNext { calculator.setNumber(it); canSave = false }))

        val alteredExpressions = expressionChanges
                .startWith(Unit)
                .map { calculator.getExpression() }
                .doOnNext { view.showState(getCurrentState()) }
                .doOnNext { view.showExpression(it) }
                .map { calculator.calculate() }

        saves.filter { val result = canSave; canSave = true; result }
                .withLatestFrom(alteredExpressions, { _, number -> number })
                .withLatestFrom(appUserSource.data(), { number, appUser -> displayResult(view, appUser, number) })
                .subscribeUntilDetached()
    }

    private fun getCurrentState() = if (calculator.isEmptyOrSingleNumber()) SAVE else CALCULATE

    private fun displayResult(view: View, appUser: AppUser, amount: BigDecimal) =
            if (resultDestination == TRANSACTION) view.displayTransaction(newTransaction(amount, appUser))
            else view.displayResult(amount)

    private fun newTransaction(amount: BigDecimal, appUser: AppUser) =
            newTransaction(timestampProvider.currentTimestamp(), Money(amount, appUser.settings.mainCurrency))

    enum class State { SAVE, CALCULATE }

    enum class ResultDestination { BACK, TRANSACTION }

    interface View : Presenter.View {
        fun digit0(): Observable<Unit>
        fun digit1(): Observable<Unit>
        fun digit2(): Observable<Unit>
        fun digit3(): Observable<Unit>
        fun digit4(): Observable<Unit>
        fun digit5(): Observable<Unit>
        fun digit6(): Observable<Unit>
        fun digit7(): Observable<Unit>
        fun digit8(): Observable<Unit>
        fun digit9(): Observable<Unit>
        fun decimalRequests(): Observable<Unit>
        fun addRequests(): Observable<Unit>
        fun subtractRequests(): Observable<Unit>
        fun multiplyRequests(): Observable<Unit>
        fun divideRequests(): Observable<Unit>
        fun deleteRequests(): Observable<Unit>
        fun clearRequests(): Observable<Unit>
        fun saveRequests(): Observable<Unit>

        fun showExpression(expression: String)
        fun showState(state: State)
        fun displayResult(number: BigDecimal)
        fun displayTransaction(transaction: Transaction)
    }
}