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

import com.mvcoding.financius.data.database.DBHelper;
import com.mvcoding.financius.util.PreferencesUtils;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@Module public class AppModuleOld {
    private final Context context;

    public AppModuleOld(@NonNull Context context) {
        this.context = context;
    }

    @Provides @Singleton @AppContext Context provideAppContext() {
        return context;
    }

    @Provides @Singleton public UserSettingsOld provideUserSettings(@AppContext Context context) {
        UserSettingsOld userSettings = PreferencesUtils.get(context, UserSettingsOld.class.getName(), UserSettingsOld.class);
        if (userSettings == null) {
            userSettings = new UserSettingsOld();
        }
        userSettings.context = context;
        return userSettings;
    }

    @Provides @Singleton public BriteDatabase provideBriteDatabase(DBHelper dbHelper) {
        return SqlBrite.create().wrapDatabaseHelper(dbHelper);
    }

    @Provides @Singleton @Named("ui") public Scheduler provideUiScheduler() {
        return AndroidSchedulers.mainThread();
    }

    @Provides @Singleton @Named("io") public Scheduler provideIoScheduler() {
        return Schedulers.io();
    }
}
