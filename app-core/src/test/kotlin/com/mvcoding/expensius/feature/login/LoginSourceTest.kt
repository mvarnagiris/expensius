package com.mvcoding.expensius.feature.login

import com.mvcoding.expensius.data.DataSource
import com.mvcoding.expensius.data.DataWriter
import com.mvcoding.expensius.model.*
import com.mvcoding.expensius.model.NullModels.noAppUser
import com.nhaarman.mockito_kotlin.*
import org.junit.Before
import org.junit.Test
import rx.Observable
import rx.lang.kotlin.PublishSubject
import rx.observers.TestSubscriber

class LoginSourceTest {

    val loginSubject = PublishSubject<LoggedInUserDetails>()
    val appUserSubject = PublishSubject<AppUser>()
    val tagsSubject = PublishSubject<List<Tag>>()
    val createTagsSubject = PublishSubject<List<CreateTag>>()

    val anonymousLogin = anAnonymousLogin()
    val googleLogin = aGoogleLogin()
    val loggedInUserDetails = aLoggedInUserDetails()
    val appUser = anAppUser()
    val tags = listOf(aTag(), aTag(), aTag())
    val createTags = listOf(aCreateTag(), aCreateTag(), aCreateTag())

    val login = mock<(Login) -> Observable<LoggedInUserDetails>>()
    val logout = mock<() -> Unit>()
    val getAppUser = mock<() -> Observable<AppUser>>()
    val appUserWriter = mock<DataWriter<AppUser>>()
    val tagsSource = mock<DataSource<List<Tag>>>()
    val defaultTagsSource = mock<DataSource<List<CreateTag>>>()
    val createTagsWriter = mock<DataWriter<Set<CreateTag>>>()
    val loginSource = LoginSource(login, logout, getAppUser, appUserWriter, tagsSource, defaultTagsSource, createTagsWriter)
    val subscriber = TestSubscriber<Unit>()

    @Before
    fun setUp() {
        whenever(login(any())).thenReturn(loginSubject)
        whenever(getAppUser()).thenReturn(appUserSubject)
        whenever(tagsSource.data()).thenReturn(tagsSubject)
        whenever(defaultTagsSource.data()).thenReturn(createTagsSubject)
    }

    @Test
    fun `can login anonymously`() {
        loginSource.data(anonymousLogin).subscribe(subscriber)

        completeLogin(loggedInUserDetails)
        receiveAppUser(appUser)
        receiveTags(tags)

        subscriber.assertNoErrors()
        subscriber.assertValue(Unit)
    }

    @Test
    fun `can login with google`() {
        loginSource.data(googleLogin).subscribe(subscriber)

        completeLogin(loggedInUserDetails)
        receiveAppUser(appUser)
        receiveTags(tags)

        subscriber.assertNoErrors()
        subscriber.assertValue(Unit)
    }

    @Test
    fun `logs out when login fails`() {
        loginSource.data(googleLogin).subscribe(subscriber)

        failLogin()

        verify(logout).invoke()
    }

    @Test
    fun `updates app user details when login is successful`() {
        loginSource.data(googleLogin).subscribe(subscriber)

        completeLogin(loggedInUserDetails)
        receiveAppUser(noAppUser)
        receiveTags(tags)

        verify(appUserWriter).write(noAppUser.withLoggedInUserDetails(loggedInUserDetails))
    }

    @Test
    fun `creates default tags when user does not have any tags`() {
        loginSource.data(googleLogin).subscribe(subscriber)

        completeLogin(loggedInUserDetails)
        receiveAppUser(appUser)
        receiveTags(emptyList())
        receiveCreateTags(createTags)

        verify(createTagsWriter).write(createTags.toSet())
    }

    @Test
    fun `does not create default tags when user already has tags`() {
        loginSource.data(googleLogin).subscribe(subscriber)

        completeLogin(loggedInUserDetails)
        receiveAppUser(appUser)
        receiveTags(tags)

        verify(createTagsWriter, never()).write(any())
    }

    fun completeLogin(loggedInUserDetails: LoggedInUserDetails) = loginSubject.onNext(loggedInUserDetails)
    fun failLogin() = loginSubject.onError(Throwable())
    fun receiveAppUser(appUser: AppUser) = appUserSubject.onNext(appUser)
    fun receiveTags(tags: List<Tag>) = tagsSubject.onNext(tags)
    fun receiveCreateTags(createTags: List<CreateTag>) = createTagsSubject.onNext(createTags)
}