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

package com.mvcoding.financius.feature.intro

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.jakewharton.rxbinding.support.v4.view.pageSelections
import com.jakewharton.rxbinding.view.clicks
import com.mvcoding.financius.extension.shankWithBoundScope
import com.mvcoding.financius.extension.toActivity
import com.mvcoding.financius.feature.login.LoginActivity
import com.mvcoding.financius.feature.overview.OverviewActivity
import kotlinx.android.synthetic.view_intro.view.loginButton
import kotlinx.android.synthetic.view_intro.view.skipButton
import kotlinx.android.synthetic.view_intro.view.viewPager
import rx.Observable

class IntroView : LinearLayout, IntroPresenter.View<Int> {
    val presenter = shankWithBoundScope(IntroView::class, context)?.provide(IntroPresenter::class.java)
    val adapter = IntroPagesAdapter()

    constructor(context: Context?) : this(context, null)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onFinishInflate() {
        super.onFinishInflate()
        viewPager.adapter = adapter
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter?.onAttachView(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter?.onDetachView(this)
    }

    override fun showIntroPages(introPages: List<IntroPage<Int>>, activeIntroPagePosition: Int) {
        adapter.introPages = introPages
        viewPager.setCurrentItem(activeIntroPagePosition, false)
    }

    override fun onSkipLogin(): Observable<Unit> = skipButton.clicks()

    override fun onLogin(): Observable<Unit> = loginButton.clicks()

    override fun onActiveIntroPagePositionChanged(): Observable<Int> = viewPager.pageSelections()

    override fun startOverview() {
        OverviewActivity.start(context)
        context.toActivity().finish()
    }

    override fun startLogin() {
        LoginActivity.start(context)
        context.toActivity().finish()
    }
}