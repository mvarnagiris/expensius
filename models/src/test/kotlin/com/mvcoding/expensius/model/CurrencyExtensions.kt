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

import com.mvcoding.expensius.aRandomItem

fun aStringCurrencyCode() = arrayOf("GBP", "EUR", "USD", "CAD", "CHF", "RUB").aRandomItem()
fun aCurrency() = Currency(aStringCurrencyCode())
fun Currency.withCode(code: String) = copy(code = code)
fun anExchangeRateCurrencies() = ExchangeRateCurrencies(aCurrency(), aCurrency())
fun ExchangeRateCurrencies.withFromCurrency(fromCurrency: Currency) = copy(fromCurrency = fromCurrency)
fun ExchangeRateCurrencies.withToCurrency(toCurrency: Currency) = copy(toCurrency = toCurrency)