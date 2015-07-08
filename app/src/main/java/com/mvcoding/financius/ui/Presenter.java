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

package com.mvcoding.financius.ui;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class Presenter<V extends PresenterView> {
    private V view;
    private CompositeSubscription subscriptions;

    @CallSuper protected void onViewAttached(@NonNull V view) {
        if (this.view != null) {
            throw new IllegalStateException("View " + this.view + " is already attached. Cannot attach " + view);
        }
        this.view = view;
    }

    @CallSuper protected void onViewDetached() {
        if (view == null) {
            throw new IllegalStateException("View is already detached.");
        }
        this.view = null;

        if (subscriptions != null) {
            subscriptions.unsubscribe();
            subscriptions = null;
        }
    }

    @CallSuper protected void unsubscribeOnDetach(@NonNull Subscription subscription) {
        if (subscriptions == null) {
            subscriptions = new CompositeSubscription();
        }
        subscriptions.add(subscription);
    }
}
