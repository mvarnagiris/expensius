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
import com.nhaarman.mockito_kotlin.inOrder
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Test
import rx.Observable.just
import rx.lang.kotlin.BehaviorSubject
import rx.lang.kotlin.PublishSubject

class PremiumPresenterTest {
    val refreshSubject = PublishSubject<Unit>()
    val subscriptionTypeSubject = BehaviorSubject(FREE)

    val remotePremiumService = mock<RemotePremiumService>()
    val premiumPurchasesProvider = PremiumPurchasesProvider(remotePremiumService)
    val donationPurchasesProvider = DonationPurchasesProvider(remotePremiumService)
    val settings = mock<Settings>()
    val view = mock<PremiumPresenter.View>()
    val presenter = PremiumPresenter(settings, premiumPurchasesProvider, donationPurchasesProvider)
    val inOrder = inOrder(view)

    @Before
    fun setUp() {
        whenever(view.onRefresh()).thenReturn(refreshSubject)
        whenever(settings.subscriptionTypes()).thenReturn(subscriptionTypeSubject)
        whenever(remotePremiumService.premiumPurchases()).thenReturn(just(emptyList()))
        whenever(remotePremiumService.donationPurchases()).thenReturn(just(emptyList()))
    }

    @Test
    fun showsFreeUserWhenUserIsFree() {
        selectSubscriptionType(FREE)

        presenter.onViewAttached(view)

        verify(view).showFreeUser()
    }

    @Test
    fun showsPremiumUserWhenUserIsPremium() {
        selectSubscriptionType(PREMIUM_PAID)

        presenter.onViewAttached(view)

        verify(view).showPremiumUser()
    }

    @Test
    fun showsPremiumPurchasesWhenUserIsUsingFreeVersion() {
        selectSubscriptionType(FREE)
        val purchases = listOf(aPurchase(), aPurchase(), aPurchase())
        whenever(remotePremiumService.premiumPurchases()).thenReturn(just(purchases))

        presenter.onViewAttached(view)

        inOrder.verify(view).showLoading()
        inOrder.verify(view).hideLoading()
        inOrder.verify(view).hideEmptyView()
        inOrder.verify(view).showPurchases(purchases)
    }

    @Test
    fun showsDonationPurchasesWhenUserIsUsingPremiumPaidVersion() {
        selectSubscriptionType(PREMIUM_PAID)
        val purchases = listOf(aPurchase(), aPurchase(), aPurchase())
        whenever(remotePremiumService.donationPurchases()).thenReturn(just(purchases))

        presenter.onViewAttached(view)

        inOrder.verify(view).showLoading()
        inOrder.verify(view).hideLoading()
        inOrder.verify(view).hideEmptyView()
        inOrder.verify(view).showPurchases(purchases)
    }

    @Test
    fun donationsAreLoadedAfterSuccessfulPremiumPurchase() {
        selectSubscriptionType(FREE)
        val premiumPurchases = listOf(aPurchase(), aPurchase(), aPurchase())
        val donationPurchases = listOf(aPurchase(), aPurchase())
        whenever(remotePremiumService.premiumPurchases()).thenReturn(just(premiumPurchases))
        whenever(remotePremiumService.donationPurchases()).thenReturn(just(donationPurchases))
        presenter.onViewAttached(view)

        selectSubscriptionType(PREMIUM_PAID)

        inOrder.verify(view).showFreeUser()
        inOrder.verify(view).showLoading()
        inOrder.verify(view).hideLoading()
        inOrder.verify(view).hideEmptyView()
        inOrder.verify(view).showPurchases(premiumPurchases)
        inOrder.verify(view).showPremiumUser()
        inOrder.verify(view).showLoading()
        inOrder.verify(view).hideLoading()
        inOrder.verify(view).hideEmptyView()
        inOrder.verify(view).showPurchases(donationPurchases)
    }

    @Test
    fun showsEmptyViewWhenThereAreNoPurchases() {
        selectSubscriptionType(FREE)
        whenever(remotePremiumService.premiumPurchases()).thenReturn(just(emptyList()))

        presenter.onViewAttached(view)

        inOrder.verify(view).showLoading()
        inOrder.verify(view).hideLoading()
        inOrder.verify(view).showEmptyView()
        inOrder.verify(view).showPurchases(emptyList())
    }

    @Test
    fun showsUpdatedPurchasesAfterRefresh() {
        selectSubscriptionType(FREE)
        val purchases = listOf(aPurchase(), aPurchase(), aPurchase())
        val updatedPurchases = listOf(aPurchase(), aPurchase())
        whenever(remotePremiumService.premiumPurchases()).thenReturn(just(purchases), just(updatedPurchases))
        presenter.onViewAttached(view)

        refresh()

        inOrder.verify(view).showLoading()
        inOrder.verify(view).hideLoading()
        inOrder.verify(view).hideEmptyView()
        inOrder.verify(view).showPurchases(purchases)
        inOrder.verify(view).showLoading()
        inOrder.verify(view).hideLoading()
        inOrder.verify(view).hideEmptyView()
        inOrder.verify(view).showPurchases(updatedPurchases)
    }

    private fun selectSubscriptionType(subscriptionType: SubscriptionType) = subscriptionTypeSubject.onNext(subscriptionType)
    private fun refresh() = refreshSubject.onNext(Unit)
}