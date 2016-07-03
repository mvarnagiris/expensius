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
import com.mvcoding.expensius.feature.ReportStep
import com.mvcoding.expensius.feature.currency.CurrenciesProvider
import com.mvcoding.expensius.model.Currency
import com.mvcoding.mvp.Presenter
import rx.Observable

class SettingsPresenter(
        private val settings: Settings,
        private val currenciesProvider: CurrenciesProvider) : Presenter<SettingsPresenter.View>() {

    override fun onViewAttached(view: View) {
        super.onViewAttached(view)
        mainCurrency(view)
        reportStep(view)
        settings.subscriptionTypes().subscribeUntilDetached { view.showSubscriptionType(it) }
        view.onSupportDeveloperSelected().subscribeUntilDetached { view.displaySupportDeveloper() }
        view.onAboutSelected().subscribeUntilDetached { view.displayAbout() }
    }

    private fun mainCurrency(view: View) {
        view.showMainCurrency(settings.mainCurrency)
        view.onMainCurrencySelected()
                .flatMap { currenciesProvider.currencies() }
                .flatMap { view.requestMainCurrency(it) }
                .doOnNext { settings.mainCurrency = it }
                .subscribeUntilDetached { view.showMainCurrency(it) }
        // TODO: Things need to get a notification that main currency has been changed and update.
    }

    private fun reportStep(view: View) {
        settings.reportSteps().subscribeUntilDetached { view.showReportStep(it) }
        view.onReportStepSelected()
                .map { ReportStep.values().toList() }
                .flatMap { view.requestReportStep(it) }
                .subscribeUntilDetached { settings.reportStep = it }
    }

    interface View : Presenter.View {
        fun onMainCurrencySelected(): Observable<Unit>
        fun onReportStepSelected(): Observable<Unit>
        fun onSupportDeveloperSelected(): Observable<Unit>
        fun onAboutSelected(): Observable<Unit>

        fun requestMainCurrency(currencies: List<Currency>): Observable<Currency>
        fun requestReportStep(reportSteps: List<ReportStep>): Observable<ReportStep>

        fun showMainCurrency(mainCurrency: Currency)
        fun showReportStep(reportStep: ReportStep)
        fun showSubscriptionType(subscriptionType: SubscriptionType)

        fun displaySupportDeveloper()
        fun displayAbout()
    }
}