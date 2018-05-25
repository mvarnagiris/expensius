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

package com.mvcoding.expensius.data

class AppUserSourceTest {

//    @Test
//    fun `behaves like memory cache`() {
//        testMemoryCache(anAppUser(), anAppUser()) { AppUserSource({ it.data() }, mock()) }
//    }
//
//    @Test
//    fun `updates app user when it changes`() {
//        val updateAppUser = mock<(AppUser) -> Unit>()
//        val appUserSource = AppUserSource(mock(), updateAppUser)
//        val appUser = anAppUser()
//
//        appUserSource.write(appUser)
//
//        verify(updateAppUser).invoke(appUser)
//    }
//
//    @Test
//    fun `puts default values for app user before writing`() {
//        val updateAppUser = mock<(AppUser) -> Unit>()
//        val getAppUser = mock<() -> Observable<AppUser>>()
//        whenever(getAppUser()).thenReturn(Observable.never())
//        val appUserSource = AppUserSource(getAppUser, updateAppUser)
//        val appUser = noAppUser
//        val subscriber = TestSubscriber<AppUser>()
//
//        appUserSource.data().subscribe(subscriber)
//        appUserSource.write(appUser)
//
//        val appUserWithDefaultValues = noAppUser.copy(settings = noSettings.copy(mainCurrency = defaultCurrency()))
//        subscriber.assertValue(appUserWithDefaultValues)
//        verify(updateAppUser).invoke(appUserWithDefaultValues)
//    }
}