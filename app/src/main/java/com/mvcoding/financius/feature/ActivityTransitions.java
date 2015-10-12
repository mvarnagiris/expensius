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

public interface ActivityTransitions {
    @NonNull ActivityTransitions slide(int gravity, @NonNull View view, View... views);
    @NonNull ActivityTransitions slide(int gravity, @IdRes int viewId, @IdRes int... viewIds);
    @NonNull ActivityTransitions fade(@NonNull View view, View... views);
    @NonNull ActivityTransitions fade(@IdRes int viewId, @IdRes int... viewIds);
    @NonNull ActivityTransitions explode(@NonNull View view, View... views);
    @NonNull ActivityTransitions explode(@IdRes int viewId, @IdRes int... viewIds);
    @NonNull ActivityTransitions fadeStatusBar();
    @NonNull ActivityTransitions fadeNavigationBar();
    @NonNull ActivityTransitions reveal(@NonNull View view, View... views);
    @NonNull ActivityTransitions sharedElementOverlay(boolean overlay);
    void asExitTransition();
    void asEnterTransition();
    void asReturnTransition();
    void asReenterTransition();
}
