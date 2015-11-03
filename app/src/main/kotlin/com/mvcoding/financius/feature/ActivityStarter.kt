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

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.view.View
import com.mvcoding.financius.isActivity
import com.mvcoding.financius.toActivity
import java.io.Serializable
import kotlin.reflect.KClass

class ActivityStarter(val context: Context, activityClass: KClass<out BaseActivity>) {
    val intent: Intent;
    var options: ActivityOptionsCompat? = null

    init {
        intent = Intent(context, activityClass.java)
    }

    fun extra(name: String, value: String): ActivityStarter {
        intent.putExtra(name, value);
        return this
    }

    fun extra(name: String, value: Serializable): ActivityStarter {
        intent.putExtra(name, value);
        return this
    }

    fun addFlags(flags: Int): ActivityStarter {
        intent.addFlags(flags);
        return this;
    }

    fun enableTransition(vararg sharedViews: View): ActivityStarter {
        if (context !is Activity) {
            return this
        }

        val elements: Array<Pair<View, String>>;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && sharedViews.size > 0) {
            elements = Array(sharedViews.size, { i -> Pair(sharedViews[i], sharedViews[i].transitionName) });
        } else {
            elements = emptyArray()
        }

        //noinspection unchecked
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(context, *elements);
        if (this.options == null) {
            this.options = options;
        } else {
            this.options!!.update(options);
        }

        return this;
    }

    fun start() {
        if (context.isActivity()) {
            ActivityCompat.startActivity(context.toActivity(), intent, options?.toBundle());
        } else {
            context.startActivity(intent)
        }
    }

    fun startForResult(requestCode: Int) {
        if (!context.isActivity()) {
            throw IllegalArgumentException("Context must be an Activity, when starting for result.")
        }

        ActivityCompat.startActivityForResult(context.toActivity(), intent, requestCode, options?.toBundle());
    }
}