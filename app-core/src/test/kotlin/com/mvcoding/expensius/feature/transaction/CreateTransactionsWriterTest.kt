/*
 * Copyright (C) 2017 Mantas Varnagiris.
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

package com.mvcoding.expensius.feature.transaction

import com.mvcoding.expensius.data.DataSource
import com.mvcoding.expensius.model.*
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Test
import rx.Observable

class CreateTransactionsWriterTest {
    val appUser = anAppUser()

    val appUserSource = mock<DataSource<AppUser>>()
    val createTransactions = mock<(UserId, Set<CreateTransaction>) -> Unit>()
    val createTransactionsWriter = CreateTransactionsWriter(appUserSource, createTransactions)

    @Before
    fun setUp() {
        whenever(appUserSource.data()).thenReturn(Observable.just(appUser))
    }

    @Test
    fun `writes transactions for app user`() {
        val createTransactionsSet = setOf(aCreateTransaction(), aCreateTransaction(), aCreateTransaction())

        createTransactionsWriter.write(createTransactionsSet)

        verify(createTransactions).invoke(appUser.userId, createTransactionsSet)
    }
}