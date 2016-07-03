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
import com.mvcoding.expensius.feature.calculator.CalculatorPresenter.ResultDestination.BACK
import com.mvcoding.expensius.feature.calculator.CalculatorPresenter.ResultDestination.TRANSACTION
import com.mvcoding.expensius.feature.calculator.CalculatorPresenter.State.CALCULATE
import com.mvcoding.expensius.feature.calculator.CalculatorPresenter.State.SAVE
import com.mvcoding.expensius.model.Currency
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argThat
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.times
import rx.lang.kotlin.PublishSubject
import java.math.BigDecimal

class CalculatorPresenterTest {
    val resultDestination = BACK
    val digit0Observable = PublishSubject<Unit>()
    val digit1Observable = PublishSubject<Unit>()
    val digit2Observable = PublishSubject<Unit>()
    val digit3Observable = PublishSubject<Unit>()
    val digit4Observable = PublishSubject<Unit>()
    val digit5Observable = PublishSubject<Unit>()
    val digit6Observable = PublishSubject<Unit>()
    val digit7Observable = PublishSubject<Unit>()
    val digit8Observable = PublishSubject<Unit>()
    val digit9Observable = PublishSubject<Unit>()
    val decimalObservable = PublishSubject<Unit>()
    val addObservable = PublishSubject<Unit>()
    val subtractObservable = PublishSubject<Unit>()
    val multiplyObservable = PublishSubject<Unit>()
    val divideObservable = PublishSubject<Unit>()
    val deleteObservable = PublishSubject<Unit>()
    val clearObservable = PublishSubject<Unit>()
    val saveObservable = PublishSubject<Unit>()
    val settings = mock<Settings>()
    val view = mock<CalculatorPresenter.View>()
    val calculator = Calculator(Interpreter())
    val presenter = CalculatorPresenter(calculator, resultDestination, settings)

    @Before
    fun setUp() {
        whenever(view.onDigit0()).thenReturn(digit0Observable)
        whenever(view.onDigit1()).thenReturn(digit1Observable)
        whenever(view.onDigit2()).thenReturn(digit2Observable)
        whenever(view.onDigit3()).thenReturn(digit3Observable)
        whenever(view.onDigit4()).thenReturn(digit4Observable)
        whenever(view.onDigit5()).thenReturn(digit5Observable)
        whenever(view.onDigit6()).thenReturn(digit6Observable)
        whenever(view.onDigit7()).thenReturn(digit7Observable)
        whenever(view.onDigit8()).thenReturn(digit8Observable)
        whenever(view.onDigit9()).thenReturn(digit9Observable)
        whenever(view.onDecimal()).thenReturn(decimalObservable)
        whenever(view.onAdd()).thenReturn(addObservable)
        whenever(view.onSubtract()).thenReturn(subtractObservable)
        whenever(view.onMultiply()).thenReturn(multiplyObservable)
        whenever(view.onDivide()).thenReturn(divideObservable)
        whenever(view.onDelete()).thenReturn(deleteObservable)
        whenever(view.onClear()).thenReturn(clearObservable)
        whenever(view.onSave()).thenReturn(saveObservable)
        whenever(settings.mainCurrency).thenReturn(Currency("GBP"))
    }

    @Test
    fun initiallyShowsSaveState() {
        presenter.attach(view)

        verify(view).showState(SAVE)
    }

    @Test
    fun showsEmptyExpressionWhenThereIsNoInitialNumber() {
        presenter.attach(view)

        verify(view).showExpression("")
    }

    @Test
    fun showsInitialNumberWhenThereIsInitialNumber() {
        val presenter = CalculatorPresenter(calculator, resultDestination, settings, BigDecimal.TEN)

        presenter.attach(view)

        verify(view).showExpression("10")
    }

    @Test
    fun showsInitialNumberAfterReattach() {
        val presenter = CalculatorPresenter(calculator, resultDestination, settings, BigDecimal.TEN)
        presenter.attach(view)

        presenter.detach(view)
        presenter.attach(view)

        verify(view, times(2)).showExpression("10")
    }

    @Test
    fun clearsExpression() {
        val presenter = CalculatorPresenter(calculator, resultDestination, settings, BigDecimal.TEN)
        presenter.attach(view)

        clear()

        verify(view).showExpression("")
    }

    @Test
    fun showsUpdatedExpressionAfterReattach() {
        val presenter = CalculatorPresenter(calculator, resultDestination, settings, BigDecimal.TEN)
        presenter.attach(view)
        clear()

        presenter.detach(view)
        presenter.attach(view)

        verify(view, times(2)).showExpression("")
    }

    @Test
    fun deletesLastSymbolFromExpression() {
        val presenter = CalculatorPresenter(calculator, resultDestination, settings, BigDecimal.TEN)
        presenter.attach(view)

        delete()

        verify(view).showExpression("1")
    }

    @Test
    fun deletesLastSymbol() {
        val presenter = CalculatorPresenter(calculator, resultDestination, settings, BigDecimal.ONE)
        presenter.attach(view)

        delete()

        verify(view).showExpression("")
    }

    @Test
    fun addsDigitWhenExpressionIsEmpty() {
        presenter.attach(view)

        clear()
        digit0()
        verify(view).showExpression("0")

        clear()
        digit1()
        verify(view).showExpression("1")

        clear()
        digit2()
        verify(view).showExpression("2")

        clear()
        digit3()
        verify(view).showExpression("3")

        clear()
        digit4()
        verify(view).showExpression("4")

        clear()
        digit5()
        verify(view).showExpression("5")

        clear()
        digit6()
        verify(view).showExpression("6")

        clear()
        digit7()
        verify(view).showExpression("7")

        clear()
        digit8()
        verify(view).showExpression("8")

        clear()
        digit9()
        verify(view).showExpression("9")
    }

    @Test
    fun addsDigitWhenExpressionEndsWithNumber() {
        presenter.attach(view)
        digit0()

        digit0()
        digit1()
        digit2()
        digit3()
        digit4()
        digit5()
        digit6()
        digit7()
        digit8()
        digit9()

        verify(view).showExpression("00123456789")
    }

    @Test
    fun addsDecimalWhenExpressionIsEmpty() {
        presenter.attach(view)

        decimal()

        verify(view).showExpression(".")
    }

    @Test
    fun addsDigitWhenExpressionEndsWithDecimal() {
        presenter.attach(view)

        clear()
        decimal()
        digit0()
        verify(view).showExpression(".0")

        clear()
        decimal()
        digit1()
        verify(view).showExpression(".1")

        clear()
        decimal()
        digit2()
        verify(view).showExpression(".2")

        clear()
        decimal()
        digit3()
        verify(view).showExpression(".3")

        clear()
        decimal()
        digit4()
        verify(view).showExpression(".4")

        clear()
        decimal()
        digit5()
        verify(view).showExpression(".5")

        clear()
        decimal()
        digit6()
        verify(view).showExpression(".6")

        clear()
        decimal()
        digit7()
        verify(view).showExpression(".7")

        clear()
        decimal()
        digit8()
        verify(view).showExpression(".8")

        clear()
        decimal()
        digit9()
        verify(view).showExpression(".9")
    }

    @Test
    fun addsSubtractOperatorWhenExpressionIsEmpty() {
        presenter.attach(view)

        subtract()

        verify(view).showExpression("-")
    }

    @Test
    fun addsDigitWhenExpressionEndsWithOperator() {
        presenter.attach(view)

        clear()
        subtract()
        digit0()
        verify(view).showExpression("-0")

        clear()
        subtract()
        digit1()
        verify(view).showExpression("-1")

        clear()
        subtract()
        digit2()
        verify(view).showExpression("-2")

        clear()
        subtract()
        digit3()
        verify(view).showExpression("-3")

        clear()
        subtract()
        digit4()
        verify(view).showExpression("-4")

        clear()
        subtract()
        digit5()
        verify(view).showExpression("-5")

        clear()
        subtract()
        digit6()
        verify(view).showExpression("-6")

        clear()
        subtract()
        digit7()
        verify(view).showExpression("-7")

        clear()
        subtract()
        digit8()
        verify(view).showExpression("-8")

        clear()
        subtract()
        digit9()
        verify(view).showExpression("-9")
    }

    @Test
    fun divideIsIgnoredWhenExpressionIsEmpty() {
        presenter.attach(view)

        divide()

        verify(view, times(2)).showExpression("")
    }

    @Test
    fun divideIsIgnoredWhenExpressionIsOnlySubtractOperator() {
        presenter.attach(view)
        subtract()

        divide()

        verify(view, times(2)).showExpression("-")
    }

    @Test
    fun addsDivideOperatorWhenExpressionEndsWithNumber() {
        presenter.attach(view)
        digit1()

        divide()

        verify(view).showExpression("1/")
    }

    @Test
    fun addsSubtractOperatorWhenExpressionEndsWithNumber() {
        presenter.attach(view)
        digit1()

        subtract()

        verify(view).showExpression("1-")
    }

    @Test
    fun divideReplacesPreviousOperatorWhenExpressionEndsWithOperator() {
        presenter.attach(view)
        digit1()
        subtract()

        divide()

        verify(view).showExpression("1/")
    }

    @Test
    fun addsDivideOperatorWhenExpressionEndsWithDecimal() {
        presenter.attach(view)
        decimal()

        divide()

        verify(view).showExpression("./")
    }

    @Test
    fun multiplyIsIgnoredWhenExpressionIsEmpty() {
        presenter.attach(view)

        multiply()

        verify(view, times(2)).showExpression("")
    }

    @Test
    fun multiplyIsIgnoredWhenExpressionIsOnlySubtractOperator() {
        presenter.attach(view)
        subtract()

        multiply()

        verify(view, times(2)).showExpression("-")
    }

    @Test
    fun multiplyReplacesPreviousOperatorWhenExpressionEndsWithOperator() {
        presenter.attach(view)
        digit1()
        subtract()

        multiply()

        verify(view).showExpression("1*")
    }

    @Test
    fun addsMultiplyOperatorWhenExpressionEndsWithNumber() {
        presenter.attach(view)
        digit1()

        multiply()

        verify(view).showExpression("1*")
    }

    @Test
    fun addsMultiplyOperatorWhenExpressionEndsWithDecimal() {
        presenter.attach(view)
        decimal()

        multiply()

        verify(view).showExpression(".*")
    }

    @Test
    fun subtractReplacesPreviousOperatorWhenExpressionEndsWithOperator() {
        presenter.attach(view)
        digit1()
        multiply()

        subtract()

        verify(view).showExpression("1-")
    }

    @Test
    fun addsSubtractOperatorWhenExpressionEndsWithDecimal() {
        presenter.attach(view)
        decimal()

        subtract()

        verify(view).showExpression(".-")
    }

    @Test
    fun addIsIgnoredWhenExpressionIsEmpty() {
        presenter.attach(view)

        add()

        verify(view, times(2)).showExpression("")
    }

    @Test
    fun addIsIgnoredWhenExpressionIsOnlySubtractOperator() {
        presenter.attach(view)
        subtract()

        add()

        verify(view, times(2)).showExpression("-")
    }

    @Test
    fun addReplacesPreviousOperatorWhenExpressionEndsWithOperator() {
        presenter.attach(view)
        digit1()
        subtract()

        add()

        verify(view).showExpression("1+")
    }

    @Test
    fun addsAddOperatorWhenExpressionEndsWithNumber() {
        presenter.attach(view)
        digit1()

        add()

        verify(view).showExpression("1+")
    }

    @Test
    fun addsAddOperatorWhenExpressionEndsWithDecimal() {
        presenter.attach(view)
        decimal()

        add()

        verify(view).showExpression(".+")
    }

    @Test
    fun addsDecimalWhenExpressionHasOneNumberWithoutDecimal() {
        presenter.attach(view)
        digit1()

        decimal()

        verify(view).showExpression("1.")
    }

    @Test
    fun decimalIsIgnoredWhenExpressionHasOneNumberThatAlreadyHasDecimal() {
        presenter.attach(view)
        digit1()
        decimal()
        digit2()

        decimal()

        verify(view, times(2)).showExpression("1.2")
    }

    @Test
    fun addsDecimalWhenExpressionHasMoreThanOneNumberAndLastOneIsWithoutDecimal() {
        presenter.attach(view)
        digit1()
        decimal()
        digit2()
        add()
        digit1()

        decimal()

        verify(view).showExpression("1.2+1.")
    }

    @Test
    fun decimalIsIgnoredWhenExpressionHasMoreThanOneNumberAndLastNumberAlreadyHasDecimal() {
        presenter.attach(view)
        digit1()
        decimal()
        digit2()
        add()
        digit1()
        decimal()
        digit2()

        decimal()

        verify(view, times(2)).showExpression("1.2+1.2")
    }

    @Test
    fun addsDecimalWhenExpressionEndsWithAnOperator() {
        presenter.attach(view)
        subtract()

        decimal()

        verify(view).showExpression("-.")
    }

    @Test
    fun showsCalculateStateWhenExpressionHasAtLeastTwoNumbers() {
        presenter.attach(view)
        digit1()
        subtract()
        digit1()

        verify(view).showState(CALCULATE)
    }

    @Test
    fun showsCalculateStateAfterReattach() {
        presenter.attach(view)
        digit1()
        subtract()
        digit1()

        presenter.detach(view)
        presenter.attach(view)

        verify(view, times(2)).showState(CALCULATE)
    }

    @Test
    fun displaysResultFromEvaluatedExpression() {
        presenter.attach(view)
        digit1()
        add()
        digit1()

        save()

        verify(view).showExpression("2")
        verify(view, never()).startResult(any())
        verify(view, never()).startTransaction(any())
    }

    @Test
    fun startsResultWithCurrentlyDisplayedNumber() {
        presenter.attach(view)
        digit1()
        add()
        digit1()
        save()

        save()

        verify(view).startResult(BigDecimal(2))
    }

    @Test
    fun startsTransactionWithCurrentlyDisplayedNumber() {
        val presenter = CalculatorPresenter(calculator, TRANSACTION, settings)
        presenter.attach(view)
        digit1()
        add()
        digit1()
        save()

        save()

        verify(view).startTransaction(argThat { amount == BigDecimal(2) })
    }

    @Test
    fun savesAreOnlySubscribedOnce() {
        presenter.attach(view)

        verify(view, times(1)).onSave()
    }

    fun digit0() {
        digit0Observable.onNext(Unit)
    }

    fun digit1() {
        digit1Observable.onNext(Unit)
    }

    fun digit2() {
        digit2Observable.onNext(Unit)
    }

    fun digit3() {
        digit3Observable.onNext(Unit)
    }

    fun digit4() {
        digit4Observable.onNext(Unit)
    }

    fun digit5() {
        digit5Observable.onNext(Unit)
    }

    fun digit6() {
        digit6Observable.onNext(Unit)
    }

    fun digit7() {
        digit7Observable.onNext(Unit)
    }

    fun digit8() {
        digit8Observable.onNext(Unit)
    }

    fun digit9() {
        digit9Observable.onNext(Unit)
    }

    fun decimal() {
        decimalObservable.onNext(Unit)
    }

    fun add() {
        addObservable.onNext(Unit)
    }

    fun subtract() {
        subtractObservable.onNext(Unit)
    }

    fun multiply() {
        multiplyObservable.onNext(Unit)
    }

    fun divide() {
        divideObservable.onNext(Unit)
    }

    fun delete() {
        deleteObservable.onNext(Unit)
    }

    fun clear() {
        clearObservable.onNext(Unit)
    }

    fun save() {
        saveObservable.onNext(Unit)
    }
}