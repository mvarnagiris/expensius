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

import android.view.View
import com.memoizrlabs.Scope
import com.memoizrlabs.Shank.registerFactory
import com.memoizrlabs.ShankModule
import com.memoizrlabs.shankkotlin.provideSingletonFor
import com.memoizrlabs.shankkotlin.registerFactory
import com.mvcoding.expensius.BuildConfig
import com.mvcoding.expensius.extension.provideSingleton
import com.mvcoding.expensius.extension.scope
import com.mvcoding.expensius.provideContext
import com.mvcoding.expensius.provideSettings
import memoizrlabs.com.shankandroid.withActivityScope

class PremiumModule : ShankModule {
    override fun registerFactories() {
        remoteBillingProductsService()
        billingFlow()
        premiumPresenter()
    }

    private fun remoteBillingProductsService() = registerFactory(RemoteBillingProductsService::class) { ->
        if (BuildConfig.DEBUG) DummyRemoteBillingProductsService(provideSettings())
        else BillingRemoteBillingProductsService(provideContext())
    }

    private fun billingFlow() = registerFactory(BillingFlow::class) { scope: Scope ->
        provideRemoteBillingProductsService(scope) as BillingFlow
    }

    private fun premiumPresenter() = registerFactory(PremiumPresenter::class.java) { scope: Scope ->
        val remoteBillingProductsService = provideRemoteBillingProductsService(scope)
        val billingProductsProvider = BillingProductsProvider(remoteBillingProductsService)
        PremiumPresenter(provideSettings(), billingProductsProvider)
    }
}

private fun provideRemoteBillingProductsService(scope: Scope): RemoteBillingProductsService = scope.provideSingleton()
fun View.provideBillingFlow(): BillingFlow = withActivityScope.provideSingletonFor(scope())
fun View.providePremiumPresenter(): PremiumPresenter = withActivityScope.provideSingletonFor(scope())