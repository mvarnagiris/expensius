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

package com.mvcoding.expensius.feature.reports.trends

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.mvcoding.expensius.extension.doNotInEditMode
import com.mvcoding.expensius.feature.reports.provideExpenseTrendReportPresenter
import com.mvcoding.expensius.model.Money
import com.mvcoding.expensius.model.Trends
import com.mvcoding.expensius.provideMoneyFormatter

class TrendsReportView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        LinearLayout(context, attrs, defStyleAttr), TrendsPresenter.View {
    override fun showTrends(trends: Trends) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private val presenter by lazy { provideExpenseTrendReportPresenter() }
    private val moneyFormatter by lazy { provideMoneyFormatter() }

    override fun onFinishInflate() {
        super.onFinishInflate()

//        kotlinx.android.synthetic.main.view_trend_report.view.lineChart.setViewPortOffsets(0f, 0f, 0f, 0f)
//        kotlinx.android.synthetic.main.view_trend_report.view.lineChart.setPinchZoom(false)
//        kotlinx.android.synthetic.main.view_trend_report.view.lineChart.setNoDataTextDescription("")
//        kotlinx.android.synthetic.main.view_trend_report.view.lineChart.setDescription("")
//        kotlinx.android.synthetic.main.view_trend_report.view.lineChart.isDoubleTapToZoomEnabled = false
//        kotlinx.android.synthetic.main.view_trend_report.view.lineChart.isDragEnabled = false
//        kotlinx.android.synthetic.main.view_trend_report.view.lineChart.isScaleXEnabled = false
//        kotlinx.android.synthetic.main.view_trend_report.view.lineChart.isScaleYEnabled = false
//        kotlinx.android.synthetic.main.view_trend_report.view.lineChart.legend.isEnabled = false
//        kotlinx.android.synthetic.main.view_trend_report.view.lineChart.axisLeft.setDrawLabels(false)
//        kotlinx.android.synthetic.main.view_trend_report.view.lineChart.axisLeft.setDrawGridLines(false)
//        kotlinx.android.synthetic.main.view_trend_report.view.lineChart.axisLeft.setDrawAxisLine(false)
//        kotlinx.android.synthetic.main.view_trend_report.view.lineChart.axisLeft.setDrawZeroLine(false)
//        kotlinx.android.synthetic.main.view_trend_report.view.lineChart.axisRight.setDrawZeroLine(false)
//        kotlinx.android.synthetic.main.view_trend_report.view.lineChart.axisRight.setDrawAxisLine(false)
//        kotlinx.android.synthetic.main.view_trend_report.view.lineChart.axisRight.setDrawGridLines(false)
//        kotlinx.android.synthetic.main.view_trend_report.view.lineChart.axisRight.setDrawLabels(false)
//        kotlinx.android.synthetic.main.view_trend_report.view.lineChart.xAxis.setDrawGridLines(false)
//        kotlinx.android.synthetic.main.view_trend_report.view.lineChart.xAxis.setDrawAxisLine(false)
//        kotlinx.android.synthetic.main.view_trend_report.view.lineChart.xAxis.setDrawLabels(false)
//        kotlinx.android.synthetic.main.view_trend_report.view.lineChart.xAxis.setDrawLimitLinesBehindData(false)
//        kotlinx.android.synthetic.main.view_trend_report.view.lineChart.animateY(700, Easing.EasingOption.EaseOutCubic)
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

    /*override fun showTrends(totalMoney: Money, currentMoney: List<Money>, previousMoney: List<Money>) {
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
    }*/

    private fun lineDataSet(moneys: List<Money>): LineDataSet {
        val lineDataSet = LineDataSet(moneys.mapIndexed { index, money -> Entry(money.amount.toFloat(), index) }, "")
        lineDataSet.setDrawCubic(true)
        lineDataSet.setDrawCircles(false)
        lineDataSet.setDrawValues(false)
        lineDataSet.setDrawHighlightIndicators(false)
        return lineDataSet
    }
}