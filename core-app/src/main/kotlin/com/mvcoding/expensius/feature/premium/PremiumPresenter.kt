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

package com.mvcoding.expensius.feature.premium

import com.mvcoding.expensius.Settings
import com.mvcoding.expensius.SubscriptionType
import com.mvcoding.expensius.SubscriptionType.FREE
import com.mvcoding.expensius.SubscriptionType.PREMIUM_PAID
import com.mvcoding.expensius.feature.Destroyable
import com.mvcoding.expensius.feature.EmptyView
import com.mvcoding.expensius.feature.ErrorView
import com.mvcoding.expensius.feature.RefreshableView
import com.mvcoding.expensius.feature.showEmptyState
import com.mvcoding.expensius.feature.showLoadingState
import com.mvcoding.mvp.Presenter
import rx.Observable
import rx.Observable.combineLatest
import rx.Observable.empty

class PremiumPresenter(
        private val settings: Settings,
        private val billingProductsProvider: BillingProductsProvider) : Presenter<PremiumPresenter.View>(), Destroyable {

    override fun onViewAttached(view: View) {
        super.onViewAttached(view)

        settings.subscriptionTypes().subscribeUntilDetached { view.showSubscriptionType(it) }
        view.onBillingProductSelected()
                .flatMap { view.displayBuyProcess(it.id).onErrorResumeNext { empty() } }
                .subscribeUntilDetached { settings.subscriptionType = PREMIUM_PAID }
        view.onRefresh().subscribeUntilDetached { billingProductsProvider.refresh() }
        billingProductsProvider.loadingStates().subscribeUntilDetached { view.showLoadingState(it) }
        billingProductsProvider.emptyStates().subscribeUntilDetached { view.showEmptyState(it) }
        billingData(view).subscribeUntilDetached {
            settings.updateToPremiumPaidIfNecessary(it, view)
            view.showBillingProducts(it.billingProducts())
        }
    }

    private fun billingData(view: View): Observable<BillingData> = combineLatest(
            billingProductsProvider.data(view),
            settings.subscriptionTypes(),
            { billingProducts, subscriptionType -> BillingData(subscriptionType, billingProducts) })

    override fun onDestroy() {
        billingProductsProvider.dispose()
    }

    private fun View.showSubscriptionType(subscriptionType: SubscriptionType) =
            if (subscriptionType == FREE) showFreeUser()
            else showPremiumUser()

    private fun Settings.updateToPremiumPaidIfNecessary(billingData: BillingData, view: View) {
        if (billingData.shouldUpdateToPremiumPaid()) {
            subscriptionType = PREMIUM_PAID
            view.showPremiumUser()
        }
    }

    private data class BillingData(private val subscriptionType: SubscriptionType, private val billingProducts: List<BillingProduct>) {
        fun subscriptionType() =
                if (subscriptionType == PREMIUM_PAID || billingProducts.any { it.isOwned && it.subscriptionType == FREE }) PREMIUM_PAID
                else FREE

        fun shouldUpdateToPremiumPaid() = subscriptionType != PREMIUM_PAID && subscriptionType() == PREMIUM_PAID
        fun billingProducts() = billingProducts.filter { it.subscriptionType == subscriptionType() }
    }

    interface View : Presenter.View, RefreshableView, EmptyView, ErrorView {
        fun onBillingProductSelected(): Observable<BillingProduct>
        fun showFreeUser()
        fun showPremiumUser()
        fun showBillingProducts(billingProducts: List<BillingProduct>)
        fun displayBuyProcess(productId: String): Observable<Unit>
    }
}