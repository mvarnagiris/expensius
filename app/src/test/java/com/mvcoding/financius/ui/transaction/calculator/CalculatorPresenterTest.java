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

package com.mvcoding.financius.ui.transaction.calculator;

import com.mvcoding.financius.ui.BasePresenterTest;
import com.mvcoding.financius.util.rx.Event;

import org.junit.Test;
import org.mockito.Mock;

import java.math.BigDecimal;

import rx.subjects.PublishSubject;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CalculatorPresenterTest extends BasePresenterTest<CalculatorPresenter, CalculatorPresenter.View> {
    private final PublishSubject<Event> click0 = PublishSubject.create();
    private final PublishSubject<Event> click1 = PublishSubject.create();
    private final PublishSubject<Event> click2 = PublishSubject.create();
    private final PublishSubject<Event> click3 = PublishSubject.create();
    private final PublishSubject<Event> click4 = PublishSubject.create();
    private final PublishSubject<Event> click5 = PublishSubject.create();
    private final PublishSubject<Event> click6 = PublishSubject.create();
    private final PublishSubject<Event> click7 = PublishSubject.create();
    private final PublishSubject<Event> click8 = PublishSubject.create();
    private final PublishSubject<Event> click9 = PublishSubject.create();
    private final PublishSubject<Event> clickAdd = PublishSubject.create();
    private final PublishSubject<Event> clickSub = PublishSubject.create();
    private final PublishSubject<Event> clickMul = PublishSubject.create();
    private final PublishSubject<Event> clickDiv = PublishSubject.create();
    private final PublishSubject<Event> clickDecimal = PublishSubject.create();
    private final PublishSubject<Event> clickClear = PublishSubject.create();
    private final PublishSubject<Event> clickDelete = PublishSubject.create();
    private final PublishSubject<Event> clickEquals = PublishSubject.create();
    private final PublishSubject<BigDecimal> numberChange = PublishSubject.create();

    @Mock private Calculator calculator;

    @Override protected CalculatorPresenter createPresenter() {
        return new CalculatorPresenter(calculator);
    }

    @Override protected CalculatorPresenter.View createView() {
        final CalculatorPresenter.View view = mock(CalculatorPresenter.View.class);
        when(view.on0Number()).thenReturn(click0);
        when(view.on1Number()).thenReturn(click1);
        when(view.on2Number()).thenReturn(click2);
        when(view.on3Number()).thenReturn(click3);
        when(view.on4Number()).thenReturn(click4);
        when(view.on5Number()).thenReturn(click5);
        when(view.on6Number()).thenReturn(click6);
        when(view.on7Number()).thenReturn(click7);
        when(view.on8Number()).thenReturn(click8);
        when(view.on9Number()).thenReturn(click9);
        when(view.onAdd()).thenReturn(clickAdd);
        when(view.onSubtract()).thenReturn(clickSub);
        when(view.onMultiply()).thenReturn(clickMul);
        when(view.onDivide()).thenReturn(clickDiv);
        when(view.onDecimal()).thenReturn(clickDecimal);
        when(view.onDelete()).thenReturn(clickDelete);
        when(view.onClear()).thenReturn(clickClear);
        when(view.onEquals()).thenReturn(clickEquals);
        when(view.onNumberChange()).thenReturn(numberChange);
        return view;
    }

    @Test public void on0Number_callsCalculatorAndShowsExpression() {
        presenterOnViewAttached();

        performEvent(click0);

        verify(calculator).digit0();
        verify(view).showExpression(any(String.class));
    }

    @Test public void on1Number_callsCalculatorAndShowsExpression() {
        presenterOnViewAttached();

        performEvent(click1);

        verify(calculator).digit1();
        verify(view).showExpression(any(String.class));
    }

    @Test public void on2Number_callsCalculatorAndShowsExpression() {
        presenterOnViewAttached();

        performEvent(click2);

        verify(calculator).digit2();
        verify(view).showExpression(any(String.class));
    }

    @Test public void on3Number_callsCalculatorAndShowsExpression() {
        presenterOnViewAttached();

        performEvent(click3);

        verify(calculator).digit3();
        verify(view).showExpression(any(String.class));
    }

    @Test public void on4Number_callsCalculatorAndShowsExpression() {
        presenterOnViewAttached();

        performEvent(click4);

        verify(calculator).digit4();
        verify(view).showExpression(any(String.class));
    }

    @Test public void on5Number_callsCalculatorAndShowsExpression() {
        presenterOnViewAttached();

        performEvent(click5);

        verify(calculator).digit5();
        verify(view).showExpression(any(String.class));
    }

    @Test public void on7Number_callsCalculatorAndShowsExpression() {
        presenterOnViewAttached();

        performEvent(click7);

        verify(calculator).digit7();
        verify(view).showExpression(any(String.class));
    }

    @Test public void on8Number_callsCalculatorAndShowsExpression() {
        presenterOnViewAttached();

        performEvent(click8);

        verify(calculator).digit8();
        verify(view).showExpression(any(String.class));
    }

    @Test public void on9Number_callsCalculatorAndShowsExpression() {
        presenterOnViewAttached();

        performEvent(click9);

        verify(calculator).digit9();
        verify(view).showExpression(any(String.class));
    }

    @Test public void onDecimal_callsCalculatorAndShowsExpression() {
        presenterOnViewAttached();

        performEvent(clickDecimal);

        verify(calculator).decimal();
        verify(view).showExpression(any(String.class));
    }

    @Test public void onAdd_callsCalculatorAndShowsExpression() {
        presenterOnViewAttached();

        performEvent(clickAdd);

        verify(calculator).add();
        verify(view).showExpression(any(String.class));
    }

    @Test public void onSubtract_callsCalculatorAndShowsExpression() {
        presenterOnViewAttached();

        performEvent(clickSub);

        verify(calculator).subtract();
        verify(view).showExpression(any(String.class));
    }

    @Test public void onMultiply_callsCalculatorAndShowsExpression() {
        presenterOnViewAttached();

        performEvent(clickMul);

        verify(calculator).multiply();
        verify(view).showExpression(any(String.class));
    }

    @Test public void onDivide_callsCalculatorAndShowsExpression() {
        presenterOnViewAttached();

        performEvent(clickDiv);

        verify(calculator).divide();
        verify(view).showExpression(any(String.class));
    }

    @Test public void onDelete_callsCalculatorAndShowsExpression() {
        presenterOnViewAttached();

        performEvent(clickDelete);

        verify(calculator).delete();
        verify(view).showExpression(any(String.class));
    }

    @Test public void onClear_callsCalculatorAndShowsExpression() {
        presenterOnViewAttached();

        performEvent(clickClear);

        verify(calculator).clear();
        verify(view).clearExpression();
    }

    @Test public void onEquals_callsCalculatorAndShowsExpression_whenThereIsAtLeastTwoNumbers() {
        presenterOnViewAttached();
        when(calculator.isEmptyOrSingleNumber()).thenReturn(false);

        performEvent(clickEquals);

        verify(calculator).calculate();
        verify(calculator).setNumber(any(BigDecimal.class));
        verify(view).showExpression(any(String.class));
    }

    @Test public void onEquals_startsResult_whenThereIsOnlyOneNumberInTheExpression() {
        presenterOnViewAttached();
        when(calculator.isEmptyOrSingleNumber()).thenReturn(true);

        performEvent(clickEquals);

        verify(calculator).calculate();
        verify(view).startResult(any(BigDecimal.class));
    }

    @Test public void onNumberChange_updatesCalculatorAndShowsExpression() {
        presenterOnViewAttached();

        numberChange.onNext(BigDecimal.ONE);

        verify(calculator).setNumber(BigDecimal.ONE);
        verify(view).showExpression(any(String.class));
    }

    @Test public void showExpression_showsCalculate_whenExpressionIsNotEmptyOrSingleNumber() {
        presenterOnViewAttached();
        when(calculator.isEmptyOrSingleNumber()).thenReturn(false);

        performEvent(clickDelete);

        verify(view).showCalculate();
    }

    @Test public void showExpression_showsStartResult_whenExpressionIsNotEmptyOrSingleNumber() {
        presenterOnViewAttached();
        when(calculator.isEmptyOrSingleNumber()).thenReturn(false);

        performEvent(clickDelete);

        verify(view).showCalculate();
    }
}