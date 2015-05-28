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

package com.mvcoding.financius.api;

import android.content.Context;

import com.mvcoding.financius.ApplicationContext;
import com.mvcoding.financius.util.PreferencesUtils;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(library = true, complete = false)
public class ApiModule {
    @Provides @Singleton public Session provideSession(@ApplicationContext Context context) {
        Session session = PreferencesUtils.get(context, Session.class.getName(), Session.class);
        if (session == null) {
            session = new Session();
        }
        session.context = context;
        return session;
    }
}
