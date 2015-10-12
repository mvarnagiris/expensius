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

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mvcoding.financius.AppContext;
import com.mvcoding.financius.api.ServiceApi;
import com.mvcoding.financius.data.DataLoadApi;
import com.mvcoding.financius.data.model.Tag;

import java.util.Set;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import rx.Scheduler;

@Module public class TagsModule {
    private final TagsPresenter.DisplayType displayType;
    private final Set<Tag> selectedItems;

    public TagsModule(@NonNull TagsPresenter.DisplayType displayType, @Nullable Set<Tag> selectedItems) {
        this.displayType = displayType;
        this.selectedItems = selectedItems;
    }

    @Provides
    TagsPresenter providerTagsPresenter(@AppContext Context context, DataLoadApi dataLoadApi, ServiceApi serviceApi, @Named("ui") Scheduler uiScheduler, @Named("io") Scheduler ioScheduler) {
        // TODO: Use context to get the required page size;
        return new TagsPresenter(displayType, selectedItems, dataLoadApi, serviceApi, 20, uiScheduler, ioScheduler);
    }
}
