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
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.view.View;

import java.io.Serializable;
import java.util.Collection;

public final class ActivityStarter {
    private final Fragment mFragment;
    private final Context mContext;
    private final Intent mIntent;
    private ActivityOptionsCompat mOptions;

    private ActivityStarter(@NonNull Context context, @NonNull Class activityClass) {
        mFragment = null;
        mContext = context;
        mIntent = createIntent(context, activityClass);
    }

    private ActivityStarter(@NonNull Fragment fragment, @NonNull Class activityClass) {
        mFragment = fragment;
        mContext = fragment.getActivity();
        mIntent = createIntent(mContext, activityClass);
    }

    @NonNull public static ActivityStarter with(@NonNull Context context, @NonNull Class activityClass) {
        return new ActivityStarter(context, activityClass);
    }

    @NonNull public static ActivityStarter with(@NonNull Fragment fragment, @NonNull Class activityClass) {
        return new ActivityStarter(fragment, activityClass);
    }

    @NonNull public ActivityStarter extra(@NonNull String name, @Nullable Parcelable value) {
        mIntent.putExtra(name, value);
        return this;
    }

    @NonNull public ActivityStarter extra(@NonNull String name, @Nullable Collection<? extends Parcelable> value) {
        if (value == null) {
            return extra(name, (Parcelable[]) null);
        }

        return extra(name, value.toArray(new Parcelable[value.size()]));
    }

    @NonNull public ActivityStarter extra(@NonNull String name, @Nullable Parcelable[] value) {
        mIntent.putExtra(name, value);
        return this;
    }

    @NonNull public ActivityStarter extra(@NonNull String name, @Nullable Serializable value) {
        mIntent.putExtra(name, value);
        return this;
    }

    @NonNull public ActivityStarter extra(@NonNull String name, @Nullable String value) {
        mIntent.putExtra(name, value);
        return this;
    }

    @NonNull public ActivityStarter extra(@NonNull String name, boolean value) {
        mIntent.putExtra(name, value);
        return this;
    }

    @NonNull public ActivityStarter extra(@NonNull String name, int value) {
        mIntent.putExtra(name, value);
        return this;
    }

    @NonNull public ActivityStarter extra(@NonNull String name, long value) {
        mIntent.putExtra(name, value);
        return this;
    }

    @NonNull public ActivityStarter extra(@NonNull String name, double value) {
        mIntent.putExtra(name, value);
        return this;
    }

    @NonNull public ActivityStarter addFlags(int flags) {
        mIntent.addFlags(flags);
        return this;
    }

    @NonNull public ActivityStarter enableTransition(@Nullable View... sharedViews) {
        if (mContext instanceof Activity) {
            Pair<View, String>[] elements = null;
            if (sharedViews != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //noinspection unchecked
                elements = new Pair[sharedViews.length];
                for (int i = 0; i < sharedViews.length; i++) {
                    elements[i] = Pair.create(sharedViews[i], sharedViews[i].getTransitionName());
                }
            }

            //noinspection unchecked
            final ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext, elements);
            if (mOptions == null) {
                mOptions = options;
            } else {
                mOptions.update(options);
            }
        }
        return this;
    }

    @NonNull public ActivityStarter expandFrom(@NonNull View view) {
        final int startX = (int) (view.getX() + view.getWidth() / 2);
        final int startY = (int) (view.getY() + view.getHeight() / 2);
        final ActivityOptionsCompat options = ActivityOptionsCompat.makeScaleUpAnimation(view, startX, startY, 0, 0);
        if (mOptions == null) {
            mOptions = options;
        } else {
            mOptions.update(options);
        }
        return this;
    }

    public void start() {
        if (mFragment != null) {
            mFragment.startActivity(mIntent);
        } else if (mContext instanceof Activity) {
            ActivityCompat.startActivity((Activity) mContext, mIntent, mOptions != null ? mOptions.toBundle() : null);
        } else {
            mContext.startActivity(mIntent);
        }
    }

    public void startForResult(int requestCode) {
        if (mFragment != null) {
            mFragment.startActivityForResult(mIntent, requestCode);
        } else if (mContext instanceof Activity) {
            ActivityCompat.startActivityForResult((Activity) mContext, mIntent, requestCode, mOptions != null ? mOptions.toBundle() : null);
        } else {
            throw new IllegalArgumentException("Context must be an Activity, when starting for result.");
        }
    }

    private Intent createIntent(Context context, Class activityClass) {
        return new Intent(context, activityClass);
    }
}
