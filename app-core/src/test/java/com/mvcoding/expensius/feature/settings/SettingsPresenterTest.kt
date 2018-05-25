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

package com.mvcoding.expensius.feature.settings

class SettingsPresenterTest {
//    val appUser = anAppUser()
//
//    val appUserSubject = BehaviorSubject(appUser)
//    val loginRequestsSubject = PublishSubject<Unit>()
//    val mainCurrencyRequestsSubject = PublishSubject<Unit>()
//    val supportDeveloperRequestsSubject = PublishSubject<Unit>()
//    val aboutRequestsSubject = PublishSubject<Unit>()
//    val chooseMainCurrencySubject = PublishSubject<Currency>()
//
//    val appUserSource = mock<DataSource<AppUser>>()
//    val appUserWriter = mock<DataWriter<AppUser>>()
//    val currenciesProvider = CurrenciesSource()
//    val view = mock<SettingsPresenter.View>()
//    val presenter = SettingsPresenter(appUserSource, appUserWriter, currenciesProvider, rxSchedulers())
//
//    @Before
//    fun setUp() {
//        whenever(view.loginRequests()).thenReturn(loginRequestsSubject)
//        whenever(view.mainCurrencyRequests()).thenReturn(mainCurrencyRequestsSubject)
//        whenever(view.supportDeveloperRequests()).thenReturn(supportDeveloperRequestsSubject)
//        whenever(view.aboutRequests()).thenReturn(aboutRequestsSubject)
//        whenever(view.chooseMainCurrency(any())).thenReturn(chooseMainCurrencySubject)
//        whenever(appUserSource.data()).thenReturn(appUserSubject)
//    }
//
//    @Test
//    fun `shows app user`() {
//        presenter.attach(view)
//
//        verify(view).showAppUser(appUser)
//    }
//
//    @Test
//    fun `login requests are ignored when user is already logged in`() {
//        val appUser = appUser.withAuthProvider(GOOGLE)
//        updateAppUser(appUser)
//        presenter.attach(view)
//
//        requestLogin()
//
//        verify(view, never()).displayLogin(any())
//    }
//
//    @Test
//    fun `displays login when user is not logged in`() {
//        val appUser = appUser.withNoAuthProviders()
//        updateAppUser(appUser)
//        presenter.attach(view)
//
//        requestLogin()
//
//        verify(view).displayLogin(RETURN)
//    }
//
//    @Test
//    fun `shows main currency`() {
//        presenter.attach(view)
//
//        verify(view).showMainCurrency(appUser.settings.mainCurrency)
//    }
//
//    @Test
//    fun `can select new main currency`() {
//        val allCurrencies = TestSubscriber<List<Currency>>().apply { currenciesProvider.data().subscribe(this) }.onNextEvents.first()
//        val newCurrency = Currency("NEW")
//        val updatedAppUser = appUser.copy(settings = appUser.settings.copy(mainCurrency = newCurrency))
//        presenter.attach(view)
//
//        requestMainCurrency()
//        chooseMainCurrency(newCurrency)
//        updateAppUser(updatedAppUser)
//
//        verify(view).chooseMainCurrency(allCurrencies)
//        verify(view).showMainCurrency(newCurrency)
//        verify(appUserWriter).write(updatedAppUser)
//    }
//
//    @Test
//    fun `shows subscription type`() {
//        presenter.attach(view)
//
//        verify(view).showSubscriptionType(appUser.settings.subscriptionType)
//    }
//
//    @Test
//    fun `shows updated subscription type`() {
//        updateAppUser(appUser.copy(settings = appUser.settings.copy(subscriptionType = FREE)))
//        presenter.attach(view)
//
//        updateAppUser(appUser.copy(settings = appUser.settings.copy(subscriptionType = PREMIUM_PAID)))
//
//        verify(view).showSubscriptionType(PREMIUM_PAID)
//    }
//
//    @Test
//    fun `displays about`() {
//        presenter.attach(view)
//
//        requestAbout()
//
//        verify(view).displayAbout()
//    }
//
//    @Test
//    fun `displays support developer when user is with non anonymous account`() {
//        updateAppUser(appUser.withAuthProvider(GOOGLE))
//        presenter.attach(view)
//
//        requestSupportDeveloper()
//
//        verify(view).displaySupportDeveloper()
//    }
//
//    @Test
//    fun `displays login when user is with anonymous account`() {
//        updateAppUser(appUser.withAuthProvider(ANONYMOUS))
//        presenter.attach(view)
//
//        requestSupportDeveloper()
//
//        verify(view).displayLogin(SUPPORT_DEVELOPER)
//    }
//
//    private fun updateAppUser(appUser: AppUser) = appUserSubject.onNext(appUser)
//    private fun requestLogin() = loginRequestsSubject.onNext(Unit)
//    private fun requestMainCurrency() = mainCurrencyRequestsSubject.onNext(Unit)
//    private fun requestSupportDeveloper() = supportDeveloperRequestsSubject.onNext(Unit)
//    private fun requestAbout() = aboutRequestsSubject.onNext(Unit)
//    private fun chooseMainCurrency(currency: Currency) = chooseMainCurrencySubject.onNext(currency)
}