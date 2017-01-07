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

package com.mvcoding.expensius

import rx.Observable
import rx.subjects.PublishSubject
import rx.subjects.SerializedSubject
import kotlin.reflect.KClass

class RxBus {
    private val bus = SerializedSubject(PublishSubject.create<Any>())

    fun send(o: Any) = bus.onNext(o)
    fun <T : Any> observe(cls: KClass<T>): Observable<T> = bus.ofType(cls.java)
}

inline fun <reified T : Any> RxBus.observe(): Observable<T> = observe(T::class)