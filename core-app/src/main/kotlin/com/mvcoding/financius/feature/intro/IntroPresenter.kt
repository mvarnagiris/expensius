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

import com.mvcoding.financius.Settings
import com.mvcoding.financius.feature.Presenter
import rx.Observable

class IntroPresenter<T>(private val introPages: List<IntroPage<T>>, private val settings: Settings) : Presenter<IntroPresenter.View<T>>() {
    var activeIntroPagePosition = 0

    override fun onAttachView(view: View<T>) {
        super.onAttachView(view)
        view.showIntroPages(introPages, activeIntroPagePosition)

        unsubscribeOnDetach(view.onLogin()
                .doOnNext { settings.setIsIntroductionSeen(true) }
                .subscribe { view.startLogin() })

        unsubscribeOnDetach(view.onSkipLogin()
                .doOnNext { settings.setIsIntroductionSeen(true) }
                .subscribe { view.startOverview() })

        unsubscribeOnDetach(view.onActiveIntroPagePositionChanged().subscribe { position -> activeIntroPagePosition = position })
    }

    interface View<T> : Presenter.View {
        fun showIntroPages(introPages: List<IntroPage<T>>, activeIntroPagePosition: Int)
        fun onSkipLogin(): Observable<Unit>
        fun onActiveIntroPagePositionChanged(): Observable<Int>
        fun onLogin(): Observable<Unit>
        fun startOverview()
        fun startLogin()
    }
}