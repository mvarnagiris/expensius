package com.mvcoding.financius.extension

import com.memoizrlabs.Shank
import kotlin.reflect.KClass

fun <T : Any> provideSingleton(cls: KClass<T>): T {
    return Shank.withScope(Any::class.java).provide(cls.java)
}