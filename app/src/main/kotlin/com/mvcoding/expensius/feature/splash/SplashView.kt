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
import com.mvcoding.expensius.extension.doNotInEditMode
import com.mvcoding.expensius.extension.toBaseActivity
import com.mvcoding.expensius.feature.overview.OverviewActivity

class SplashView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        FrameLayout(context, attrs, defStyleAttr), SplashPresenter.View {

    private val presenter by lazy { provideSplashPresenter() }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        doNotInEditMode { presenter.attach(this) }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter.detach(this)
    }

    override fun displayLogin() {
        // TODO: Display Login
    }

    override fun displayApp() {
        OverviewActivity.start(context)
        context.toBaseActivity().finish()
    }
}