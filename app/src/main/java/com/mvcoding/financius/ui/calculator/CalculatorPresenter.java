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

package com.mvcoding.financius.ui.calculator;

import android.support.annotation.NonNull;

import com.mvcoding.financius.ui.ActivityScope;
import com.mvcoding.financius.ui.Presenter;
import com.mvcoding.financius.ui.PresenterView;

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

        final Observable<Object> equalsObservable = view.onEquals();
        final Observable<Object> numberObservable = view.on0Number()
                .doOnNext(o -> calculator.digit0())
                .mergeWith(view.on1Number().doOnNext(o -> calculator.digit1()))
                .mergeWith(view.on2Number().doOnNext(o -> calculator.digit2()))
                .mergeWith(view.on3Number().doOnNext(o -> calculator.digit3()))
                .mergeWith(view.on4Number().doOnNext(o -> calculator.digit4()))
                .mergeWith(view.on5Number().doOnNext(o -> calculator.digit5()))
                .mergeWith(view.on6Number().doOnNext(o -> calculator.digit6()))
                .mergeWith(view.on7Number().doOnNext(o -> calculator.digit7()))
                .mergeWith(view.on8Number().doOnNext(o -> calculator.digit8()))
                .mergeWith(view.on9Number().doOnNext(o -> calculator.digit9()));

        final List<Observable<Object>> expressionUpdates = new ArrayList<>();
        expressionUpdates.add(numberObservable);
        expressionUpdates.add(view.onDecimal().doOnNext(o -> calculator.decimal()));
        expressionUpdates.add(view.onAdd().doOnNext(o -> calculator.add()));
        expressionUpdates.add(view.onSubtract().doOnNext(o -> calculator.subtract()));
        expressionUpdates.add(view.onMultiply().doOnNext(o -> calculator.multiply()));
        expressionUpdates.add(view.onDivide().doOnNext(o -> calculator.divide()));
        expressionUpdates.add(view.onDelete().doOnNext(o -> calculator.delete()));
        expressionUpdates.add(view.onClear().doOnNext(o -> calculator.clear()).doOnNext(o -> view.clearExpression()));
        expressionUpdates.add(view.onNumberChange().doOnNext(calculator::setNumber).map(number -> new Object()));
        expressionUpdates.add(equalsObservable.filter(o -> !calculator.isEmptyOrSingleNumber())
                                      .map(o -> calculator.calculate())
                                      .doOnNext(calculator::setNumber)
                                      .map(number -> new Object()));
        expressionUpdates.add(calculator.getExpression().isEmpty() ? Observable.empty() : Observable.just(new Object()));

        unsubscribeOnDetach(Observable.merge(expressionUpdates)
                                    .map(o -> calculator.getExpression())
                                    .doOnNext(view::showExpression)
                                    .subscribe(expression -> {
                                        if (calculator.isEmptyOrSingleNumber()) {
                                            view.showStartResult();
                                        } else {
                                            view.showCalculate();
                                        }
                                    }));

        unsubscribeOnDetach(equalsObservable.filter(o -> calculator.isEmptyOrSingleNumber())
                                    .map(o -> calculator.calculate())
                                    .subscribe(view::startResult));
    }

    public interface View extends PresenterView {
        @NonNull Observable<Object> on0Number();
        @NonNull Observable<Object> on1Number();
        @NonNull Observable<Object> on2Number();
        @NonNull Observable<Object> on3Number();
        @NonNull Observable<Object> on4Number();
        @NonNull Observable<Object> on5Number();
        @NonNull Observable<Object> on6Number();
        @NonNull Observable<Object> on7Number();
        @NonNull Observable<Object> on8Number();
        @NonNull Observable<Object> on9Number();
        @NonNull Observable<Object> onDecimal();
        @NonNull Observable<Object> onEquals();
        @NonNull Observable<Object> onDivide();
        @NonNull Observable<Object> onMultiply();
        @NonNull Observable<Object> onSubtract();
        @NonNull Observable<Object> onAdd();
        @NonNull Observable<Object> onDelete();
        @NonNull Observable<Object> onClear();
        @NonNull Observable<BigDecimal> onNumberChange();
        void showExpression(@NonNull String expression);
        void clearExpression();
        void showCalculate();
        void showStartResult();
        void startResult(@NonNull BigDecimal result);
    }
}
