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

import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.oauth.OAuthRequestException;
import com.google.appengine.api.users.User;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.ObjectifyService;
import com.mvcoding.financius.backend.entity.BaseEntity;
import com.mvcoding.financius.backend.entity.Transaction;
import com.mvcoding.financius.backend.entity.UserAccount;
import com.mvcoding.financius.backend.util.EndpointUtils;
import com.mvcoding.financius.core.endpoints.body.TransactionBody;
import com.mvcoding.financius.core.model.ModelState;
import com.mvcoding.financius.core.model.TransactionState;
import com.mvcoding.financius.core.model.TransactionType;

import org.junit.After;
import org.junit.Before;

import java.io.Closeable;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;

import javax.annotation.Nonnull;

import static com.mvcoding.financius.backend.OfyService.ofy;

public class BaseTest {
    private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
    private Closeable objectifyService;

    @Before public void setUp() {
        helper.setUp();
        objectifyService = ObjectifyService.begin();
    }

    @After public void tearDown() throws IOException {
        objectifyService.close();
        helper.tearDown();
    }

    protected User registerUser() {
        return registerUser(mockUser());
    }

    protected User registerUser1() {
        return registerUser(mockUser1());
    }

    protected User registerUser2() {
        return registerUser(mockUser2());
    }

    protected User registerUser(User user) {
        final UserAccount userAccount = mockUserAccount(user);
        saveEntity(userAccount);
        return user;
    }

    protected User mockUser() {
        return mockUser("example@email.com");
    }

    protected User mockUser1() {
        return mockUser("example1@email.com");
    }

    protected User mockUser2() {
        return mockUser("example2@email.com");
    }

    protected User mockUser(String email) {
        return new User(email, "email.com");
    }

    protected UserAccount mockUserAccount(@Nonnull User user) {
        final UserAccount userAccount = new UserAccount();
        userAccount.updateDefaults();
        userAccount.setEmail(user.getEmail());
        userAccount.setGoogleId("any");
        userAccount.setIsPremium(true);
        return userAccount;
    }

    protected Transaction mockTransaction(@Nonnull User user) throws OAuthRequestException, NotFoundException {
        final Transaction transaction = new Transaction();
        transaction.setId(UUID.randomUUID().toString());
        transaction.setUserAccount(EndpointUtils.getRequiredUserAccount(user));
        transaction.setModelState(ModelState.Normal);
        transaction.setTimestamp(1);
        transaction.setTransactionType(TransactionType.Expense);
        transaction.setTransactionState(TransactionState.Confirmed);
        transaction.setDate(System.currentTimeMillis());
        transaction.setAmount(BigDecimal.ONE);
        transaction.setCurrency("GBP");
        transaction.setPlaceId(null);
        transaction.setTagIds(null);
        transaction.setNote(null);
        return transaction;
    }

    protected TransactionBody mockTransactionBody() throws OAuthRequestException, NotFoundException {
        final TransactionBody body = new TransactionBody();
        body.setId(UUID.randomUUID().toString());
        body.setModelState(ModelState.Normal);
        body.setTransactionType(TransactionType.Expense);
        body.setTransactionState(TransactionState.Confirmed);
        body.setDate(System.currentTimeMillis());
        body.setAmount(BigDecimal.ONE);
        body.setCurrency("GBP");
        body.setPlaceId(null);
        body.setTagIds(null);
        body.setNote(null);
        return body;
    }

    protected <T extends BaseEntity> T saveEntity(T entity) {
        ofy().save().entity(entity).now();
        return entity;
    }
}
