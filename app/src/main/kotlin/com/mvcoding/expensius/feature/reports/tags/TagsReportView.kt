/*
 * Copyright (C) 2017 Mantas Varnagiris.
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

package com.mvcoding.expensius.feature.reports.tags

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.mvcoding.expensius.extension.doNotInEditMode
import com.mvcoding.expensius.feature.reports.provideTagsReportPresenter
import com.mvcoding.expensius.model.NullModels.noMoney
import com.mvcoding.expensius.model.TagsReport

class TagsReportView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        LinearLayout(context, attrs, defStyleAttr), TagsReportPresenter.View {

    private val presenter by lazy { provideTagsReportPresenter() }

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

    override fun showTagsReport(tagsReport: TagsReport) {
        removeAllViews()
        val maxMoney = tagsReport.currentMoneys.firstOrNull()?.money ?: noMoney
        tagsReport.currentMoneys.forEach {
            val tagReportItemView = TagMoneyItemView.inflate(this)
            addView(tagReportItemView)
            tagReportItemView.setTag(it.group)
            tagReportItemView.setMoney(it.money)
            tagReportItemView.setProgress(it.money.amount.toFloat() / maxMoney.amount.toFloat())
        }
    }
}