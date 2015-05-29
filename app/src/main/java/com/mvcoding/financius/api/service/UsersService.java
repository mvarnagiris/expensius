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

package com.mvcoding.financius.api.service;

import com.mvcoding.financius.api.model.User;
import com.mvcoding.financius.core.endpoints.body.RegisterUserBody;

import retrofit.http.Body;
import retrofit.http.POST;
import rx.Observable;

public interface UsersService {
    @POST("/v1/users") Observable<User> registerUser(@Body RegisterUserBody body);
}
