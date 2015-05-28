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

import com.google.appengine.api.users.User;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.mvcoding.financius.backend.entity.UserAccount;

import org.junit.After;
import org.junit.Before;

import javax.annotation.Nonnull;

import static com.mvcoding.financius.backend.OfyService.ofy;

public class BaseTest {
    private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    protected static User mockUser() {
        return new User("example@email.com", "email.com");
    }

    @Before public void setUp() {
        helper.setUp();
    }

    @After public void tearDown() {
        helper.tearDown();
    }

    protected UserAccount saveUserAccount(@Nonnull User user) {
        final UserAccount userAccount = new UserAccount();
        userAccount.onCreate();
        userAccount.setEmail(user.getEmail());
        userAccount.setGoogleId("any");

        ofy().save().entity(userAccount).now();

        return userAccount;
    }
}
