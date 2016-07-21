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

package com.mvcoding.expensius.feature.report

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.mvcoding.expensius.extension.doNotInEditMode

class TagReportItemsView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        LinearLayout(context, attrs, defStyleAttr), TagsTotalsReportPresenter.View {

    private val presenter by lazy { provideTagTotalsReportPresenter() }

    init {
        orientation = VERTICAL
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        doNotInEditMode { presenter.attach(this) }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter.detach(this)
    }

    override fun showTagsTotals(tagAmounts: List<TagsTotalsReportPresenter.TagAmount>) {
        removeAllViews()
        val maxAmount = tagAmounts.first().amount
        tagAmounts.forEach {
            val tagReportItemView = TagReportItemView.inflate(this)
            addView(tagReportItemView)
            tagReportItemView.setTag(it.tag)
            tagReportItemView.setAmount(it.amount)
            tagReportItemView.setProgress(it.amount.toFloat() / maxAmount.toFloat())
        }
    }
}