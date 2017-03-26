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

import com.mvcoding.expensius.model.AppUser
import com.mvcoding.expensius.model.UserId
import com.mvcoding.expensius.model.anAppUser
import com.mvcoding.expensius.model.withId
import com.nhaarman.mockito_kotlin.*
import org.junit.Test
import rx.Observable
import rx.lang.kotlin.PublishSubject
import rx.observers.TestSubscriber

class UserIdRealtimeDataSourceTest {
    @Test
    fun `creates new realtime list when app user id changes`() {
        createsNewRealtimeListWhenAppUserIdChanges<Int> { appUserSource, createRealtimeList -> UserIdRealtimeDataSource(appUserSource, createRealtimeList, Int::toString) }
    }

    @Test
    fun `does not create new realtime list when app user changes but user id remains the same`() {
        doesNotCreateNewRealtimeListWhenAppUserChangesButUserIdRemainsTheSame<Int> { appUserSource, createRealtimeList -> UserIdRealtimeDataSource(appUserSource, createRealtimeList, Int::toString) }
    }

    @Test
    fun `closes previously created realtime list when new one is created`() {
        closesPreviouslyCreatedRealtimeListWhenNewOneIsCreated<Int> { appUserSource, createRealtimeList -> UserIdRealtimeDataSource(appUserSource, createRealtimeList, Int::toString) }
    }

    @Test
    fun `does not create new realtime list for another subscriber`() {
        doesNotCreateNewRealtimeListWhenAppUserChangesButUserIdRemainsTheSame<Int> { appUserSource, createRealtimeList -> UserIdRealtimeDataSource(appUserSource, createRealtimeList, Int::toString) }
    }
}

fun <ITEM> testUserIdRealtimeDataSource(createDataSource: (DataSource<AppUser>, (UserId) -> RealtimeList<ITEM>) -> DataSource<*>) {
    createsNewRealtimeListWhenAppUserIdChanges(createDataSource)
    doesNotCreateNewRealtimeListWhenAppUserChangesButUserIdRemainsTheSame(createDataSource)
    closesPreviouslyCreatedRealtimeListWhenNewOneIsCreated(createDataSource)
    doesNotCreateNewRealtimeListForAnotherSubscriber(createDataSource)
}

private fun <ITEM> createsNewRealtimeListWhenAppUserIdChanges(createDataSource: (DataSource<AppUser>, (UserId) -> RealtimeList<ITEM>) -> DataSource<*>) {
    val appUserSubject = PublishSubject<AppUser>()
    val appUser = anAppUser().withId("1")
    val anotherAppUser = anAppUser().withId("2")
    val appUserSource = mock<DataSource<AppUser>>()
    val createRealtimeList = mock<(UserId) -> RealtimeList<ITEM>>()
    val subscriber = TestSubscriber<Any>()
    whenever(appUserSource.data()).thenReturn(appUserSubject)
    setupRealtimeLists(createRealtimeList, mock())

    val dataSource = createDataSource(appUserSource, createRealtimeList)

    dataSource.data().subscribe(subscriber)
    appUserSubject.onNext(appUser)
    appUserSubject.onNext(anotherAppUser)

    subscriber.assertNoErrors()
    verify(createRealtimeList).invoke(appUser.userId)
    verify(createRealtimeList).invoke(anotherAppUser.userId)
    verify(createRealtimeList, times(2)).invoke(any())
}

private fun <ITEM> doesNotCreateNewRealtimeListWhenAppUserChangesButUserIdRemainsTheSame(createDataSource: (DataSource<AppUser>, (UserId) -> RealtimeList<ITEM>) -> DataSource<*>) {
    val appUserSubject = PublishSubject<AppUser>()
    val appUser = anAppUser().withId("1")
    val anotherAppUser = anAppUser().withId("1")
    val appUserSource = mock<DataSource<AppUser>>()
    val createRealtimeList = mock<(UserId) -> RealtimeList<ITEM>>()
    val subscriber = TestSubscriber<Any>()
    whenever(appUserSource.data()).thenReturn(appUserSubject)
    setupRealtimeLists(createRealtimeList, mock())

    val dataSource = createDataSource(appUserSource, createRealtimeList)

    dataSource.data().subscribe(subscriber)
    appUserSubject.onNext(appUser)
    appUserSubject.onNext(anotherAppUser)

    verify(createRealtimeList).invoke(appUser.userId)
    verify(createRealtimeList, times(1)).invoke(any())
}

private fun <ITEM> closesPreviouslyCreatedRealtimeListWhenNewOneIsCreated(createDataSource: (DataSource<AppUser>, (UserId) -> RealtimeList<ITEM>) -> DataSource<*>) {
    val appUserSubject = PublishSubject<AppUser>()
    val appUser = anAppUser().withId("1")
    val anotherAppUser = anAppUser().withId("2")
    val appUserSource = mock<DataSource<AppUser>>()
    val createRealtimeList = mock<(UserId) -> RealtimeList<ITEM>>()
    val subscriber = TestSubscriber<Any>()
    val realtimeList = mock<RealtimeList<ITEM>>()
    whenever(appUserSource.data()).thenReturn(appUserSubject)
    setupRealtimeLists(createRealtimeList, realtimeList)

    val dataSource = createDataSource(appUserSource, createRealtimeList)

    dataSource.data().subscribe(subscriber)
    appUserSubject.onNext(appUser)
    appUserSubject.onNext(anotherAppUser)

    subscriber.assertNoErrors()
    verify(realtimeList).close()
}

private fun <ITEM> doesNotCreateNewRealtimeListForAnotherSubscriber(createDataSource: (DataSource<AppUser>, (UserId) -> RealtimeList<ITEM>) -> DataSource<*>) {
    val appUserSource = mock<DataSource<AppUser>>()
    val createRealtimeList = mock<(UserId) -> RealtimeList<ITEM>>()
    val subscriber = TestSubscriber<Any>()
    val anotherSubscriber = TestSubscriber<Any>()
    whenever(appUserSource.data()).thenReturn(Observable.just(anAppUser()))
    setupRealtimeLists(createRealtimeList, mock())

    val dataSource = createDataSource(appUserSource, createRealtimeList)

    dataSource.data().subscribe(subscriber)
    dataSource.data().subscribe(anotherSubscriber)

    verify(createRealtimeList, times(1)).invoke(any())
}

private fun <ITEM> setupRealtimeLists(createRealtimeList: (UserId) -> RealtimeList<ITEM>, realtimeList: RealtimeList<ITEM>, vararg otherRealtimeList: RealtimeList<ITEM>) {
    whenever(createRealtimeList(any())).thenReturn(realtimeList, *otherRealtimeList)
    whenever(realtimeList.getAllItems()).thenReturn(Observable.never())
    whenever(realtimeList.getAddedItems()).thenReturn(Observable.never())
    whenever(realtimeList.getChangedItems()).thenReturn(Observable.never())
    whenever(realtimeList.getRemovedItems()).thenReturn(Observable.never())
    whenever(realtimeList.getMovedItem()).thenReturn(Observable.never())
    otherRealtimeList.forEach {
        whenever(it.getAllItems()).thenReturn(Observable.never())
        whenever(it.getAddedItems()).thenReturn(Observable.never())
        whenever(it.getChangedItems()).thenReturn(Observable.never())
        whenever(it.getRemovedItems()).thenReturn(Observable.never())
        whenever(it.getMovedItem()).thenReturn(Observable.never())
    }
}