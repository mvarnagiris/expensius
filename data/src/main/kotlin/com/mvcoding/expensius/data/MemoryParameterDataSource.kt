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

package com.mvcoding.expensius.data

import rx.Observable
import rx.lang.kotlin.onError
import java.util.concurrent.ConcurrentHashMap

class MemoryParameterDataSource<in PARAMETER, DATA>(private val dataSource: ParameterDataSource<PARAMETER, DATA>) : ParameterDataSource<PARAMETER, DATA> {

    private val observablesMap = ConcurrentHashMap<PARAMETER, Observable<DATA>>()

    override fun data(parameter: PARAMETER): Observable<DATA> {
        var observable = observablesMap[parameter]
        if (observable == null) {
            observable = dataSource.data(parameter)
                    .onError { observablesMap.remove(parameter) }
                    .replay(1)
                    .autoConnect()
            observablesMap.put(parameter, observable)
        }

        return observable!!
    }
}