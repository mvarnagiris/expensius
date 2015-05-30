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

package com.mvcoding.financius.backend.endpoint;

import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiReference;
import com.google.api.server.spi.response.BadRequestException;
import com.google.appengine.api.oauth.OAuthRequestException;
import com.google.appengine.api.users.User;
import com.mvcoding.financius.backend.entity.UserAccount;
import com.mvcoding.financius.backend.util.EndpointUtils;
import com.mvcoding.financius.core.endpoints.body.RegisterUserBody;

import java.io.IOException;

import static com.mvcoding.financius.backend.OfyService.ofy;

@ApiReference(Endpoint.class)
public class UsersEndpoint {
    private static final String PATH = "users";

    @ApiMethod(name = "registerUser", httpMethod = "POST", path = PATH)
    public UserAccount register(RegisterUserBody body, User user) throws OAuthRequestException, BadRequestException, IOException {
        EndpointUtils.verifyAuthenticated(user);
        EndpointUtils.validateBody(body);

        UserAccount userAccount = UserAccount.find(user);
        if (userAccount == null) {
            userAccount = new UserAccount();
            userAccount.setEmail(user.getEmail());
        }

        userAccount.updateDefaults();
        userAccount.setGoogleId(body.getGoogleId());

        ofy().save().entity(userAccount).now();

        return userAccount;
    }
}