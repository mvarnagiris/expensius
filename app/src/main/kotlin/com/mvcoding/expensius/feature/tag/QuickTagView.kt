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
import android.content.res.ColorStateList
import android.support.v4.content.ContextCompat.getColor
import android.support.v4.content.ContextCompat.getColorStateList
import android.support.v4.graphics.ColorUtils.calculateContrast
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.mvcoding.expensius.R

class QuickTagView : CardView {
    private val titleTextView by lazy { findViewById(R.id.titleTextView) as TextView }

    private val darkTextColor by lazy { getColorStateList(context, R.color.text_primary) }
    private val lightTextColor by lazy { getColorStateList(context, R.color.text_primary_inverse) }

    constructor(context: Context?) : this(context, null)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    companion object {
        fun inflate(parent: ViewGroup) = LayoutInflater.from(parent.context).inflate(R.layout.view_quick_tag, parent, false) as QuickTagView
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (isInEditMode) {
            setQuickTag(QuickTag("Quick tag text", getColor(context, R.color.lime_500)))
        }
    }

    fun setQuickTag(quickTag: QuickTag) {
        titleTextView.text = quickTag.text
        titleTextView.setTextColor(calculateTextColor(quickTag.color))
        setCardBackgroundColor(quickTag.color)
    }

    private fun calculateTextColor(backgroundColor: Int): ColorStateList {
        if (calculateContrast(lightTextColor.defaultColor, backgroundColor) > 2) {
            return lightTextColor
        } else {
            return darkTextColor
        }
    }

    data class QuickTag(val text: String, val color: Int)
}