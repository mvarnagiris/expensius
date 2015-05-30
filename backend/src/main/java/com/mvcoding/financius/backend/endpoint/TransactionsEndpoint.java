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
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.ForbiddenException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.oauth.OAuthRequestException;
import com.google.appengine.api.users.User;
import com.googlecode.objectify.Key;
import com.mvcoding.financius.backend.entity.Transaction;
import com.mvcoding.financius.backend.entity.UserAccount;
import com.mvcoding.financius.backend.util.EndpointUtils;
import com.mvcoding.financius.core.endpoints.UpdateData;
import com.mvcoding.financius.core.endpoints.body.TransactionBody;
import com.mvcoding.financius.core.endpoints.body.TransactionsBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

import static com.mvcoding.financius.backend.OfyService.ofy;

@ApiReference(Endpoint.class)
public class TransactionsEndpoint {
    private static final String PATH = "transactions";

    @ApiMethod(name = "getTransactions", httpMethod = "GET", path = PATH)
    public CollectionResponse<Transaction> listTransactions(@Named("timestamp") long timestamp, User user) throws OAuthRequestException, BadRequestException, IOException, NotFoundException, ForbiddenException {
        final UserAccount userAccount = EndpointUtils.getRequiredUserAccountAndVerifyPermissions(user);

        final List<Transaction> entities = ofy().load()
                .type(Transaction.class)
                .filter("userAccount", Key.create(UserAccount.class, userAccount.getId()))
                .filter("editTimestamp >", timestamp)
                .list();

        return CollectionResponse.<Transaction>builder().setItems(entities).build();
    }

    @ApiMethod(name = "saveTransactions", httpMethod = "POST", path = PATH)
    public UpdateData saveTransactions(TransactionsBody body, User user) throws OAuthRequestException, BadRequestException, IOException, NotFoundException, ForbiddenException {
        final UserAccount userAccount = EndpointUtils.getRequiredUserAccountAndVerifyPermissions(user);
        EndpointUtils.validateBody(body);

        final List<Transaction> entities = new ArrayList<Transaction>();
        final long timestamp = System.currentTimeMillis();
        for (TransactionBody transactionBody : body.getTransactions()) {
            final Transaction transaction = Transaction.from(userAccount, transactionBody);
            transaction.setTimestamp(timestamp);
            entities.add(transaction);
        }

        ofy().save().entities(entities).now();
        return new UpdateData(timestamp);
    }
}