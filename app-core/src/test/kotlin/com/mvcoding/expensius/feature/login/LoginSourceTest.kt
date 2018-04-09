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

package com.mvcoding.expensius.feature.login

class LoginSourceTest {

//    val loginSubject = PublishSubject<LoggedInUserDetails>()
//    val appUserSubject = PublishSubject<AppUser>()
//    val tagsSubject = PublishSubject<List<Tag>>()
//    val createTagsSubject = PublishSubject<List<CreateTag>>()
//
//    val anonymousLogin = anAnonymousLogin()
//    val googleLogin = aGoogleLogin()
//    val loggedInUserDetails = aLoggedInUserDetails()
//    val appUser = anAppUser()
//    val tags = listOf(aTag(), aTag(), aTag())
//    val createTags = listOf(aCreateTag(), aCreateTag(), aCreateTag())
//
//    val login = mock<(Login) -> Observable<LoggedInUserDetails>>()
//    val logout = mock<() -> Unit>()
//    val getAppUser = mock<() -> Observable<AppUser>>()
//    val appUserWriter = mock<DataWriter<AppUser>>()
//    val tagsSource = mock<DataSource<List<Tag>>>()
//    val defaultTagsSource = mock<DataSource<List<CreateTag>>>()
//    val createTagsWriter = mock<DataWriter<Set<CreateTag>>>()
//    val loginSource = LoginSource(login, logout, getAppUser, appUserWriter, tagsSource, defaultTagsSource, createTagsWriter)
//    val subscriber = TestSubscriber<Unit>()
//
//    @Before
//    fun setUp() {
//        whenever(login(any())).thenReturn(loginSubject)
//        whenever(getAppUser()).thenReturn(appUserSubject)
//        whenever(tagsSource.data()).thenReturn(tagsSubject)
//        whenever(defaultTagsSource.data()).thenReturn(createTagsSubject)
//    }
//
//    @Test
//    fun `can login anonymously`() {
//        loginSource.data(anonymousLogin).subscribe(subscriber)
//
//        completeLogin(loggedInUserDetails)
//        receiveAppUser(appUser)
//        receiveTags(tags)
//
//        subscriber.assertNoErrors()
//        subscriber.assertValue(Unit)
//    }
//
//    @Test
//    fun `can login with google`() {
//        loginSource.data(googleLogin).subscribe(subscriber)
//
//        completeLogin(loggedInUserDetails)
//        receiveAppUser(appUser)
//        receiveTags(tags)
//
//        subscriber.assertNoErrors()
//        subscriber.assertValue(Unit)
//    }
//
//    @Test
//    fun `logs out when login fails`() {
//        loginSource.data(googleLogin).subscribe(subscriber)
//
//        failLogin()
//
//        verify(logout).invoke()
//    }
//
//    @Test
//    fun `updates app user details when login is successful`() {
//        loginSource.data(googleLogin).subscribe(subscriber)
//
//        completeLogin(loggedInUserDetails)
//        receiveAppUser(noAppUser)
//        receiveTags(tags)
//
//        verify(appUserWriter).write(noAppUser.withLoggedInUserDetails(loggedInUserDetails))
//    }
//
//    @Test
//    fun `creates default tags when user does not have any tags`() {
//        loginSource.data(googleLogin).subscribe(subscriber)
//
//        completeLogin(loggedInUserDetails)
//        receiveAppUser(appUser)
//        receiveTags(emptyList())
//        receiveCreateTags(createTags)
//
//        verify(createTagsWriter).write(createTags.toSet())
//    }
//
//    @Test
//    fun `does not create default tags when user already has tags`() {
//        loginSource.data(googleLogin).subscribe(subscriber)
//
//        completeLogin(loggedInUserDetails)
//        receiveAppUser(appUser)
//        receiveTags(tags)
//
//        verify(createTagsWriter, never()).write(any())
//    }
//
//    fun completeLogin(loggedInUserDetails: LoggedInUserDetails) = loginSubject.onNext(loggedInUserDetails)
//    fun failLogin() = loginSubject.onError(Throwable())
//    fun receiveAppUser(appUser: AppUser) = appUserSubject.onNext(appUser)
//    fun receiveTags(tags: List<Tag>) = tagsSubject.onNext(tags)
//    fun receiveCreateTags(createTags: List<CreateTag>) = createTagsSubject.onNext(createTags)
}