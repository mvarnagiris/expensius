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

import android.support.annotation.NonNull;

import com.mvcoding.financius.UserSettingsOld;
import com.mvcoding.financius.core.model.TransactionState;
import com.mvcoding.financius.core.model.TransactionType;
import com.mvcoding.financius.data.Currencies;
import com.mvcoding.financius.data.DataSaveApi;
import com.mvcoding.financius.data.converter.TransactionConverter;
import com.mvcoding.financius.data.model.Place;
import com.mvcoding.financius.data.model.Tag;
import com.mvcoding.financius.data.model.Transaction;
import com.mvcoding.financius.feature.ActivityScope;
import com.mvcoding.financius.feature.CloseablePresenterView;
import com.mvcoding.financius.feature.ErrorPresenterView;
import com.mvcoding.financius.feature.Presenter;
import com.mvcoding.financius.feature.PresenterView;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import javax.inject.Named;

import rx.Observable;
import rx.Scheduler;

@ActivityScope class TransactionPresenter extends Presenter<TransactionPresenter.View> {
    private final Transaction transaction;
    private final DataSaveApi dataSaveApi;
    private final Currencies currencies;
    private final UserSettingsOld userSettings;
    private final TransactionConverter transactionConverter;
    private final Scheduler uiScheduler;
    private final Scheduler ioScheduler;

    TransactionPresenter(@NonNull Transaction transaction, @NonNull DataSaveApi dataSaveApi, @NonNull Currencies currencies, @NonNull UserSettingsOld userSettings, @NonNull TransactionConverter transactionConverter, @NonNull @Named("ui") Scheduler uiScheduler, @NonNull @Named("io") Scheduler ioScheduler) {
        this.transaction = transaction;
        this.dataSaveApi = dataSaveApi;
        this.currencies = currencies;
        this.userSettings = userSettings;
        this.transactionConverter = transactionConverter;
        this.uiScheduler = uiScheduler;
        this.ioScheduler = ioScheduler;
    }

    @Override protected void onViewAttached(@NonNull View view) {
        super.onViewAttached(view);

        unsubscribeOnDetach(view.onSave()
                                    .withLatestFrom(transactionObservable(view).doOnNext(view::showTransaction), (event, transaction) -> transaction)
                                    .filter(this::validate)
                                    .observeOn(ioScheduler)
                                    .flatMap(dataSaveApi::saveTransaction)
                                    .observeOn(uiScheduler)
                                    .subscribeOn(uiScheduler)
                                    .subscribe(view::startResult, throwable -> showFatalError(throwable, view, view, uiScheduler)));
    }

    @NonNull private Observable<Transaction> transactionObservable(@NonNull View view) {
        final Observable<Transaction> transactionObservable = Observable.just(transaction);
        final Observable<TransactionType> transactionTypeObservable = view.onTransactionTypeChanged()
                .startWith(transaction.getTransactionType());
        final Observable<TransactionState> transactionStateObservable = view.onTransactionStateChanged()
                .startWith(transaction.getTransactionState());
        final Observable<Long> dateObservable = view.onDateChanged().startWith(transaction.getDate());
        final Observable<BigDecimal> amountObservable = view.onAmountChanged().startWith(transaction.getAmount());
        final Observable<String> currencyObservable = view.onRequestCurrency()
                .flatMap(event -> view.showCurrencies(currencies.getCurrencies()))
                .doOnNext(userSettings::setCurrency)
                .startWith(transaction.getCurrency());
        final Observable<Place> placeObservable = view.onPlaceChanged().startWith(transaction.getPlace());
        final Observable<Set<Tag>> tagsObservable = view.onTagsChanged().startWith(transaction.getTags());
        final Observable<String> noteObservable = view.onNoteChanged().startWith(transaction.getNote());
        return Observable.combineLatest(transactionObservable, transactionTypeObservable, transactionStateObservable, dateObservable, amountObservable, currencyObservable, placeObservable, tagsObservable, noteObservable, (transaction, transactionType, transactionState, date, amount, currency, place, tags, note) -> {
            transaction.setTransactionType(transactionType);
            transaction.setTransactionState(transactionState);
            transaction.setDate(date);
            transaction.setAmount(amount);
            transaction.setCurrency(currency);
            transaction.setPlace(place);
            transaction.setTags(tags);
            transaction.setNote(note);
            return transaction;
        });
    }

    private boolean validate(@NonNull Transaction transaction) {
        try {
            transactionConverter.toBody(transaction).validate();
        } catch (RuntimeException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public interface View extends PresenterView, ErrorPresenterView, CloseablePresenterView {
        @NonNull Observable<TransactionType> onTransactionTypeChanged();
        @NonNull Observable<TransactionState> onTransactionStateChanged();
        @NonNull Observable<Long> onDateChanged();
        @NonNull Observable<BigDecimal> onAmountChanged();
        @NonNull Observable<String> showCurrencies(@NonNull List<String> currencies);
        @NonNull Observable<Place> onPlaceChanged();
        @NonNull Observable<Set<Tag>> onTagsChanged();
        @NonNull Observable<String> onNoteChanged();
        @NonNull Observable<Object> onRequestCurrency();
        @NonNull Observable<Object> onSave();
        void showTransaction(@NonNull Transaction transaction);
        void startResult(@NonNull Transaction transaction);
    }
}
