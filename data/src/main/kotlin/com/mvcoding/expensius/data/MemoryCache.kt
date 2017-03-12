package com.mvcoding.expensius.data

import rx.Observable
import rx.lang.kotlin.BehaviorSubject

class MemoryCache<DATA>(dataSource: DataSource<DATA>) : DataSource<DATA>, DataWriter<DATA> {

    private val dataSource = MemoryDataSource(dataSource)
    private val subject = BehaviorSubject<DATA>()

    override fun data(): Observable<DATA> = dataSource.data()
            .takeUntil(subject)
            .mergeWith(subject)

    override fun write(data: DATA) {
        subject.onNext(data)
    }
}