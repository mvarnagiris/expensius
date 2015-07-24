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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mvcoding.financius.UserSettings;
import com.mvcoding.financius.core.model.TransactionState;
import com.mvcoding.financius.core.model.TransactionType;
import com.mvcoding.financius.data.DataApi;
import com.mvcoding.financius.data.model.Place;
import com.mvcoding.financius.data.model.Tag;
import com.mvcoding.financius.data.model.Transaction;
import com.mvcoding.financius.ui.ActivityScope;
import com.mvcoding.financius.ui.CloseablePresenterView;
import com.mvcoding.financius.ui.ErrorPresenterView;
import com.mvcoding.financius.ui.Presenter;
import com.mvcoding.financius.ui.PresenterView;
import com.mvcoding.financius.util.rx.Event;

import java.math.BigDecimal;
import java.util.Set;

import javax.inject.Named;

import rx.Observable;
import rx.Scheduler;

@ActivityScope class TransactionPresenter extends Presenter<TransactionPresenter.View> {
    private final Transaction transaction;
    private final UserSettings userSettings;
    private final DataApi dataApi;
    private final Scheduler uiScheduler;
    private final Scheduler ioScheduler;

    TransactionPresenter(@Nullable Transaction transaction, @NonNull UserSettings userSettings, @NonNull DataApi dataApi, @NonNull @Named("ui") Scheduler uiScheduler, @NonNull @Named("io") Scheduler ioScheduler) {
        this.transaction = transaction;
        this.userSettings = userSettings;
        this.dataApi = dataApi;
        this.uiScheduler = uiScheduler;
        this.ioScheduler = ioScheduler;
    }

    @Override protected void onViewAttached(@NonNull View view) {
        super.onViewAttached(view);

        unsubscribeOnDetach(view.onSave()
                                    .withLatestFrom(transactionObservable(view).doOnNext(view::showTransaction), (event, transaction) -> transaction)
                                    .filter(this::validate)
                                    .observeOn(ioScheduler)
                                    .flatMap(dataApi::saveTransaction)
                                    .observeOn(uiScheduler)
                                    .subscribeOn(uiScheduler)
                                    .subscribe(view::startResult, throwable -> showFatalError(throwable, view, view, uiScheduler)));
    }

    @NonNull private Observable<Transaction> transactionObservable(@NonNull View view) {
        final Transaction defaultTransaction = this.transaction == null ? new Transaction().withDefaultValues(userSettings) : this.transaction;
        final Observable<TransactionType> transactionTypeObservable = view.onTransactionTypeChanged()
                .startWith(defaultTransaction.getTransactionType());
        final Observable<TransactionState> transactionStateObservable = view.onTransactionStateChanged()
                .startWith(defaultTransaction.getTransactionState());
        final Observable<Long> dateObservable = view.onDateChanged().startWith(defaultTransaction.getDate());
        final Observable<BigDecimal> amountObservable = view.onAmountChanged().startWith(defaultTransaction.getAmount());
        final Observable<String> currencyObservable = view.onCurrencyChanged().startWith(defaultTransaction.getCurrency());
        final Observable<Place> placeObservable = view.onPlaceChanged().startWith(defaultTransaction.getPlace());
        final Observable<Set<Tag>> tagsObservable = view.onTagsChanged().startWith(defaultTransaction.getTags());
        final Observable<String> noteObservable = view.onNoteChanged().startWith(defaultTransaction.getNote());
        return Observable.combineLatest(transactionTypeObservable, transactionStateObservable, dateObservable, amountObservable, currencyObservable, placeObservable, tagsObservable, noteObservable, (transactionType, transactionState, date, amount, currency, place, tags, note) -> {
            final Transaction transaction = new Transaction();
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
            transaction.validate();
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
        @NonNull Observable<String> onCurrencyChanged();
        @NonNull Observable<Place> onPlaceChanged();
        @NonNull Observable<Set<Tag>> onTagsChanged();
        @NonNull Observable<String> onNoteChanged();
        @NonNull Observable<Event> onSave();
        void showTransaction(@NonNull Transaction transaction);
        void startResult(@NonNull Transaction transaction);
    }
}
