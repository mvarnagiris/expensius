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

        final List<Observable<Event>> expressionUpdates = new ArrayList<>();
        expressionUpdates.add(view.on0Number().doOnNext(e -> calculator.digit0()));
        expressionUpdates.add(view.on1Number().doOnNext(e -> calculator.digit1()));
        expressionUpdates.add(view.on2Number().doOnNext(e -> calculator.digit2()));
        expressionUpdates.add(view.on3Number().doOnNext(e -> calculator.digit3()));
        expressionUpdates.add(view.on4Number().doOnNext(e -> calculator.digit4()));
        expressionUpdates.add(view.on5Number().doOnNext(e -> calculator.digit5()));
        expressionUpdates.add(view.on6Number().doOnNext(e -> calculator.digit6()));
        expressionUpdates.add(view.on7Number().doOnNext(e -> calculator.digit7()));
        expressionUpdates.add(view.on8Number().doOnNext(e -> calculator.digit8()));
        expressionUpdates.add(view.on9Number().doOnNext(e -> calculator.digit9()));
        expressionUpdates.add(view.onDecimal().doOnNext(e -> calculator.decimal()));
        expressionUpdates.add(view.onAdd().doOnNext(e -> calculator.add()));
        expressionUpdates.add(view.onSubtract().doOnNext(e -> calculator.subtract()));
        expressionUpdates.add(view.onMultiply().doOnNext(e -> calculator.multiply()));
        expressionUpdates.add(view.onDivide().doOnNext(e -> calculator.divide()));
        expressionUpdates.add(view.onDelete().doOnNext(e -> calculator.delete()));
        expressionUpdates.add(view.onClear().doOnNext(e -> calculator.clear()).doOnNext(e -> view.clearExpression()));
        expressionUpdates.add(view.onNumberChange().doOnNext(calculator::setNumber).map(number -> new Event()));
        expressionUpdates.add(view.onEquals()
                                      .filter(e -> !calculator.isEmptyOrSingleNumber())
                                      .map(e -> calculator.calculate())
                                      .doOnNext(calculator::setNumber)
                                      .map(number -> new Event()));

        unsubscribeOnDetach(view.onEquals()
                                    .filter(c -> calculator.isEmptyOrSingleNumber())
                                    .map(c -> calculator.calculate())
                                    .subscribe(view::startResult));

        unsubscribeOnDetach(Observable.merge(expressionUpdates)
                                    .map(e -> calculator.getExpression())
                                    .doOnNext(view::showExpression)
                                    .subscribe(expression -> {
                                        if (calculator.isEmptyOrSingleNumber()) {
                                            view.showStartResult();
                                        } else {
                                            view.showCalculate();
                                        }
                                    }));
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
