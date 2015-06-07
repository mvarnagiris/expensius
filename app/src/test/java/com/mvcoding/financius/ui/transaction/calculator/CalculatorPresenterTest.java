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

import org.junit.Test;
import org.mockito.Mock;

import java.math.BigDecimal;

import rx.android.view.OnClickEvent;
import rx.subjects.PublishSubject;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CalculatorPresenterTest extends BasePresenterTest<CalculatorPresenter, CalculatorPresenter.View> {
    private final PublishSubject<OnClickEvent> click0 = PublishSubject.create();
    private final PublishSubject<OnClickEvent> click1 = PublishSubject.create();
    private final PublishSubject<OnClickEvent> click2 = PublishSubject.create();
    private final PublishSubject<OnClickEvent> click3 = PublishSubject.create();
    private final PublishSubject<OnClickEvent> click4 = PublishSubject.create();
    private final PublishSubject<OnClickEvent> click5 = PublishSubject.create();
    private final PublishSubject<OnClickEvent> click6 = PublishSubject.create();
    private final PublishSubject<OnClickEvent> click7 = PublishSubject.create();
    private final PublishSubject<OnClickEvent> click8 = PublishSubject.create();
    private final PublishSubject<OnClickEvent> click9 = PublishSubject.create();
    private final PublishSubject<OnClickEvent> clickAdd = PublishSubject.create();
    private final PublishSubject<OnClickEvent> clickSub = PublishSubject.create();
    private final PublishSubject<OnClickEvent> clickMul = PublishSubject.create();
    private final PublishSubject<OnClickEvent> clickDiv = PublishSubject.create();
    private final PublishSubject<OnClickEvent> clickDecimal = PublishSubject.create();
    private final PublishSubject<OnClickEvent> clickClear = PublishSubject.create();
    private final PublishSubject<OnClickEvent> clickDelete = PublishSubject.create();
    private final PublishSubject<OnClickEvent> clickEquals = PublishSubject.create();

    @Mock private Calculator calculator;

    @Override protected CalculatorPresenter createPresenter() {
        return new CalculatorPresenter(calculator);
    }

    @Override protected CalculatorPresenter.View createView() {
        final CalculatorPresenter.View view = mock(CalculatorPresenter.View.class);
        when(view.on0Click()).thenReturn(click0);
        when(view.on1Click()).thenReturn(click1);
        when(view.on2Click()).thenReturn(click2);
        when(view.on3Click()).thenReturn(click3);
        when(view.on4Click()).thenReturn(click4);
        when(view.on5Click()).thenReturn(click5);
        when(view.on6Click()).thenReturn(click6);
        when(view.on7Click()).thenReturn(click7);
        when(view.on8Click()).thenReturn(click8);
        when(view.on9Click()).thenReturn(click9);
        when(view.onAddClick()).thenReturn(clickAdd);
        when(view.onSubtractClick()).thenReturn(clickSub);
        when(view.onMultiplyClick()).thenReturn(clickMul);
        when(view.onDivideClick()).thenReturn(clickDiv);
        when(view.onDecimalClick()).thenReturn(clickDecimal);
        when(view.onDeleteClick()).thenReturn(clickDelete);
        when(view.onClearClick()).thenReturn(clickClear);
        when(view.onEqualsClick()).thenReturn(clickEquals);
        return view;
    }

    @Test public void on0Click_callsCalculatorAndShowsExpression() {
        presenterJumpToOnViewAttached();

        performClick(click0);

        verify(calculator).digit0();
        verify(view).showExpression(any(String.class));
    }

    @Test public void on1Click_callsCalculatorAndShowsExpression() {
        presenterJumpToOnViewAttached();

        performClick(click1);

        verify(calculator).digit1();
        verify(view).showExpression(any(String.class));
    }

    @Test public void on2Click_callsCalculatorAndShowsExpression() {
        presenterJumpToOnViewAttached();

        performClick(click2);

        verify(calculator).digit2();
        verify(view).showExpression(any(String.class));
    }

    @Test public void on3Click_callsCalculatorAndShowsExpression() {
        presenterJumpToOnViewAttached();

        performClick(click3);

        verify(calculator).digit3();
        verify(view).showExpression(any(String.class));
    }

    @Test public void on4Click_callsCalculatorAndShowsExpression() {
        presenterJumpToOnViewAttached();

        performClick(click4);

        verify(calculator).digit4();
        verify(view).showExpression(any(String.class));
    }

    @Test public void on5Click_callsCalculatorAndShowsExpression() {
        presenterJumpToOnViewAttached();

        performClick(click5);

        verify(calculator).digit5();
        verify(view).showExpression(any(String.class));
    }

    @Test public void on7Click_callsCalculatorAndShowsExpression() {
        presenterJumpToOnViewAttached();

        performClick(click7);

        verify(calculator).digit7();
        verify(view).showExpression(any(String.class));
    }

    @Test public void on8Click_callsCalculatorAndShowsExpression() {
        presenterJumpToOnViewAttached();

        performClick(click8);

        verify(calculator).digit8();
        verify(view).showExpression(any(String.class));
    }

    @Test public void on9Click_callsCalculatorAndShowsExpression() {
        presenterJumpToOnViewAttached();

        performClick(click9);

        verify(calculator).digit9();
        verify(view).showExpression(any(String.class));
    }

    @Test public void onDecimalClick_callsCalculatorAndShowsExpression() {
        presenterJumpToOnViewAttached();

        performClick(clickDecimal);

        verify(calculator).decimal();
        verify(view).showExpression(any(String.class));
    }

    @Test public void onAddClick_callsCalculatorAndShowsExpression() {
        presenterJumpToOnViewAttached();

        performClick(clickAdd);

        verify(calculator).add();
        verify(view).showExpression(any(String.class));
    }

    @Test public void onSubtractClick_callsCalculatorAndShowsExpression() {
        presenterJumpToOnViewAttached();

        performClick(clickSub);

        verify(calculator).subtract();
        verify(view).showExpression(any(String.class));
    }

    @Test public void onMultiplyClick_callsCalculatorAndShowsExpression() {
        presenterJumpToOnViewAttached();

        performClick(clickMul);

        verify(calculator).multiply();
        verify(view).showExpression(any(String.class));
    }

    @Test public void onDivideClick_callsCalculatorAndShowsExpression() {
        presenterJumpToOnViewAttached();

        performClick(clickDiv);

        verify(calculator).divide();
        verify(view).showExpression(any(String.class));
    }

    @Test public void onDeleteClick_callsCalculatorAndShowsExpression() {
        presenterJumpToOnViewAttached();

        performClick(clickDelete);

        verify(calculator).delete();
        verify(view).showExpression(any(String.class));
    }

    @Test public void onClearClick_callsCalculatorAndShowsExpression() {
        presenterJumpToOnViewAttached();

        performClick(clickClear);

        verify(calculator).clear();
        verify(view).clearExpression();
    }

    @Test public void onEqualsClick_callsCalculatorAndShowsExpression_whenThereIsAtLeastTwoNumbers() {
        presenterJumpToOnViewAttached();
        when(calculator.isEmptyOrSingleNumber()).thenReturn(false);

        performClick(clickEquals);

        verify(calculator).calculate();
        verify(view).showExpression(any(String.class));
    }

    @Test public void onEqualsClick_startsResult_whenThereIsOnlyOneNumberInTheExpression() {
        presenterJumpToOnViewAttached();
        when(calculator.isEmptyOrSingleNumber()).thenReturn(true);

        performClick(clickEquals);

        verify(calculator).calculate();
        verify(view).startResult(any(BigDecimal.class));
    }
}