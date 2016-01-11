/*
 * Copyright (C) 2016 Mantas Varnagiris.
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

package com.mvcoding.expensius.feature.tag

import android.content.Context
import android.util.AttributeSet
import android.view.View.MeasureSpec.getSize
import android.view.ViewGroup
import java.lang.Math.max

class QuickTagsView2 : ViewGroup {
    constructor(context: Context?) : this(context, null)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = getSize(widthMeasureSpec) - paddingLeft - paddingRight
        var measuredHeight = 0
        var usedWidth = 0;
        var currentLineHeight = 0;

        0.rangeTo(childCount).forEach {
            val child = getChildAt(it)
            val childLayoutParams = child.layoutParams as MarginLayoutParams
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0)

            val childTakenWidth = childLayoutParams.leftMargin + child.measuredWidth + childLayoutParams.rightMargin
            val childTakenHeight = childLayoutParams.topMargin + child.measuredHeight + childLayoutParams.bottomMargin

            val exceedsWidth = width < usedWidth + childTakenWidth
            if (exceedsWidth) {
                measuredHeight += currentLineHeight
                currentLineHeight = childTakenHeight
                usedWidth = 0
            } else {
                usedWidth += childTakenWidth
                currentLineHeight = max(currentLineHeight, childTakenHeight)
            }
        }
        measuredHeight += currentLineHeight

        setMeasuredDimension(width, measuredHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        throw UnsupportedOperationException()
    }
}