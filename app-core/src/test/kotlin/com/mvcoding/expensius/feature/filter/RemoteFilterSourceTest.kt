/*
 * Copyright (C) 2018 Mantas Varnagiris.
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

package com.mvcoding.expensius.feature.filter

class RemoteFilterSourceTest {

    /*val appUser = anAppUser()
    val timestamp = aTimestamp()

    val appUserSource = mock<DataSource<AppUser>>()
    val timestampProvider = mock<TimestampProvider>()
    val remoteFilterSource = RemoteFilterSource(appUserSource, timestampProvider)
    val subscriber = TestSubscriber<RemoteFilter>()

    @Before
    fun setUp() {
        whenever(appUserSource.data()).thenReturn(just(appUser))
        whenever(timestampProvider.currentTimestamp()).thenReturn(timestamp)
    }

    @Test
    fun `creates remote filter from app user`() {
        remoteFilterSource.data().subscribe(subscriber)

        subscriber.assertValue(RemoteFilter(appUser.userId, appUser.settings.reportPeriod.interval(timestamp)))
    }

    @Test
    fun `emits updated remote filter when user id changes`() {
        val appUserWithUpdatedUserId = appUser.withId("updated")
        whenever(appUserSource.data()).thenReturn(from(listOf(appUser, appUserWithUpdatedUserId)))

        remoteFilterSource.data().subscribe(subscriber)

        subscriber.assertValues(
                RemoteFilter(appUser.userId, appUser.settings.reportPeriod.interval(timestamp)),
                RemoteFilter(appUserWithUpdatedUserId.userId, appUserWithUpdatedUserId.settings.reportPeriod.interval(timestamp)))
    }

    @Test
    fun `emits updated remote filter when report period changes`() {
        // TODO Implement when there are more ReportPeriod values
    }

    @Test
    fun `does not emit again when user id or remote period do not change`() {
        val appUserWithImportantValuesUnchanged = anAppUser().withId(appUser.userId.id).withReportPeriod(appUser.settings.reportPeriod)
        whenever(appUserSource.data()).thenReturn(from(listOf(appUser, appUserWithImportantValuesUnchanged)))

        remoteFilterSource.data().subscribe(subscriber)

        subscriber.assertValue(RemoteFilter(appUser.userId, appUser.settings.reportPeriod.interval(timestamp)))
    }*/
}