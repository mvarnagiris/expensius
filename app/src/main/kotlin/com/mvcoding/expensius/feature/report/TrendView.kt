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

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.LinearLayout
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.mvcoding.expensius.extension.doNotInEditMode
import com.mvcoding.expensius.extension.getColorFromTheme
import com.mvcoding.expensius.model.Currency
import com.mvcoding.expensius.provideAmountFormatter
import kotlinx.android.synthetic.main.view_trend.view.*
import java.math.BigDecimal

class TrendView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        LinearLayout(context, attrs, defStyleAttr), TrendPresenter.View {

    private val presenter by lazy { provideExpensesTrendsPresenter() }
    private val amountFormatter by lazy { provideAmountFormatter() }

    override fun onFinishInflate() {
        super.onFinishInflate()

        lineChart.setViewPortOffsets(0f, 0f, 0f, 0f)
        lineChart.setPinchZoom(false)
        lineChart.setNoDataTextDescription("")
        lineChart.setDescription("")
        lineChart.isDoubleTapToZoomEnabled = false
        lineChart.isDragEnabled = false
        lineChart.isScaleXEnabled = false
        lineChart.isScaleYEnabled = false
        lineChart.legend.isEnabled = false
        lineChart.axisLeft.setDrawLabels(false)
        lineChart.axisLeft.setDrawGridLines(false)
        lineChart.axisLeft.setDrawAxisLine(false)
        lineChart.axisLeft.setDrawZeroLine(false)
        lineChart.axisRight.setDrawZeroLine(false)
        lineChart.axisRight.setDrawAxisLine(false)
        lineChart.axisRight.setDrawGridLines(false)
        lineChart.axisRight.setDrawLabels(false)
        lineChart.xAxis.setDrawGridLines(false)
        lineChart.xAxis.setDrawAxisLine(false)
        lineChart.xAxis.setDrawLabels(false)
        lineChart.xAxis.setDrawLimitLinesBehindData(false)
        lineChart.animateY(700, Easing.EasingOption.EaseOutCubic)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        doNotInEditMode { presenter.attach(this) }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter.detach(this)
    }

    override fun showTrends(currency: Currency, totalAmount: BigDecimal, currentAmounts: List<BigDecimal>, previousAmounts: List<BigDecimal>) {
        val lineDataSet = LineDataSet(currentAmounts.mapIndexed { index, amount -> Entry(amount.toFloat(), index) }, "")
        lineDataSet.setDrawCubic(true)
        lineDataSet.setDrawCircles(false)
        lineDataSet.setDrawValues(false)
        lineDataSet.setDrawHighlightIndicators(false)
        lineDataSet.setDrawFilled(false)
        lineDataSet.color = getColorFromTheme(android.R.attr.textColorPrimary)
        lineDataSet.lineWidth = 3f

        val lastLineDataSet = LineDataSet(previousAmounts.mapIndexed { index, amount -> Entry(amount.toFloat(), index) }, "")
        lastLineDataSet.setDrawCubic(true)
        lastLineDataSet.setDrawCircles(false)
        lastLineDataSet.setDrawValues(false)
        lastLineDataSet.setDrawHighlightIndicators(false)
        lastLineDataSet.setDrawFilled(true)
        lastLineDataSet.fillColor = Color.BLACK
        lastLineDataSet.fillAlpha = 20
        lastLineDataSet.setColor(Color.BLACK, 1)

        val lineData = LineData(currentAmounts.map { "" }, listOf(lastLineDataSet, lineDataSet))
        lineChart.data = lineData

        val animator = ValueAnimator.ofFloat(0f, 1f).setDuration(700)
        animator.addUpdateListener {
            thisPeriodAmountTextView.text = amountFormatter.format(totalAmount.multiply(BigDecimal.valueOf(it.animatedFraction.toDouble())), currency)
        }
        animator.start()
    }
}