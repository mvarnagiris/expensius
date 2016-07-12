/*
 * Copyright (C) 2016 Mantas Varnagiris.
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

import com.mvcoding.expensius.feature.currency.CurrenciesProvider
import com.mvcoding.expensius.feature.login.LoginPresenter.Destination.SUPPORT_DEVELOPER
import com.mvcoding.expensius.model.AppUser
import com.mvcoding.expensius.model.AuthProvider.ANONYMOUS
import com.mvcoding.expensius.model.AuthProvider.GOOGLE
import com.mvcoding.expensius.model.Currency
import com.mvcoding.expensius.model.ReportGroup
import com.mvcoding.expensius.model.ReportGroup.DAY
import com.mvcoding.expensius.model.ReportGroup.WEEK
import com.mvcoding.expensius.model.SubscriptionType.FREE
import com.mvcoding.expensius.model.SubscriptionType.PREMIUM_PAID
import com.mvcoding.expensius.model.anAppUser
import com.mvcoding.expensius.model.withAuthProvider
import com.mvcoding.expensius.rxSchedulers
import com.mvcoding.expensius.service.AppUserService
import com.mvcoding.expensius.service.AppUserWriteService
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Test
import rx.Observable.just
import rx.lang.kotlin.BehaviorSubject
import rx.lang.kotlin.PublishSubject
import rx.observers.TestSubscriber

class SettingsPresenterTest {
    val appUser = anAppUser()

    val appUserSubject = BehaviorSubject(appUser)
    val mainCurrencyRequestsSubject = PublishSubject<Unit>()
    val reportStepRequestsSubject = PublishSubject<Unit>()
    val supportDeveloperRequestsSubject = PublishSubject<Unit>()
    val aboutRequestsSubject = PublishSubject<Unit>()
    val chooseMainCurrencySubject = PublishSubject<Currency>()
    val chooseReportGroupSubject = PublishSubject<ReportGroup>()

    val appUserService: AppUserService = mock()
    val appUserWriteService: AppUserWriteService = mock()
    val currenciesProvider = CurrenciesProvider()
    val view: SettingsPresenter.View = mock()
    val presenter = SettingsPresenter(appUserService, appUserWriteService, currenciesProvider, rxSchedulers())

    @Before
    fun setUp() {
        whenever(view.mainCurrencyRequests()).thenReturn(mainCurrencyRequestsSubject)
        whenever(view.reportStepRequests()).thenReturn(reportStepRequestsSubject)
        whenever(view.supportDeveloperRequests()).thenReturn(supportDeveloperRequestsSubject)
        whenever(view.aboutRequests()).thenReturn(aboutRequestsSubject)
        whenever(view.chooseMainCurrency(any())).thenReturn(chooseMainCurrencySubject)
        whenever(view.chooseReportGroup(any())).thenReturn(chooseReportGroupSubject)
        whenever(appUserService.appUser()).thenReturn(appUserSubject)
        whenever(appUserWriteService.saveSettings(any())).thenReturn(just(Unit))
    }

    @Test
    fun showsMainCurrency() {
        presenter.attach(view)

        verify(view).showMainCurrency(appUser.settings.currency)
    }

    @Test
    fun canSelectNewMainCurrency() {
        val allCurrencies = TestSubscriber<List<Currency>>().apply { currenciesProvider.currencies().subscribe(this) }.onNextEvents.first()
        val newCurrency = Currency("NEW")
        val updatedSettings = appUser.settings.withCurrency(newCurrency)
        presenter.attach(view)

        requestMainCurrency()
        chooseMainCurrency(newCurrency)
        updateAppUser(appUser.withSettings(updatedSettings))

        verify(view).chooseMainCurrency(allCurrencies)
        verify(view).showMainCurrency(newCurrency)
        verify(appUserWriteService).saveSettings(updatedSettings)
    }

    @Test
    fun showsReportGroup() {
        presenter.attach(view)

        verify(view).showReportGroup(appUser.settings.reportGroup)
    }

    @Test
    fun canSelectNewReportStep() {
        val updatedSettings = appUser.settings.withReportGroup(WEEK)
        updateAppUser(appUser.withSettings(appUser.settings.withReportGroup(DAY)))
        presenter.attach(view)

        requestReportGroup()
        chooseReportGroup(WEEK)
        updateAppUser(appUser.withSettings(updatedSettings))

        verify(view).chooseReportGroup(ReportGroup.values().toList())
        verify(view).showReportGroup(WEEK)
        verify(appUserWriteService).saveSettings(updatedSettings)
    }

    @Test
    fun showsSubscriptionType() {
        presenter.attach(view)

        verify(view).showSubscriptionType(appUser.settings.subscriptionType)
    }

    @Test
    fun showsUpdatedSubscriptionType() {
        updateAppUser(appUser.withSettings(appUser.settings.withSubscriptionType(FREE)))
        presenter.attach(view)

        updateAppUser(appUser.withSettings(appUser.settings.withSubscriptionType(PREMIUM_PAID)))

        verify(view).showSubscriptionType(PREMIUM_PAID)
    }

    @Test
    fun displaysAbout() {
        presenter.attach(view)

        requestAbout()

        verify(view).displayAbout()
    }

    @Test
    fun displaysSupportDeveloperWhenUserIsWithNonAnonymousAccount() {
        updateAppUser(appUser.withAuthProvider(GOOGLE))
        presenter.attach(view)

        requestSupportDeveloper()

        verify(view).displaySupportDeveloper()
    }

    @Test
    fun displaysLoginWhenUserIsWithAnonymousAccount() {
        updateAppUser(appUser.withAuthProvider(ANONYMOUS))
        presenter.attach(view)

        requestSupportDeveloper()

        verify(view).displayLogin(SUPPORT_DEVELOPER)
    }

    private fun updateAppUser(appUser: AppUser) = appUserSubject.onNext(appUser)
    private fun requestMainCurrency() = mainCurrencyRequestsSubject.onNext(Unit)
    private fun requestReportGroup() = reportStepRequestsSubject.onNext(Unit)
    private fun requestSupportDeveloper() = supportDeveloperRequestsSubject.onNext(Unit)
    private fun requestAbout() = aboutRequestsSubject.onNext(Unit)
    private fun chooseMainCurrency(currency: Currency) = chooseMainCurrencySubject.onNext(currency)
    private fun chooseReportGroup(reportGroup: ReportGroup) = chooseReportGroupSubject.onNext(reportGroup)
}