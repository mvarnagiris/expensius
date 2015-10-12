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

import com.mvcoding.financius.api.ApiModule;
import com.mvcoding.financius.feature.ActivityComponent;
import com.mvcoding.financius.feature.ActivityModule;
import com.mvcoding.financius.feature.DateDialogFragment;
import com.mvcoding.financius.feature.TimeDialogFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton @Component(modules = {AppModule.class, ApiModule.class}) public interface AppComponent extends BaseComponent {
    ActivityComponent plus(ActivityModule module);

    void inject(DateDialogFragment fragment);
    void inject(TimeDialogFragment fragment);
}
