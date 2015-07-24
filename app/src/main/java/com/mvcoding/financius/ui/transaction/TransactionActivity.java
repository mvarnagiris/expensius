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

import com.mvcoding.financius.R;
import com.mvcoding.financius.core.model.TransactionState;
import com.mvcoding.financius.core.model.TransactionType;
import com.mvcoding.financius.data.model.Place;
import com.mvcoding.financius.data.model.Tag;
import com.mvcoding.financius.data.model.Transaction;
import com.mvcoding.financius.ui.ActivityComponent;
import com.mvcoding.financius.ui.BaseActivity;
import com.mvcoding.financius.util.rx.Event;

import java.math.BigDecimal;
import java.util.Set;

import javax.inject.Inject;

import rx.Observable;

public class TransactionActivity extends BaseActivity<TransactionPresenter.View, TransactionComponent> implements TransactionPresenter.View {
    @Inject TransactionPresenter presenter;

    @Override protected int getLayoutId() {
        return R.layout.activity_transaction;
    }

    @NonNull @Override protected TransactionComponent createComponent(@NonNull ActivityComponent component) {
        // TODO: Pass in the transaction
        return component.plus(new TransactionModule(null));
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
        return null;
    }

    @NonNull @Override public Observable<TransactionState> onTransactionStateChanged() {
        return null;
    }

    @NonNull @Override public Observable<Long> onDateChanged() {
        return null;
    }

    @NonNull @Override public Observable<BigDecimal> onAmountChanged() {
        return null;
    }

    @NonNull @Override public Observable<String> onCurrencyChanged() {
        return null;
    }

    @NonNull @Override public Observable<Place> onPlaceChanged() {
        return null;
    }

    @NonNull @Override public Observable<Set<Tag>> onTagsChanged() {
        return null;
    }

    @NonNull @Override public Observable<String> onNoteChanged() {
        return null;
    }

    @NonNull @Override public Observable<Event> onSave() {
        return null;
    }

    @Override public void showTransaction(@NonNull Transaction transaction) {

    }

    @Override public void startResult(@NonNull Transaction transaction) {

    }
}
