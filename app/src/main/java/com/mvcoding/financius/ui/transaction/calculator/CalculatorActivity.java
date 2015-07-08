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

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mvcoding.financius.R;
import com.mvcoding.financius.ui.ActivityComponent;
import com.mvcoding.financius.ui.ActivityStarter;
import com.mvcoding.financius.ui.BaseActivity;
import com.mvcoding.financius.util.ThemeUtils;

import java.math.BigDecimal;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnLongClick;
import rx.Observable;
import rx.android.view.OnClickEvent;
import rx.android.view.ViewObservable;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;

public class CalculatorActivity extends BaseActivity<CalculatorPresenter.View, CalculatorComponent> implements CalculatorPresenter.View {
    private static final String EXTRA_NUMBER = "EXTRA_NUMBER";

    private static final String RESULT_EXTRA_NUMBER = "RESULT_EXTRA_NUMBER";

    private static final PublishSubject<OnClickEvent> clearSubject = PublishSubject.create();
    private static final BehaviorSubject<BigDecimal> numberChangeSubject = BehaviorSubject.create();

    @Bind(R.id.resultTextView) TextView resultTextView;
    @Bind(R.id.number0Button) Button number0Button;
    @Bind(R.id.number1Button) Button number1Button;
    @Bind(R.id.number2Button) Button number2Button;
    @Bind(R.id.number3Button) Button number3Button;
    @Bind(R.id.number4Button) Button number4Button;
    @Bind(R.id.number5Button) Button number5Button;
    @Bind(R.id.number6Button) Button number6Button;
    @Bind(R.id.number7Button) Button number7Button;
    @Bind(R.id.number8Button) Button number8Button;
    @Bind(R.id.number9Button) Button number9Button;
    @Bind(R.id.decimalButton) Button decimalButton;
    @Bind(R.id.addButton) Button addButton;
    @Bind(R.id.subtractButton) Button subtractButton;
    @Bind(R.id.multiplyButton) Button multiplyButton;
    @Bind(R.id.divideButton) Button divideButton;
    @Bind(R.id.deleteButton) Button deleteButton;
    @Bind(R.id.equalsButton) FloatingActionButton equalsButton;

    @Inject CalculatorPresenter presenter;

    public static void start(@NonNull Context context, @Nullable BigDecimal number) {
        ActivityStarter.with(context, CalculatorActivity.class).extra(EXTRA_NUMBER, number).start();
    }

    public static BigDecimal getResultNumber(@NonNull Intent data) {
        return (BigDecimal) data.getSerializableExtra(RESULT_EXTRA_NUMBER);
    }

    @Override protected int getLayoutId() {
        return R.layout.activity_calculator;
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final BigDecimal number = (BigDecimal) getIntent().getSerializableExtra(EXTRA_NUMBER);
        numberChangeSubject.onNext(number);
    }

    @NonNull @Override protected CalculatorComponent createComponent(@NonNull ActivityComponent component) {
        return component.plus(new CalculatorModule());
    }

    @Override protected void inject(@NonNull CalculatorComponent component) {
        component.inject(this);
    }

    @NonNull @Override public CalculatorPresenter getPresenter() {
        return presenter;
    }

    @NonNull @Override protected CalculatorPresenter.View getPresenterView() {
        return this;
    }

    @NonNull @Override public Observable<OnClickEvent> on0Click() {
        return ViewObservable.clicks(number0Button);
    }

    @NonNull @Override public Observable<OnClickEvent> on1Click() {
        return ViewObservable.clicks(number1Button);
    }

    @NonNull @Override public Observable<OnClickEvent> on2Click() {
        return ViewObservable.clicks(number2Button);
    }

    @NonNull @Override public Observable<OnClickEvent> on3Click() {
        return ViewObservable.clicks(number3Button);
    }

    @NonNull @Override public Observable<OnClickEvent> on4Click() {
        return ViewObservable.clicks(number4Button);
    }

    @NonNull @Override public Observable<OnClickEvent> on5Click() {
        return ViewObservable.clicks(number5Button);
    }

    @NonNull @Override public Observable<OnClickEvent> on6Click() {
        return ViewObservable.clicks(number6Button);
    }

    @NonNull @Override public Observable<OnClickEvent> on7Click() {
        return ViewObservable.clicks(number7Button);
    }

    @NonNull @Override public Observable<OnClickEvent> on8Click() {
        return ViewObservable.clicks(number8Button);
    }

    @NonNull @Override public Observable<OnClickEvent> on9Click() {
        return ViewObservable.clicks(number9Button);
    }

    @NonNull @Override public Observable<OnClickEvent> onDecimalClick() {
        return ViewObservable.clicks(decimalButton);
    }

    @NonNull @Override public Observable<OnClickEvent> onEqualsClick() {
        return ViewObservable.clicks(equalsButton);
    }

    @NonNull @Override public Observable<OnClickEvent> onDivideClick() {
        return ViewObservable.clicks(divideButton);
    }

    @NonNull @Override public Observable<OnClickEvent> onMultiplyClick() {
        return ViewObservable.clicks(multiplyButton);
    }

    @NonNull @Override public Observable<OnClickEvent> onSubtractClick() {
        return ViewObservable.clicks(subtractButton);
    }

    @NonNull @Override public Observable<OnClickEvent> onAddClick() {
        return ViewObservable.clicks(addButton);
    }

    @NonNull @Override public Observable<OnClickEvent> onDeleteClick() {
        return ViewObservable.clicks(deleteButton);
    }

    @NonNull @Override public Observable<OnClickEvent> onClearClick() {
        return clearSubject;
    }

    @NonNull @Override public Observable<BigDecimal> onNumberChange() {
        return numberChangeSubject;
    }

    @Override public void showExpression(@NonNull String expression) {
        resultTextView.setText(expression);
    }

    @Override public void clearExpression() {
        resultTextView.setText(null);
    }

    @Override public void showCalculate() {
        equalsButton.setBackgroundTintList(ColorStateList.valueOf(ThemeUtils.getColor(this, R.attr.colorPrimary)));
    }

    @Override public void showStartResult() {
        equalsButton.setBackgroundTintList(ColorStateList.valueOf(ThemeUtils.getColor(this, R.attr.colorAccent)));
    }

    @Override public void startResult(@NonNull BigDecimal result) {
        final Intent data = new Intent();
        data.putExtra(RESULT_EXTRA_NUMBER, result);
        setResult(RESULT_OK, data);
        finish();
    }

    @OnLongClick(R.id.deleteButton) boolean onDeleteLongClick(@NonNull View view) {
        clearSubject.onNext(OnClickEvent.create(view));
        return true;
    }
}
