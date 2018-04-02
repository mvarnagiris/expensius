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

package com.mvcoding.expensius.feature

import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import kotlin.reflect.KClass

class RxState<in STATE : Any>(initialState: STATE? = null) {

    private val stateSubject = BehaviorSubject.create<STATE>()
    private val stateActions = hashMapOf<KClass<out STATE>, (STATE) -> List<Disposable>>()
    private val currentSubscriptions = arrayListOf<Disposable>()
    private var stateSubscription: Disposable? = null

    init {
        if (initialState != null) stateSubject.onNext(initialState)
    }

    fun <PRECISE_STATE : STATE> registerState(state: KClass<PRECISE_STATE>, action: (PRECISE_STATE) -> List<Disposable>) {
        @Suppress("UNCHECKED_CAST")
        stateActions.put(state, action as (STATE) -> List<Disposable>)
    }

    fun subscribe() {
        stateSubscription = stateSubject.subscribe {
            currentSubscriptions.forEach { it.dispose() }
            currentSubscriptions.clear()
            currentSubscriptions.addAll(stateActions[it::class]?.invoke(it) ?: emptyList())
        }
    }

    fun setState(state: STATE): Unit = stateSubject.onNext(state)

    fun unsubscribe() {
        stateSubscription?.dispose()
        stateSubscription = null
    }
}