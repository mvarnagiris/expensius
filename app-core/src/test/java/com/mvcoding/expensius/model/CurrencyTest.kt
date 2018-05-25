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

package com.mvcoding.expensius.model

import com.mvcoding.expensius.extensions.toSystemCurrency
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.nullValue
import org.junit.Assert.assertThat
import org.junit.Test
import java.util.Currency.getInstance

class CurrencyTest {
    @Test
    fun convertsToSystemCurrency() {
        assertThat(Currency("GBP").toSystemCurrency(), equalTo(getInstance("GBP")))
    }

    @Test
    fun returnsNullWhenSystemCurrencyDoesNotExist() {
        assertThat(Currency("Does not exist").toSystemCurrency(), nullValue(java.util.Currency::class.java))
    }
}