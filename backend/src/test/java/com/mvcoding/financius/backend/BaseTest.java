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

package com.mvcoding.financius.backend;

import com.google.appengine.api.users.User;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.ObjectifyService;
import com.mvcoding.financius.backend.entity.UserAccount;

import org.junit.After;
import org.junit.Before;

import java.io.Closeable;
import java.io.IOException;

import javax.annotation.Nonnull;

import static com.mvcoding.financius.backend.OfyService.ofy;

public class BaseTest {
    private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
    private Closeable objectifyService;

    protected static User mockUser() {
        return new User("example@email.com", "email.com");
    }

    @Before public void setUp() {
        helper.setUp();
        objectifyService = ObjectifyService.begin();
    }

    @After public void tearDown() throws IOException {
        objectifyService.close();
        helper.tearDown();
    }

    protected UserAccount saveUserAccount(@Nonnull User user) {
        final UserAccount userAccount = new UserAccount();
        userAccount.updateDefaults();
        userAccount.setEmail(user.getEmail());
        userAccount.setGoogleId("any");

        ofy().save().entity(userAccount).now();

        return userAccount;
    }
}
