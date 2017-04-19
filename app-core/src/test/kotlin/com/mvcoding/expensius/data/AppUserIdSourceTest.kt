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

package com.mvcoding.expensius.data

import com.mvcoding.expensius.model.*
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Test
import rx.Observable.from
import rx.Observable.just
import rx.observers.TestSubscriber

class AppUserIdSourceTest {

    val appUserSource = mock<DataSource<AppUser>>()
    val appUserIdSource = AppUserIdSource(appUserSource)
    val subscriber = TestSubscriber<UserId>()

    @Test
    fun `returns user id from app user`() {
        val appUser = anAppUser()
        whenever(appUserSource.data()).thenReturn(just(appUser))

        appUserIdSource.data().subscribe(subscriber)

        subscriber.assertValue(appUser.userId)
    }

    @Test
    fun `emits new user id only after it changes`() {
        val appUser = anAppUser().withId("initial")
        whenever(appUserSource.data()).thenReturn(from(listOf(appUser, appUser.copy(name = Name("changed")), appUser.withId("changed"))))

        appUserIdSource.data().subscribe(subscriber)

        subscriber.assertValues(appUser.userId, UserId("changed"))
    }
}