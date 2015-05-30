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

package com.mvcoding.financius.backend.util;

import com.google.api.server.spi.response.BadRequestException;
import com.google.api.server.spi.response.ForbiddenException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.oauth.OAuthRequestException;
import com.google.appengine.api.users.User;
import com.mvcoding.financius.backend.BaseTest;
import com.mvcoding.financius.backend.entity.UserAccount;
import com.mvcoding.financius.core.endpoints.body.Body;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

public class EndpointUtilsTest extends BaseTest {
    @Test(expected = BadRequestException.class) public void validateBody_throwsBadRequestException_whenBodyIsNull() throws Exception {
        EndpointUtils.validateBody(null);
    }

    @Test(expected = BadRequestException.class) public void validateBody_throwsBadRequestException_whenBodyIsNotValid() throws Exception {
        final Body body = mock(Body.class);
        doThrow(RuntimeException.class).when(body).validate();
        EndpointUtils.validateBody(body);
    }

    @Test public void validateBody_doesNotThrowException_whenBodyIsValid() throws Exception {
        final Body body = mock(Body.class);
        EndpointUtils.validateBody(body);
    }

    @Test(expected = OAuthRequestException.class)
    public void verifyAuthenticated_throwsOAuthRequestException_whenUserIsNull() throws Exception {
        EndpointUtils.verifyAuthenticated(null);
    }

    @Test public void verifyAuthenticated_doesNotThrowException_whenUserIsNotNull() throws Exception {
        EndpointUtils.verifyAuthenticated(mockUser());
    }

    @Test(expected = NotFoundException.class)
    public void getRequiredUserAccount_throwsNotFoundException_whenUserAccountIsNotRegistered() throws Exception {
        EndpointUtils.getRequiredUserAccount(mockUser());
    }

    @Test public void getRequiredUserAccount_returnsRegisteredUserAccount_whenUserAccountIsRegistered() throws Exception {
        final User user = mockUser();
        final UserAccount userAccount = saveEntity(mockUserAccount(user));

        final UserAccount foundUserAccount = EndpointUtils.getRequiredUserAccount(user);

        assertThat(foundUserAccount.getId()).isEqualTo(userAccount.getId());
    }

    @Test(expected = ForbiddenException.class)
    public void getRequiredUserAccountAndVerifyPermissions_throwsNotFoundException_whenUserAccountIsRegisteredButNotPremium() throws Exception {
        final User user = mockUser();
        UserAccount userAccount = mockUserAccount(user);
        userAccount.setIsPremium(false);
        saveEntity(userAccount);

        EndpointUtils.getRequiredUserAccountAndVerifyPermissions(user);
    }

    @Test
    public void getRequiredUserAccountAndVerifyPermissions_doesNotThrowException_whenUserAccountIsRegisteredAndPremium() throws Exception {
        final User user = registerUser();

        EndpointUtils.getRequiredUserAccountAndVerifyPermissions(user);
    }
}
