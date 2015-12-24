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

package com.mvcoding.expensius.feature.tag

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.mvcoding.expensius.ModelState.ARCHIVED
import com.mvcoding.expensius.R
import com.mvcoding.expensius.extension.getColorFromTheme
import com.mvcoding.expensius.extension.makeOutlineProviderOval

class TagItemView : LinearLayout {
    private val colorImageView by lazy { findViewById(R.id.colorImageView) as ImageView }
    private val titleTextView by lazy { findViewById(R.id.titleTextView) as TextView }

    private val textColorPrimary by lazy { getColorFromTheme(context, android.R.attr.textColorPrimary) }
    private val textColorSecondary by lazy { getColorFromTheme(context, android.R.attr.textColorSecondary) }

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    companion object {
        fun inflate(viewGroup: ViewGroup): TagItemView {
            return LayoutInflater.from(viewGroup.context).inflate(R.layout.item_view_tag, viewGroup, false) as TagItemView
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        colorImageView.makeOutlineProviderOval()
    }

    fun setTag(tag: Tag) {
        colorImageView.setColorFilter(getIconColor(tag))
        titleTextView.setTextColor(getTextColor(tag))
        titleTextView.text = tag.title
    }

    private fun getIconColor(tag: Tag) = if (tag.modelState == ARCHIVED) textColorSecondary else tag.color

    private fun getTextColor(tag: Tag) = if (tag.modelState == ARCHIVED) textColorSecondary else textColorPrimary
}