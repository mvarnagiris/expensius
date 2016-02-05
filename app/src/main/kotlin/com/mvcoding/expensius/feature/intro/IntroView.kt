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

package com.mvcoding.expensius.feature.intro

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.widget.Button
import android.widget.LinearLayout
import com.jakewharton.rxbinding.support.v4.view.pageSelections
import com.jakewharton.rxbinding.view.clicks
import com.mvcoding.expensius.R
import com.mvcoding.expensius.extension.provideActivityScopedSingleton
import com.mvcoding.expensius.extension.toBaseActivity
import com.mvcoding.expensius.feature.login.LoginActivity
import com.mvcoding.expensius.feature.overview.OverviewActivity

class IntroView : LinearLayout, IntroPresenter.View<Int> {
    private val viewPager by lazy { findViewById(R.id.viewPager) as ViewPager }
    private val loginButton by lazy { findViewById(R.id.loginButton) as Button }
    private val skipButton by lazy { findViewById(R.id.skipButton) as Button }

    @Suppress("UNCHECKED_CAST")
    private val presenter by lazy { provideActivityScopedSingleton(IntroPresenter::class) as IntroPresenter<Int> }
    private val adapter = IntroPagesAdapter()

    constructor(context: Context?) : this(context, null)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onFinishInflate() {
        super.onFinishInflate()
        viewPager.adapter = adapter
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter.onAttachView(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter.onDetachView(this)
    }

    override fun showIntroPages(introPages: List<IntroPage<Int>>, activeIntroPagePosition: Int) {
        adapter.introPages = introPages
        viewPager.setCurrentItem(activeIntroPagePosition, false)
    }

    override fun onSkipLogin() = skipButton.clicks()

    override fun onLogin() = loginButton.clicks()

    override fun onActiveIntroPagePositionChanged() = viewPager.pageSelections()

    override fun startOverview() {
        OverviewActivity.start(context)
        context.toBaseActivity().finish()
    }

    override fun startLogin() {
        LoginActivity.start(context)
        context.toBaseActivity().finish()
    }
}