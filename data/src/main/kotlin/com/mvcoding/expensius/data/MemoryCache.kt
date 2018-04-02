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

package com.mvcoding.expensius.data

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class MemoryCache<DATA>(dataSource: DataSource<DATA>) : Cache<DATA> {

    private val subject = BehaviorSubject.create<DATA>()
    private val dataSource = MemoryDataSource(FunctionDataSource { dataSource.data().mergeWith(subject) })

    override fun write(data: DATA): Unit = subject.onNext(data)
    override fun data(): Observable<DATA> = dataSource.data()
}