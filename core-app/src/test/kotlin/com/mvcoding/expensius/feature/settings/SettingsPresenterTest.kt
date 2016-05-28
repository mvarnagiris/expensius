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

import com.mvcoding.expensius.Settings
import com.mvcoding.expensius.SubscriptionType
import com.mvcoding.expensius.SubscriptionType.FREE
import com.mvcoding.expensius.SubscriptionType.PREMIUM_PAID
import com.mvcoding.expensius.feature.ReportStep
import com.mvcoding.expensius.feature.ReportStep.DAY
import com.mvcoding.expensius.feature.ReportStep.WEEK
import com.mvcoding.expensius.feature.currency.CurrenciesProvider
import com.mvcoding.expensius.model.Currency
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doAnswer
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Test
import rx.lang.kotlin.BehaviorSubject
import rx.lang.kotlin.PublishSubject
import rx.observers.TestSubscriber.create

class SettingsPresenterTest {
    val selectMainCurrencySubject = PublishSubject<Unit>()
    val selectReportStepSubject = PublishSubject<Unit>()
    val selectSupportDeveloperSubject = PublishSubject<Unit>()
    val selectAboutSubject = PublishSubject<Unit>()

    val mainCurrencyRequestSubject = PublishSubject<Currency>()
    val reportStepRequestSubject = PublishSubject<ReportStep>()

    val reportStepsSubject = BehaviorSubject(DAY)
    val subscriptionTypesSubject = BehaviorSubject(FREE)

    val settings = mock<Settings>()
    val currenciesProvider = CurrenciesProvider()
    val view = mock<SettingsPresenter.View>()
    val presenter = SettingsPresenter(settings, currenciesProvider)

    @Before
    fun setUp() {
        whenever(view.onMainCurrencySelected()).thenReturn(selectMainCurrencySubject)
        whenever(view.onReportStepSelected()).thenReturn(selectReportStepSubject)
        whenever(view.onSupportDeveloperSelected()).thenReturn(selectSupportDeveloperSubject)
        whenever(view.onAboutSelected()).thenReturn(selectAboutSubject)
        whenever(view.requestMainCurrency(any())).thenReturn(mainCurrencyRequestSubject)
        whenever(view.requestReportStep(any())).thenReturn(reportStepRequestSubject)
        whenever(settings.subscriptionTypes()).thenReturn(subscriptionTypesSubject)
        whenever(settings.reportSteps()).thenReturn(reportStepsSubject)
        whenever(settings.reportStep).thenReturn(DAY)
        doAnswer { reportStepsSubject.onNext(it.getArgument(0)) }.whenever(settings).reportStep = any()
    }

    @Test
    fun showsMainCurrency() {
        val mainCurrency = Currency("GBP")
        whenever(settings.mainCurrency).thenReturn(mainCurrency)

        presenter.onViewAttached(view)

        verify(view).showMainCurrency(mainCurrency)
    }

    @Test
    fun canSelectNewMainCurrency() {
        val oldCurrency = Currency("GBP")
        val newCurrency = Currency("EUR")
        val allCurrencies = create<List<Currency>>().apply { currenciesProvider.currencies().subscribe(this) }.onNextEvents.first()
        whenever(settings.mainCurrency).thenReturn(oldCurrency)
        presenter.onViewAttached(view)

        selectMainCurrency()
        setMainCurrency(newCurrency)

        verify(view).requestMainCurrency(allCurrencies)
        verify(view).showMainCurrency(newCurrency)
        verify(settings).mainCurrency = newCurrency
    }

    @Test
    fun showsReportStep() {
        presenter.onViewAttached(view)

        verify(view).showReportStep(DAY)
    }

    @Test
    fun canSelectNewReportStep() {
        presenter.onViewAttached(view)

        selectReportStep()
        setReportStep(WEEK)

        verify(view).requestReportStep(ReportStep.values().toList())
        verify(view).showReportStep(WEEK)
        verify(settings).reportStep = WEEK
    }

    @Test
    fun showsSubscriptionType() {
        setSubscriptionType(PREMIUM_PAID)

        presenter.onViewAttached(view)

        verify(view).showSubscriptionType(PREMIUM_PAID)
    }

    @Test
    fun showsUpdatedSubscriptionType() {
        setSubscriptionType(FREE)
        presenter.onViewAttached(view)

        setSubscriptionType(PREMIUM_PAID)

        verify(view).showSubscriptionType(PREMIUM_PAID)
    }

    @Test
    fun displaysAbout() {
        presenter.onViewAttached(view)

        selectAbout()

        verify(view).displayAbout()
    }

    @Test
    fun displaysSupportDeveloper() {
        presenter.onViewAttached(view)

        selectSupportDeveloper()

        verify(view).displaySupportDeveloper()
    }

    private fun selectMainCurrency() = selectMainCurrencySubject.onNext(Unit)
    private fun selectReportStep() = selectReportStepSubject.onNext(Unit)
    private fun selectSupportDeveloper() = selectSupportDeveloperSubject.onNext(Unit)
    private fun selectAbout() = selectAboutSubject.onNext(Unit)
    private fun setMainCurrency(currency: Currency) = mainCurrencyRequestSubject.onNext(currency)
    private fun setReportStep(reportStep: ReportStep) = reportStepRequestSubject.onNext(reportStep)
    private fun setSubscriptionType(subscriptionType: SubscriptionType) = subscriptionTypesSubject.onNext(subscriptionType)
}