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
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.oauth.OAuthRequestException;
import com.google.appengine.api.users.User;
import com.mvcoding.financius.backend.BaseTest;
import com.mvcoding.financius.backend.entity.Transaction;
import com.mvcoding.financius.core.endpoints.body.TransactionBody;
import com.mvcoding.financius.core.endpoints.body.TransactionsBody;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static com.mvcoding.financius.backend.OfyService.ofy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TransactionsEndpointTest extends BaseTest {
    private final TransactionsEndpoint transactionsEndpoint = new TransactionsEndpoint();

    @Test(expected = OAuthRequestException.class)
    public void listTransactions_throwsOAuthRequestException_whenUserIsNull() throws Exception {
        transactionsEndpoint.listTransactions(0, null);
    }

    @Test(expected = NotFoundException.class)
    public void listTransactions_throwsNotFoundException_whenUserIsNotRegistered() throws Exception {
        transactionsEndpoint.listTransactions(0, mockUser());
    }

    @Test public void listTransactions_returnsEmptyList_whenThereAreNoTransactions() throws Exception {
        final User user = registerUser();
        final CollectionResponse<Transaction> transactions = transactionsEndpoint.listTransactions(0, user);

        assertThat(transactions).isNotNull();
        assertThat(transactions.getItems()).isNotNull().isEmpty();
    }

    @Test public void listTransactions_returnsAllTransactionsForUser_whenTimestampIs0() throws Exception {
        final User user1 = registerUser1();
        final User user2 = registerUser2();
        saveEntity(mockTransaction(user1));
        saveEntity(mockTransaction(user1));
        saveEntity(mockTransaction(user2));

        final CollectionResponse<Transaction> transactions1 = transactionsEndpoint.listTransactions(0, user1);
        final CollectionResponse<Transaction> transactions2 = transactionsEndpoint.listTransactions(0, user2);

        assertThat(transactions1).isNotNull();
        assertThat(transactions1.getItems()).isNotNull().hasSize(2);
        assertThat(transactions2).isNotNull();
        assertThat(transactions2.getItems()).isNotNull().hasSize(1);
    }

    @Test public void listTransactions_returnsOnlyLaterTransactionsThanTimestamp_whenTimestampIsNot0() throws Exception {
        final User user = registerUser();
        Transaction transaction = mockTransaction(user);
        transaction.setTimestamp(0);
        saveEntity(transaction);
        transaction = mockTransaction(user);
        transaction.setTimestamp(1);
        saveEntity(transaction);
        transaction = mockTransaction(user);
        transaction.setTimestamp(2);
        saveEntity(transaction);

        final CollectionResponse<Transaction> transactions = transactionsEndpoint.listTransactions(1, user);

        assertThat(transactions.getItems()).isNotNull().hasSize(1).containsExactlyElementsOf(Collections.singletonList(transaction));
    }

    @Test(expected = OAuthRequestException.class)
    public void saveTransactions_throwsOAuthRequestException_whenUserIsNull() throws Exception {
        transactionsEndpoint.saveTransactions(mock(TransactionsBody.class), null);
    }

    @Test(expected = NotFoundException.class)
    public void saveTransactions_throwsNotFoundException_whenUserIsNotRegistered() throws Exception {
        transactionsEndpoint.saveTransactions(mock(TransactionsBody.class), mockUser());
    }

    @Test(expected = BadRequestException.class) public void saveTransactions_throwsBadRequestException_whenBodyIsNull() throws Exception {
        transactionsEndpoint.saveTransactions(null, registerUser());
    }

    @Test(expected = BadRequestException.class)
    public void saveTransactions_throwsBadRequestException_whenBodyIsNotValid() throws Exception {
        final TransactionsBody body = mock(TransactionsBody.class);
        doThrow(RuntimeException.class).when(body).validate();
        transactionsEndpoint.saveTransactions(body, registerUser());
    }

    @Test(expected = BadRequestException.class)
    public void saveTransactions_throwsBadRequestException_whenAtLeastOneTransactionBodyIsInvalid() throws Exception {
        final TransactionsBody body = mock(TransactionsBody.class);
        final TransactionBody transactionBody1 = mock(TransactionBody.class);
        final TransactionBody transactionBody2 = mock(TransactionBody.class);
        doThrow(RuntimeException.class).when(transactionBody2).validate();
        when(body.getTransactions()).thenReturn(Arrays.asList(transactionBody1, transactionBody2));

        transactionsEndpoint.saveTransactions(body, registerUser());
    }

    @Test public void saveTransactions_savesAllTransactionsFromBody_whenAllTransactionsAreNew() throws Exception {
        final TransactionsBody body = mock(TransactionsBody.class);
        when(body.getTransactions()).thenReturn(Arrays.asList(mockTransactionBody(), mockTransactionBody()));

        transactionsEndpoint.saveTransactions(body, registerUser());

        assertThat(ofy().load().type(Transaction.class).list()).hasSize(2);
    }

    @Test public void saveTransactions_setsTheSameTimestampToAllSavedTransactions() throws Exception {
        final TransactionsBody body = mock(TransactionsBody.class);
        when(body.getTransactions()).thenReturn(Arrays.asList(mockTransactionBody(), mockTransactionBody()));

        transactionsEndpoint.saveTransactions(body, registerUser());

        Long timestamp = null;
        for (Transaction transaction : ofy().load().type(Transaction.class).list()) {
            if (timestamp == null) {
                timestamp = transaction.getTimestamp();
            }

            assertThat(transaction.getTimestamp()).isEqualTo(timestamp);
        }
    }

    @Test public void saveTransactions_savesNewTransactionsAndUpdatesExistingOnes_whenThereAreExistingTransactions() throws Exception {
        final User user = registerUser();
        final Transaction existingTransaction = saveEntity(mockTransaction(user));
        final TransactionBody existingTransactionBody = mockTransactionBody();
        existingTransactionBody.setId(existingTransaction.getId());
        final TransactionsBody body = mock(TransactionsBody.class);
        when(body.getTransactions()).thenReturn(Arrays.asList(existingTransactionBody, mockTransactionBody()));

        transactionsEndpoint.saveTransactions(body, registerUser());

        assertThat(ofy().load().type(Transaction.class).list()).hasSize(2);
    }
}
