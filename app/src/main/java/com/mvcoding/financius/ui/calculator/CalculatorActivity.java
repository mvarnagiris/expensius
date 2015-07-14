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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;

import com.mvcoding.financius.R;
import com.mvcoding.financius.ui.ActivityComponent;
import com.mvcoding.financius.ui.ActivityStarter;
import com.mvcoding.financius.ui.BaseActivity;
import com.mvcoding.financius.util.rx.Event;

import java.math.BigDecimal;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnLongClick;
import rx.Observable;
import rx.android.view.OnClickEvent;
import rx.android.view.ViewObservable;
import rx.subjects.PublishSubject;

public class CalculatorActivity extends BaseActivity<CalculatorPresenter.View, CalculatorComponent> implements CalculatorPresenter.View {
    private static final String EXTRA_NUMBER = "EXTRA_NUMBER";

    private static final String RESULT_EXTRA_NUMBER = "RESULT_EXTRA_NUMBER";

    private static final PublishSubject<OnClickEvent> clearSubject = PublishSubject.create();
    private static final PublishSubject<BigDecimal> numberChangeSubject = PublishSubject.create();

    @Bind(R.id.resultContainerView) View resultContainerView;
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
    @Bind(R.id.equalsFloatingActionButton) FloatingActionButton equalsFloatingActionButton;

    @Inject CalculatorPresenter presenter;

    private boolean isInShowCalculateMode;

    public static void start(@NonNull Context context, @Nullable BigDecimal number, @Nullable View... sharedViews) {
        ActivityStarter.with(context, CalculatorActivity.class).extra(EXTRA_NUMBER, number).enableTransition(sharedViews).start();
    }

    public static BigDecimal getResultNumber(@NonNull Intent data) {
        return (BigDecimal) data.getSerializableExtra(RESULT_EXTRA_NUMBER);
    }

    @Override protected int getLayoutId() {
        return R.layout.activity_calculator;
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        transitions().slide(Gravity.TOP, resultContainerView)
                .slide(Gravity.END, deleteButton, divideButton, multiplyButton, subtractButton, addButton)
                .slide(Gravity.START, number0Button, number1Button, number2Button, number3Button, number4Button, number5Button, number6Button, number7Button, number8Button, number9Button, decimalButton)
                .asEnterTransition();

        if (savedInstanceState == null) {
            final BigDecimal number = (BigDecimal) getIntent().getSerializableExtra(EXTRA_NUMBER);
            numberChangeSubject.onNext(number);
        }

        resultTextView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override public void onGlobalLayout() {
                resultTextView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                resultTextView.getLayoutParams().height = resultTextView.getHeight();
            }
        });
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

    @NonNull @Override public Observable<Event> on0Number() {
        return ViewObservable.clicks(number0Button).compose(clickTransformer);
    }

    @NonNull @Override public Observable<Event> on1Number() {
        return ViewObservable.clicks(number1Button).compose(clickTransformer);
    }

    @NonNull @Override public Observable<Event> on2Number() {
        return ViewObservable.clicks(number2Button).compose(clickTransformer);
    }

    @NonNull @Override public Observable<Event> on3Number() {
        return ViewObservable.clicks(number3Button).compose(clickTransformer);
    }

    @NonNull @Override public Observable<Event> on4Number() {
        return ViewObservable.clicks(number4Button).compose(clickTransformer);
    }

    @NonNull @Override public Observable<Event> on5Number() {
        return ViewObservable.clicks(number5Button).compose(clickTransformer);
    }

    @NonNull @Override public Observable<Event> on6Number() {
        return ViewObservable.clicks(number6Button).compose(clickTransformer);
    }

    @NonNull @Override public Observable<Event> on7Number() {
        return ViewObservable.clicks(number7Button).compose(clickTransformer);
    }

    @NonNull @Override public Observable<Event> on8Number() {
        return ViewObservable.clicks(number8Button).compose(clickTransformer);
    }

    @NonNull @Override public Observable<Event> on9Number() {
        return ViewObservable.clicks(number9Button).compose(clickTransformer);
    }

    @NonNull @Override public Observable<Event> onDecimal() {
        return ViewObservable.clicks(decimalButton).compose(clickTransformer);
    }

    @NonNull @Override public Observable<Event> onEquals() {
        return ViewObservable.clicks(equalsFloatingActionButton).compose(clickTransformer);
    }

    @NonNull @Override public Observable<Event> onDivide() {
        return ViewObservable.clicks(divideButton).compose(clickTransformer);
    }

    @NonNull @Override public Observable<Event> onMultiply() {
        return ViewObservable.clicks(multiplyButton).compose(clickTransformer);
    }

    @NonNull @Override public Observable<Event> onSubtract() {
        return ViewObservable.clicks(subtractButton).compose(clickTransformer);
    }

    @NonNull @Override public Observable<Event> onAdd() {
        return ViewObservable.clicks(addButton).compose(clickTransformer);
    }

    @NonNull @Override public Observable<Event> onDelete() {
        return ViewObservable.clicks(deleteButton).compose(clickTransformer);
    }

    @NonNull @Override public Observable<Event> onClear() {
        return clearSubject.compose(clickTransformer);
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
        if (isInShowCalculateMode) {
            return;
        }

        isInShowCalculateMode = true;
        equalsFloatingActionButton.setSelected(true);
    }

    @Override public void showStartResult() {
        if (!isInShowCalculateMode) {
            return;
        }

        isInShowCalculateMode = false;
        equalsFloatingActionButton.setSelected(false);
    }

    @Override public void startResult(@NonNull BigDecimal result) {
        final Intent data = new Intent();
        data.putExtra(RESULT_EXTRA_NUMBER, result);
        setResult(RESULT_OK, data);
        close();
    }

    @OnLongClick(R.id.deleteButton) boolean onDeleteLongClick(@NonNull View view) {
        clearSubject.onNext(OnClickEvent.create(view));
        return true;
    }
}
