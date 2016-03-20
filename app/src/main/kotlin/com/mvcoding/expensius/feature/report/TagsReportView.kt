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
import android.graphics.Color
import android.support.v7.widget.CardView
import android.util.AttributeSet
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.mvcoding.expensius.extension.doNotInEditMode
import com.mvcoding.expensius.extension.provideActivityScopedSingleton
import com.mvcoding.expensius.provideAmountFormatter
import com.mvcoding.expensius.provideDateFormatter
import com.mvcoding.expensius.provideSettings
import kotlinx.android.synthetic.main.view_tags_report.view.*
import java.math.BigDecimal

class TagsReportView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        CardView(context, attrs, defStyleAttr), TagsReportPresenter.View {

    private val dateFormatter by lazy { provideDateFormatter() }
    private val amountFormatter by lazy { provideAmountFormatter() }
    private val settings by lazy { provideSettings() }
    private val presenter by lazy { provideActivityScopedSingleton(TagsReportPresenter::class) }

    override fun onFinishInflate() {
        super.onFinishInflate()
        barChart.setPinchZoom(false)
        barChart.isDoubleTapToZoomEnabled = false
        barChart.isDragEnabled = false
        barChart.isScaleXEnabled = false
        barChart.isScaleYEnabled = false
        barChart.legend.isEnabled = false
        barChart.axisLeft.setDrawLabels(false)
        barChart.axisLeft.setDrawGridLines(false)
        barChart.axisLeft.setDrawAxisLine(false)
        barChart.axisRight.setValueFormatter { value, yAxis -> amountFormatter.format(BigDecimal(value.toDouble()), settings.mainCurrency) }
        barChart.axisRight.setDrawZeroLine(true)
        barChart.axisRight.setDrawAxisLine(true)
        barChart.axisRight.setDrawGridLines(true)
        barChart.setDescription("")
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        doNotInEditMode { presenter.onViewAttached(this) }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter.onViewDetached(this)
    }

    override fun showTagsReportItems(tagsReportItems: List<TagsReportPresenter.TagsReportItem>) {
        val xAxis = tagsReportItems.map { dateFormatter.formatDateShort(it.interval.start) }
        barChart.data = tagsReportItems
                .map { it.tagsWithAmount }
                .mapIndexed { index, tagsWithAmount ->
                    BarEntry(tagsWithAmount.map { it.amount.toFloat() }.toFloatArray(), index)
                }
                .let {
                    BarDataSet(it, "TEMP TEST").apply { colors = listOf(Color.RED, Color.BLUE) }
                }
                .let { BarData(xAxis, it) }
    }
}