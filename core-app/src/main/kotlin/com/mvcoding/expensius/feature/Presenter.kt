/*
 * Copyright (C) 2015 Mantas Varnagiris.
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

import rx.Subscription
import rx.subscriptions.CompositeSubscription

abstract class Presenter<V : Presenter.View> {
    private val lifetimeSubscriptions: CompositeSubscription = CompositeSubscription()
    private lateinit var viewSubscriptions: CompositeSubscription
    private var view: View? = null

    open fun onAttachView(view: V) {
        if (this.view != null) {
            throw IllegalStateException("Cannot attach $view, because ${this.view} is already attached")
        }

        this.view = view
        this.viewSubscriptions = CompositeSubscription()
    }

    open fun onDetachView(view: V) {
        if (this.view == null) {
            throw IllegalStateException("View is already detached.")
        }

        if (this.view != view) {
            throw IllegalStateException("Trying to detach different view. We have view: ${this.view}. Trying to detach view: $view")
        }

        this.view = null;
        this.viewSubscriptions.unsubscribe()
    }

    open fun onDestroy() {
        if (lifetimeSubscriptions.isUnsubscribed) {
            throw IllegalStateException("Presenter $this has already been destroyed.")
        }
        lifetimeSubscriptions.unsubscribe()
    }

    protected fun unsubscribeOnDetach(subscription: Subscription) {
        viewSubscriptions.add(subscription)
    }

    protected fun unsubscribeOnDestroy(subscription: Subscription) {
        lifetimeSubscriptions.add(subscription)
    }

    interface View
}