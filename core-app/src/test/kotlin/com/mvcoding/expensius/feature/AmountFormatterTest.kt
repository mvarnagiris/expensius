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

package com.mvcoding.expensius.feature

import com.mvcoding.expensius.feature.currency.CurrencyFormatsProvider
import com.mvcoding.expensius.model.Currency
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test
import rx.Observable.empty
import java.math.BigDecimal

class AmountFormatterTest {
    val value = BigDecimal("1234.567")
    val currencyFormatsProvider = mock<CurrencyFormatsProvider>()
    val amountFormatter = AmountFormatter(currencyFormatsProvider)

    @Test
    fun usesDefaultCurrencyFormatWhenOneIsNotProvided() {
        whenever(currencyFormatsProvider.currencyFormats()).thenReturn(empty())

        val result = amountFormatter.format(value, Currency("UNKNOWN"))

        assertThat(result, equalTo("UNKNOWN 1,234.57"))
    }
}