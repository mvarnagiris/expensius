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

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionSet;
import android.view.View;

@TargetApi(Build.VERSION_CODES.LOLLIPOP) class ActivityTransitionsLollipop implements ActivityTransitions {
    private final Activity activity;
    private final TransitionSet transitions;

    ActivityTransitionsLollipop(@NonNull Activity activity) {
        this.activity = activity;
        transitions = new TransitionSet();
    }

    @Override @NonNull public ActivityTransitionsLollipop slide(int gravity, @NonNull View view, View... views) {
        slide(gravity, view);
        if (views != null) {
            for (View otherView : views) {
                slide(gravity, otherView);
            }
        }

        return this;
    }

    @Override @NonNull public ActivityTransitionsLollipop slide(int gravity, @IdRes int viewId, @IdRes int... viewIds) {
        slide(gravity, viewId);
        if (viewIds != null) {
            for (int otherViewId : viewIds) {
                slide(gravity, otherViewId);
            }
        }

        return this;
    }

    @Override @NonNull public ActivityTransitionsLollipop fade(@NonNull View view, View... views) {
        fade(view);
        if (views != null) {
            for (View otherView : views) {
                fade(otherView);
            }
        }

        return this;
    }

    @Override @NonNull public ActivityTransitionsLollipop fade(@IdRes int viewId, @IdRes int... viewIds) {
        fade(viewId);
        if (viewIds != null) {
            for (int otherViewId : viewIds) {
                fade(otherViewId);
            }
        }

        return this;
    }

    @NonNull @Override public ActivityTransitions explode(@NonNull View view, View... views) {
        explode(view);
        if (views != null) {
            for (View otherView : views) {
                explode(otherView);
            }
        }

        return this;
    }

    @NonNull @Override public ActivityTransitions explode(@IdRes int viewId, @IdRes int... viewIds) {
        explode(viewId);
        if (viewIds != null) {
            for (int otherViewId : viewIds) {
                explode(otherViewId);
            }
        }

        return this;
    }

    @Override @NonNull public ActivityTransitionsLollipop fadeStatusBar() {
        fade(android.R.id.statusBarBackground);
        return this;
    }

    @Override @NonNull public ActivityTransitionsLollipop fadeNavigationBar() {
        fade(android.R.id.navigationBarBackground);
        return this;
    }

    @Override @NonNull public ActivityTransitionsLollipop reveal(@NonNull View view, View... views) {
        reveal(view);
        if (views != null) {
            for (View otherView : views) {
                reveal(otherView);
            }
        }

        return this;
    }

    @Override @NonNull public ActivityTransitionsLollipop sharedElementOverlay(boolean overlay) {
        activity.getWindow().setSharedElementsUseOverlay(overlay);
        return this;
    }

    @Override public void asExitTransition() {
        activity.getWindow().setExitTransition(transitions);
    }

    @Override public void asEnterTransition() {
        activity.getWindow().setEnterTransition(transitions);
    }

    @Override public void asReturnTransition() {
        activity.getWindow().setReturnTransition(transitions);
    }

    @Override public void asReenterTransition() {
        activity.getWindow().setReenterTransition(transitions);
    }

    private void slide(int gravity, View view) {
        final Slide slide = new Slide(gravity);
        slide.addTarget(view);
        transitions.addTransition(slide);
    }

    private void slide(int gravity, int viewId) {
        final Slide slide = new Slide(gravity);
        slide.addTarget(viewId);
        transitions.addTransition(slide);
    }

    private void fade(View view) {
        final Fade fade = new Fade();
        fade.addTarget(view);
        transitions.addTransition(fade);
    }

    private void fade(int viewId) {
        final Fade fade = new Fade();
        fade.addTarget(viewId);
        transitions.addTransition(fade);
    }

    private void reveal(View view) {
        final RevealTransition revealTransition = new RevealTransition();
        revealTransition.addTarget(view);
        transitions.addTransition(revealTransition);
    }

    private void explode(View view) {
        final Explode explode = new Explode();
        explode.addTarget(view);
        transitions.addTransition(explode);
    }

    private void explode(int viewId) {
        final Explode explode = new Explode();
        explode.addTarget(viewId);
        transitions.addTransition(explode);
    }
}
