package com.mvcoding.expensius.data

import rx.Observable

interface ParameterDataSource<in PARAMETER, DATA> {
    fun data(parameter: PARAMETER): Observable<DATA>
}