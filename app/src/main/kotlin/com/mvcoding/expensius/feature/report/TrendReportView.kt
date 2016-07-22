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
import com.mvcoding.expensius.model.Money
import com.mvcoding.expensius.provideMoneyFormatter
import kotlinx.android.synthetic.main.view_trend_report.view.*

class TrendReportView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        LinearLayout(context, attrs, defStyleAttr), TrendReportPresenter.View {

    private val presenter by lazy { provideExpenseTrendReportPresenter() }
    private val moneyFormatter by lazy { provideMoneyFormatter() }

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

    private val ANIMATION_DURATION_MILLIS = 500

    override fun showTrends(totalMoney: Money, currentMoney: List<Money>, previousMoney: List<Money>) {
        val lineDataSet = lineDataSet(currentMoney)

        lineDataSet.setDrawFilled(false)
        lineDataSet.color = getColorFromTheme(android.R.attr.textColorPrimary)
        lineDataSet.lineWidth = 3f

        val lastLineDataSet = lineDataSet(previousMoney)
        lastLineDataSet.setDrawFilled(true)
        lastLineDataSet.fillColor = Color.BLACK
        lastLineDataSet.fillAlpha = 20
        lastLineDataSet.setColor(Color.BLACK, 1)

        val lineData = LineData(currentMoney.map { "" }, listOf(lastLineDataSet, lineDataSet))
        lineChart.data = lineData
        lineChart.animateY(ANIMATION_DURATION_MILLIS)

        val animator = ValueAnimator.ofFloat(0f, 1f).setDuration(ANIMATION_DURATION_MILLIS.toLong())
        animator.addUpdateListener { thisPeriodAmountTextView.text = moneyFormatter.format(totalMoney.multiply(it.animatedFraction)) }
        animator.start()
    }

    private fun lineDataSet(moneys: List<Money>): LineDataSet {
        val lineDataSet = LineDataSet(moneys.mapIndexed { index, money -> Entry(money.amount.toFloat(), index) }, "")
        lineDataSet.setDrawCubic(true)
        lineDataSet.setDrawCircles(false)
        lineDataSet.setDrawValues(false)
        lineDataSet.setDrawHighlightIndicators(false)
        return lineDataSet
    }
}