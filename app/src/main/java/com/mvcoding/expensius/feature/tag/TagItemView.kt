/*
 * Copyright (C) 2018 Mantas Varnagiris.
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

//class TagItemView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
//        LinearLayout(context, attrs, defStyleAttr) {
//
//    private val colorImageView by lazy { findViewById(R.id.colorImageView) as ImageView }
//    private val titleTextView by lazy { findViewById(R.id.titleTextView) as TextView }
//
//    private val textColorPrimary by lazy { getColorFromTheme(context, android.R.attr.textColorPrimary) }
//    private val textColorSecondary by lazy { getColorFromTheme(context, android.R.attr.textColorSecondary) }
//
//    override fun onFinishInflate() {
//        super.onFinishInflate()
//        colorImageView.makeOutlineProviderOval()
//    }
//
//    fun setTag(tag: Tag) {
//        colorImageView.setColorFilter(getIconColor(tag))
//        titleTextView.setTextColor(getTextColor(tag))
//        titleTextView.text = tag.title.text
//    }
//
//    private fun getIconColor(tag: Tag) = if (tag.modelState == ARCHIVED) textColorSecondary else tag.color.rgb
//    private fun getTextColor(tag: Tag) = if (tag.modelState == ARCHIVED) textColorSecondary else textColorPrimary
//}