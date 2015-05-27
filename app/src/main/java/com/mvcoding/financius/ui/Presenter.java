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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

public abstract class Presenter<V extends PresenterView> {
    private static final Presenter EMPTY = new Presenter() {
    };

    private CompositeSubscription viewSubscriptions;

    private V view;

    static <V extends PresenterView> Presenter<V> empty() {
        //noinspection unchecked
        return EMPTY;
    }

    /**
     * Called once, after the presenter is created.
     */
    protected void onCreate() {
    }

    /**
     * Called when view is created but not visible yet. Can be called multiple times. Corresponding method {@link
     * #onViewDetached(PresenterView)}.
     */
    protected void onViewAttached(@NonNull V view) {
        checkArgument(this.view == null, "View is already created.");
        this.view = checkNotNull(view, "View cannot be null.");
        viewSubscriptions = new CompositeSubscription();
    }

    /**
     * Called when view becomes visible. Can be called multiple times. Corresponding method {@link #onViewPaused(PresenterView)}.
     */
    protected void onViewResumed(@NonNull V view) {

    }

    /**
     * Called when view becomes invisible. Can be called multiple times. Corresponding method {@link #onViewResumed(PresenterView)}.
     */
    protected void onViewPaused(@NonNull V view) {
    }

    /**
     * Called when view is destroyed. Can be called multiple times. Corresponding method {@link #onViewAttached(PresenterView)}. A good
     * place to release all view related resources.
     */
    protected void onViewDetached(@NonNull V view) {
        checkState(this.view != null, "View already detached.");
        this.view = null;
        viewSubscriptions.unsubscribe();
        viewSubscriptions.clear();
    }

    /**
     * Called once, when presenter is being destroyed. A good place to free resources.
     */
    protected void onDestroy() {
    }

    @Nullable protected V getView() {
        return view;
    }

    /**
     * This will make sure to unsubscribe when view is detached.
     *
     * @param subscription Subscription to unsubscribe when view is detached.
     */
    protected void unsubscribeOnDetach(@NonNull Subscription subscription) {
        viewSubscriptions.add(subscription);
    }
}
