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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mvcoding.financius.util.PreferencesUtils;

import javax.inject.Singleton;

@Singleton public class SessionOld {
    transient Context context;

    private String email;
    private String token;
    private String gcmId;

    private static void persist(@NonNull Context context, @NonNull SessionOld session) {
        PreferencesUtils.put(context.getApplicationContext(), SessionOld.class.getName(), session);
    }

    public String getTokenForRequest() {
        return "Bearer " + token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(@Nullable String token) {
        this.token = token;
        persist(context, this);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(@Nullable String email) {
        this.email = email;
        persist(context, this);
    }

    public String getGcmId() {
        return gcmId;
    }

    public void setGcmId(@Nullable String gcmId) {
        this.gcmId = gcmId;
        persist(context, this);
    }

    public boolean isLoggedIn() {
        return token != null && !token.isEmpty();
    }

    public void clear() {
        email = null;
        token = null;
        persist(context, this);
    }
}
