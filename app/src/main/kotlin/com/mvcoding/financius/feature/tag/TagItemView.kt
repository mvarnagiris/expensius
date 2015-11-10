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
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import com.mvcoding.financius.R
import com.mvcoding.financius.extension.makeOutlineProviderOval
import kotlinx.android.synthetic.item_view_tag.view.colorImageView
import kotlinx.android.synthetic.item_view_tag.view.titleTextView

class TagItemView : LinearLayout {
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
        colorImageView.setColorFilter(tag.color)
        titleTextView.text = tag.title
    }
}