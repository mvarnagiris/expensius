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
import com.mvcoding.expensius.feature.transaction.Currency
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.mockito.BDDMockito.*
import rx.subjects.PublishSubject
import java.math.BigDecimal

class CalculatorPresenterTest {
    val resultDestination = BACK
    val digit0Observable = PublishSubject.create<Unit>()
    val digit1Observable = PublishSubject.create<Unit>()
    val digit2Observable = PublishSubject.create<Unit>()
    val digit3Observable = PublishSubject.create<Unit>()
    val digit4Observable = PublishSubject.create<Unit>()
    val digit5Observable = PublishSubject.create<Unit>()
    val digit6Observable = PublishSubject.create<Unit>()
    val digit7Observable = PublishSubject.create<Unit>()
    val digit8Observable = PublishSubject.create<Unit>()
    val digit9Observable = PublishSubject.create<Unit>()
    val decimalObservable = PublishSubject.create<Unit>()
    val addObservable = PublishSubject.create<Unit>()
    val subtractObservable = PublishSubject.create<Unit>()
    val multiplyObservable = PublishSubject.create<Unit>()
    val divideObservable = PublishSubject.create<Unit>()
    val deleteObservable = PublishSubject.create<Unit>()
    val clearObservable = PublishSubject.create<Unit>()
    val calculateObservable = PublishSubject.create<Unit>()
    val saveObservable = PublishSubject.create<Unit>()
    val settings = mock(Settings::class.java)
    val view = mock(CalculatorPresenter.View::class.java)
    val calculator = Calculator(Interpreter())
    val presenter = CalculatorPresenter(calculator, resultDestination, settings)

    @Before
    fun setUp() {
        given(view.onDigit0()).willReturn(digit0Observable)
        given(view.onDigit1()).willReturn(digit1Observable)
        given(view.onDigit2()).willReturn(digit2Observable)
        given(view.onDigit3()).willReturn(digit3Observable)
        given(view.onDigit4()).willReturn(digit4Observable)
        given(view.onDigit5()).willReturn(digit5Observable)
        given(view.onDigit6()).willReturn(digit6Observable)
        given(view.onDigit7()).willReturn(digit7Observable)
        given(view.onDigit8()).willReturn(digit8Observable)
        given(view.onDigit9()).willReturn(digit9Observable)
        given(view.onDecimal()).willReturn(decimalObservable)
        given(view.onAdd()).willReturn(addObservable)
        given(view.onSubtract()).willReturn(subtractObservable)
        given(view.onMultiply()).willReturn(multiplyObservable)
        given(view.onDivide()).willReturn(divideObservable)
        given(view.onDelete()).willReturn(deleteObservable)
        given(view.onClear()).willReturn(clearObservable)
        given(view.onCalculate()).willReturn(calculateObservable)
        given(view.onSave()).willReturn(saveObservable)
        given(settings.getMainCurrency()).willReturn(Currency("GBP"))
    }

    @Test
    fun initiallyShowsSaveState() {
        presenter.onAttachView(view)

        verify(view).showState(SAVE)
    }

    @Test
    fun showsEmptyExpressionWhenThereIsNoInitialNumber() {
        presenter.onAttachView(view)

        verify(view).showExpression("")
    }

    @Test
    fun showsInitialNumberWhenThereIsInitialNumber() {
        val presenter = CalculatorPresenter(calculator, resultDestination, settings, BigDecimal.TEN)

        presenter.onAttachView(view)

        verify(view).showExpression("10")
    }

    @Test
    fun showsInitialNumberAfterReattach() {
        val presenter = CalculatorPresenter(calculator, resultDestination, settings, BigDecimal.TEN)
        presenter.onAttachView(view)

        presenter.onDetachView(view)
        presenter.onAttachView(view)

        verify(view, times(2)).showExpression("10")
    }

    @Test
    fun clearsExpression() {
        val presenter = CalculatorPresenter(calculator, resultDestination, settings, BigDecimal.TEN)
        presenter.onAttachView(view)

        clear()

        verify(view).showExpression("")
    }

    @Test
    fun showsUpdatedExpressionAfterReattach() {
        val presenter = CalculatorPresenter(calculator, resultDestination, settings, BigDecimal.TEN)
        presenter.onAttachView(view)
        clear()

        presenter.onDetachView(view)
        presenter.onAttachView(view)

        verify(view, times(2)).showExpression("")
    }

    @Test
    fun deletesLastSymbolFromExpression() {
        val presenter = CalculatorPresenter(calculator, resultDestination, settings, BigDecimal.TEN)
        presenter.onAttachView(view)

        delete()

        verify(view).showExpression("1")
    }

    @Test
    fun deletesLastSymbol() {
        val presenter = CalculatorPresenter(calculator, resultDestination, settings, BigDecimal.ONE)
        presenter.onAttachView(view)

        delete()

        verify(view).showExpression("")
    }

    @Test
    fun addsDigitWhenExpressionIsEmpty() {
        presenter.onAttachView(view)

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
        presenter.onAttachView(view)
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
        presenter.onAttachView(view)

        decimal()

        verify(view).showExpression(".")
    }

    @Test
    fun addsDigitWhenExpressionEndsWithDecimal() {
        presenter.onAttachView(view)

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
        presenter.onAttachView(view)

        subtract()

        verify(view).showExpression("-")
    }

    @Test
    fun addsDigitWhenExpressionEndsWithOperator() {
        presenter.onAttachView(view)

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
        presenter.onAttachView(view)

        divide()

        verify(view, times(2)).showExpression("")
    }

    @Test
    fun divideIsIgnoredWhenExpressionIsOnlySubtractOperator() {
        presenter.onAttachView(view)
        subtract()

        divide()

        verify(view, times(2)).showExpression("-")
    }

    @Test
    fun addsDivideOperatorWhenExpressionEndsWithNumber() {
        presenter.onAttachView(view)
        digit1()

        divide()

        verify(view).showExpression("1/")
    }

    @Test
    fun addsSubtractOperatorWhenExpressionEndsWithNumber() {
        presenter.onAttachView(view)
        digit1()

        subtract()

        verify(view).showExpression("1-")
    }

    @Test
    fun divideReplacesPreviousOperatorWhenExpressionEndsWithOperator() {
        presenter.onAttachView(view)
        digit1()
        subtract()

        divide()

        verify(view).showExpression("1/")
    }

    @Test
    fun addsDivideOperatorWhenExpressionEndsWithDecimal() {
        presenter.onAttachView(view)
        decimal()

        divide()

        verify(view).showExpression("./")
    }

    @Test
    fun multiplyIsIgnoredWhenExpressionIsEmpty() {
        presenter.onAttachView(view)

        multiply()

        verify(view, times(2)).showExpression("")
    }

    @Test
    fun multiplyIsIgnoredWhenExpressionIsOnlySubtractOperator() {
        presenter.onAttachView(view)
        subtract()

        multiply()

        verify(view, times(2)).showExpression("-")
    }

    @Test
    fun multiplyReplacesPreviousOperatorWhenExpressionEndsWithOperator() {
        presenter.onAttachView(view)
        digit1()
        subtract()

        multiply()

        verify(view).showExpression("1*")
    }

    @Test
    fun addsMultiplyOperatorWhenExpressionEndsWithNumber() {
        presenter.onAttachView(view)
        digit1()

        multiply()

        verify(view).showExpression("1*")
    }

    @Test
    fun addsMultiplyOperatorWhenExpressionEndsWithDecimal() {
        presenter.onAttachView(view)
        decimal()

        multiply()

        verify(view).showExpression(".*")
    }

    @Test
    fun subtractReplacesPreviousOperatorWhenExpressionEndsWithOperator() {
        presenter.onAttachView(view)
        digit1()
        multiply()

        subtract()

        verify(view).showExpression("1-")
    }

    @Test
    fun addsSubtractOperatorWhenExpressionEndsWithDecimal() {
        presenter.onAttachView(view)
        decimal()

        subtract()

        verify(view).showExpression(".-")
    }

    @Test
    fun addIsIgnoredWhenExpressionIsEmpty() {
        presenter.onAttachView(view)

        add()

        verify(view, times(2)).showExpression("")
    }

    @Test
    fun addIsIgnoredWhenExpressionIsOnlySubtractOperator() {
        presenter.onAttachView(view)
        subtract()

        add()

        verify(view, times(2)).showExpression("-")
    }

    @Test
    fun addReplacesPreviousOperatorWhenExpressionEndsWithOperator() {
        presenter.onAttachView(view)
        digit1()
        subtract()

        add()

        verify(view).showExpression("1+")
    }

    @Test
    fun addsAddOperatorWhenExpressionEndsWithNumber() {
        presenter.onAttachView(view)
        digit1()

        add()

        verify(view).showExpression("1+")
    }

    @Test
    fun addsAddOperatorWhenExpressionEndsWithDecimal() {
        presenter.onAttachView(view)
        decimal()

        add()

        verify(view).showExpression(".+")
    }

    @Test
    fun addsDecimalWhenExpressionHasOneNumberWithoutDecimal() {
        presenter.onAttachView(view)
        digit1()

        decimal()

        verify(view).showExpression("1.")
    }

    @Test
    fun decimalIsIgnoredWhenExpressionHasOneNumberThatAlreadyHasDecimal() {
        presenter.onAttachView(view)
        digit1()
        decimal()
        digit2()

        decimal()

        verify(view, times(2)).showExpression("1.2")
    }

    @Test
    fun addsDecimalWhenExpressionHasMoreThanOneNumberAndLastOneIsWithoutDecimal() {
        presenter.onAttachView(view)
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
        presenter.onAttachView(view)
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
        presenter.onAttachView(view)
        subtract()

        decimal()

        verify(view).showExpression("-.")
    }

    @Test
    fun showsCalculateStateWhenExpressionHasAtLeastTwoNumbers() {
        presenter.onAttachView(view)
        digit1()
        subtract()
        digit1()

        verify(view).showState(CALCULATE)
    }

    @Test
    fun showsCalculateStateAfterReattach() {
        presenter.onAttachView(view)
        digit1()
        subtract()
        digit1()

        presenter.onDetachView(view)
        presenter.onAttachView(view)

        verify(view, times(2)).showState(CALCULATE)
    }

    @Test
    fun displaysResultFromEvaluatedExpression() {
        presenter.onAttachView(view)
        digit1()
        add()
        digit1()

        calculate()

        verify(view).showExpression("2")
    }

    @Test
    fun startsResultWithCurrentlyDisplayedNumber() {
        presenter.onAttachView(view)
        digit1()
        add()
        digit1()
        calculate()

        save()

        verify(view).startResult(BigDecimal(2))
    }

    @Ignore
    @Test
    fun startsTransactionWithCurrentlyDisplayedNumber() {
        val presenter = CalculatorPresenter(calculator, TRANSACTION, settings)
        presenter.onAttachView(view)
        digit1()
        add()
        digit1()
        calculate()

        save()

        //        verify(view).startTransaction(any(Transaction::class.java))
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

    fun calculate() {
        calculateObservable.onNext(Unit)
    }

    fun save() {
        saveObservable.onNext(Unit)
    }
}