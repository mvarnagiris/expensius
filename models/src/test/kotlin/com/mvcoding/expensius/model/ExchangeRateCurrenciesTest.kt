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

package com.mvcoding.expensius.model

import com.memoizr.assertk.expect
import org.junit.Test

class ExchangeRateCurrenciesTest {

    @Test
    fun `has same currencies returns true when currencies are equal`() {
        val currency = aCurrency()
        expect that anExchangeRateCurrencies().withFromCurrency(currency).withToCurrency(currency).hasSameCurrencies() _is true
    }

    @Test
    fun `has same currencies returns false when currencies are not equal`() {
        val fromCurrency = aCurrency().withCode("AAA")
        val toCurrency = aCurrency().withCode("BBB")
        expect that anExchangeRateCurrencies().withFromCurrency(fromCurrency).withToCurrency(toCurrency).hasSameCurrencies() _is false
    }
}