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
import android.support.v7.widget.CardView
import android.util.AttributeSet
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.mvcoding.expensius.R
import com.mvcoding.expensius.extension.doNotInEditMode
import com.mvcoding.expensius.extension.getColorFromTheme
import com.mvcoding.expensius.extension.provideActivityScopedSingleton
import com.mvcoding.expensius.feature.transaction.TransactionType
import com.mvcoding.expensius.feature.transaction.TransactionType.EXPENSE
import com.mvcoding.expensius.model.Tag
import com.mvcoding.expensius.provideAmountFormatter
import com.mvcoding.expensius.provideDateFormatter
import com.mvcoding.expensius.provideSettings
import kotlinx.android.synthetic.main.view_tags_report.view.*
import java.math.BigDecimal
import java.util.*

class TagsReportView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        CardView(context, attrs, defStyleAttr), TagsReportPresenter.View {

    private val dateFormatter by lazy { provideDateFormatter() }
    private val amountFormatter by lazy { provideAmountFormatter() }
    private val settings by lazy { provideSettings() }

    private lateinit var presenter: TagsReportPresenter
    private var defaultColor = 0

    fun initialize(transactionType: TransactionType) {
        presenter = provideActivityScopedSingleton(TagsReportPresenter::class, transactionType)
        defaultColor = if (transactionType == EXPENSE) getColorFromTheme(R.attr.colorExpense) else getColorFromTheme(R.attr.colorIncome)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        lineChart.setPinchZoom(false)
        lineChart.isDoubleTapToZoomEnabled = false
        lineChart.isDragEnabled = false
        lineChart.isScaleXEnabled = false
        lineChart.isScaleYEnabled = false
        lineChart.legend.isEnabled = false
        lineChart.axisLeft.setDrawLabels(false)
        lineChart.axisLeft.setDrawGridLines(false)
        lineChart.axisLeft.setDrawAxisLine(false)
        lineChart.axisRight.setValueFormatter { value, yAxis ->
            amountFormatter.format(BigDecimal(value.toDouble()), settings.mainCurrency)
        }
        lineChart.axisRight.setDrawZeroLine(true)
        lineChart.axisRight.setDrawAxisLine(false)
        lineChart.axisRight.setDrawGridLines(false)
        lineChart.axisRight.setDrawLabels(true)
        lineChart.setDescription("")
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
        lineChart.data = tagsReportItems
                .map { it.tagsWithAmount }
                .collectToTagEntries()
                .map { it.toLineDataSet() }
                .let { LineData(xAxis, it).apply { setDrawValues(false) } }
    }

    override fun showIntervalIsRequired() {
        // Interval will always be there
    }

    override fun hideIntervalIsRequired() {
        // Interval will always be there
    }

    private fun List<List<TagsReportPresenter.TagWithAmount>>.collectToTagEntries() = foldIndexed(hashMapOf<Tag, ArrayList<Entry>>(), {
        index, tagsEntries, tagsWithAmount ->
        tagsEntries.apply {
            tagsWithAmount.forEach { getOrPut(it.tag, { arrayListOf<Entry>() }).apply { add(Entry(it.amount.toFloat(), index)) } }
        }
    })

    private fun Map.Entry<Tag, List<Entry>>.toLineDataSet() = LineDataSet(value, key.title).apply {
        color = if (key.color == 0) defaultColor else key.color
        setCircleColor(color)
        setDrawHighlightIndicators(false)
        setDrawFilled(true)
        fillColor = color
        fillAlpha = 100
        lineWidth = 2f
    }
}