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

import android.support.annotation.NonNull;

import com.mvcoding.financius.ui.Presenter;
import com.mvcoding.financius.ui.PresenterView;

import java.math.BigDecimal;

import rx.Observable;
import rx.android.view.OnClickEvent;

class CalculatorPresenter extends Presenter<CalculatorPresenter.View> {
    private final Calculator calculator;

    CalculatorPresenter(@NonNull Calculator calculator) {
        this.calculator = calculator;
    }

    @Override protected void onViewAttached(@NonNull View view) {
        super.onViewAttached(view);

        unsubscribeOnDetach(view.on0Click().subscribe(onClickEvent -> calculator.digit0()));
        unsubscribeOnDetach(view.on1Click().subscribe(onClickEvent -> calculator.digit1()));
        unsubscribeOnDetach(view.on2Click().subscribe(onClickEvent -> calculator.digit2()));
        unsubscribeOnDetach(view.on3Click().subscribe(onClickEvent -> calculator.digit3()));
        unsubscribeOnDetach(view.on4Click().subscribe(onClickEvent -> calculator.digit4()));
        unsubscribeOnDetach(view.on5Click().subscribe(onClickEvent -> calculator.digit5()));
        unsubscribeOnDetach(view.on6Click().subscribe(onClickEvent -> calculator.digit6()));
        unsubscribeOnDetach(view.on7Click().subscribe(onClickEvent -> calculator.digit7()));
        unsubscribeOnDetach(view.on8Click().subscribe(onClickEvent -> calculator.digit8()));
        unsubscribeOnDetach(view.on9Click().subscribe(onClickEvent -> calculator.digit9()));
        unsubscribeOnDetach(view.onDecimalClick().subscribe(onClickEvent -> calculator.decimal()));
        unsubscribeOnDetach(view.onAddClick().subscribe(onClickEvent -> calculator.add()));
        unsubscribeOnDetach(view.onSubtractClick().subscribe(onClickEvent -> calculator.subtract()));
        unsubscribeOnDetach(view.onMultiplyClick().subscribe(onClickEvent -> calculator.multiply()));
        unsubscribeOnDetach(view.onDivideClick().subscribe(onClickEvent -> calculator.divide()));
        unsubscribeOnDetach(view.onDeleteClick().subscribe(onClickEvent -> calculator.delete()));
        unsubscribeOnDetach(view.onClearClick().subscribe(onClickEvent -> calculator.clear()));
    }

    public interface View extends PresenterView {
        @NonNull Observable<OnClickEvent> on0Click();

        @NonNull Observable<OnClickEvent> on1Click();

        @NonNull Observable<OnClickEvent> on2Click();

        @NonNull Observable<OnClickEvent> on3Click();

        @NonNull Observable<OnClickEvent> on4Click();

        @NonNull Observable<OnClickEvent> on5Click();

        @NonNull Observable<OnClickEvent> on6Click();

        @NonNull Observable<OnClickEvent> on7Click();

        @NonNull Observable<OnClickEvent> on8Click();

        @NonNull Observable<OnClickEvent> on9Click();

        @NonNull Observable<OnClickEvent> onDecimalClick();

        @NonNull Observable<OnClickEvent> onEqualsClick();

        @NonNull Observable<OnClickEvent> onDivideClick();

        @NonNull Observable<OnClickEvent> onMultiplyClick();

        @NonNull Observable<OnClickEvent> onSubtractClick();

        @NonNull Observable<OnClickEvent> onAddClick();

        @NonNull Observable<OnClickEvent> onDeleteClick();

        @NonNull Observable<OnClickEvent> onClearClick();

        void showExpression(@NonNull String expression);

        void clearExpression();

        void startResult(@NonNull BigDecimal result);
    }
}
