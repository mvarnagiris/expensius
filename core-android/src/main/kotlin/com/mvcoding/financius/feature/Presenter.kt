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

package com.mvcoding.financius.feature

import rx.Subscription
import rx.subscriptions.CompositeSubscription

abstract class Presenter<in V : Presenter.View> {
    private var view: View? = null
    private var subscriptions: CompositeSubscription? = null

    open fun onAttachView(view: V) {
        if (this.view !== null) {
            throw IllegalStateException("View " + this.view + " is already defined")
        }

        this.view = view
        this.subscriptions = CompositeSubscription()
    }

    open fun onDetachView(view: V) {
        if (this.view == null) {
            throw IllegalStateException("View is already detached.")
        }

        if (this.view !== view) {
            throw IllegalStateException("Trying to detach different view. We have view: " + this.view + ". Trying to detach view: " + view)
        }
        this.view = null;
        this.subscriptions?.unsubscribe()
        this.subscriptions = null;
    }

    protected fun unsubscribeOnDetach(subscription: Subscription) {
        subscriptions!!.add(subscription)
    }

    interface View {
    }
}
