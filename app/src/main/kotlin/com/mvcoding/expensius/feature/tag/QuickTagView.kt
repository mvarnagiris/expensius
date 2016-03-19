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
import android.support.v4.content.ContextCompat.getColor
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.ViewGroup
import com.mvcoding.expensius.R
import com.mvcoding.expensius.extension.doInEditMode
import com.mvcoding.expensius.extension.getColorFromTheme
import com.mvcoding.expensius.extension.inflate
import com.mvcoding.expensius.extension.pickForegroundColor
import kotlinx.android.synthetic.main.view_quick_tag.view.*

class QuickTagView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        CardView(context, attrs, defStyleAttr) {

    private val darkTextColor = getColorFromTheme(context, android.R.attr.textColorPrimary)
    private val lightTextColor = getColorFromTheme(context, android.R.attr.textColorPrimaryInverse)
    private val unselectedBackgroundColor = getColorFromTheme(context, R.attr.colorBackgroundPrimary)

    private var color = 0

    companion object {
        fun inflate(parent: ViewGroup) = parent.inflate<QuickTagView>(R.layout.view_quick_tag)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        doInEditMode { setQuickTag(QuickTag("Quick tag text", getColor(context, R.color.lime_500))) }
    }

    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)
        updateBackgroundColor()
    }

    fun setQuickTag(quickTag: QuickTag) {
        color = quickTag.color
        titleTextView.text = quickTag.text
        updateBackgroundColor()
    }

    private fun updateBackgroundColor() {
        val backgroundColor = if (isSelected) color else unselectedBackgroundColor
        titleTextView.setTextColor(pickForegroundColor(backgroundColor, lightTextColor, darkTextColor))
        setCardBackgroundColor(backgroundColor)
    }


    data class QuickTag(val text: String, val color: Int)
}