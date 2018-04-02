/*
 * Copyright (C) 2018 Mantas Varnagiris.
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

package com.mvcoding.expensius.feature.filter

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.jakewharton.rxbinding2.view.clicks
import com.mvcoding.expensius.extension.doNotInEditMode
import com.mvcoding.expensius.model.ReportPeriod
import com.mvcoding.expensius.provideDateFormatter
import io.reactivex.Observable
import kotlinx.android.synthetic.main.view_filter.view.*
import org.joda.time.Interval

class FilterView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        LinearLayout(context, attrs, defStyleAttr), FilterPresenter.View {

    private val presenter by lazy { provideFilterPresenter() }
    private val dateFormatter by lazy { provideDateFormatter() }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        doNotInEditMode { presenter.attach(this) }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter.detach(this)
    }

    override fun previousIntervalRequests(): Observable<Unit> = previousIntervalImageButton.clicks()
    override fun nextIntervalRequests(): Observable<Unit> = nextIntervalImageButton.clicks()
    override fun showInterval(interval: Interval, reportPeriod: ReportPeriod): Unit = with(intervalTextView) { text = dateFormatter.formatInterval(reportPeriod, interval) }
}