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

package com.mvcoding.expensius.model

import com.mvcoding.expensius.model.CurrencyFormat.DecimalSeparator
import com.mvcoding.expensius.model.CurrencyFormat.GroupSeparator
import com.mvcoding.expensius.model.CurrencyFormat.SymbolDistance
import com.mvcoding.expensius.model.CurrencyFormat.SymbolPosition
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test
import java.math.BigDecimal

class CurrencyFormatTest {
    val currencyFormat = CurrencyFormat("$", SymbolPosition.START, SymbolDistance.FAR, DecimalSeparator.DOT, GroupSeparator.COMMA, 2, 2)
    val value = BigDecimal("1234.567")

    @Test
    fun symbolIsAtTheRightPosition() {
        assertThat(currencyFormat.copy(
                symbol = "$",
                symbolPosition = SymbolPosition.START,
                symbolDistance = SymbolDistance.FAR).format(value), equalTo("$ 1,234.57"))

        assertThat(currencyFormat.copy(
                symbol = "USD",
                symbolPosition = SymbolPosition.START,
                symbolDistance = SymbolDistance.CLOSE).format(value), equalTo("USD1,234.57"))

        assertThat(currencyFormat.copy(
                symbol = "AnyThing",
                symbolPosition = SymbolPosition.END,
                symbolDistance = SymbolDistance.FAR).format(value), equalTo("1,234.57 AnyThing"))

        assertThat(currencyFormat.copy(
                symbol = "$",
                symbolPosition = SymbolPosition.END,
                symbolDistance = SymbolDistance.CLOSE).format(value), equalTo("1,234.57$"))
    }

    @Test
    fun usesCorrectDecimalSeparator() {
        assertThat(currencyFormat.copy(decimalSeparator = DecimalSeparator.DOT).format(value), equalTo("$ 1,234.57"))
        assertThat(currencyFormat.copy(decimalSeparator = DecimalSeparator.COMMA).format(value), equalTo("$ 1,234,57"))
        assertThat(currencyFormat.copy(decimalSeparator = DecimalSeparator.SPACE).format(value), equalTo("$ 1,234 57"))
    }

    @Test
    fun usesCorrectGroupSeparator() {
        assertThat(currencyFormat.copy(groupSeparator = GroupSeparator.COMMA).format(value), equalTo("$ 1,234.57"))
        assertThat(currencyFormat.copy(groupSeparator = GroupSeparator.DOT).format(value), equalTo("$ 1.234.57"))
        assertThat(currencyFormat.copy(groupSeparator = GroupSeparator.SPACE).format(value), equalTo("$ 1 234.57"))
        assertThat(currencyFormat.copy(groupSeparator = GroupSeparator.NONE).format(value), equalTo("$ 1\u0000234.57"))
    }

    @Test
    fun usesCorrectMinFractionDigits() {
        val value = BigDecimal("1234")

        assertThat(currencyFormat.copy(minFractionDigits = 0).format(value), equalTo("$ 1,234"))
        assertThat(currencyFormat.copy(minFractionDigits = 1).format(value), equalTo("$ 1,234.0"))
        assertThat(currencyFormat.copy(minFractionDigits = 2).format(value), equalTo("$ 1,234.00"))
        assertThat(currencyFormat.copy(minFractionDigits = 3, maxFractionDigits = 3).format(value), equalTo("$ 1,234.000"))
    }

    @Test
    fun usesCorrectMaxFractionDigits() {
        val value = BigDecimal("1234.5678")

        assertThat(currencyFormat.copy(maxFractionDigits = 0).format(value), equalTo("$ 1,235"))
        assertThat(currencyFormat.copy(maxFractionDigits = 1).format(value), equalTo("$ 1,234.6"))
        assertThat(currencyFormat.copy(maxFractionDigits = 2).format(value), equalTo("$ 1,234.57"))
        assertThat(currencyFormat.copy(maxFractionDigits = 3).format(value), equalTo("$ 1,234.568"))
    }
}