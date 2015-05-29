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

import android.support.annotation.NonNull;

import javax.inject.Inject;

import retrofit.RequestInterceptor;

public class EndpointInterceptor implements RequestInterceptor {
    private final Session session;

    @Inject public EndpointInterceptor(@NonNull Session session) {
        this.session = session;
    }

    @Override public void intercept(RequestFacade request) {
        if (session.isLoggedIn()) {
            request.addHeader("Authorization", session.getTokenForRequest());
        }
    }
}
