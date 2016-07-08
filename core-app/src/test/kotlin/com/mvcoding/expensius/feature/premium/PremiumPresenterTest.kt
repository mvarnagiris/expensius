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
import com.mvcoding.expensius.model.SubscriptionType
import com.mvcoding.expensius.model.SubscriptionType.FREE
import com.mvcoding.expensius.model.SubscriptionType.PREMIUM_PAID
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.inOrder
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
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
    val billingProductSubject = PublishSubject<BillingProduct>()
    var purchaseSubject = PublishSubject<Unit>()

    val remoteBillingProductsService = mock<RemoteBillingProductsService>()
    val billingProductsProvider = BillingProductsProvider(remoteBillingProductsService)
    val settings = mock<Settings>()
    val view = mock<PremiumPresenter.View>()
    val presenter = PremiumPresenter(settings, billingProductsProvider)
    val inOrder = inOrder(view)

    @Before
    fun setUp() {
        whenever(view.onRefresh()).thenReturn(refreshSubject)
        whenever(view.onBillingProductSelected()).thenReturn(billingProductSubject)
        whenever(view.displayBuyProcess(any())).thenReturn(purchaseSubject)
        whenever(settings.subscriptionTypes()).thenReturn(subscriptionTypeSubject)
        whenever(remoteBillingProductsService.billingProducts()).thenReturn(just(emptyList()))
    }

    @Test
    fun showsFreeUserWhenUserIsFree() {
        setSubscriptionType(FREE)

        presenter.attach(view)

        verify(view).showFreeUser()
    }

    @Test
    fun showsPremiumUserWhenUserIsPremium() {
        setSubscriptionType(PREMIUM_PAID)

        presenter.attach(view)

        verify(view).showPremiumUser()
    }

    @Test
    fun showsPremiumBillingProductsWhenUserIsUsingFreeVersionAndDoesNotOwnPremiumProducts() {
        setSubscriptionType(FREE)
        val billingProducts = listOf(
                aBillingProduct().asPremium().notOwned(),
                aBillingProduct().asDonation(),
                aBillingProduct().asPremium().notOwned())
        whenever(remoteBillingProductsService.billingProducts()).thenReturn(just(billingProducts))

        presenter.attach(view)

        inOrder.verify(view).showLoading()
        inOrder.verify(view).hideLoading()
        inOrder.verify(view).hideEmptyView()
        inOrder.verify(view).showBillingProducts(billingProducts.filter { it.subscriptionType == FREE })
    }

    @Test
    fun convertsUserToPremiumPaidIfSheWasAFreeUserButHasOwnedPremiumBillingProducts() {
        setSubscriptionType(FREE)
        val billingProducts = listOf(
                aBillingProduct().asPremium().owned(),
                aBillingProduct().asDonation(),
                aBillingProduct().asPremium().notOwned())
        whenever(remoteBillingProductsService.billingProducts()).thenReturn(just(billingProducts))

        presenter.attach(view)

        inOrder.verify(view).showLoading()
        inOrder.verify(view).hideLoading()
        inOrder.verify(view).hideEmptyView()
        inOrder.verify(view).showBillingProducts(billingProducts.filter { it.subscriptionType == PREMIUM_PAID })
        verify(settings).subscriptionType = PREMIUM_PAID
    }

    @Test
    fun showsDonationBillingProductsWhenUserIsUsingPremiumPaidVersion() {
        setSubscriptionType(PREMIUM_PAID)
        val billingProducts = listOf(
                aBillingProduct().asPremium().owned(),
                aBillingProduct().asDonation(),
                aBillingProduct().asPremium().notOwned())
        whenever(remoteBillingProductsService.billingProducts()).thenReturn(just(billingProducts))

        presenter.attach(view)

        inOrder.verify(view).showLoading()
        inOrder.verify(view).hideLoading()
        inOrder.verify(view).hideEmptyView()
        inOrder.verify(view).showBillingProducts(billingProducts.filter { it.subscriptionType == PREMIUM_PAID })
        verify(settings, never()).subscriptionType = any()
    }

    @Test
    fun donationsAreLoadedAfterSuccessfulPremiumPurchase() {
        setSubscriptionType(FREE)
        val billingProducts = listOf(
                aBillingProduct().asPremium().notOwned(),
                aBillingProduct().asDonation(),
                aBillingProduct().asPremium().notOwned())
        whenever(remoteBillingProductsService.billingProducts()).thenReturn(just(billingProducts))
        presenter.attach(view)

        setSubscriptionType(PREMIUM_PAID)

        inOrder.verify(view).showFreeUser()
        inOrder.verify(view).showLoading()
        inOrder.verify(view).hideLoading()
        inOrder.verify(view).hideEmptyView()
        inOrder.verify(view).showBillingProducts(billingProducts.filter { it.subscriptionType == FREE })
        inOrder.verify(view).showPremiumUser()
        inOrder.verify(view).showBillingProducts(billingProducts.filter { it.subscriptionType == PREMIUM_PAID })
    }

    @Test
    fun showsEmptyViewWhenThereAreNoPurchases() {
        setSubscriptionType(FREE)
        whenever(remoteBillingProductsService.billingProducts()).thenReturn(just(emptyList()))

        presenter.attach(view)

        inOrder.verify(view).showLoading()
        inOrder.verify(view).hideLoading()
        inOrder.verify(view).showEmptyView()
        inOrder.verify(view).showBillingProducts(emptyList())
    }

    @Test
    fun showsUpdatedPurchasesAfterRefresh() {
        setSubscriptionType(FREE)
        val billingProducts = listOf(aBillingProduct().asPremium().notOwned(), aBillingProduct().asPremium().notOwned())
        val updatedBillingProducts = listOf(aBillingProduct().asPremium().notOwned())
        whenever(remoteBillingProductsService.billingProducts()).thenReturn(just(billingProducts), just(updatedBillingProducts))
        presenter.attach(view)

        refresh()

        inOrder.verify(view).showLoading()
        inOrder.verify(view).hideLoading()
        inOrder.verify(view).hideEmptyView()
        inOrder.verify(view).showBillingProducts(billingProducts)
        inOrder.verify(view).showLoading()
        inOrder.verify(view).hideLoading()
        inOrder.verify(view).hideEmptyView()
        inOrder.verify(view).showBillingProducts(updatedBillingProducts)
    }

    @Test
    fun billingProductsProviderIsDisposedOnDestroy() {
        setSubscriptionType(FREE)
        presenter.attach(view)

        presenter.onDestroy()

        verify(remoteBillingProductsService).dispose()
    }

    @Test
    fun successfulPremiumPurchaseSwitchesToPremiumPaid() {
        setSubscriptionType(FREE)
        val billingProducts = listOf(
                aBillingProduct().asPremium().notOwned(),
                aBillingProduct().asDonation())
        whenever(remoteBillingProductsService.billingProducts()).thenReturn(just(billingProducts))
        presenter.attach(view)

        selectBillingProduct(billingProducts.first())
        makeSuccessfulPurchase()

        verify(view).displayBuyProcess(billingProducts.first().id)
        verify(settings).subscriptionType = PREMIUM_PAID
    }

    @Test
    fun failedPremiumPurchaseDoesNotSwitchToPremiumPaid() {
        setSubscriptionType(FREE)
        val billingProducts = listOf(
                aBillingProduct().asPremium().notOwned(),
                aBillingProduct().asDonation())
        whenever(remoteBillingProductsService.billingProducts()).thenReturn(just(billingProducts))
        presenter.attach(view)

        selectBillingProduct(billingProducts.first())
        makeFailedPurchase()

        verify(view).displayBuyProcess(billingProducts.first().id)
        verify(settings, never()).subscriptionType = any()
    }

    @Test
    fun canMakeAnotherPurchaseAfterAFailedOne() {
        setSubscriptionType(FREE)
        val billingProducts = listOf(
                aBillingProduct().asPremium().notOwned(),
                aBillingProduct().asDonation())
        whenever(remoteBillingProductsService.billingProducts()).thenReturn(just(billingProducts))
        presenter.attach(view)
        selectBillingProduct(billingProducts.first())
        makeFailedPurchase()

        selectBillingProduct(billingProducts.first())
        makeSuccessfulPurchase()

        verify(settings).subscriptionType = PREMIUM_PAID
    }

    private fun setSubscriptionType(subscriptionType: SubscriptionType) = subscriptionTypeSubject.onNext(subscriptionType)
    private fun selectBillingProduct(billingProduct: BillingProduct) = billingProductSubject.onNext(billingProduct)
    private fun refresh() = refreshSubject.onNext(Unit)
    private fun makeSuccessfulPurchase() = purchaseSubject.onNext(Unit)
    private fun makeFailedPurchase() = purchaseSubject.onError(Throwable()).run {
        purchaseSubject = PublishSubject()
        whenever(view.displayBuyProcess(any())).thenReturn(purchaseSubject)
    }
}