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

package com.mvcoding.expensius.feature.premium

import com.mvcoding.expensius.RxSchedulers
import com.mvcoding.expensius.data.DataSource
import com.mvcoding.expensius.data.DataWriter
import com.mvcoding.expensius.feature.*
import com.mvcoding.expensius.model.AppUser
import com.mvcoding.expensius.model.SubscriptionType
import com.mvcoding.expensius.model.SubscriptionType.FREE
import com.mvcoding.expensius.model.SubscriptionType.PREMIUM_PAID
import com.mvcoding.mvp.Presenter
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables.combineLatest
import io.reactivex.rxkotlin.withLatestFrom

class PremiumPresenter(
        private val appUserSource: DataSource<AppUser>,
        private val appUserWriter: DataWriter<AppUser>,
        private val billingProductsService: BillingProductsService,
        private val schedulers: RxSchedulers) : Presenter<PremiumPresenter.View>() {

    override fun onViewAttached(view: View) {
        super.onViewAttached(view)

        appUserSource.data()
                .subscribeOn(schedulers.io)
                .map { it.settings.subscriptionType }
                .observeOn(schedulers.main)
                .subscribeUntilDetached { view.showSubscriptionType(it) }

        view.refreshes()
                .startWith(Unit)
                .doOnNext { view.showLoading() }
                .observeOn(schedulers.io)
                .switchMap { billingData().handleError(view, schedulers, doBeforeShowError = { view.hideLoading() }) }
                .doOnNext { it.updateToPremiumPaidIfNecessary() }
                .map { it.billingProducts() }
                .observeOn(schedulers.main)
                .doOnNext { view.hideLoading() }
                .doOnNext { view.updateEmptyView(it) }
                .subscribeUntilDetached { view.showBillingProducts(it) }

        view.billingProductSelects()
                .switchMap { view.displayBuyProcess(it.id).ignoreError() }
                .observeOn(schedulers.io)
                .withLatestFrom(appUserSource.data(), { _, appUser -> appUser })
                .subscribeUntilDetached { updateToPremiumPaid(it) }
    }

    private fun billingData(): Observable<BillingData> = combineLatest(appUserSource.data(), billingProductsService.billingProducts(), ::BillingData)

    private fun View.showSubscriptionType(subscriptionType: SubscriptionType) =
            if (subscriptionType == FREE) showFreeUser()
            else showPremiumUser()

    private fun BillingData.updateToPremiumPaidIfNecessary() {
        if (shouldUpdateToPremiumPaid()) updateToPremiumPaid(appUser)
    }

    private fun updateToPremiumPaid(appUser: AppUser) = appUserWriter.write(appUser.copy(settings = appUser.settings.copy(subscriptionType = PREMIUM_PAID)))

    private data class BillingData(val appUser: AppUser, private val billingProducts: List<BillingProduct>) {
        fun subscriptionType() =
                if (appUser.settings.subscriptionType == PREMIUM_PAID || billingProducts.any { it.isOwned && it.subscriptionType == FREE }) PREMIUM_PAID
                else FREE

        fun shouldUpdateToPremiumPaid() = appUser.settings.subscriptionType != PREMIUM_PAID && subscriptionType() == PREMIUM_PAID
        fun billingProducts() = billingProducts.filter { it.subscriptionType == subscriptionType() }
    }

    interface View : Presenter.View, RefreshableView, EmptyView, ErrorView {
        fun billingProductSelects(): Observable<BillingProduct>
        fun showFreeUser()
        fun showPremiumUser()
        fun showBillingProducts(billingProducts: List<BillingProduct>)
        fun displayBuyProcess(productId: String): Observable<Unit>
    }
}