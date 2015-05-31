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

import com.mvcoding.financius.api.ApiModule;
import com.mvcoding.financius.ui.UIModule;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(library = true, includes = {ApiModule.class, UIModule.class})
public class AppModule {
    private final App app;

    public AppModule(App app) {
        this.app = app;
    }

    @Provides @Singleton @ApplicationContext public Context provideApplicationContext() {
        return app;
    }
}
