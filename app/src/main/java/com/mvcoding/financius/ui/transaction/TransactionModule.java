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

import android.support.annotation.Nullable;

import com.mvcoding.financius.UserSettings;
import com.mvcoding.financius.data.DataApi;
import com.mvcoding.financius.data.model.Transaction;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import rx.Scheduler;

@Module public class TransactionModule {
    private final Transaction transaction;

    public TransactionModule(@Nullable Transaction transaction) {
        this.transaction = transaction;
    }

    @Provides
    TransactionPresenter provideTransactionPresenter(UserSettings userSettings, DataApi dataApi, @Named("ui") Scheduler uiScheduler, @Named("io") Scheduler ioScheduler) {
        return new TransactionPresenter(transaction == null ? new Transaction().withDefaultValues(userSettings) : transaction, dataApi, uiScheduler, ioScheduler);
    }
}
