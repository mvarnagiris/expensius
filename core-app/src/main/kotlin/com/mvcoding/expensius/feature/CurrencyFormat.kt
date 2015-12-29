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

import com.mvcoding.expensius.feature.CurrencyFormat.SymbolDistance.FAR
import com.mvcoding.expensius.feature.CurrencyFormat.SymbolPosition.END
import com.mvcoding.expensius.feature.CurrencyFormat.SymbolPosition.START
import java.math.BigDecimal
import java.text.DecimalFormat

data class CurrencyFormat(
        private val symbol: String,
        private val symbolPosition: SymbolPosition,
        private val symbolDistance: SymbolDistance,
        private val decimalSeparator: DecimalSeparator,
        private val groupSeparator: GroupSeparator,
        private val minFractionDigits: Int,
        private val maxFractionDigits: Int) {
    private val decimalFormat: DecimalFormat;

    init {
        decimalFormat = DecimalFormat.getInstance() as DecimalFormat

        val decimalFormatSymbols = decimalFormat.decimalFormatSymbols;
        decimalFormatSymbols.groupingSeparator = groupSeparator.symbol
        decimalFormatSymbols.decimalSeparator = decimalSeparator.symbol

        decimalFormat.minimumFractionDigits = minFractionDigits
        decimalFormat.maximumFractionDigits = maxFractionDigits
        decimalFormat.positivePrefix = if (symbolPosition == START) symbol + (if (symbolDistance == FAR) " " else "") else ""
        decimalFormat.negativePrefix = if (symbolPosition == START) symbol + (if (symbolDistance == FAR) " -" else "-") else ""
        decimalFormat.positiveSuffix = if (symbolPosition == END) (if (symbolDistance == FAR) " " else "") + symbol else ""
        decimalFormat.negativeSuffix = if (symbolPosition == END) (if (symbolDistance == FAR) " " else "") + symbol else ""
    }

    fun format(amount: BigDecimal): String {
        return decimalFormat.format(amount);
    }

    enum class SymbolPosition {
        START, END
    }

    enum class SymbolDistance {
        CLOSE, FAR
    }

    enum class DecimalSeparator(val symbol: Char) {
        DOT('.'), COMMA(','), SPACE(' ')
    }

    enum class GroupSeparator(val symbol: Char) {
        NONE('\u0000'), DOT('.'), COMMA(','), SPACE(' ')
    }
}