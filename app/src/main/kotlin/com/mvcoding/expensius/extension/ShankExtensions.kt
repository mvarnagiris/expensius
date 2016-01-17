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

package com.mvcoding.expensius.extension

import com.memoizrlabs.Scope
import com.memoizrlabs.Shank
import rx.Observable
import kotlin.reflect.KClass

fun <T : Any> provideSingleton(cls: KClass<T>): T {
    return Shank.provideSingleton(cls.java)
}

fun <T : Any> provideScopedSingleton(cls: KClass<T>,
                                     scope: Any,
                                     scopeDestroyObservable: Observable<Unit>,
                                     arg1: Any? = null,
                                     arg2: Any? = null,
                                     arg3: Any? = null,
                                     arg4: Any? = null): T {
    val shankScope = Scope.scope(scope)
    scopeDestroyObservable.take(1).subscribe { shankScope.clear() }

    val shank = Shank.with(shankScope)
    when {
        arg4 != null -> return shank.provide(cls.java, arg1, arg2, arg3, arg4)
        arg3 != null -> return shank.provide(cls.java, arg1, arg2, arg3)
        arg2 != null -> return shank.provide(cls.java, arg1, arg2)
        arg1 != null -> return shank.provide(cls.java, arg1)
        else -> return shank.provide(cls.java)
    }
}