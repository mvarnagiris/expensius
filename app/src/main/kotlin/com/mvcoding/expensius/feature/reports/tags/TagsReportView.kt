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
import android.support.v7.widget.LinearLayoutManager
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.FrameLayout
import com.mvcoding.expensius.extension.doNotInEditMode
import com.mvcoding.expensius.extension.setGone
import com.mvcoding.expensius.extension.setVisible
import com.mvcoding.expensius.feature.BaseAdapter
import com.mvcoding.expensius.feature.ViewHolder
import com.mvcoding.expensius.feature.reports.provideTagsReportPresenter
import com.mvcoding.expensius.model.GroupedMoney
import com.mvcoding.expensius.model.NullModels.noMoney
import com.mvcoding.expensius.model.Tag
import com.mvcoding.expensius.model.TagsReport
import kotlinx.android.synthetic.main.view_tags_report.view.*
import java.math.BigDecimal

class TagsReportView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        FrameLayout(context, attrs, defStyleAttr), TagsReportPresenter.View {

    private val presenter by lazy { provideTagsReportPresenter() }
    private val adapter by lazy { Adapter() }

    override fun onFinishInflate() {
        super.onFinishInflate()
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean = !isEnabled

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        doNotInEditMode { presenter.attach(this) }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter.detach(this)
    }

    override fun showTagsReport(tagsReport: TagsReport) {
        adapter.maxMoneyAmount = (tagsReport.currentMoneys.firstOrNull()?.money ?: noMoney).amount
        adapter.setItems(tagsReport.currentMoneys)
    }

    override fun showEmptyView(): Unit = emptyTextView.setVisible()
    override fun hideEmptyView(): Unit = emptyTextView.setGone()

    private class Adapter : BaseAdapter<GroupedMoney<Tag>, ViewHolder>() {
        var maxMoneyAmount: BigDecimal = BigDecimal.ONE

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(TagMoneyItemView.inflate(parent))

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val view = holder.getView<TagMoneyItemView>()
            val item = getItem(position)
            view.setTag(item.group)
            view.setMoney(item.money)
            view.setProgress(item.money.amount.toFloat() / maxMoneyAmount.toFloat())
        }
    }
}