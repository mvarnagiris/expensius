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

package com.mvcoding.financius.feature.transaction;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.ListPopupWindow;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.mvcoding.financius.R;
import com.mvcoding.financius.core.model.TransactionState;
import com.mvcoding.financius.core.model.TransactionType;
import com.mvcoding.financius.data.model.Place;
import com.mvcoding.financius.data.model.Tag;
import com.mvcoding.financius.data.model.Transaction;
import com.mvcoding.financius.feature.ActivityComponent;
import com.mvcoding.financius.feature.ActivityStarter;
import com.mvcoding.financius.feature.BaseActivity;
import com.mvcoding.financius.feature.DateDialogFragment;
import com.mvcoding.financius.feature.TimeDialogFragment;
import com.mvcoding.financius.feature.calculator.CalculatorActivity;
import com.mvcoding.financius.feature.tag.TagsActivity;
import com.mvcoding.financius.util.date.DateFormatter;
import com.mvcoding.financius.util.rx.RxBus;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import butterknife.Bind;
import rx.Observable;
import rx.subjects.PublishSubject;

public class TransactionActivity extends BaseActivity<TransactionPresenter.View, TransactionComponent> implements TransactionPresenter.View {
    private static final String EXTRA_TRANSACTION = "EXTRA_TRANSACTION";

    private static final String RESULT_EXTRA_TRANSACTION = "RESULT_EXTRA_TRANSACTION";

    private static final int REQUEST_DATE = 1;
    private static final int REQUEST_TIME = 2;
    private static final int REQUEST_AMOUNT = 3;
    private static final int REQUEST_PLACE = 4;
    private static final int REQUEST_TAGS = 5;
    private static final int REQUEST_RESOLVE_ERROR = 6;

    private static final PublishSubject<TransactionType> transactionTypeSubject = PublishSubject.create();
    private static final PublishSubject<TransactionState> transactionStateSubject = PublishSubject.create();
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

        // TODO: Run these through presenter.
        amountButton.setOnClickListener(v -> CalculatorActivity.startForResult(this, REQUEST_AMOUNT, transaction.getAmount()));
        placeButton.setOnClickListener(v -> onPlaceRequested());
        tagsButton.setOnClickListener(v -> TagsActivity.startForResult(this, REQUEST_TAGS, transaction.getTags()));
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_AMOUNT:
                if (resultCode == RESULT_OK) {
                    amountSubject.onNext(CalculatorActivity.getResultNumber(data));
                }
                break;
            case REQUEST_PLACE:
                if (resultCode == RESULT_OK) {
                    final com.google.android.gms.location.places.Place place = PlacePicker.getPlace(data, this);
                    placeSubject.onNext(new Place(place));
                }
                break;
            case REQUEST_TAGS:
                if (resultCode == RESULT_OK) {
                    final Set<Tag> tags = TagsActivity.getResultTags(data);
                    tagsSubject.onNext(tags);
                }
                break;
            case REQUEST_RESOLVE_ERROR:
                if (resultCode == RESULT_OK) {
                    placeButton.performClick();
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
                .mergeWith(RxView.clicks(dateButton)
                                   .flatMap(o -> DateDialogFragment.show(getSupportFragmentManager(), REQUEST_DATE, rxBus, transaction.getDate())))
                .map(dateDialogResult -> {
                    final DateTime dateTime = new DateTime(transaction.getDate());
                    return new DateTime(dateDialogResult.getYear(), dateDialogResult.getMonthOfYear(), dateDialogResult.getDayOfMonth(), dateTime
                            .getHourOfDay(), dateTime.getMinuteOfHour()).getMillis();
                });

        final Observable<Long> timeObservable = rxBus.observe(TimeDialogFragment.TimeDialogResult.class)
                .mergeWith(RxView.clicks(timeButton)
                                   .flatMap(o -> TimeDialogFragment.show(getSupportFragmentManager(), REQUEST_TIME, rxBus, transaction.getDate())))
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

    @NonNull @Override public Observable<String> showCurrencies(@NonNull List<String> currencies) {
        final ListPopupWindow listPopupWindow = new ListPopupWindow(this);
        listPopupWindow.setAnchorView(currencyButton);
        listPopupWindow.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, android.R.id.text1, currencies));
        listPopupWindow.setOnItemClickListener((parent, view, position, id) -> {
            currencySubject.onNext(currencies.get(position));
            listPopupWindow.dismiss();
        });
        listPopupWindow.show();

        return currencySubject;
    }

    @NonNull @Override public Observable<Place> onPlaceChanged() {
        return placeSubject;
    }

    @NonNull @Override public Observable<Set<Tag>> onTagsChanged() {
        return tagsSubject;
    }

    @NonNull @Override public Observable<String> onNoteChanged() {
        return RxTextView.textChanges(noteEditText).map(CharSequence::toString);
    }

    @NonNull @Override public Observable<Object> onRequestCurrency() {
        return RxView.clicks(currencyButton);
    }

    @NonNull @Override public Observable<Object> onSave() {
        return RxView.clicks(saveButton);
    }

    @Override public void showTransaction(@NonNull Transaction transaction) {
        this.transaction = transaction;
        ignoreChanges = true;

        transactionTypeRadioGroup.check(transaction.getTransactionType() == TransactionType.Expense ? R.id.transactionTypeExpenseRadioButton : R.id.transactionTypeIncomeRadioButton);
        transactionStateRadioGroup.check(transaction.getTransactionState() == TransactionState.Confirmed ? R.id.transactionStateConfirmedRadioButton : R.id.transactionStatePendingRadioButton);
        dateButton.setText(DateFormatter.date(this, transaction.getDate()));
        timeButton.setText(DateFormatter.time(this, transaction.getDate()));
        amountButton.setText(transaction.getAmount().toString());
        currencyButton.setText(transaction.getCurrency());
        final Place place = transaction.getPlace();
        placeButton.setText(place == null ? null : place.getName());
        final Set<Tag> tags = transaction.getTags();
        final StringBuilder tagsStringBuilder = new StringBuilder();
        if (tags != null) {
            for (Tag tag : tags) {
                if (tagsStringBuilder.length() > 0) {
                    tagsStringBuilder.append(", ");
                }
                tagsStringBuilder.append(tag.getTitle());
            }
        }
        tagsButton.setText(tagsStringBuilder.toString());

        ignoreChanges = false;
    }

    @Override public void startResult(@NonNull Transaction transaction) {
        final Intent data = new Intent();
        data.putExtra(RESULT_EXTRA_TRANSACTION, transaction);
        setResult(RESULT_OK, data);
        close();
    }

    private void onPlaceRequested() {
        try {
            final PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
            final Place place = transaction.getPlace();
            if (place != null) {
                intentBuilder.setLatLngBounds(LatLngBounds.builder()
                                                      .include(new LatLng(place.getLatitude(), place.getLongitude()))
                                                      .build());
            }
            startActivityForResult(intentBuilder.build(this), REQUEST_PLACE);
        } catch (Exception e) {
            e.printStackTrace();
            resolvePlayServicesError(e);
        }
    }

    private void resolvePlayServicesError(@NonNull Throwable throwable) {
        if (throwable instanceof GooglePlayServicesRepairableException) {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(((GooglePlayServicesRepairableException) throwable).getConnectionStatusCode(), this, REQUEST_RESOLVE_ERROR);
            dialog.show();
        }
    }
}
