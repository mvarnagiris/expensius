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

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.mvcoding.financius.AppContext;
import com.mvcoding.financius.BuildConfig;
import com.squareup.okhttp.Authenticator;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.Proxy;

import javax.inject.Inject;

public class EndpointAuthenticator implements Authenticator {
    private final Context context;
    private final Session session;

    @Inject public EndpointAuthenticator(@AppContext @NonNull Context context, @NonNull Session session) {
        this.context = context;
        this.session = session;
    }

    @Override public Request authenticate(Proxy proxy, Response response) throws IOException {
        String token = session.isLoggedIn() ? session.getToken() : null;
        boolean alreadyHadToken = token != null && !token.isEmpty();
        if (!alreadyHadToken) {
            token = getNewToken();
        }

        final String retryHeader = response.request().header("Retry");
        boolean isNoTokenOrAlreadyRetried = token == null || token.isEmpty() || !(retryHeader == null || retryHeader.isEmpty());
        if (isNoTokenOrAlreadyRetried) {
            return null;
        }

        if (alreadyHadToken) {
            token = getNewToken();
        }

        session.setToken(token);

        return response.request().newBuilder().header("Authorization", session.getTokenForRequest()).header("Retry", "true").build();
    }

    @Override public Request authenticateProxy(Proxy proxy, Response response) throws IOException {
        return null;
    }

    private String getNewToken() throws IOException {
        try {
            return GoogleAuthUtil.getToken(context.getApplicationContext(), session.getEmail(), "audience:server:client_id:" + BuildConfig.ENDPOINT_WEB_CLIENT_ID);
        } catch (GoogleAuthException e) {
            e.printStackTrace();
        }
        return null;
    }
}