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

import android.content.Context
import android.support.percent.PercentRelativeLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.mvcoding.expensius.R

class IntroPageView : PercentRelativeLayout {
    private val imageView by lazy { findViewById(R.id.imageView) as ImageView }
    private val titleTextView by lazy { findViewById(R.id.titleTextView) as TextView }
    private val messageTextView by lazy { findViewById(R.id.messageTextView) as TextView }

    constructor(context: Context?) : this(context, null)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    companion object {
        fun inflate(viewGroup: ViewGroup): IntroPageView {
            return LayoutInflater.from(viewGroup.context).inflate(R.layout.view_intro_page, viewGroup, false) as IntroPageView
        }
    }

    fun setIntroPage(introPage: IntroPage<Int>) {
        imageView.setImageResource(introPage.image.value)
        titleTextView.text = introPage.title
        messageTextView.text = introPage.message
    }
}