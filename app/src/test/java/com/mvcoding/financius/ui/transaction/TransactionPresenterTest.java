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

import com.mvcoding.financius.UserSettings;
import com.mvcoding.financius.core.model.TransactionState;
import com.mvcoding.financius.core.model.TransactionType;
import com.mvcoding.financius.data.Api;
import com.mvcoding.financius.data.model.Place;
import com.mvcoding.financius.data.model.Tag;
import com.mvcoding.financius.data.model.Transaction;
import com.mvcoding.financius.ui.BasePresenterTest;
import com.mvcoding.financius.util.rx.Event;

import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.Set;

import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

import static org.mockito.Mockito.mock;
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
    private final PublishSubject<Event> saveSubject = PublishSubject.create();

    @Mock private Transaction defaultTransaction;
    @Mock private UserSettings userSettings;
    @Mock private Api api;

    @Override protected TransactionPresenter createPresenter() {
        return new TransactionPresenter(defaultTransaction, userSettings, api, Schedulers.immediate(), Schedulers.immediate());
    }

    @Override protected TransactionPresenter.View createView() {
        final TransactionPresenter.View view = mock(TransactionPresenter.View.class);
        when(view.onTransactionTypeChanged()).thenReturn(transactionTypeSubject);
        when(view.onTransactionStateChanged()).thenReturn(transactionStateSubject);
        when(view.onDateChanged()).thenReturn(dateSubject);
        when(view.onAmountChanged()).thenReturn(amountSubject);
        when(view.onCurrencyChanged()).thenReturn(currencySubject);
        when(view.onPlaceChanged()).thenReturn(placeSubject);
        when(view.onTagsChanged()).thenReturn(tagsSubject);
        when(view.onNoteChanged()).thenReturn(noteSubject);
        when(view.onSave()).thenReturn(saveSubject);
        return view;
    }
}