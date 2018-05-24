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

import com.memoizrlabs.ShankModule

class PremiumModule : ShankModule {
    override fun registerFactories() {
//        billing()
//        premiumPresenter()
    }

//    private fun billing() = registerFactory(Billing::class) { ->
//        if (BuildConfig.DEBUG) DummyBillingProductsService(provideAppUserSource())
//        else RealBillingProductsService(provideContext())
//    }

//    private fun premiumPresenter() = registerFactory(PremiumPresenter::class) { scope: Scope ->
//        PremiumPresenter(provideAppUserSource(), provideAppUserSource(), provideBilling(scope), provideRxSchedulers())
//    }
}

//private fun provideBilling(scope: Scope) = scope.provideSingletonFor<Billing>()
//fun BaseActivity.provideBillingFlow(): BillingFlow = provideBilling(scope)
//fun BaseActivity.providePremiumPresenter(): PremiumPresenter = withThisScope.provideSingletonFor(scope)