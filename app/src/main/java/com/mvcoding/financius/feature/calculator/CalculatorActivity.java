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

package com.mvcoding.financius.feature.calculator;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.Gravity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.mvcoding.financius.R;
import com.mvcoding.financius.UserSettingsOld;
import com.mvcoding.financius.data.model.Transaction;
import com.mvcoding.financius.feature.ActivityComponent;
import com.mvcoding.financius.feature.ActivityStarter;
import com.mvcoding.financius.feature.BaseActivity;
import com.mvcoding.financius.feature.transaction.TransactionActivity;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnLongClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.PublishSubject;

public class CalculatorActivity extends BaseActivity<CalculatorPresenter.View, CalculatorComponent> implements CalculatorPresenter.View {
    private static final String EXTRA_NUMBER = "EXTRA_NUMBER";
    private static final String EXTRA_RESULT_DESTINATION = "EXTRA_RESULT_DESTINATION";

    private static final String RESULT_EXTRA_NUMBER = "RESULT_EXTRA_NUMBER";

    private static final long ANIMATION_DURATION_CLEAR_START = 400;

    private final PublishSubject<Object> clearSubject = PublishSubject.create();
    private final PublishSubject<Object> equalsSubject = PublishSubject.create();
    private final PublishSubject<BigDecimal> numberChangeSubject = PublishSubject.create();

    @Bind(R.id.resultContainerView) View resultContainerView;
    @Bind(R.id.resultTextView) TextView resultTextView;
    @Bind(R.id.revealView) View revealView;
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
    @Inject UserSettingsOld userSettings;

    private boolean isInShowCalculateMode;

    public static void start(@NonNull Context context, @Nullable BigDecimal number, @Nullable View... sharedViews) {
        ActivityStarter.with(context, CalculatorActivity.class)
                .extra(EXTRA_NUMBER, number)
                .extra(EXTRA_RESULT_DESTINATION, ResultDestination.Transaction)
                .addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                .enableTransition(sharedViews)
                .start();
    }

    public static void startForResult(@NonNull Context context, int requestCode, @Nullable BigDecimal number, @Nullable View... sharedViews) {
        ActivityStarter.with(context, CalculatorActivity.class)
                .extra(EXTRA_NUMBER, number)
                .extra(EXTRA_RESULT_DESTINATION, ResultDestination.Return)
                .enableTransition(sharedViews)
                .startForResult(requestCode);
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

    @NonNull @Override public Observable<Object> on0Number() {
        return RxView.clicks(number0Button);
    }

    @NonNull @Override public Observable<Object> on1Number() {
        return RxView.clicks(number1Button);
    }

    @NonNull @Override public Observable<Object> on2Number() {
        return RxView.clicks(number2Button);
    }

    @NonNull @Override public Observable<Object> on3Number() {
        return RxView.clicks(number3Button);
    }

    @NonNull @Override public Observable<Object> on4Number() {
        return RxView.clicks(number4Button);
    }

    @NonNull @Override public Observable<Object> on5Number() {
        return RxView.clicks(number5Button);
    }

    @NonNull @Override public Observable<Object> on6Number() {
        return RxView.clicks(number6Button);
    }

    @NonNull @Override public Observable<Object> on7Number() {
        return RxView.clicks(number7Button);
    }

    @NonNull @Override public Observable<Object> on8Number() {
        return RxView.clicks(number8Button);
    }

    @NonNull @Override public Observable<Object> on9Number() {
        return RxView.clicks(number9Button);
    }

    @NonNull @Override public Observable<Object> onDecimal() {
        return RxView.clicks(decimalButton);
    }

    @NonNull @Override public Observable<Object> onEquals() {
        return equalsSubject;
    }

    @NonNull @Override public Observable<Object> onDivide() {
        return RxView.clicks(divideButton);
    }

    @NonNull @Override public Observable<Object> onMultiply() {
        return RxView.clicks(multiplyButton);
    }

    @NonNull @Override public Observable<Object> onSubtract() {
        return RxView.clicks(subtractButton);
    }

    @NonNull @Override public Observable<Object> onAdd() {
        return RxView.clicks(addButton);
    }

    @NonNull @Override public Observable<Object> onDelete() {
        return RxView.clicks(deleteButton);
    }

    @NonNull @Override public Observable<Object> onClear() {
        return clearSubject.doOnNext(o -> clearAnimation())
                .delay(ANIMATION_DURATION_CLEAR_START, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @NonNull @Override public Observable<BigDecimal> onNumberChange() {
        return numberChangeSubject;
    }

    @Override public void showExpression(@NonNull String expression) {
        resultTextView.setText(expression.replace("*", "\u00D7").replace("/", "\u00F7"));
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
        final ResultDestination resultDestination = (ResultDestination) getIntent().getSerializableExtra(EXTRA_RESULT_DESTINATION);
        switch (resultDestination) {
            case Return:
                final Intent data = new Intent();
                data.putExtra(RESULT_EXTRA_NUMBER, result);
                setResult(RESULT_OK, data);
                close();
                break;
            case Transaction:
                final Transaction transaction = new Transaction().withDefaultValues(userSettings);
                transaction.setAmount(result);
                TransactionActivity.startForResult(this, 0, transaction);
                close();
                break;
            default:
                throw new IllegalArgumentException("ResultDestination " + resultDestination + " is not supported.");
        }
    }

    private void clearAnimation() {
        final Animator startAnimator;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final int revealSize = resultContainerView.getWidth();
            final int[] location = new int[2];
            deleteButton.getLocationInWindow(location);
            final int centerX = location[0] + deleteButton.getWidth() / 2;
            startAnimator = ViewAnimationUtils.createCircularReveal(revealView, centerX, resultContainerView.getHeight(), 0, revealSize);
        } else {
            startAnimator = ObjectAnimator.ofFloat(revealView, View.TRANSLATION_Y, resultContainerView.getHeight(), 0);
        }
        startAnimator.setDuration(ANIMATION_DURATION_CLEAR_START);

        final Animator endAnimator = ObjectAnimator.ofFloat(revealView, View.ALPHA, 1, 0);

        final AnimatorSet animator = new AnimatorSet();
        animator.addListener(new Animator.AnimatorListener() {
            @Override public void onAnimationStart(Animator animation) {
                revealView.setVisibility(View.VISIBLE);
            }

            @Override public void onAnimationEnd(Animator animation) {
                revealView.setVisibility(View.GONE);
                revealView.setAlpha(1);
            }

            @Override public void onAnimationCancel(Animator animation) {

            }

            @Override public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.playSequentially(startAnimator, endAnimator);
        animator.start();
    }

    @OnClick(R.id.equalsFloatingActionButton) void onEqualsClick(@NonNull View view) {
        equalsSubject.onNext(new Object());
    }

    @OnLongClick(R.id.deleteButton) boolean onDeleteLongClick(@NonNull View view) {
        clearSubject.onNext(new Object());
        return true;
    }

    public enum ResultDestination {
        Return, Transaction
    }
}
