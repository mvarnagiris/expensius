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

import com.mvcoding.financius.AppContext;
import com.mvcoding.financius.BuildConfig;
import com.mvcoding.financius.api.service.UsersService;
import com.mvcoding.financius.util.PreferencesUtils;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

@Module public class ApiModule {
    private static final String ENDPOINT = "http://" + BuildConfig.LOCAL_SERVER_IP + ":8080/_ah/api/locl";

    @Provides @Singleton
    public RestAdapter provideRestAdapter(EndpointAuthenticator endpointAuthenticator, EndpointInterceptor endpointInterceptor) {
        final OkHttpClient httpClient = new OkHttpClient();
        httpClient.setAuthenticator(endpointAuthenticator);
        httpClient.setConnectTimeout(20, TimeUnit.SECONDS);
        httpClient.setReadTimeout(20, TimeUnit.SECONDS);
        httpClient.setWriteTimeout(20, TimeUnit.SECONDS);

        return new RestAdapter.Builder().setClient(new OkClient(httpClient))
                .setEndpoint(ENDPOINT)
                .setRequestInterceptor(endpointInterceptor)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
    }

    @Provides @Singleton public SessionOld provideSession(@AppContext Context context) {
        SessionOld session = PreferencesUtils.get(context, SessionOld.class.getName(), SessionOld.class);
        if (session == null) {
            session = new SessionOld();
        }
        session.context = context;
        return session;
    }

    @Provides @Singleton public UsersService provideUsersService(RestAdapter restAdapter) {
        return restAdapter.create(UsersService.class);
    }
}
