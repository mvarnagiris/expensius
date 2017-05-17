package com.mvcoding.expensius.data

import rx.Observable

class FunctionDataSource<DATA>(private val getData: () -> Observable<DATA>) : DataSource<DATA> {
    override fun data(): Observable<DATA> = getData()
}