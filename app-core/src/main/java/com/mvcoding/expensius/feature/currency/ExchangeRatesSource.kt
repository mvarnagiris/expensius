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

import com.mvcoding.expensius.data.FunctionParameterDataSource
import com.mvcoding.expensius.data.MemoryParameterDataSource
import com.mvcoding.expensius.data.ParameterDataSource
import com.mvcoding.expensius.model.ExchangeRateCurrencies
import io.reactivex.Observable
import io.reactivex.Observable.just
import java.math.BigDecimal

class ExchangeRatesSource(getExchangeRate: (ExchangeRateCurrencies) -> Observable<BigDecimal>) : ParameterDataSource<ExchangeRateCurrencies, BigDecimal> {

    private val dataSource = MemoryParameterDataSource(FunctionParameterDataSource(getExchangeRate))

    override fun data(parameter: ExchangeRateCurrencies): Observable<BigDecimal> =
            if (parameter.hasSameCurrencies()) just(BigDecimal.ONE)
            else dataSource.data(parameter)
}