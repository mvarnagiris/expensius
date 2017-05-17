package com.mvcoding.expensius.feature

import rx.Subscription
import rx.lang.kotlin.BehaviorSubject
import kotlin.reflect.KClass

class RxState<in STATE : Any>(initialState: STATE? = null) {

    private val stateSubject = BehaviorSubject<STATE>()
    private val stateActions = hashMapOf<KClass<out STATE>, (STATE) -> List<Subscription>>()
    private val currentSubscriptions = arrayListOf<Subscription>()
    private var stateSubscription: Subscription? = null

    init {
        if (initialState != null) stateSubject.onNext(initialState)
    }

    fun <PRECISE_STATE : STATE> registerState(state: KClass<PRECISE_STATE>, action: (PRECISE_STATE) -> List<Subscription>) {
        @Suppress("UNCHECKED_CAST")
        stateActions.put(state, action as (STATE) -> List<Subscription>)
    }

    fun subscribe() {
        stateSubscription = stateSubject.subscribe {
            currentSubscriptions.forEach { it.unsubscribe() }
            currentSubscriptions.clear()
            currentSubscriptions.addAll(stateActions[it::class]?.invoke(it) ?: emptyList())
        }
    }

    fun setState(state: STATE): Unit = stateSubject.onNext(state)

    fun unsubscribe() {
        stateSubscription?.unsubscribe()
        stateSubscription = null
    }
}