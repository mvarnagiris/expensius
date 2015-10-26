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

import com.mvcoding.financius.feature.Presenter
import com.mvcoding.financius.feature.UserSettings
import rx.Observable

class IntroPresenter(private val introPages: List<IntroPage>, private val userSettings: UserSettings) : Presenter<IntroPresenter.View>() {
    override fun onAttachView(view: View) {
        super.onAttachView(view)
        view.showIntroPages(introPages)

        unsubscribeOnDetach(view.onLogin()
                .doOnNext { userSettings.setIsIntroductionSeen(true) }
                .subscribe { view.startLogin() })

        unsubscribeOnDetach(view.onSkipLogin()
                .doOnNext { userSettings.setIsIntroductionSeen(true) }
                .subscribe { view.startOverview() })
    }

    interface View : Presenter.View {
        fun showIntroPages(introPages: List<IntroPage>)
        fun onSkipLogin(): Observable<Any>
        fun onLogin(): Observable<Any>
        fun startOverview()
        fun startLogin()
    }
}