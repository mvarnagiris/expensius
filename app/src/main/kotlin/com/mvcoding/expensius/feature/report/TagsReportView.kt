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
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.mvcoding.expensius.R
import com.mvcoding.expensius.extension.doNotInEditMode
import com.mvcoding.expensius.extension.provideActivityScopedSingleton
import com.mvcoding.expensius.model.Tag
import com.mvcoding.expensius.provideAmountFormatter
import com.mvcoding.expensius.provideDateFormatter
import com.mvcoding.expensius.provideSettings
import java.math.BigDecimal
import java.util.*

class TagsReportView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        CardView(context, attrs, defStyleAttr), TagsReportPresenter.View {

    private val lineChart by lazy { findViewById(R.id.lineChart) as LineChart }
    private val dateFormatter by lazy { provideDateFormatter() }
    private val amountFormatter by lazy { provideAmountFormatter() }
    private val settings by lazy { provideSettings() }
    private val presenter by lazy { provideActivityScopedSingleton(TagsReportPresenter::class) }

    override fun onFinishInflate() {
        super.onFinishInflate()
        lineChart.setPinchZoom(false)
        lineChart.isDoubleTapToZoomEnabled = false
        lineChart.isDragEnabled = false
        lineChart.isScaleXEnabled = false
        lineChart.isScaleYEnabled = false
        lineChart.legend.isEnabled = false
        lineChart.axisLeft.setDrawLabels(false)
        lineChart.axisRight.setValueFormatter { value, yAxis ->
            amountFormatter.format(BigDecimal(value.toDouble()), settings.getMainCurrency())
        }
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
        lineChart.data = tagsReportItems.allTags()
                .mapToEmptyEntriesArrayList()
                .fillWith(tagsReportItems)
                .toLineDataSets()
                .toLineData(xAxis)
                .withAmountFormatter()
    }

    private fun List<TagsReportPresenter.TagsReportItem>.allTags() = first().tagsWithAmount.keys.toTypedArray()
    private fun Array<Tag>.mapToEmptyEntriesArrayList() = mapOf(*map { it to arrayListOf<Entry>() }.toTypedArray())
    private fun Map<Tag, ArrayList<Entry>>.toLineDataSets() = map { LineDataSet(it.value, it.key.title).apply { color = it.key.color } }
    private fun List<LineDataSet>.toLineData(xAxisValues: List<String>) = LineData(xAxisValues, this)

    private fun LineData.withAmountFormatter() = apply {
        setValueFormatter { value, entry, position, viewPortHandler ->
            amountFormatter.format(BigDecimal(value.toDouble()), settings.getMainCurrency())
        }
    }

    private fun Map<Tag, ArrayList<Entry>>.fillWith(tagsReportItems: List<TagsReportPresenter.TagsReportItem>) = apply {
        tagsReportItems.forEachIndexed { index, tagsReportItem ->
            tagsReportItem.tagsWithAmount.forEach {
                val tag = it.key
                val amount = it.value.toFloat()
                get(tag)!!.add(Entry(amount, index))
            }
        }
    }
}