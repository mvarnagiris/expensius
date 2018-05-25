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

package com.mvcoding.expensius.feature.currency

import com.mvcoding.expensius.extensions.toSystemCurrency
import com.mvcoding.expensius.model.Currency
import com.mvcoding.expensius.model.CurrencyFormat
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

class SystemCurrencyFormatsProvider : CurrencyFormatsProvider {
    override fun getCurrencyFormat(currency: Currency): CurrencyFormat = currency.toSystemCurrency()?.toCurrencyFormat() ?: currency.getDefaultCurrencyFormat()

    private fun Currency.getDefaultCurrencyFormat(): CurrencyFormat {
        return NumberFormat.getCurrencyInstance(Locale.getDefault())
                .let {
                    CurrencyFormat(
                            code,
                            it.symbolPosition(),
                            symbolDistance(),
                            it.decimalSeparator(),
                            it.groupSeparator(),
                            it.minFractionDigits(),
                            it.maxFractionDigits())
                }
    }

    private fun java.util.Currency.toCurrencyFormat(): CurrencyFormat {
        return NumberFormat.getCurrencyInstance(Locale.getDefault())
                .apply { currency = this@toCurrencyFormat }
                .let {
                    CurrencyFormat(
                            symbol,
                            it.symbolPosition(),
                            symbolDistance(),
                            it.decimalSeparator(),
                            it.groupSeparator(),
                            it.minFractionDigits(),
                            it.maxFractionDigits())
                }
    }

    private fun NumberFormat.symbolPosition(): CurrencyFormat.SymbolPosition = if (this is DecimalFormat) {
        toLocalizedPattern().indexOf('\u00A4').let { if (it == 0) CurrencyFormat.SymbolPosition.START else CurrencyFormat.SymbolPosition.END }
    } else {
        CurrencyFormat.SymbolPosition.START
    }

    private fun NumberFormat.decimalSeparator(): CurrencyFormat.DecimalSeparator = if (this is DecimalFormat) {
        decimalFormatSymbols.decimalSeparator.let {
            when (it) {
                ',' -> CurrencyFormat.DecimalSeparator.COMMA
                ' ' -> CurrencyFormat.DecimalSeparator.SPACE
                else -> CurrencyFormat.DecimalSeparator.DOT
            }
        }
    } else {
        CurrencyFormat.DecimalSeparator.DOT
    }

    private fun NumberFormat.groupSeparator(): CurrencyFormat.GroupSeparator = if (this is DecimalFormat) {
        decimalFormatSymbols.groupingSeparator.let {
            when (it) {
                '.' -> CurrencyFormat.GroupSeparator.DOT
                ',' -> CurrencyFormat.GroupSeparator.COMMA
                ' ' -> CurrencyFormat.GroupSeparator.SPACE
                else -> CurrencyFormat.GroupSeparator.NONE
            }
        }
    } else {
        CurrencyFormat.GroupSeparator.NONE
    }

    private fun symbolDistance() = CurrencyFormat.SymbolDistance.FAR
    private fun NumberFormat.minFractionDigits() = minimumFractionDigits
    private fun NumberFormat.maxFractionDigits() = maximumFractionDigits
}