/*
 * Copyright (C) 2015 Mantas Varnagiris.
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

package com.mvcoding.expensius.feature.overview

import android.content.Context
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.Toolbar
import android.util.AttributeSet
import android.widget.FrameLayout
import com.jakewharton.rxbinding.support.v7.widget.itemClicks
import com.jakewharton.rxbinding.view.clicks
import com.mvcoding.expensius.R
import com.mvcoding.expensius.extension.provideActivityScopedSingleton
import com.mvcoding.expensius.feature.calculator.CalculatorActivity
import com.mvcoding.expensius.feature.tag.TagsActivity

class OverviewView : FrameLayout, OverviewPresenter.View {
    private val toolbar by lazy { findViewById(R.id.toolbar) as Toolbar }
    private val floatingActionButton by lazy { findViewById(R.id.floatingActionButton) as FloatingActionButton }

    private val presenter by lazy { provideActivityScopedSingleton(OverviewPresenter::class) }

    constructor(context: Context?) : this(context, null)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter?.onAttachView(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter?.onDetachView(this)
    }

    override fun onAddNewTransaction() = floatingActionButton.clicks()

    override fun onStartTags() = toolbar.itemClicks().map { it.itemId }.filter { it == R.id.action_tags }.map { Unit }

    override fun startTags() {
        TagsActivity.startView(context)
    }

    override fun startTransactionEdit() {
        CalculatorActivity.start(context)
    }
}