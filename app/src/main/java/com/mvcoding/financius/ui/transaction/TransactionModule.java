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

import com.mvcoding.financius.UserSettings;
import com.mvcoding.financius.data.Currencies;
import com.mvcoding.financius.data.DataSaveApi;
import com.mvcoding.financius.data.converter.TransactionConverter;
import com.mvcoding.financius.data.model.Transaction;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import rx.Scheduler;

@Module public class TransactionModule {
    private final Transaction transaction;

    public TransactionModule(@NonNull Transaction transaction) {
        this.transaction = transaction;
    }

    @Provides
    TransactionPresenter provideTransactionPresenter(UserSettings userSettings, DataSaveApi dataSaveApi, Currencies currencies, TransactionConverter transactionConverter, @Named("ui") Scheduler uiScheduler, @Named("io") Scheduler ioScheduler) {
        return new TransactionPresenter(transaction, dataSaveApi, currencies, userSettings, transactionConverter, uiScheduler, ioScheduler);
    }
}
