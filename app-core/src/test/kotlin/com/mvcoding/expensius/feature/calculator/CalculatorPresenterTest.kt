/*
 * Copyright (C) 2018 Mantas Varnagiris.
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

class CalculatorPresenterTest {
//    val resultDestination = BACK
//    val digit0Observable = PublishSubject<Unit>()
//    val digit1Observable = PublishSubject<Unit>()
//    val digit2Observable = PublishSubject<Unit>()
//    val digit3Observable = PublishSubject<Unit>()
//    val digit4Observable = PublishSubject<Unit>()
//    val digit5Observable = PublishSubject<Unit>()
//    val digit6Observable = PublishSubject<Unit>()
//    val digit7Observable = PublishSubject<Unit>()
//    val digit8Observable = PublishSubject<Unit>()
//    val digit9Observable = PublishSubject<Unit>()
//    val decimalObservable = PublishSubject<Unit>()
//    val addObservable = PublishSubject<Unit>()
//    val subtractObservable = PublishSubject<Unit>()
//    val multiplyObservable = PublishSubject<Unit>()
//    val divideObservable = PublishSubject<Unit>()
//    val deleteObservable = PublishSubject<Unit>()
//    val clearObservable = PublishSubject<Unit>()
//    val saveObservable = PublishSubject<Unit>()
//    val view = mock<CalculatorPresenter.View>()
//    val appUserSource = mock<DataSource<AppUser>>()
//    val calculator = Calculator(Interpreter())
//    val timestampProvider = aFixedTimestampProvider()
//    val presenter = CalculatorPresenter(calculator, resultDestination, appUserSource, timestampProvider)
//
//    @Before
//    fun setUp() {
//        whenever(view.digit0()).thenReturn(digit0Observable)
//        whenever(view.digit1()).thenReturn(digit1Observable)
//        whenever(view.digit2()).thenReturn(digit2Observable)
//        whenever(view.digit3()).thenReturn(digit3Observable)
//        whenever(view.digit4()).thenReturn(digit4Observable)
//        whenever(view.digit5()).thenReturn(digit5Observable)
//        whenever(view.digit6()).thenReturn(digit6Observable)
//        whenever(view.digit7()).thenReturn(digit7Observable)
//        whenever(view.digit8()).thenReturn(digit8Observable)
//        whenever(view.digit9()).thenReturn(digit9Observable)
//        whenever(view.decimalRequests()).thenReturn(decimalObservable)
//        whenever(view.addRequests()).thenReturn(addObservable)
//        whenever(view.subtractRequests()).thenReturn(subtractObservable)
//        whenever(view.multiplyRequests()).thenReturn(multiplyObservable)
//        whenever(view.divideRequests()).thenReturn(divideObservable)
//        whenever(view.deleteRequests()).thenReturn(deleteObservable)
//        whenever(view.clearRequests()).thenReturn(clearObservable)
//        whenever(view.saveRequests()).thenReturn(saveObservable)
//        whenever(appUserSource.data()).thenReturn(just(anAppUser()))
//    }
//
//    @Test
//    fun `initially shows save state`() {
//        presenter.attach(view)
//
//        verify(view).showState(SAVE)
//    }
//
//    @Test
//    fun `shows empty expression when there is no initial number`() {
//        presenter.attach(view)
//
//        verify(view).showExpression("")
//    }
//
//    @Test
//    fun `shows initial number when there is initial number`() {
//        val presenter = CalculatorPresenter(calculator, resultDestination, appUserSource, timestampProvider, BigDecimal.TEN)
//
//        presenter.attach(view)
//
//        verify(view).showExpression("10")
//    }
//
//    @Test
//    fun `shows initial number after reattach`() {
//        val presenter = CalculatorPresenter(calculator, resultDestination, appUserSource, timestampProvider, BigDecimal.TEN)
//        presenter.attach(view)
//
//        presenter.detach(view)
//        presenter.attach(view)
//
//        verify(view, times(2)).showExpression("10")
//    }
//
//    @Test
//    fun `clears expression`() {
//        val presenter = CalculatorPresenter(calculator, resultDestination, appUserSource, timestampProvider, BigDecimal.TEN)
//        presenter.attach(view)
//
//        clear()
//
//        verify(view).showExpression("")
//    }
//
//    @Test
//    fun `shows updated expression after reattach`() {
//        val presenter = CalculatorPresenter(calculator, resultDestination, appUserSource, timestampProvider, BigDecimal.TEN)
//        presenter.attach(view)
//        clear()
//
//        presenter.detach(view)
//        presenter.attach(view)
//
//        verify(view, times(2)).showExpression("")
//    }
//
//    @Test
//    fun `deletes last symbol from expression`() {
//        val presenter = CalculatorPresenter(calculator, resultDestination, appUserSource, timestampProvider, BigDecimal.TEN)
//        presenter.attach(view)
//
//        delete()
//
//        verify(view).showExpression("1")
//    }
//
//    @Test
//    fun `deletes last symbol`() {
//        val presenter = CalculatorPresenter(calculator, resultDestination, appUserSource, timestampProvider, BigDecimal.ONE)
//        presenter.attach(view)
//
//        delete()
//
//        verify(view).showExpression("")
//    }
//
//    @Test
//    fun `adds digit when expression is empty`() {
//        presenter.attach(view)
//
//        clear()
//        digit0()
//        verify(view).showExpression("0")
//
//        clear()
//        digit1()
//        verify(view).showExpression("1")
//
//        clear()
//        digit2()
//        verify(view).showExpression("2")
//
//        clear()
//        digit3()
//        verify(view).showExpression("3")
//
//        clear()
//        digit4()
//        verify(view).showExpression("4")
//
//        clear()
//        digit5()
//        verify(view).showExpression("5")
//
//        clear()
//        digit6()
//        verify(view).showExpression("6")
//
//        clear()
//        digit7()
//        verify(view).showExpression("7")
//
//        clear()
//        digit8()
//        verify(view).showExpression("8")
//
//        clear()
//        digit9()
//        verify(view).showExpression("9")
//    }
//
//    @Test
//    fun `adds digit when expression ends with number`() {
//        presenter.attach(view)
//        digit0()
//
//        digit0()
//        digit1()
//        digit2()
//        digit3()
//        digit4()
//        digit5()
//        digit6()
//        digit7()
//        digit8()
//        digit9()
//
//        verify(view).showExpression("00123456789")
//    }
//
//    @Test
//    fun `adds decimal when expression is empty`() {
//        presenter.attach(view)
//
//        decimal()
//
//        verify(view).showExpression(".")
//    }
//
//    @Test
//    fun `adds digit when expression ends with decimal`() {
//        presenter.attach(view)
//
//        clear()
//        decimal()
//        digit0()
//        verify(view).showExpression(".0")
//
//        clear()
//        decimal()
//        digit1()
//        verify(view).showExpression(".1")
//
//        clear()
//        decimal()
//        digit2()
//        verify(view).showExpression(".2")
//
//        clear()
//        decimal()
//        digit3()
//        verify(view).showExpression(".3")
//
//        clear()
//        decimal()
//        digit4()
//        verify(view).showExpression(".4")
//
//        clear()
//        decimal()
//        digit5()
//        verify(view).showExpression(".5")
//
//        clear()
//        decimal()
//        digit6()
//        verify(view).showExpression(".6")
//
//        clear()
//        decimal()
//        digit7()
//        verify(view).showExpression(".7")
//
//        clear()
//        decimal()
//        digit8()
//        verify(view).showExpression(".8")
//
//        clear()
//        decimal()
//        digit9()
//        verify(view).showExpression(".9")
//    }
//
//    @Test
//    fun `adds subtract operator when expression is empty`() {
//        presenter.attach(view)
//
//        subtract()
//
//        verify(view).showExpression("-")
//    }
//
//    @Test
//    fun `adds digit when expression ends with operator`() {
//        presenter.attach(view)
//
//        clear()
//        subtract()
//        digit0()
//        verify(view).showExpression("-0")
//
//        clear()
//        subtract()
//        digit1()
//        verify(view).showExpression("-1")
//
//        clear()
//        subtract()
//        digit2()
//        verify(view).showExpression("-2")
//
//        clear()
//        subtract()
//        digit3()
//        verify(view).showExpression("-3")
//
//        clear()
//        subtract()
//        digit4()
//        verify(view).showExpression("-4")
//
//        clear()
//        subtract()
//        digit5()
//        verify(view).showExpression("-5")
//
//        clear()
//        subtract()
//        digit6()
//        verify(view).showExpression("-6")
//
//        clear()
//        subtract()
//        digit7()
//        verify(view).showExpression("-7")
//
//        clear()
//        subtract()
//        digit8()
//        verify(view).showExpression("-8")
//
//        clear()
//        subtract()
//        digit9()
//        verify(view).showExpression("-9")
//    }
//
//    @Test
//    fun `divide is ignored when expression is empty`() {
//        presenter.attach(view)
//
//        divide()
//
//        verify(view, times(2)).showExpression("")
//    }
//
//    @Test
//    fun `divide is ignored when expression is only subtract operator`() {
//        presenter.attach(view)
//        subtract()
//
//        divide()
//
//        verify(view, times(2)).showExpression("-")
//    }
//
//    @Test
//    fun `adds divide operator when expression ends with number`() {
//        presenter.attach(view)
//        digit1()
//
//        divide()
//
//        verify(view).showExpression("1/")
//    }
//
//    @Test
//    fun `adds subtract operator when expression ends with number`() {
//        presenter.attach(view)
//        digit1()
//
//        subtract()
//
//        verify(view).showExpression("1-")
//    }
//
//    @Test
//    fun `divide replaces previous operator when expression ends with operator`() {
//        presenter.attach(view)
//        digit1()
//        subtract()
//
//        divide()
//
//        verify(view).showExpression("1/")
//    }
//
//    @Test
//    fun `adds divide operator when expression ends with decimal`() {
//        presenter.attach(view)
//        decimal()
//
//        divide()
//
//        verify(view).showExpression("./")
//    }
//
//    @Test
//    fun `multiply is ignored when expression is empty`() {
//        presenter.attach(view)
//
//        multiply()
//
//        verify(view, times(2)).showExpression("")
//    }
//
//    @Test
//    fun `multiply is ignored when expression is only subtract operator`() {
//        presenter.attach(view)
//        subtract()
//
//        multiply()
//
//        verify(view, times(2)).showExpression("-")
//    }
//
//    @Test
//    fun `multiply replaces previous operator when expression ends with operator`() {
//        presenter.attach(view)
//        digit1()
//        subtract()
//
//        multiply()
//
//        verify(view).showExpression("1*")
//    }
//
//    @Test
//    fun `adds multiply operator when expression ends with number`() {
//        presenter.attach(view)
//        digit1()
//
//        multiply()
//
//        verify(view).showExpression("1*")
//    }
//
//    @Test
//    fun `adds multiply operator when expression ends with decimal`() {
//        presenter.attach(view)
//        decimal()
//
//        multiply()
//
//        verify(view).showExpression(".*")
//    }
//
//    @Test
//    fun `subtract replaces previous operator when expression ends with operator`() {
//        presenter.attach(view)
//        digit1()
//        multiply()
//
//        subtract()
//
//        verify(view).showExpression("1-")
//    }
//
//    @Test
//    fun `adds subtract operator when expression ends with decimal`() {
//        presenter.attach(view)
//        decimal()
//
//        subtract()
//
//        verify(view).showExpression(".-")
//    }
//
//    @Test
//    fun `add is ignored when expression is empty`() {
//        presenter.attach(view)
//
//        add()
//
//        verify(view, times(2)).showExpression("")
//    }
//
//    @Test
//    fun `add is ignored when expression is only subtract operator`() {
//        presenter.attach(view)
//        subtract()
//
//        add()
//
//        verify(view, times(2)).showExpression("-")
//    }
//
//    @Test
//    fun `add replaces previous operator when expression ends with operator`() {
//        presenter.attach(view)
//        digit1()
//        subtract()
//
//        add()
//
//        verify(view).showExpression("1+")
//    }
//
//    @Test
//    fun `adds add operator when expression ends with number`() {
//        presenter.attach(view)
//        digit1()
//
//        add()
//
//        verify(view).showExpression("1+")
//    }
//
//    @Test
//    fun `adds add operator when expression ends with decimal`() {
//        presenter.attach(view)
//        decimal()
//
//        add()
//
//        verify(view).showExpression(".+")
//    }
//
//    @Test
//    fun `adds decimal when expression has one number without decimal`() {
//        presenter.attach(view)
//        digit1()
//
//        decimal()
//
//        verify(view).showExpression("1.")
//    }
//
//    @Test
//    fun `decimal is ignored when expression has one number that already has decimal`() {
//        presenter.attach(view)
//        digit1()
//        decimal()
//        digit2()
//
//        decimal()
//
//        verify(view, times(2)).showExpression("1.2")
//    }
//
//    @Test
//    fun `adds decimal when expression has more than one number and last one is without decimal`() {
//        presenter.attach(view)
//        digit1()
//        decimal()
//        digit2()
//        add()
//        digit1()
//
//        decimal()
//
//        verify(view).showExpression("1.2+1.")
//    }
//
//    @Test
//    fun `decimal is ignored when expression has more than one number and last number already has decimal`() {
//        presenter.attach(view)
//        digit1()
//        decimal()
//        digit2()
//        add()
//        digit1()
//        decimal()
//        digit2()
//
//        decimal()
//
//        verify(view, times(2)).showExpression("1.2+1.2")
//    }
//
//    @Test
//    fun `adds decimal when expression ends with an operator`() {
//        presenter.attach(view)
//        subtract()
//
//        decimal()
//
//        verify(view).showExpression("-.")
//    }
//
//    @Test
//    fun `shows calculate state when expression has at least two numbers`() {
//        presenter.attach(view)
//        digit1()
//        subtract()
//        digit1()
//
//        verify(view).showState(CALCULATE)
//    }
//
//    @Test
//    fun `shows calculate state after reattach`() {
//        presenter.attach(view)
//        digit1()
//        subtract()
//        digit1()
//
//        presenter.detach(view)
//        presenter.attach(view)
//
//        verify(view, times(2)).showState(CALCULATE)
//    }
//
//    @Test
//    fun `displays result from evaluated expression`() {
//        presenter.attach(view)
//        digit1()
//        add()
//        digit1()
//
//        save()
//
//        verify(view).showExpression("2")
//        verify(view, never()).displayResult(any())
//        verify(view, never()).displayTransaction(any())
//    }
//
//    @Test
//    fun `starts result with currently displayed number`() {
//        presenter.attach(view)
//        digit1()
//        add()
//        digit1()
//        save()
//
//        save()
//
//        verify(view).displayResult(BigDecimal(2))
//    }
//
//    @Test
//    fun `starts transaction with currently displayed number`() {
//        val appUser = anAppUser()
//        whenever(appUserSource.data()).thenReturn(just(appUser))
//        val presenter = CalculatorPresenter(calculator, TRANSACTION, appUserSource, timestampProvider)
//        presenter.attach(view)
//        digit1()
//        add()
//        digit1()
//        save()
//
//        save()
//
//        verify(view).displayTransaction(newTransaction(timestampProvider.currentTimestamp(), Money(BigDecimal(2), appUser.settings.mainCurrency)))
//    }
//
//    @Test
//    fun `saves are only subscribed once`() {
//        presenter.attach(view)
//
//        verify(view, times(1)).saveRequests()
//    }
//
//    fun digit0() = digit0Observable.onNext(Unit)
//    fun digit1() = digit1Observable.onNext(Unit)
//    fun digit2() = digit2Observable.onNext(Unit)
//    fun digit3() = digit3Observable.onNext(Unit)
//    fun digit4() = digit4Observable.onNext(Unit)
//    fun digit5() = digit5Observable.onNext(Unit)
//    fun digit6() = digit6Observable.onNext(Unit)
//    fun digit7() = digit7Observable.onNext(Unit)
//    fun digit8() = digit8Observable.onNext(Unit)
//    fun digit9() = digit9Observable.onNext(Unit)
//    fun decimal() = decimalObservable.onNext(Unit)
//    fun add() = addObservable.onNext(Unit)
//    fun subtract() = subtractObservable.onNext(Unit)
//    fun multiply() = multiplyObservable.onNext(Unit)
//    fun divide() = divideObservable.onNext(Unit)
//    fun delete() = deleteObservable.onNext(Unit)
//    fun clear() = clearObservable.onNext(Unit)
//    fun save() = saveObservable.onNext(Unit)
}