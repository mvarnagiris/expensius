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

package com.mvcoding.financius.ui.transaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.mvcoding.financius.R;
import com.mvcoding.financius.core.model.TransactionState;
import com.mvcoding.financius.core.model.TransactionType;
import com.mvcoding.financius.data.model.Place;
import com.mvcoding.financius.data.model.Tag;
import com.mvcoding.financius.data.model.Transaction;
import com.mvcoding.financius.ui.ActivityComponent;
import com.mvcoding.financius.ui.ActivityStarter;
import com.mvcoding.financius.ui.BaseActivity;
import com.mvcoding.financius.ui.DateDialogFragment;
import com.mvcoding.financius.ui.TimeDialogFragment;
import com.mvcoding.financius.ui.calculator.CalculatorActivity;
import com.mvcoding.financius.util.date.DateFormatter;
import com.mvcoding.financius.util.rx.Event;
import com.mvcoding.financius.util.rx.RxBus;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.Set;

import javax.inject.Inject;

import butterknife.Bind;
import rx.Observable;
import rx.android.view.ViewObservable;
import rx.android.widget.WidgetObservable;
import rx.subjects.PublishSubject;

public class TransactionActivity extends BaseActivity<TransactionPresenter.View, TransactionComponent> implements TransactionPresenter.View {
    private static final String EXTRA_TRANSACTION = "EXTRA_TRANSACTION";

    private static final String RESULT_EXTRA_TRANSACTION = "RESULT_EXTRA_TRANSACTION";

    private static final int REQUEST_DATE = 1;
    private static final int REQUEST_TIME = 2;
    private static final int REQUEST_AMOUNT = 3;

    private static final PublishSubject<TransactionType> transactionTypeSubject = PublishSubject.create();
    private static final PublishSubject<TransactionState> transactionStateSubject = PublishSubject.create();
    private static final PublishSubject<Long> dateSubject = PublishSubject.create();
    private static final PublishSubject<BigDecimal> amountSubject = PublishSubject.create();
    private static final PublishSubject<String> currencySubject = PublishSubject.create();
    private static final PublishSubject<Place> placeSubject = PublishSubject.create();
    private static final PublishSubject<Set<Tag>> tagsSubject = PublishSubject.create();

    @Bind(R.id.transactionTypeRadioGroup) RadioGroup transactionTypeRadioGroup;
    @Bind(R.id.transactionStateRadioGroup) RadioGroup transactionStateRadioGroup;
    @Bind(R.id.dateButton) Button dateButton;
    @Bind(R.id.timeButton) Button timeButton;
    @Bind(R.id.amountButton) Button amountButton;
    @Bind(R.id.currencyButton) Button currencyButton;
    @Bind(R.id.placeButton) Button placeButton;
    @Bind(R.id.tagsButton) Button tagsButton;
    @Bind(R.id.noteEditText) EditText noteEditText;
    @Bind(R.id.saveButton) Button saveButton;

    @Inject TransactionPresenter presenter;
    @Inject RxBus rxBus;

    private boolean ignoreChanges = false;
    private Transaction transaction;

    public static void startForResult(@NonNull Context context, int requestCode, @NonNull Transaction transaction) {
        ActivityStarter.with(context, TransactionActivity.class).extra(EXTRA_TRANSACTION, transaction).startForResult(requestCode);
    }

    @Override protected int getLayoutId() {
        return R.layout.activity_transaction;
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        transactionTypeRadioGroup.setOnCheckedChangeListener((group, checkedId) -> transactionTypeSubject.onNext(checkedId == R.id.transactionTypeExpenseRadioButton ? TransactionType.Expense : TransactionType.Income));
        transactionStateRadioGroup.setOnCheckedChangeListener((group, checkedId) -> transactionStateSubject.onNext(checkedId == R.id.transactionStateConfirmedRadioButton ? TransactionState.Confirmed : TransactionState.Pending));
        amountButton.setOnClickListener(v -> CalculatorActivity.startForResult(this, REQUEST_AMOUNT, transaction.getAmount()));
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_AMOUNT:
                if (resultCode == RESULT_OK) {
                    amountSubject.onNext(CalculatorActivity.getResultNumber(data));
                }
                break;
        }
    }

    @NonNull @Override protected TransactionComponent createComponent(@NonNull ActivityComponent component) {
        final Transaction transaction = getIntent().getParcelableExtra(EXTRA_TRANSACTION);
        return component.plus(new TransactionModule(transaction));
    }

    @Override protected void inject(@NonNull TransactionComponent component) {
        component.inject(this);
    }

    @NonNull @Override protected TransactionPresenter getPresenter() {
        return presenter;
    }

    @NonNull @Override protected TransactionPresenter.View getPresenterView() {
        return this;
    }

    @NonNull @Override public Observable<TransactionType> onTransactionTypeChanged() {
        return transactionTypeSubject.filter(transactionType -> !ignoreChanges);
    }

    @NonNull @Override public Observable<TransactionState> onTransactionStateChanged() {
        return transactionStateSubject.filter(transactionState -> !ignoreChanges);
    }

    @NonNull @Override public Observable<Long> onDateChanged() {
        final Observable<Long> dateObservable = rxBus.observe(DateDialogFragment.DateDialogResult.class)
                .mergeWith(ViewObservable.clicks(dateButton)
                                   .flatMap(onClickEvent -> DateDialogFragment.show(getSupportFragmentManager(), REQUEST_DATE, rxBus, transaction
                                           .getDate())))
                .map(dateDialogResult -> {
                    final DateTime dateTime = new DateTime(transaction.getDate());
                    return new DateTime(dateDialogResult.getYear(), dateDialogResult.getMonthOfYear(), dateDialogResult.getDayOfMonth(), dateTime
                            .getHourOfDay(), dateTime.getMinuteOfHour()).getMillis();
                });

        final Observable<Long> timeObservable = rxBus.observe(TimeDialogFragment.TimeDialogResult.class)
                .mergeWith(ViewObservable.clicks(timeButton)
                                   .flatMap(onClickEvent -> TimeDialogFragment.show(getSupportFragmentManager(), REQUEST_TIME, rxBus, transaction
                                           .getDate())))
                .map(timeDialogResult -> {
                    final DateTime dateTime = new DateTime(transaction.getDate());
                    return new DateTime(dateTime.getYear(), dateTime.getMonthOfYear(), dateTime.getDayOfMonth(), timeDialogResult.getHourOfDay(), timeDialogResult
                            .getMinuteOfHour()).getMillis();
                });

        return Observable.merge(dateObservable, timeObservable);
    }

    @NonNull @Override public Observable<BigDecimal> onAmountChanged() {
        return amountSubject;
    }

    @NonNull @Override public Observable<String> onCurrencyChanged() {
        return currencySubject;
    }

    @NonNull @Override public Observable<Place> onPlaceChanged() {
        return placeSubject;
    }

    @NonNull @Override public Observable<Set<Tag>> onTagsChanged() {
        return tagsSubject;
    }

    @NonNull @Override public Observable<String> onNoteChanged() {
        return WidgetObservable.text(noteEditText).map(onTextChangeEvent -> onTextChangeEvent.text().toString());
    }

    @NonNull @Override public Observable<Event> onSave() {
        return ViewObservable.clicks(saveButton).map(onClickEvent -> new Event());
    }

    @Override public void showTransaction(@NonNull Transaction transaction) {
        this.transaction = transaction;
        ignoreChanges = true;

        transactionTypeRadioGroup.check(transaction.getTransactionType() == TransactionType.Expense ? R.id.transactionTypeExpenseRadioButton : R.id.transactionTypeIncomeRadioButton);
        transactionStateRadioGroup.check(transaction.getTransactionState() == TransactionState.Confirmed ? R.id.transactionStateConfirmedRadioButton : R.id.transactionStatePendingRadioButton);
        dateButton.setText(DateFormatter.date(this, transaction.getDate()));
        timeButton.setText(DateFormatter.time(this, transaction.getDate()));
        amountButton.setText(transaction.getAmount().toString());

        ignoreChanges = false;
    }

    @Override public void startResult(@NonNull Transaction transaction) {
        final Intent data = new Intent();
        data.putExtra(RESULT_EXTRA_TRANSACTION, transaction);
        setResult(RESULT_OK, data);
    }
}
