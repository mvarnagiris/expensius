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

import com.mvcoding.financius.ui.ActivityScope;
import com.mvcoding.financius.ui.Presenter;
import com.mvcoding.financius.ui.PresenterView;
import com.mvcoding.financius.util.rx.Event;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

@ActivityScope class CalculatorPresenter extends Presenter<CalculatorPresenter.View> {
    private final Calculator calculator;

    @Inject CalculatorPresenter(@NonNull Calculator calculator) {
        this.calculator = calculator;
    }

    @Override protected void onViewAttached(@NonNull View view) {
        super.onViewAttached(view);

        final List<Observable<String>> expressionUpdates = new ArrayList<>();
        expressionUpdates.add(view.on0Number().doOnNext(c -> calculator.digit0()).map(c -> calculator.getExpression()));
        expressionUpdates.add(view.on1Number().doOnNext(c -> calculator.digit1()).map(c -> calculator.getExpression()));
        expressionUpdates.add(view.on2Number().doOnNext(c -> calculator.digit2()).map(c -> calculator.getExpression()));
        expressionUpdates.add(view.on3Number().doOnNext(c -> calculator.digit3()).map(c -> calculator.getExpression()));
        expressionUpdates.add(view.on4Number().doOnNext(c -> calculator.digit4()).map(c -> calculator.getExpression()));
        expressionUpdates.add(view.on5Number().doOnNext(c -> calculator.digit5()).map(c -> calculator.getExpression()));
        expressionUpdates.add(view.on6Number().doOnNext(c -> calculator.digit6()).map(c -> calculator.getExpression()));
        expressionUpdates.add(view.on7Number().doOnNext(c -> calculator.digit7()).map(c -> calculator.getExpression()));
        expressionUpdates.add(view.on8Number().doOnNext(c -> calculator.digit8()).map(c -> calculator.getExpression()));
        expressionUpdates.add(view.on9Number().doOnNext(c -> calculator.digit9()).map(c -> calculator.getExpression()));
        expressionUpdates.add(view.onDecimal().doOnNext(c -> calculator.decimal()).map(c -> calculator.getExpression()));
        expressionUpdates.add(view.onAdd().doOnNext(c -> calculator.add()).map(c -> calculator.getExpression()));
        expressionUpdates.add(view.onSubtract().doOnNext(c -> calculator.subtract()).map(c -> calculator.getExpression()));
        expressionUpdates.add(view.onMultiply().doOnNext(c -> calculator.multiply()).map(c -> calculator.getExpression()));
        expressionUpdates.add(view.onDivide().doOnNext(c -> calculator.divide()).map(c -> calculator.getExpression()));
        expressionUpdates.add(view.onDelete().doOnNext(c -> calculator.delete()).map(c -> calculator.getExpression()));
        unsubscribeOnDetach(Observable.merge(expressionUpdates).subscribe(view::showExpression));

        unsubscribeOnDetach(view.onClear().doOnNext(c -> calculator.clear()).subscribe(c -> view.clearExpression()));

        final Observable<Event> equalsObservable = view.onEquals();
        unsubscribeOnDetach(equalsObservable.filter(c -> calculator.isEmptyOrSingleNumber())
                                    .map(c -> calculator.calculate())
                                    .subscribe(view::startResult));
        unsubscribeOnDetach(equalsObservable.filter(c -> !calculator.isEmptyOrSingleNumber())
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
        @NonNull Observable<Event> on0Number();
        @NonNull Observable<Event> on1Number();
        @NonNull Observable<Event> on2Number();
        @NonNull Observable<Event> on3Number();
        @NonNull Observable<Event> on4Number();
        @NonNull Observable<Event> on5Number();
        @NonNull Observable<Event> on6Number();
        @NonNull Observable<Event> on7Number();
        @NonNull Observable<Event> on8Number();
        @NonNull Observable<Event> on9Number();
        @NonNull Observable<Event> onDecimal();
        @NonNull Observable<Event> onEquals();
        @NonNull Observable<Event> onDivide();
        @NonNull Observable<Event> onMultiply();
        @NonNull Observable<Event> onSubtract();
        @NonNull Observable<Event> onAdd();
        @NonNull Observable<Event> onDelete();
        @NonNull Observable<Event> onClear();
        @NonNull Observable<BigDecimal> onNumberChange();
        void showExpression(@NonNull String expression);
        void clearExpression();
        void showCalculate();
        void showStartResult();
        void startResult(@NonNull BigDecimal result);
    }
}
