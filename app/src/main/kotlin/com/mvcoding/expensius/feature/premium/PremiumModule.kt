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

import android.content.Context
import com.memoizrlabs.Scope
import com.memoizrlabs.Shank.registerFactory
import com.memoizrlabs.ShankModule
import com.mvcoding.expensius.BuildConfig
import com.mvcoding.expensius.extension.provideActivityScopedSingleton
import com.mvcoding.expensius.extension.provideScopedSingleton
import com.mvcoding.expensius.extension.toBaseActivity
import com.mvcoding.expensius.provideContext
import com.mvcoding.expensius.provideSettings

class PremiumModule : ShankModule {
    override fun registerFactories() {
        remoteBillingProductsService()
        billingFlow()
        premiumPresenter()
    }

    private fun remoteBillingProductsService() {
        registerFactory(RemoteBillingProductsService::class.java) { ->
            if (BuildConfig.DEBUG) DummyRemoteBillingProductsService(provideSettings())
            else BillingRemoteBillingProductsService(provideContext())
        }
    }

    private fun billingFlow() = registerFactory(BillingFlow::class.java) { scope: Scope ->
        provideRemoteBillingProductsService(scope) as BillingFlow
    }

    private fun premiumPresenter() = registerFactory(PremiumPresenter::class.java) { scope: Scope ->
        val remoteBillingProductsService = provideRemoteBillingProductsService(scope)
        val billingProductsProvider = BillingProductsProvider(remoteBillingProductsService)
        PremiumPresenter(provideSettings(), billingProductsProvider)
    }
}

fun provideRemoteBillingProductsService(scope: Scope) = provideScopedSingleton(RemoteBillingProductsService::class, scope)
fun provideBillingFlow(scope: Scope) = provideScopedSingleton(BillingFlow::class, scope, scope)
fun providePremiumPresenter(context: Context) =
        provideActivityScopedSingleton(PremiumPresenter::class, context, context.toBaseActivity().scope)