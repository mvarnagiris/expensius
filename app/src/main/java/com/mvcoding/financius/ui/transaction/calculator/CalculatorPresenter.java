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
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.view.OnClickEvent;

class CalculatorPresenter extends Presenter<CalculatorPresenter.View> {
    private final Calculator calculator;

    CalculatorPresenter(@NonNull Calculator calculator) {
        this.calculator = calculator;
    }

    @Override protected void onViewAttached(@NonNull View view) {
        super.onViewAttached(view);

        final List<Observable<String>> expressionUpdates = new ArrayList<>();
        expressionUpdates.add(view.on0Click().doOnNext(c -> calculator.digit0()).map(c -> calculator.getExpression()));
        expressionUpdates.add(view.on1Click().doOnNext(c -> calculator.digit1()).map(c -> calculator.getExpression()));
        expressionUpdates.add(view.on2Click().doOnNext(c -> calculator.digit2()).map(c -> calculator.getExpression()));
        expressionUpdates.add(view.on3Click().doOnNext(c -> calculator.digit3()).map(c -> calculator.getExpression()));
        expressionUpdates.add(view.on4Click().doOnNext(c -> calculator.digit4()).map(c -> calculator.getExpression()));
        expressionUpdates.add(view.on5Click().doOnNext(c -> calculator.digit5()).map(c -> calculator.getExpression()));
        expressionUpdates.add(view.on6Click().doOnNext(c -> calculator.digit6()).map(c -> calculator.getExpression()));
        expressionUpdates.add(view.on7Click().doOnNext(c -> calculator.digit7()).map(c -> calculator.getExpression()));
        expressionUpdates.add(view.on8Click().doOnNext(c -> calculator.digit8()).map(c -> calculator.getExpression()));
        expressionUpdates.add(view.on9Click().doOnNext(c -> calculator.digit9()).map(c -> calculator.getExpression()));
        expressionUpdates.add(view.onDecimalClick().doOnNext(c -> calculator.decimal()).map(c -> calculator.getExpression()));
        expressionUpdates.add(view.onAddClick().doOnNext(c -> calculator.add()).map(c -> calculator.getExpression()));
        expressionUpdates.add(view.onSubtractClick().doOnNext(c -> calculator.subtract()).map(c -> calculator.getExpression()));
        expressionUpdates.add(view.onMultiplyClick().doOnNext(c -> calculator.multiply()).map(c -> calculator.getExpression()));
        expressionUpdates.add(view.onDivideClick().doOnNext(c -> calculator.divide()).map(c -> calculator.getExpression()));
        expressionUpdates.add(view.onDeleteClick().doOnNext(c -> calculator.delete()).map(c -> calculator.getExpression()));
        unsubscribeOnDetach(Observable.merge(expressionUpdates).subscribe(view::showExpression));

        unsubscribeOnDetach(view.onClearClick().doOnNext(c -> calculator.clear()).subscribe(c -> view.clearExpression()));

        final Observable<OnClickEvent> equalsClickObservable = view.onEqualsClick();
        unsubscribeOnDetach(equalsClickObservable.filter(c -> calculator.isEmptyOrSingleNumber())
                                    .map(c -> calculator.calculate())
                                    .subscribe(view::startResult));
        unsubscribeOnDetach(equalsClickObservable.filter(c -> !calculator.isEmptyOrSingleNumber())
                                    .map(c -> calculator.calculate())
                                    .doOnNext(calculator::setNumber)
                                    .map(result -> calculator.getExpression())
                                    .subscribe(view::showExpression));

        unsubscribeOnDetach(view.onNumberChange()
                                    .doOnNext(calculator::setNumber)
                                    .map(number -> calculator.getExpression())
                                    .subscribe(view::showExpression));
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

        @NonNull Observable<BigDecimal> onNumberChange();

        void showExpression(@NonNull String expression);

        void clearExpression();

        void startResult(@NonNull BigDecimal result);
    }
}
