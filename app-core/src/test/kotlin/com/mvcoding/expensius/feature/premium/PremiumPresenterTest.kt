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

import com.mvcoding.expensius.data.DataSource
import com.mvcoding.expensius.data.DataWriter
import com.mvcoding.expensius.model.AppUser
import com.mvcoding.expensius.model.SubscriptionType
import com.mvcoding.expensius.model.SubscriptionType.FREE
import com.mvcoding.expensius.model.SubscriptionType.PREMIUM_PAID
import com.mvcoding.expensius.model.extensions.anAppUser
import com.mvcoding.expensius.rxSchedulers
import com.nhaarman.mockito_kotlin.*
import org.junit.Before
import org.junit.Test
import rx.Observable.just
import rx.lang.kotlin.BehaviorSubject
import rx.lang.kotlin.PublishSubject

class PremiumPresenterTest {
    private val appUser = anAppUser()

    val appUserSubject = BehaviorSubject(appUser)
    val refreshSubject = PublishSubject<Unit>()
    val billingProductSubject = PublishSubject<BillingProduct>()
    var purchaseSubject = PublishSubject<Unit>()

    val billingProductsService = mock<BillingProductsService>()
    val appUserService = mock<DataSource<AppUser>>()
    val appUserWriter = mock<DataWriter<AppUser>>()
    val view = mock<PremiumPresenter.View>()
    val presenter = PremiumPresenter(appUserService, appUserWriter, billingProductsService, rxSchedulers())
    val inOrder = inOrder(view)

    @Before
    fun setUp() {
        whenever(appUserService.data()).thenReturn(appUserSubject)
        whenever(view.refreshes()).thenReturn(refreshSubject)
        whenever(view.billingProductSelects()).thenReturn(billingProductSubject)
        whenever(view.displayBuyProcess(any())).thenReturn(purchaseSubject)
        whenever(billingProductsService.billingProducts()).thenReturn(just(emptyList()))
    }

    @Test
    fun `shows free user when user is free`() {
        setSubscriptionType(FREE)

        presenter.attach(view)

        verify(view).showFreeUser()
    }

    @Test
    fun `shows premium user when user is premium`() {
        setSubscriptionType(PREMIUM_PAID)

        presenter.attach(view)

        verify(view).showPremiumUser()
    }

    @Test
    fun `shows premium billing products when user is using free version and does not own premium products`() {
        setSubscriptionType(FREE)
        val billingProducts = listOf(
                aBillingProduct().asPremium().notOwned(),
                aBillingProduct().asDonation(),
                aBillingProduct().asPremium().notOwned())
        whenever(billingProductsService.billingProducts()).thenReturn(just(billingProducts))

        presenter.attach(view)

        inOrder.verify(view).showLoading()
        inOrder.verify(view).hideLoading()
        inOrder.verify(view).hideEmptyView()
        inOrder.verify(view).showBillingProducts(billingProducts.filter { it.subscriptionType == FREE })
    }

    @Test
    fun `converts user to premium paid if she was a free user but has owned premium billing products`() {
        setSubscriptionType(FREE)
        val billingProducts = listOf(
                aBillingProduct().asPremium().owned(),
                aBillingProduct().asDonation(),
                aBillingProduct().asPremium().notOwned())
        whenever(billingProductsService.billingProducts()).thenReturn(just(billingProducts))

        presenter.attach(view)

        inOrder.verify(view).showLoading()
        inOrder.verify(view).hideLoading()
        inOrder.verify(view).hideEmptyView()
        inOrder.verify(view).showBillingProducts(billingProducts.filter { it.subscriptionType == PREMIUM_PAID })
        verify(appUserWriter).write(appUser.copy(settings = appUser.settings.copy(subscriptionType = PREMIUM_PAID)))
    }

    @Test
    fun `shows donation billing products when user is using premium paid version`() {
        setSubscriptionType(PREMIUM_PAID)
        val billingProducts = listOf(
                aBillingProduct().asPremium().owned(),
                aBillingProduct().asDonation(),
                aBillingProduct().asPremium().notOwned())
        whenever(billingProductsService.billingProducts()).thenReturn(just(billingProducts))

        presenter.attach(view)

        inOrder.verify(view).showLoading()
        inOrder.verify(view).hideLoading()
        inOrder.verify(view).hideEmptyView()
        inOrder.verify(view).showBillingProducts(billingProducts.filter { it.subscriptionType == PREMIUM_PAID })
        verify(appUserWriter, never()).write(any())
    }

    @Test
    fun `donations are loaded after successful premium purchase`() {
        setSubscriptionType(FREE)
        val billingProducts = listOf(
                aBillingProduct().asPremium().notOwned(),
                aBillingProduct().asDonation(),
                aBillingProduct().asPremium().notOwned())
        whenever(billingProductsService.billingProducts()).thenReturn(just(billingProducts))
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
    fun `shows empty view when there are no purchases`() {
        setSubscriptionType(FREE)
        whenever(billingProductsService.billingProducts()).thenReturn(just(emptyList()))

        presenter.attach(view)

        inOrder.verify(view).showLoading()
        inOrder.verify(view).hideLoading()
        inOrder.verify(view).showEmptyView()
        inOrder.verify(view).showBillingProducts(emptyList())
    }

    @Test
    fun `shows updated purchases after refresh`() {
        setSubscriptionType(FREE)
        val billingProducts = listOf(aBillingProduct().asPremium().notOwned(), aBillingProduct().asPremium().notOwned())
        val updatedBillingProducts = listOf(aBillingProduct().asPremium().notOwned())
        whenever(billingProductsService.billingProducts()).thenReturn(just(billingProducts), just(updatedBillingProducts))
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
    fun `billing products provider is closed on destroy`() {
        presenter.onDestroy()

        verify(billingProductsService).close()
    }

    @Test
    fun `successful premium purchase switches to premium paid`() {
        setSubscriptionType(FREE)
        val billingProducts = listOf(
                aBillingProduct().asPremium().notOwned(),
                aBillingProduct().asDonation())
        whenever(billingProductsService.billingProducts()).thenReturn(just(billingProducts))
        presenter.attach(view)

        selectBillingProduct(billingProducts.first())
        makeSuccessfulPurchase()

        verify(view).displayBuyProcess(billingProducts.first().id)
        verify(appUserWriter).write(appUser.copy(settings = appUser.settings.copy(subscriptionType = PREMIUM_PAID)))
    }

    @Test
    fun `failed premium purchase does not switch to premium paid`() {
        setSubscriptionType(FREE)
        val billingProducts = listOf(
                aBillingProduct().asPremium().notOwned(),
                aBillingProduct().asDonation())
        whenever(billingProductsService.billingProducts()).thenReturn(just(billingProducts))
        presenter.attach(view)

        selectBillingProduct(billingProducts.first())
        makeFailedPurchase()

        verify(view).displayBuyProcess(billingProducts.first().id)
        verify(appUserWriter, never()).write(any())
    }

    @Test
    fun `can make another purchase after aFailed one`() {
        setSubscriptionType(FREE)
        val billingProducts = listOf(
                aBillingProduct().asPremium().notOwned(),
                aBillingProduct().asDonation())
        whenever(billingProductsService.billingProducts()).thenReturn(just(billingProducts))
        presenter.attach(view)
        selectBillingProduct(billingProducts.first())
        makeFailedPurchase()

        selectBillingProduct(billingProducts.first())
        makeSuccessfulPurchase()

        verify(appUserWriter).write(appUser.copy(settings = appUser.settings.copy(subscriptionType = PREMIUM_PAID)))
    }

    private fun setSubscriptionType(subscriptionType: SubscriptionType) =
            appUserSubject.onNext(appUser.copy(settings = appUser.settings.copy(subscriptionType = subscriptionType)))

    private fun selectBillingProduct(billingProduct: BillingProduct) = billingProductSubject.onNext(billingProduct)
    private fun refresh() = refreshSubject.onNext(Unit)
    private fun makeSuccessfulPurchase() = purchaseSubject.onNext(Unit)
    private fun makeFailedPurchase() = purchaseSubject.onError(Throwable()).run {
        purchaseSubject = PublishSubject()
        whenever(view.displayBuyProcess(any())).thenReturn(purchaseSubject)
    }
}