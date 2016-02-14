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
import android.widget.TextView
import com.mvcoding.expensius.R
import com.mvcoding.expensius.extension.doInEditMode
import com.mvcoding.expensius.extension.getColorFromTheme
import com.mvcoding.expensius.extension.inflate
import com.mvcoding.expensius.extension.pickForegroundColor

class QuickTagView : CardView {
    private val titleTextView by lazy { findViewById(R.id.titleTextView) as TextView }

    private val darkTextColor by lazy { getColor(context, R.color.text_primary) }
    private val lightTextColor by lazy { getColor(context, R.color.text_primary_inverse) }
    private val unselectedBackgroundColor by lazy { getColorFromTheme(context, R.attr.colorBackgroundPrimary) }

    private var color = 0

    constructor(context: Context?) : this(context, null)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

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