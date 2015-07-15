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

import com.mvcoding.financius.core.model.TransactionType;
import com.mvcoding.financius.data.model.Tag;
import com.mvcoding.financius.data.model.Transaction;
import com.mvcoding.financius.ui.ActivityScope;
import com.mvcoding.financius.ui.Presenter;
import com.mvcoding.financius.ui.PresenterView;
import com.mvcoding.financius.util.rx.Event;

import java.math.BigDecimal;
import java.util.Set;

import javax.inject.Inject;

import rx.Observable;

@ActivityScope class TransactionPresenter extends Presenter<TransactionPresenter.View> {
    @Inject TransactionPresenter() {
    }

    @Override protected void onViewAttached(@NonNull View view) {
        super.onViewAttached(view);

        final Observable<Transaction> transactionObservable = Observable.never();
    }

    public interface View extends PresenterView {
        @NonNull Observable<TransactionType> onTransactionTypeChanged();
        @NonNull Observable<BigDecimal> onAmountChanged();
        @NonNull Observable<String> onCurrencyChanged();
        @NonNull Observable<Set<Tag>> onTagsChanged();
        @NonNull Observable<String> onNoteChanged();
        @NonNull Observable<Event> onSave();
        void showTransaction(@NonNull Transaction transaction);
    }
}
