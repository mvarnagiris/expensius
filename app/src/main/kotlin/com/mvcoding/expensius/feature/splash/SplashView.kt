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

package com.mvcoding.expensius.feature.splash

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.mvcoding.expensius.extension.provideActivityScopedSingleton
import com.mvcoding.expensius.extension.toBaseActivity
import com.mvcoding.expensius.feature.intro.IntroActivity
import com.mvcoding.expensius.feature.overview.OverviewActivity

class SplashView : FrameLayout, SplashPresenter.View {
    private val presenter = provideActivityScopedSingleton(SplashPresenter::class, context)

    constructor(context: Context?) : this(context, null)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter.onAttachView(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter.onDetachView(this)
    }

    override fun startOverview() {
        OverviewActivity.start(context)
        context.toBaseActivity().finish()
    }

    override fun startIntro() {
        IntroActivity.start(context)
        context.toBaseActivity().finish()
    }
}