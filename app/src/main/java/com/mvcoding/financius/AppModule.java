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

package com.mvcoding.financius;

import android.content.Context;
import android.support.annotation.NonNull;

import com.mvcoding.financius.util.PreferencesUtils;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@Module public class AppModule {
    private final Context context;

    public AppModule(@NonNull Context context) {
        this.context = context;
    }

    @Provides @Singleton @AppContext Context provideAppContext() {
        return context;
    }

    @Provides @Singleton public UserSettings provideUserSettings(@AppContext Context context) {
        UserSettings userSettings = PreferencesUtils.get(context, UserSettings.class.getName(), UserSettings.class);
        if (userSettings == null) {
            userSettings = new UserSettings();
        }
        userSettings.context = context;
        return userSettings;
    }

    @Provides @Singleton @Named("ui") public Scheduler provideUiScheduler() {
        return AndroidSchedulers.mainThread();
    }

    @Provides @Singleton @Named("io") public Scheduler provideIoScheduler() {
        return Schedulers.io();
    }
}
