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

import com.mvcoding.financius.UserSettings;
import com.mvcoding.financius.core.endpoints.body.TransactionBody;
import com.mvcoding.financius.core.model.TransactionState;
import com.mvcoding.financius.core.model.TransactionType;
import com.mvcoding.financius.data.Currencies;
import com.mvcoding.financius.data.DataSaveApi;
import com.mvcoding.financius.data.converter.TransactionConverter;
import com.mvcoding.financius.data.model.Place;
import com.mvcoding.financius.data.model.Tag;
import com.mvcoding.financius.data.model.Transaction;
import com.mvcoding.financius.feature.BasePresenterTest;

import org.junit.Test;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import rx.Observable;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TransactionPresenterTest extends BasePresenterTest<TransactionPresenter, TransactionPresenter.View> {
    private final PublishSubject<TransactionType> transactionTypeSubject = PublishSubject.create();
    private final PublishSubject<TransactionState> transactionStateSubject = PublishSubject.create();
    private final PublishSubject<Long> dateSubject = PublishSubject.create();
    private final PublishSubject<BigDecimal> amountSubject = PublishSubject.create();
    private final PublishSubject<String> currencySubject = PublishSubject.create();
    private final PublishSubject<Place> placeSubject = PublishSubject.create();
    private final PublishSubject<Set<Tag>> tagsSubject = PublishSubject.create();
    private final PublishSubject<String> noteSubject = PublishSubject.create();
    private final PublishSubject<Object> requestCurrencySubject = PublishSubject.create();
    private final PublishSubject<Object> saveSubject = PublishSubject.create();

    @Mock private UserSettings userSettings;
    @Mock private DataSaveApi dataSaveApi;
    @Mock private Currencies currencies;
    @Mock private TransactionConverter transactionConverter;
    @Mock private Place place;
    @Mock private TransactionBody transactionBody;

    private Transaction initialTransaction;
    private List<String> availableCurrencies;

    @Override protected TransactionPresenter createPresenter() {
        availableCurrencies = new ArrayList<>();
        availableCurrencies.add("USD");
        availableCurrencies.add("GBP");
        availableCurrencies.add("EUR");
        when(currencies.getCurrencies()).thenReturn(availableCurrencies);

        when(userSettings.getCurrency()).thenReturn("USD");

        final Set<Tag> tags = new HashSet<>();
        initialTransaction = new Transaction().withDefaultValues(userSettings);
        initialTransaction.setTransactionType(TransactionType.Income);
        initialTransaction.setTransactionState(TransactionState.Pending);
        initialTransaction.setDate(3);
        initialTransaction.setAmount(BigDecimal.TEN);
        initialTransaction.setCurrency("GBP");
        initialTransaction.setPlace(place);
        initialTransaction.setTags(tags);
        initialTransaction.setNote("note");

        when(dataSaveApi.saveTransaction(initialTransaction)).thenReturn(Observable.just(initialTransaction));
        when(transactionConverter.toBody(initialTransaction)).thenReturn(transactionBody);

        return new TransactionPresenter(initialTransaction, dataSaveApi, currencies, userSettings, transactionConverter, Schedulers.immediate(), Schedulers
                .immediate());
    }

    @Override protected TransactionPresenter.View createView() {
        final TransactionPresenter.View view = mock(TransactionPresenter.View.class);
        when(view.onTransactionTypeChanged()).thenReturn(transactionTypeSubject);
        when(view.onTransactionStateChanged()).thenReturn(transactionStateSubject);
        when(view.onDateChanged()).thenReturn(dateSubject);
        when(view.onAmountChanged()).thenReturn(amountSubject);
        when(view.showCurrencies(any())).thenReturn(currencySubject);
        when(view.onPlaceChanged()).thenReturn(placeSubject);
        when(view.onTagsChanged()).thenReturn(tagsSubject);
        when(view.onNoteChanged()).thenReturn(noteSubject);
        when(view.onRequestCurrency()).thenReturn(requestCurrencySubject);
        when(view.onSave()).thenReturn(saveSubject);
        return view;
    }

    @Test public void onViewAttached_showInitialTransaction() throws Exception {
        presenterOnViewAttached();

        verify(view).showTransaction(initialTransaction);
    }

    @Test public void showUpdatedTransaction_whenTransactionFieldsAreUpdated() throws Exception {
        presenterOnViewAttached();

        verify(view).showTransaction(initialTransaction);

        transactionTypeSubject.onNext(TransactionType.Expense);
        verify(view, times(2)).showTransaction(initialTransaction);

        transactionStateSubject.onNext(TransactionState.Confirmed);
        verify(view, times(3)).showTransaction(initialTransaction);

        dateSubject.onNext(123L);
        verify(view, times(4)).showTransaction(initialTransaction);

        amountSubject.onNext(BigDecimal.ONE);
        verify(view, times(5)).showTransaction(initialTransaction);

        requestCurrencySubject.onNext(new Object());
        currencySubject.onNext("EUR");
        verify(view, times(6)).showTransaction(initialTransaction);

        placeSubject.onNext(null);
        verify(view, times(7)).showTransaction(initialTransaction);

        tagsSubject.onNext(null);
        verify(view, times(8)).showTransaction(initialTransaction);

        noteSubject.onNext("someOtherNote");
        verify(view, times(9)).showTransaction(initialTransaction);
    }

    @Test public void onSave_startResult_whenTransactionIsSavedSuccessfully() throws Exception {
        presenterOnViewAttached();

        saveSubject.onNext(new Object());

        verify(dataSaveApi).saveTransaction(initialTransaction);
        verify(view).startResult(initialTransaction);
    }

    @Test public void onSave_doNotSave_whenValidationFails() throws Exception {
        final Throwable throwable = mock(RuntimeException.class);
        doThrow(throwable).when(transactionBody).validate();
        presenterOnViewAttached();

        saveSubject.onNext(new Object());

        verify(dataSaveApi, never()).saveTransaction(initialTransaction);
        verify(view, never()).startResult(initialTransaction);
    }

    @Test public void onSave_showError_whenSavingFails() throws Exception {
        final Throwable throwable = mock(RuntimeException.class);
        doThrow(throwable).when(dataSaveApi).saveTransaction(initialTransaction);
        presenterOnViewAttached();

        saveSubject.onNext(new Object());

        verify(dataSaveApi).saveTransaction(initialTransaction);
        verify(view, never()).startResult(initialTransaction);
        verify(view).showError(throwable);
    }

    @Test public void onRequestCurrency_showCurrencies() throws Exception {
        presenterOnViewAttached();

        requestCurrencySubject.onNext(new Object());

        verify(view).showCurrencies(availableCurrencies);
    }

    @Test public void updateUserSettings_whenCurrencyChanges() throws Exception {
        presenterOnViewAttached();

        requestCurrencySubject.onNext(new Object());
        currencySubject.onNext("EUR");

        verify(userSettings).setCurrency("EUR");
    }
}