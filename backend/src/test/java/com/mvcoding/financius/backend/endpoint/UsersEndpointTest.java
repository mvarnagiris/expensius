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

import com.google.api.server.spi.response.BadRequestException;
import com.google.appengine.api.oauth.OAuthRequestException;
import com.google.appengine.api.users.User;
import com.mvcoding.financius.backend.BaseTest;
import com.mvcoding.financius.backend.entity.UserAccount;
import com.mvcoding.financius.core.endpoints.body.RegisterUserBody;

import org.junit.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UsersEndpointTest extends BaseTest {
    private final UsersEndpoint usersEndpoint = new UsersEndpoint();

    @Test(expected = OAuthRequestException.class) public void register_throwsOAuthRequestException_whenUserIsNull() throws Exception {
        usersEndpoint.register(mock(RegisterUserBody.class), null);
    }

    @Test(expected = BadRequestException.class) public void register_throwsBadRequestException_whenBodyIsNull() throws Exception {
        usersEndpoint.register(null, mockUser());
    }

    @Test(expected = BadRequestException.class) public void register_throwsBadRequestException_whenBodyIsNotValid() throws Exception {
        RegisterUserBody body = mock(RegisterUserBody.class);
        doThrow(RuntimeException.class).when(body).validate();

        usersEndpoint.register(body, mockUser());
    }

    @Test public void register_savesNewUserAccount_whenUserAccountDoesNotExist() throws Exception {
        final User user = mockUser();
        UserAccount userAccount = UserAccount.find(user);
        assertThat(userAccount).isNull();

        final RegisterUserBody body = mock(RegisterUserBody.class);
        when(body.getGoogleId()).thenReturn("any");

        userAccount = usersEndpoint.register(body, user);

        assertThat(userAccount).isNotNull();
        assertThat(userAccount.getEmail()).isEqualTo(user.getEmail());
        assertThat(userAccount.getGoogleId()).isEqualTo("any");
        assertThat(userAccount.getTimestamp()).isEqualTo(userAccount.getTimestamp());
    }

    @Test public void register_updatesExistingUserAccount_whenUserAccountExists() throws Exception {
        final User user = mockUser();
        final long existingUserAccountTimestamp = saveEntity(mockUserAccount(user)).getTimestamp();

        final RegisterUserBody body = mock(RegisterUserBody.class);
        final String googleId = UUID.randomUUID().toString();
        when(body.getGoogleId()).thenReturn(googleId);

        usersEndpoint.register(body, user);
        final UserAccount userAccount = UserAccount.find(user);

        assertThat(userAccount).isNotNull();
        assertThat(userAccount.getGoogleId()).isEqualTo(googleId);
        assertThat(userAccount.getTimestamp()).isGreaterThan(existingUserAccountTimestamp);
    }
}
