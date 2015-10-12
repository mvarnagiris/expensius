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

package com.mvcoding.financius.feature;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.view.View;

class ActivityTransitionsNoOp implements ActivityTransitions {
    @NonNull @Override public ActivityTransitions slide(int gravity, @NonNull View view, View... views) {
        return this;
    }

    @NonNull @Override public ActivityTransitions slide(int gravity, @IdRes int viewId, @IdRes int... viewIds) {
        return this;
    }

    @NonNull @Override public ActivityTransitions fade(@NonNull View view, View... views) {
        return this;
    }

    @NonNull @Override public ActivityTransitions fade(@IdRes int viewId, @IdRes int... viewIds) {
        return this;
    }

    @NonNull @Override public ActivityTransitions explode(@NonNull View view, View... views) {
        return this;
    }

    @NonNull @Override public ActivityTransitions explode(@IdRes int viewId, @IdRes int... viewIds) {
        return this;
    }

    @NonNull @Override public ActivityTransitions fadeStatusBar() {
        return this;
    }

    @NonNull @Override public ActivityTransitions fadeNavigationBar() {
        return this;
    }

    @NonNull @Override public ActivityTransitions reveal(@NonNull View view, View... views) {
        return this;
    }

    @NonNull @Override public ActivityTransitions sharedElementOverlay(boolean overlay) {
        return this;
    }

    @Override public void asExitTransition() {
    }

    @Override public void asEnterTransition() {
    }

    @Override public void asReturnTransition() {
    }

    @Override public void asReenterTransition() {
    }
}
