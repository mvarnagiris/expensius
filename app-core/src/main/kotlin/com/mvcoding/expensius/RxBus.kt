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

package com.mvcoding.expensius

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlin.reflect.KClass

class RxBus {
    private val bus = PublishSubject.create<Any>().toSerialized()

    fun send(o: Any) = bus.onNext(o)
    fun <T : Any> observe(cls: KClass<T>): Observable<T> = bus.ofType(cls.java)
}

inline fun <reified T : Any> RxBus.observe(): Observable<T> = observe(T::class)