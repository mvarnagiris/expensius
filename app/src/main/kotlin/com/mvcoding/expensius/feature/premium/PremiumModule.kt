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

import com.memoizrlabs.Scope
import com.memoizrlabs.Shank.registerFactory
import com.memoizrlabs.ShankModule
import com.memoizrlabs.shankkotlin.provideSingletonFor
import com.memoizrlabs.shankkotlin.registerFactory
import com.mvcoding.expensius.BuildConfig
import com.mvcoding.expensius.extension.provideSingleton
import com.mvcoding.expensius.feature.BaseActivity
import com.mvcoding.expensius.provideAppUserService
import com.mvcoding.expensius.provideAppUserWriteService
import com.mvcoding.expensius.provideContext
import com.mvcoding.expensius.provideRxSchedulers
import memoizrlabs.com.shankandroid.withThisScope

class PremiumModule : ShankModule {
    override fun registerFactories() {
        remoteBillingProductsService()
        billingFlow()
        premiumPresenter()
    }

    private fun remoteBillingProductsService() = registerFactory(BillingProductsService::class) { ->
        if (BuildConfig.DEBUG) DummyBillingProductsService(provideAppUserService())
        else BillingBillingProductsService(provideContext())
    }

    private fun billingFlow() = registerFactory(BillingFlow::class) { scope: Scope ->
        provideBillingProductsService(scope) as BillingFlow
    }

    private fun premiumPresenter() = registerFactory(PremiumPresenter::class.java) { scope: Scope ->
        val billingProductsService = provideBillingProductsService(scope)
        PremiumPresenter(provideAppUserService(), provideAppUserWriteService(), billingProductsService, provideRxSchedulers())
    }
}

private fun provideBillingProductsService(scope: Scope): BillingProductsService = scope.provideSingleton()
fun BaseActivity.provideBillingFlow(): BillingFlow = withThisScope.provideSingletonFor(scope)
fun BaseActivity.providePremiumPresenter(): PremiumPresenter = withThisScope.provideSingletonFor(scope)