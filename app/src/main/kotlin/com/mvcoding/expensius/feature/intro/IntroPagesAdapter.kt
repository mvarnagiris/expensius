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

package com.mvcoding.expensius.feature.intro

import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup

class IntroPagesAdapter() : PagerAdapter() {
    var introPages: List<IntroPage<Int>>? = null
        set(value) {
            field = value
            notifyDataSetChanged();
        }

    override fun instantiateItem(container: ViewGroup, position: Int): Any? {
        val introPageView = IntroPageView.inflate(container)
        introPageView.setIntroPage(introPages!![position])
        container.addView(introPageView)
        return introPageView
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any?) {
        if (obj is View) {
            container.removeView(obj)
        }
    }

    override fun getItemPosition(obj: Any?): Int {
        return POSITION_NONE
    }

    override fun isViewFromObject(view: View?, obj: Any?): Boolean = view === obj

    override fun getCount(): Int = introPages?.size ?: 0
}