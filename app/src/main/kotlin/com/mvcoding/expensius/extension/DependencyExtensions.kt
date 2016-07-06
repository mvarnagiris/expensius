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
import com.memoizrlabs.Shank.registerFactory
import com.memoizrlabs.shankkotlin.provideSingletonFor
import kotlin.reflect.KClass

inline fun <reified T : Any> Scope.provideSingleton() = Shank.with(this).provideSingletonFor<T>()
fun <T : Any, A : Any> registerFactory(objectClass: KClass<T>, factory: (arg1: A) -> T) = registerFactory(objectClass.java, factory)
fun <T : Any, A : Any, B : Any> registerFactory(objectClass: KClass<T>, factory: (arg1: A, arg2: B) -> T) =
        registerFactory(objectClass.java, factory)