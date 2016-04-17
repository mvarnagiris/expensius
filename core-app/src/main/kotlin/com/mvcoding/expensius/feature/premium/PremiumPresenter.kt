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
import com.mvcoding.expensius.feature.*
import rx.Observable.merge

class PremiumPresenter(
        private val settings: Settings,
        private val billingProductsProvider: BillingProductsProvider) : Presenter<PremiumPresenter.View>() {

    override fun onViewAttached(view: View) {
        super.onViewAttached(view)

        view.onRefresh()
                .withLatestFrom(settings.subscriptionTypes(), { unit, subscriptionType -> subscriptionType })
                .subscribeUntilDetached { refresh(it) }
        loadingStates().subscribeUntilDetached { view.showLoadingState(it) }
        emptyStates().subscribeUntilDetached { view.showEmptyState(it) }
        settings.subscriptionTypes()
                .doOnNext { view.showSubscriptionType(it) }
                .flatMap { purchases(view, it) }
                .subscribeUntilDetached { view.showPurchases(it) }
    }

    private fun View.showSubscriptionType(subscriptionType: SubscriptionType) =
            if (subscriptionType == FREE) showFreeUser()
            else showPremiumUser()

    private fun refresh(subscriptionType: SubscriptionType) =
            if (subscriptionType == FREE) billingProductsProvider.refresh()
            else donationPurchasesProvider.refresh()

    private fun purchases(view: View, subscriptionType: SubscriptionType) =
            if (subscriptionType == FREE) billingProductsProvider.data(view)
            else donationPurchasesProvider.data(view)

    private fun loadingStates() = merge(billingProductsProvider.loadingStates(), donationPurchasesProvider.loadingStates())
    private fun emptyStates() = merge(billingProductsProvider.emptyStates(), donationPurchasesProvider.emptyStates())

    interface View : Presenter.View, RefreshableView, EmptyView, ErrorView {
        fun showFreeUser()
        fun showPremiumUser()
        fun showPurchases(billingProducts: List<BillingProduct>)
    }
}