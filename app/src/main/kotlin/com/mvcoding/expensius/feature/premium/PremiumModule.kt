/*
 * Copyright (C) 2017 Mantas Varnagiris.
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
import com.memoizrlabs.ShankModule
import com.memoizrlabs.shankkotlin.provideSingletonFor
import com.memoizrlabs.shankkotlin.registerFactory
import com.mvcoding.expensius.BuildConfig
import com.mvcoding.expensius.extension.provideSingletonFor
import com.mvcoding.expensius.feature.BaseActivity
import com.mvcoding.expensius.provideAppUserSource
import com.mvcoding.expensius.provideContext
import com.mvcoding.expensius.provideRxSchedulers
import memoizrlabs.com.shankandroid.withThisScope

class PremiumModule : ShankModule {
    override fun registerFactories() {
        billing()
        premiumPresenter()
    }

    private fun billing() = registerFactory(Billing::class) { ->
        if (BuildConfig.DEBUG) DummyBillingProductsService(provideAppUserSource())
        else RealBillingProductsService(provideContext())
    }

    private fun premiumPresenter() = registerFactory(PremiumPresenter::class) { scope: Scope ->
        PremiumPresenter(provideAppUserSource(), provideAppUserSource(), provideBilling(scope), provideRxSchedulers())
    }
}

private fun provideBilling(scope: Scope) = scope.provideSingletonFor<Billing>()
fun BaseActivity.provideBillingFlow(): BillingFlow = provideBilling(scope)
fun BaseActivity.providePremiumPresenter(): PremiumPresenter = withThisScope.provideSingletonFor(scope)