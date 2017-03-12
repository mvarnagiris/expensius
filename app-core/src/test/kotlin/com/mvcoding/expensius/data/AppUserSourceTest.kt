package com.mvcoding.expensius.data

import com.mvcoding.expensius.model.AppUser
import com.mvcoding.expensius.model.NullModels.noAppUser
import com.mvcoding.expensius.model.NullModels.noSettings
import com.mvcoding.expensius.model.anAppUser
import com.mvcoding.expensius.model.defaultCurrency
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Test
import rx.Observable
import rx.observers.TestSubscriber

class AppUserSourceTest {

    @Test
    fun `behaves like memory cache`() {
        testMemoryCache(anAppUser(), anAppUser()) { AppUserSource({ it.data() }, mock()) }
    }

    @Test
    fun `updates app user when it changes`() {
        val updateAppUser = mock<(AppUser) -> Unit>()
        val appUserSource = AppUserSource(mock(), updateAppUser)
        val appUser = anAppUser()

        appUserSource.write(appUser)

        verify(updateAppUser).invoke(appUser)
    }

    @Test
    fun `puts default values for app user before writing`() {
        val updateAppUser = mock<(AppUser) -> Unit>()
        val getAppUser = mock<() -> Observable<AppUser>>()
        whenever(getAppUser()).thenReturn(Observable.never())
        val appUserSource = AppUserSource(getAppUser, updateAppUser)
        val appUser = noAppUser
        val subscriber = TestSubscriber<AppUser>()

        appUserSource.data().subscribe(subscriber)
        appUserSource.write(appUser)

        val appUserWithDefaultValues = noAppUser.copy(settings = noSettings.copy(mainCurrency = defaultCurrency()))
        subscriber.assertValue(appUserWithDefaultValues)
        verify(updateAppUser).invoke(appUserWithDefaultValues)
    }
}