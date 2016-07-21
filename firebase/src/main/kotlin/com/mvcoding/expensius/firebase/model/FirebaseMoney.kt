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

package com.mvcoding.expensius.firebase.model

import com.mvcoding.expensius.firebase.defaultCurrency
import com.mvcoding.expensius.model.Currency
import com.mvcoding.expensius.model.Money
import java.math.BigDecimal
import java.math.BigDecimal.ONE
import java.math.BigDecimal.ZERO

data class FirebaseMoney(
        val amount: String? = null,
        val currency: String? = null,
        val exchangeRate: String? = null) {

    fun toMoney() = Money(
            amount?.let { BigDecimal(it) } ?: ZERO,
            currency?.let { Currency(it) } ?: defaultCurrency(),
            exchangeRate?.let { BigDecimal(it) } ?: ONE)
}