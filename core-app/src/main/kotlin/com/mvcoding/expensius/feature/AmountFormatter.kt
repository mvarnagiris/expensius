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

import com.mvcoding.expensius.feature.currency.CurrencyFormatsProvider
import com.mvcoding.expensius.model.Currency
import com.mvcoding.expensius.model.CurrencyFormat
import com.mvcoding.expensius.model.CurrencyFormat.*
import com.mvcoding.expensius.model.CurrencyFormat.SymbolPosition.END
import com.mvcoding.expensius.model.CurrencyFormat.SymbolPosition.START
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

class AmountFormatter(currencyFormatsProvider: CurrencyFormatsProvider) {

    fun format(amount: BigDecimal, currency: Currency): String {
        val currencyFormat = currency.toSystemCurrency()?.toCurrencyFormat() ?: currency.getDefaultCurrencyFormat()
        return currencyFormat.format(amount)
    }

    private fun Currency.getDefaultCurrencyFormat(): CurrencyFormat {
        return NumberFormat.getCurrencyInstance(Locale.getDefault())
                .let {
                    CurrencyFormat(
                            code,
                            it.symbolPosition(),
                            it.symbolDistance(),
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
                            it.symbolDistance(),
                            it.decimalSeparator(),
                            it.groupSeparator(),
                            it.minFractionDigits(),
                            it.maxFractionDigits())
                }
    }

    private fun NumberFormat.symbolPosition(): CurrencyFormat.SymbolPosition {
        return if (this is DecimalFormat) {
            toLocalizedPattern().indexOf('\u00A4').let { if (it == 0) START else END }
        } else {
            START
        }
    }

    private fun NumberFormat.decimalSeparator(): DecimalSeparator {
        return if (this is DecimalFormat) {
            decimalFormatSymbols.decimalSeparator.let {
                when (it) {
                    ',' -> DecimalSeparator.COMMA
                    ' ' -> DecimalSeparator.SPACE
                    else -> DecimalSeparator.DOT
                }
            }
        } else {
            DecimalSeparator.DOT
        }
    }

    private fun NumberFormat.groupSeparator(): GroupSeparator {
        return if (this is DecimalFormat) {
            decimalFormatSymbols.groupingSeparator.let {
                when (it) {
                    '.' -> GroupSeparator.DOT
                    ',' -> GroupSeparator.COMMA
                    ' ' -> GroupSeparator.SPACE
                    else -> GroupSeparator.NONE
                }
            }
        } else {
            GroupSeparator.NONE
        }
    }

    private fun NumberFormat.symbolDistance() = SymbolDistance.FAR
    private fun NumberFormat.minFractionDigits() = minimumFractionDigits
    private fun NumberFormat.maxFractionDigits() = maximumFractionDigits
}