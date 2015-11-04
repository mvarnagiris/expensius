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

package com.mvcoding.financius.feature.tag

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import kotlinx.android.synthetic.view_tag.view.lobsterPicker
import kotlinx.android.synthetic.view_tag.view.lobsterShadeSlider

class TagView : LinearLayout {
    constructor(context: Context?) : this(context, null)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onFinishInflate() {
        super.onFinishInflate()
        lobsterPicker.addDecorator(lobsterShadeSlider)
        //        lobsterPicker.colorAdapter = object : ColorAdapter {
        //            override fun size(): Int {
        //                return 6
        //            }
        //
        //            override fun color(position: Int, shade: Int): Int {
        //                return Color.RED
        //            }
        //
        //            override fun shades(position: Int): Int {
        //                return 6
        //            }
        //        }
        //        lobsterShadeSlider.setColorAdapter(object : ColorAdapter {
        //            override fun size(): Int {
        //                return 6
        //            }
        //
        //            override fun color(position: Int, shade: Int): Int {
        //                return 0
        //            }
        //
        //            override fun shades(position: Int): Int {
        //                return 6
        //            }
        //
        //        })
    }
}