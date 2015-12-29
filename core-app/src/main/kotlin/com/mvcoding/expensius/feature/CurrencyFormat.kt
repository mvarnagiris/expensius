/*
 * Copyright (C) 2015 Mantas Varnagiris.
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

package com.mvcoding.expensius.feature

import java.math.BigDecimal

data class CurrencyFormat(
        private val symbol: String,
        private val symbolPosition: SymbolPosition,
        private val decimalSeparator: DecimalSeparator,
        private val groupSeparator: GroupSeparator,
        private val decimalCount: Int) {

    fun format(amount: BigDecimal) = amount.toPlainString()

    enum class SymbolPosition {
        CLOSE_END, FAR_END, CLOSE_START, FAR_START
    }

    enum class DecimalSeparator(val separator: Char) {
        DOT('.'), COMMA(','), SPACE(' ')
    }

    enum class GroupSeparator(val separator: Char) {
        NONE('\u0000'), DOT('.'), COMMA(','), SPACE(' ')
    }
}