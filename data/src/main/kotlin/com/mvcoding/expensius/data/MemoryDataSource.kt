package com.mvcoding.expensius.data

import rx.Observable

class MemoryDataSource<DATA>(private val dataSource: DataSource<DATA>) : DataSource<DATA> {

    private var dataObservable: Observable<DATA>? = null

    override fun data(): Observable<DATA> {
        return dataObservable ?: dataSource.data()
                .doOnError { dataObservable = null }
                .replay(1)
                .autoConnect()
                .apply { dataObservable = this }
    }
}