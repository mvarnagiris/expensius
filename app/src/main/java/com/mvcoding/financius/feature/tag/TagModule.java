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

package com.mvcoding.financius.feature.tag;

import android.support.annotation.NonNull;

import com.mvcoding.financius.UserSettingsOld;
import com.mvcoding.financius.data.Currencies;
import com.mvcoding.financius.data.DataSaveApi;
import com.mvcoding.financius.data.converter.TagConverter;
import com.mvcoding.financius.data.model.Tag;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import rx.Scheduler;

@Module public class TagModule {
    private final Tag tag;

    public TagModule(@NonNull Tag tag) {
        this.tag = tag;
    }

    @Provides
    TagPresenter provideTransactionPresenter(UserSettingsOld userSettings, DataSaveApi dataSaveApi, Currencies currencies, TagConverter tagConverter, @Named("ui") Scheduler uiScheduler, @Named("io") Scheduler ioScheduler) {
        return new TagPresenter(tag, dataSaveApi, tagConverter, uiScheduler, ioScheduler);
    }
}
