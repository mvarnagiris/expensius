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

package com.mvcoding.expensius.feature.currency

import com.memoizrlabs.ShankModule
import com.memoizrlabs.shankkotlin.provideGlobalSingleton
import com.memoizrlabs.shankkotlin.provideNew
import com.memoizrlabs.shankkotlin.registerFactory

class CurrenciesModule : ShankModule {
    override fun registerFactories() {
        currenciesProvider()
        currencyFormatsProvider()
        exchangeRatesProvider()
    }

    private fun currenciesProvider() = registerFactory(CurrenciesProvider::class) { -> CurrenciesProvider() }
    private fun currencyFormatsProvider() = registerFactory(CurrencyFormatsProvider::class) { -> SystemCurrencyFormatsProvider() }
    private fun exchangeRatesProvider() = registerFactory(ExchangeRatesProvider::class) { -> StupidExchangeRatesProvider() }
}

fun provideCurrenciesProvider(): CurrenciesProvider = provideNew()
fun provideCurrencyFormatsProvider(): CurrencyFormatsProvider = provideGlobalSingleton()
fun provideExchangeRatesProvider(): ExchangeRatesProvider = provideGlobalSingleton()