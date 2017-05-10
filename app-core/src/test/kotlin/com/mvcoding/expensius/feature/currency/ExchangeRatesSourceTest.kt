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

package com.mvcoding.expensius.feature.currency

import com.mvcoding.expensius.data.ParameterDataSource
import com.mvcoding.expensius.data.testMemoryParameterDataSource
import com.mvcoding.expensius.model.ExchangeRateCurrencies
import com.mvcoding.expensius.model.extensions.aCurrency
import com.mvcoding.expensius.model.extensions.anAmount
import com.mvcoding.expensius.model.extensions.anExchangeRateCurrencies
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import org.junit.Test
import rx.Observable
import rx.observers.TestSubscriber
import java.math.BigDecimal

class ExchangeRatesSourceTest {

    @Test
    fun `behaves like memory parameter data source`() {
        testMemoryParameterDataSource(
                anExchangeRateCurrencies(),
                anExchangeRateCurrencies(),
                anAmount(),
                anAmount()) { dataSource: ParameterDataSource<ExchangeRateCurrencies, BigDecimal> -> ExchangeRatesSource { dataSource.data(it) } }
    }

    @Test
    fun `returns ONE when currencies are same`() {
        val currency = aCurrency()
        val getExchangeRate = mock<(ExchangeRateCurrencies) -> Observable<BigDecimal>>()
        val exchangeRatesSource = ExchangeRatesSource(getExchangeRate)
        val subscriber = TestSubscriber<BigDecimal>()

        exchangeRatesSource.data(ExchangeRateCurrencies(currency, currency)).subscribe(subscriber)

        subscriber.assertValue(BigDecimal.ONE)
        verify(getExchangeRate, never()).invoke(any())
    }
}