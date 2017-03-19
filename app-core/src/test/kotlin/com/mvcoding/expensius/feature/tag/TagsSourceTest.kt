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

package com.mvcoding.expensius.feature.tag

import com.mvcoding.expensius.data.DataSource
import com.mvcoding.expensius.data.RealtimeData
import com.mvcoding.expensius.data.RealtimeList
import com.mvcoding.expensius.model.*
import com.nhaarman.mockito_kotlin.*
import org.junit.Before
import org.junit.Test
import rx.Observable.just
import rx.Observable.never
import rx.lang.kotlin.PublishSubject
import rx.observers.TestSubscriber

class TagsSourceTest {

    val appUser = anAppUser()
    val realtimeList = mock<RealtimeList<Tag>>()
    val anotherRealtimeList = mock<RealtimeList<Tag>>()

    val appUserSource = mock<DataSource<AppUser>>()
    val createRealtimeList = mock<(UserId) -> RealtimeList<Tag>>()
    val tagsSource = TagsSource(appUserSource, createRealtimeList)
    val subscriber = TestSubscriber<RealtimeData<Tag>>()
    val anotherSubscriber = TestSubscriber<RealtimeData<Tag>>()

    @Before
    fun setUp() {
        whenever(appUserSource.data()).thenReturn(just(appUser))
        whenever(createRealtimeList(any())).thenReturn(realtimeList, anotherRealtimeList)
        whenever(realtimeList.getAllItems()).thenReturn(never())
        whenever(realtimeList.getAddedItems()).thenReturn(never())
        whenever(realtimeList.getChangedItems()).thenReturn(never())
        whenever(realtimeList.getRemovedItems()).thenReturn(never())
        whenever(realtimeList.getMovedItem()).thenReturn(never())
        whenever(anotherRealtimeList.getAllItems()).thenReturn(never())
        whenever(anotherRealtimeList.getAddedItems()).thenReturn(never())
        whenever(anotherRealtimeList.getChangedItems()).thenReturn(never())
        whenever(anotherRealtimeList.getRemovedItems()).thenReturn(never())
        whenever(anotherRealtimeList.getMovedItem()).thenReturn(never())
    }

    @Test
    fun `creates new realtime list when app user id changes`() {
        val appUserSubject = PublishSubject<AppUser>()
        val appUser = anAppUser().withId("1")
        val anotherAppUser = anAppUser().withId("2")
        whenever(appUserSource.data()).thenReturn(appUserSubject)

        tagsSource.data().subscribe(subscriber)
        appUserSubject.onNext(appUser)
        appUserSubject.onNext(anotherAppUser)

        subscriber.assertNoErrors()
        verify(createRealtimeList).invoke(appUser.userId)
        verify(createRealtimeList).invoke(anotherAppUser.userId)
        verify(createRealtimeList, times(2)).invoke(any())
    }

    @Test
    fun `does not create new realtime list when app user changes but user id remains the same`() {
        val appUserSubject = PublishSubject<AppUser>()
        val appUser = anAppUser().withId("1")
        val anotherAppUser = anAppUser().withId("1")
        whenever(appUserSource.data()).thenReturn(appUserSubject)

        tagsSource.data().subscribe(subscriber)
        appUserSubject.onNext(appUser)
        appUserSubject.onNext(anotherAppUser)

        verify(createRealtimeList).invoke(appUser.userId)
        verify(createRealtimeList, times(1)).invoke(any())
    }

    @Test
    fun `closes previously created realtime list when new one is created`() {
        val appUserSubject = PublishSubject<AppUser>()
        val appUser = anAppUser().withId("1")
        val anotherAppUser = anAppUser().withId("2")
        whenever(appUserSource.data()).thenReturn(appUserSubject)

        tagsSource.data().subscribe(subscriber)
        appUserSubject.onNext(appUser)
        appUserSubject.onNext(anotherAppUser)

        subscriber.assertNoErrors()
        verify(realtimeList).close()
    }

    @Test
    fun `does not create new realtime list for another subscriber`() {
        tagsSource.data().subscribe(subscriber)

        tagsSource.data().subscribe(anotherSubscriber)

        verify(createRealtimeList, times(1)).invoke(any())
    }
}