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

import com.mvcoding.expensius.data.ParameterDataSource
import com.mvcoding.expensius.model.Money
import com.mvcoding.expensius.model.MoneyConversion
import io.reactivex.Observable

class MoneyConversionSource : ParameterDataSource<MoneyConversion, Money> {
    override fun data(parameter: MoneyConversion): Observable<Money> = Observable.just(parameter.money)
}