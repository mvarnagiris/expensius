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

package com.mvcoding.expensius.feature.reports

import com.mvcoding.expensius.feature.currency.ExchangeRatesProvider
import com.mvcoding.expensius.model.Currency
import com.mvcoding.expensius.model.Money
import java.math.BigDecimal
import java.math.BigDecimal.ZERO

fun List<Money>.sumMoney(currency: Currency, exchangeRatesProvider: ExchangeRatesProvider): Money =
        fold(Money(ZERO, currency)) { totalMoney, money -> totalMoney.plus(money, exchangeRatesProvider) }

fun Money.plus(other: Money, exchangeRatesProvider: ExchangeRatesProvider) =
        Money(amount + other.amount.multiply(exchangeRatesProvider.getExchangeRate(currency, other.currency)), currency)

fun Money.multiply(multiplyBy: Float) = Money(amount.multiply(BigDecimal.valueOf(multiplyBy.toDouble())), currency)