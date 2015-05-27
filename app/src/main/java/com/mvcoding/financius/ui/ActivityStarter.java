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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import java.io.Serializable;

import static com.google.common.base.Preconditions.checkNotNull;

public class ActivityStarter {
    private final Fragment fragment;
    private final Context context;
    private final Class activityClass;
    private final Intent intent;
    private boolean isEnterAnimationEnabled = true;

    private ActivityStarter(@NonNull Context context, @NonNull Class activityClass) {
        this.fragment = null;
        this.context = checkNotNull(context, "Context cannot be null.");
        this.activityClass = checkNotNull(activityClass, "Activity class cannot be null.");
        this.intent = createIntent(context, activityClass);
    }

    private ActivityStarter(@NonNull Fragment fragment, @NonNull Class activityClass) {
        this.fragment = checkNotNull(fragment, "Fragment cannot be null.");
        this.context = checkNotNull(fragment.getActivity(), "Fragment must be attached to Activity.");
        this.activityClass = checkNotNull(activityClass, "Activity class cannot be null.");
        this.intent = createIntent(context, activityClass);
    }

    public static ActivityStarter with(@NonNull Context context, @NonNull Class activityClass) {
        return new ActivityStarter(context, activityClass);
    }

    public static ActivityStarter with(@NonNull Fragment fragment, @NonNull Class activityClass) {
        return new ActivityStarter(fragment, activityClass);
    }

    public ActivityStarter extra(String name, Parcelable value) {
        intent.putExtra(name, value);
        return this;
    }

    public ActivityStarter extra(String name, Serializable value) {
        intent.putExtra(name, value);
        return this;
    }

    public ActivityStarter extra(String name, String value) {
        intent.putExtra(name, value);
        return this;
    }

    public ActivityStarter extra(String name, boolean value) {
        intent.putExtra(name, value);
        return this;
    }

    public ActivityStarter extra(String name, long value) {
        intent.putExtra(name, value);
        return this;
    }

    public ActivityStarter extra(String name, double value) {
        intent.putExtra(name, value);
        return this;
    }

    public ActivityStarter addFlags(int flags) {
        intent.addFlags(flags);
        return this;
    }

    public ActivityStarter setIsEnterAnimationEnabled(boolean isEnterAnimationEnabled) {
        this.isEnterAnimationEnabled = isEnterAnimationEnabled;
        return this;
    }

    public void start() {
        if (fragment != null) {
            fragment.startActivity(intent);
        } else {
            context.startActivity(intent);
        }

        if (!isEnterAnimationEnabled && context instanceof Activity) {
            ((Activity) context).overridePendingTransition(0, 0);
        }
    }

    public void startForResult(int requestCode) {
        if (fragment != null) {
            fragment.startActivityForResult(intent, requestCode);
        } else if (context instanceof Activity) {
            ((Activity) context).startActivityForResult(intent, requestCode);
        } else {
            throw new IllegalArgumentException("Context must be an Activity, when starting for result.");
        }
    }

    private Intent createIntent(Context context, Class activityClass) {
        return new Intent(context, activityClass);
    }
}
